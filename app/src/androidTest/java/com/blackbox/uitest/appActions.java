package com.blackbox.uitest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

class appActions {
    private static final String PACKAGE_UNDER_TEST
            = "com.lalamove.techchallenge";

    private static final int LAUNCH_TIMEOUT = 5000;

    private UiDevice phonedevice = UiDevice.getInstance(getInstrumentation());
    UiObject titlebar = phonedevice.findObject(new UiSelector().text("Delivery List"));
    UiScrollable deliverylist = new UiScrollable(new UiSelector().scrollable(true));

    /**
     * Gets Count of delivery list.
     *
     * @return cont of items loaded in delivery list
     */
    int getdeliverylistcount() {
        UiScrollable deliverylist = new UiScrollable(new UiSelector().scrollable(true));
        UiObject progressbar = phonedevice.findObject(
                new UiSelector().className("android.widget.ProgressBar"));
        assertTrue("Delivery List not getting Loaded", deliverylist.waitForExists(10000));
        int dlistcount = 0;
        do {
            phonedevice.pressDPadDown();

            dlistcount++;

        } while (!(progressbar.exists()));

        return dlistcount - 1;

    }


    /**
     * Launches app
     */
    void launchapp() {

        phonedevice.pressHome();
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        phonedevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);
        Context context = getApplicationContext();
        final Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(PACKAGE_UNDER_TEST);
        assert intent != null;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        phonedevice.wait(Until.hasObject(By.pkg(PACKAGE_UNDER_TEST).depth(0)), LAUNCH_TIMEOUT);
    }

    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.`
     */
    private String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = getApplicationContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

    /**
     * Exits app
     */
    void exitapp() {
        UiObject currentapp = phonedevice.findObject(
                new UiSelector().packageName("com.lalamove.techchallenge"));
        while (currentapp.exists()) {
            phonedevice.pressBack();

        }

    }

    /**
     * waits till delivery list is loaded
     */
    void wait_for_deliveryList() {

        deliverylist.waitForExists(10000);
    }

    String[] get_deliveryitem_info(int i) throws UiObjectNotFoundException {

        UiObject deliveryitem = deliverylist.getChild(
                new UiSelector().className("android.view.ViewGroup").index(i));
        String dinfo[] = new String[2];
        dinfo[0] = deliveryitem.getChild(
                new UiSelector().resourceId(
                        "com.lalamove.techchallenge:id/textView_description")).getText();
        dinfo[1] = deliveryitem.getChild(
                new UiSelector().resourceId(
                        "com.lalamove.techchallenge:id/textView_address")).getText();
        return dinfo;

    }


}
