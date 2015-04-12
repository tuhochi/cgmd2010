A summary of frequently asked questions and answers to them. The Questions are related to OpenGL ES, Android development, and the VL computer graphics on mobile devices, Vienna University of Technology.




---


## Does the Android Emulator support OpenGL ES 2.0? ##

Not at the moment (March 2010)! The official note on this issue reads:
"The Android emulator does not support OpenGL ES 2.0 hardware emulation at this time. Running and testing code that uses this API requires a real device with such capabilities."

## Can we use OpenGL ES 2.0 in the Lab? ##
Although we will buy Android hardware that is capable of doing OpenGL ES 2.0, it is not recommended to use it in the Lab. You will have only very limited access to the hardware and that will probably not be enough to debug your whole code.

## Where can we find a modelloader for our level? ##

Group 17 provide an **[obj-modelloader](http://www.informatik-forum.at/showthread.php?79216-OBJ-Loader)**. But you can use any other free modelloader that is compatible with the license of our game.

## How do I test my level? ##

Run the application and press "Menu" in the Emulator Controls. At the bottom of the display, a field is appearing where you can enable/disable Debug Mode. If enabled, the Debug screen appears. There you can start a level or help Activy directly.

## How do I disable Debug Mode again? ##

While in the Debug screen, press "Start Menu Activity" to load the Main Game screen. Pressing "Menu" in the Emulator Controls lets you disable Debug Mode again.

## We have problems with VBOs. How can we solve these problems? ##

Look at the blog post [here](http://apistudios.com/hosted/marzec/badlogic/wordpress/?p=21). (Thx to MrM -> Thread in our [forum](http://www.informatik-forum.at/showthread.php?79661-VertexBufferObject-Troubles-and-how-to-quot-solve-quot-them))
However VBOs are **NOT** mandatory in CGMD!

## How do I set the view to Landscape? ##

Add "android:screenOrientation="landscape">" into your activity node in AndroidManifest.xml.

## How to switch between Portrait and Landscape view in the emulator? ##

Press Ctrl-F11 or Ctrl-F12 to switch between the modes.

## How fast is the Nexus One? ##

Extremely fast! And much faster than the first Android phone G1. Remember that when you test your level on the Nexus One. You can find some benchmarks on the net like  [this one](http://www.androidpit.de/de/android/blog/391685/Nexus-One-im-Vergleich-mit-Milestone-G1-iPhone-Palm-Pre-Benchmarks).

## We tried to test our level on the Nexus One, but got an "Re-installation failed due to different application signatures" - error. ##

You have to uninstall BIFTH. "Click" on the menu button when you can see the home screen: Settings > Applications > Manage Applications > The Big Incredible and Funny ... > Uninstall. Of course you can enter 'adb uninstall at.ac.tuwien.cg.cgmd.bifth2010' in the shell as suggested in the console. Launch BIFTH via Eclipse once more.

## How do I solve the Install\_failed\_insufficient\_storage error when trying to run the project in the emulator? ##

Open a Command Window and enter:
'emulator -avd #AVD\_NAME# -partition-size 1024 -memory 1024'
where #AVD\_NAME# is the name of your emulator.

## How do I solve the "Type Conversion to Dalvik format failed: Unable to execute dex:null" error? ##
If you get that error permanently edit the eclipse.ini which is located at your Eclipse installation directory. Alter the values for the Java heap size: "-Xms256m
-Xmx1024m". After that you have to restart Eclipse. (see [this thread](http://www.informatik-forum.at/showthread.php?80744-Type-Conversion-to-Dalvik-format-failed-Unable-to-execute-dex-null) for more information)

## How do I take a Screenshot of my OpenGL app (DDMS has vsync problems)? ##
You can use the following method:
```
at.ac.tuwien.cg.cgmd.bifth2010.level42.util.managers.OGLManager.saveScreenshot(final int x, final int y, final int w, final int h, final OutputStream os)
```
x, y, w, h specify the window to snap in pixels, the screenshot will be written into os, and os will be closed.