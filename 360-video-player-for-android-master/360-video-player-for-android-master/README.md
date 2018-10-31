360 Video Player for Android
============================

360 video is the new hotness in interactive media. Imagine teleporting yourself
to the most interesting places in the world, training with Lebron James, or
speeding across the Jakku desert from Star Wars. These are the kinds of
immersive experiences that 360 video enables.

Enclosed you'll find a sample Android application that demonstrates how to
playback 360 video, specifically equirectangular video, using MediaPlayer,
TextureView, and OpenGL ES. Touch and drag is supported to adjust the yaw and
the pitch to see more of the 360 video.

The launch activity will load a sample 360 video included in:
res/raw/sample360.mp4

Today's 360 cameras generally either output directly in equirectangular format
or provide software to convert to equirectangular. Facebook ingests 360 videos
in equirectangular format.

Visit:

* https://www.facebook.com/Facebook360 - To see the latest trending 360 Videos on Facebook
* https://360video.fb.com - To learn about creating 360 Videos for Facebook

## Disclaimer

* This code is intended as an example and is by no means production quality.
* It may not be entirely stable.
* It has been tested on limited high end Android devices
  * Works on Genymotion with Nexus 5 (5.1.0).
  * Didn't work on the new Android Studio 2.1 Preview 1 emulator (Nexus
    5X AVD with HAX enabled).
* It's not intended as a demonstration of "the right way" to do things.
* It will not be supported.

## Requirements

Requires Android OS version KitKat (4.0) or higher.

To build you'll need the Android SDK with build tools.

*Note: the gradle project was created with Android Studio Preview 2.1 Preview 1*

The included sample video is 4k 30fps so be sure to use an Android device that
can handle that video decode.

GearVR compatible devices such as the Note4, Note5, S6, S7, should have no
issues with video decode performance.

## Usage

### Android Studio

Open the directory in Android Studio and the gradle project should be imported.
Run & Debug options should allow you to build the project.

### Command Line

Assure you have a local.properties file in the top level directory with:
*sdk.dir=/path/to/your/Android/sdk*


    $ gradlew installDebug
    $ adb shell am start -n com.oculus.sample/.SphericalPlayerActivity
