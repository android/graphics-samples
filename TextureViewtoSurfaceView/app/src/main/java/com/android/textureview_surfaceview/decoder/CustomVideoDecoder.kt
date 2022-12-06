package com.android.textureview_surfaceview.decoder

import android.content.res.AssetFileDescriptor
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaCodecList
import android.media.MediaExtractor
import android.media.MediaExtractor.SEEK_TO_PREVIOUS_SYNC
import android.media.MediaFormat
import android.view.Surface
import java.io.IOException

/**
 * Custom Decoder for decoding video frame to a surface using the [MediaFormat] & [MediaCodec] APIs.
 */
class CustomVideoDecoder(
    private val extractor: MediaExtractor,
    private val format: MediaFormat,
) {
    companion object {
        private const val CV_MIME_PREFIX: String = "video/"
        private const val CV_TIMEOUT_US: Long = 10000

        /**
         * Companion builder object used to construct a [CustomVideoDecoder] using an
         * [AssetFileDescriptor]
         */
        fun buildWithAssetFile(assetFile: AssetFileDescriptor): CustomVideoDecoder {
            val extractor = MediaExtractor()
            extractor.setDataSource(assetFile)
            for (i in 0 until extractor.trackCount) {
                val format = extractor.getTrackFormat(i)
                format.getString(MediaFormat.KEY_MIME)?.let {
                    if (!it.startsWith(CV_MIME_PREFIX)) return@let
                    extractor.selectTrack(i)
                    return CustomVideoDecoder(extractor, format)
                }
            }

            throw Exception()
        }
    }

    /**
     * Dedicated thread to handle decoding media
     */
    private var thread: Thread? = null

    /**
     * If the thread should be running or now
     */
    private var isRunning = false

    /**
     * [Surface] in which the decoder will output too
     */
    private var surface: Surface? = null

    fun setSurface(surface: Surface) {
        this.surface = surface
    }

    fun start(loop: Boolean) {
        if (surface == null) throw AssertionError("You must set a surface using setSurface()")
        isRunning = true
        thread = Thread {
            process(loop)
        }.also {
            it.start()
        }
    }

    fun stop() {
        isRunning = false
        runCatching { thread?.join() }
        thread = null
    }

    private fun setUpDecoder(): MediaCodec = try {
        val codecList = MediaCodecList(MediaCodecList.ALL_CODECS)
        val codecName = codecList.findDecoderForFormat(format)
        MediaCodec.createByCodecName(codecName)
    } catch (e: IOException) {
        e.printStackTrace()
        throw IOException("Could not create decoder", e)
    }.also {
        it.configure(format, surface, null, 0)
        it.start()
    }

    private fun process(loop: Boolean) {
        val decoder = setUpDecoder()
        val info = MediaCodec.BufferInfo()
        val startTime = System.currentTimeMillis()
        while (isRunning) {
            val inputBufferId = decoder.dequeueInputBuffer(CV_TIMEOUT_US)
            if (inputBufferId >= 0) {
                val inputBuffer = decoder.getInputBuffer(inputBufferId)
                val sampleSize = inputBuffer?.let { extractor.readSampleData(it, 0) }
                if (sampleSize != null && sampleSize > 0) {
                    val sampleTime = extractor.sampleTime
                    decoder.queueInputBuffer(inputBufferId, 0, sampleSize, sampleTime, 0)
                    extractor.advance()
                } else {
                    decoder.queueInputBuffer(
                        inputBufferId, 0, 0, 0,
                        MediaCodec.BUFFER_FLAG_END_OF_STREAM
                    )
                    if (!loop) isRunning = false
                }
            }

            val outputBufferId = decoder.dequeueOutputBuffer(info, CV_TIMEOUT_US);
            if (outputBufferId >= 0) {
                val sampleTime = info.presentationTimeUs / 1000
                val playTime: Long = System.currentTimeMillis() - startTime
                val sleepTime = sampleTime - playTime
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime)
                }

                // Drop the buffer if it's more than 100 ms late.
                decoder.releaseOutputBuffer(outputBufferId, sleepTime >= -100)
                if (info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) break
            }

            // All decoded frames have been rendered, we can stop playing now
            if (info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) break
        }

        decoder.stop()
        decoder.release()
        if (loop && isRunning) {
            extractor.seekTo(0, SEEK_TO_PREVIOUS_SYNC)
            process(loop)
        } else {
            extractor.release()
        }
    }
}