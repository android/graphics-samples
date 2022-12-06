
TextureView to SurfaceView
===================================

This sample shows the similarities and differences between [TextureView][1] and [SurfaceView][2] 
with the goal ultimately being to migrate away from TextureView entirely and migrating to using 
SurfaceView for most, if not all cases.

[1]: https://developer.android.com/reference/android/view/TextureView
[2]: https://developer.android.com/reference/android/view/SurfaceView

Introduction
------------

SurfaceView has been around since the beginning of Android (API 1). Some of the benefits off 
SurfaceView over TextureView include:

* Better power efficiency
* 10-Bit HDR Support (dependent on support from the display on the device)
* DRM playback Support.

Google recommends using SurfaceView for displaying content like video playback and camera preview 
instead of using TextureView.

Screenshots
-------------

<img src="screenshots/sample.png" height="400" alt="Screenshot"/>

Getting Started
---------------

This sample uses the Gradle build system. To build this project, use the
"gradlew build" command or use "Import Project" in Android Studio.

Note that this may not work well with the Android Emulator due to hardware requirements like an HDR
capable display, so use a device when possible.

Support
-------

- Stack Overflow: http://stackoverflow.com/questions/tagged/android

If you've found an error in this sample, please file an issue:
https://github.com/android/graphics

Patches are encouraged, and may be submitted by forking this project and
submitting a pull request through GitHub. Please see CONTRIBUTING.md for more details.
