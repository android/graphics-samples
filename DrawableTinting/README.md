
Android DrawableTinting Sample
==============================

Sample that shows applying tinting and color filters to Drawables both programmatically
and as Drawable resources in XML.

Tinting is set on a nine-patch drawable through the "tint" and "tintMode" parameters.
A color state list is referenced as the tint color, which defines colors for different
states of a View (for example disabled/enabled, focused, pressed or selected).

Programmatically, tinting is applied to a Drawable through its "setColorFilter" method,
with a reference to a color and a PorterDuff blend mode. The color and blend mode can be
changed from the UI to see the effect of different options.

Pre-requisites
--------------

- Android SDK 28
- Android Build Tools v28.0.3
- Android Support Repository

Getting Started
---------------

This sample uses the Gradle build system. To build this project, use the
"gradlew build" command or use "Import Project" in Android Studio.

Support
-------

- Stack Overflow: http://stackoverflow.com/questions/tagged/android

If you've found an error in this sample, please file an issue:
https://github.com/android/graphics

Patches are encouraged, and may be submitted by forking this project and
submitting a pull request through GitHub. Please see CONTRIBUTING.md for more details.
