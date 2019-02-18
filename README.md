# blackboxUiTest
Demo app Ui Tests

Steps to execute test:

If you have Android Studio:
1. Import this project in Android Studio
2. Build the App
3. Add Android Instrumentation Test to run Configuration
4. Run on an Emulator or Connected device
5. Test Reports can be seen and exported as XML or HTML in Android studio(You will find the exported reports at root folder of this project).

If you do not have Android Studio:
1. Build the project on terminal(at project root folder):
    $ ./gradlew clean build 
    
2. Install app to tested on intended phone/emulator
3. Connect phone/emulator via ADB and Install the test project apps:
    $ adb push <project location>/blackboxUiTest/app/build/outputs/apk/debug/app-debug.apk /data/local/tmp/com.blackbox.uitest
    $ adb shell pm install -t -r "/data/local/tmp/com.blackbox.uitest"
    
    $ adb push <project location>/blackboxUiTest/app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk /data/local/tmp/com.blackbox.uitest.test
    $ adb shell pm install -t -r "/data/local/tmp/com.blackbox.uitest.test"
    
4. Run the tests:
     $ adb shell am instrument -w -r   -e debug false -e class 'com.blackbox.uitest.BlackboxUiInstrumentedTest' com.blackbox.uitest.test/android.support.test.runner.AndroidJUnitRunner

5. Test Report is printed on Terminal

  
