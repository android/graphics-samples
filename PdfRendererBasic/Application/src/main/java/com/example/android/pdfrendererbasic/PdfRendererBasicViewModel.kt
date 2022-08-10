/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.pdfrendererbasic

import android.app.Application
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class PdfRendererBasicViewModel @JvmOverloads constructor(
    application: Application,
    useInstantExecutor: Boolean = false
) : AndroidViewModel(application) {

    companion object {
        const val FILENAME = "sample.pdf"
    }

    private val job = Job()
    private val executor = if (useInstantExecutor) {
        Executor { it.run() }
    } else {
        Executors.newSingleThreadExecutor()
    }
    private val scope = CoroutineScope(executor.asCoroutineDispatcher() + job)

    private var fileDescriptor: ParcelFileDescriptor? = null
    private var pdfRenderer: PdfRenderer? = null
    private var currentPage: PdfRenderer.Page? = null
    private var cleared = false

    private val _pageBitmap = MutableLiveData<Bitmap>()
    val pageBitmap: LiveData<Bitmap>
        get() = _pageBitmap

    private val _previousEnabled = MutableLiveData<Boolean>()
    val previousEnabled: LiveData<Boolean>
        get() = _previousEnabled

    private val _nextEnabled = MutableLiveData<Boolean>()
    val nextEnabled: LiveData<Boolean>
        get() = _nextEnabled

    private val _pageInfo = MutableLiveData<Pair<Int, Int>>()
    val pageInfo: LiveData<Pair<Int, Int>>
        get() = _pageInfo

    init {
        scope.launch {
            openPdfRenderer()
            showPage(0)
            if (cleared) {
                closePdfRenderer()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.launch {
            closePdfRenderer()
            cleared = true
            job.cancel()
        }
    }

    fun showPrevious() {
        scope.launch {
            currentPage?.let { page ->
                if (page.index > 0) {
                    showPage(page.index - 1)
                }
            }
        }
    }

    fun showNext() {
        scope.launch {
            pdfRenderer?.let { renderer ->
                currentPage?.let { page ->
                    if (page.index + 1 < renderer.pageCount) {
                        showPage(page.index + 1)
                    }
                }
            }
        }
    }

    private fun openPdfRenderer() {
        val application = getApplication<Application>()
        val file = File(application.cacheDir, FILENAME)
        if (!file.exists()) {
            application.assets.open(FILENAME).use { asset ->
                file.writeBytes(asset.readBytes())
            }
        }
        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY).also {
            pdfRenderer = PdfRenderer(it)
        }
    }

    private fun showPage(index: Int) {
        // Make sure to close the current page before opening another one.
        currentPage?.let { page ->
            currentPage = null
            page.close()
        }
        pdfRenderer?.let { renderer ->
            // Use `openPage` to open a specific page in PDF.
            val page = renderer.openPage(index).also {
                currentPage = it
            }
            // Important: the destination bitmap must be ARGB (not RGB).
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            // Here, we render the page onto the Bitmap.
            // To render a portion of the page, use the second and third parameter. Pass nulls to get
            // the default result.
            // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            _pageBitmap.postValue(bitmap)
            val count = renderer.pageCount
            _pageInfo.postValue(index to count)
            _previousEnabled.postValue(index > 0)
            _nextEnabled.postValue(index + 1 < count)
        }
    }

    private fun closePdfRenderer() {
        currentPage?.close()
        pdfRenderer?.close()
        fileDescriptor?.close()
    }

}
