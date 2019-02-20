package com.blackbox.uitest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static java.lang.Math.round;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;


/**
 * class defining app actions
 */
class appActions {
    deliverylistpage listpage = new deliverylistpage();
    deliverydetailspage detailspage = new deliverydetailspage();
    private static final String PACKAGE_UNDER_TEST
            = "com.lalamove.techchallenge";

    static final int LAUNCH_TIMEOUT = 5000;
    static final int NETWORK_TIMEOUT = 10000;

    private UiDevice phonedevice = UiDevice.getInstance(getInstrumentation());




    /**
     * Gets Count of delivery list.
     *
     * @return count of items loaded in delivery list
     */
    int getdeliverylistcount() throws UiObjectNotFoundException {
        wait_for_deliveryList();

        Rect listbounds=listpage.deliverylist.getBounds();
        Rect titlebounds=listpage.titlebar.getBounds();
        int cnt=0;
        boolean lastswipestatus;
        do{
            UiObject lastitem=getdeliveryitem(listpage.deliverylist.getChildCount()-1);
            cnt=cnt+listpage.deliverylist.getChildCount();

            lastswipestatus=phonedevice.swipe(round(listbounds.centerX()),lastitem.getBounds().top,
                    round(titlebounds.exactCenterX()),titlebounds.top,55);

        }while (!(listpage.progressbar.waitForExists(1000)));
        if (lastswipestatus){
            cnt=cnt+listpage.deliverylist.getChildCount();
        }



//        returning cnt-1 as progressbar is also a child of deliverylist scrollable
        return cnt-1;


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
    int wait_for_deliveryList() throws UiObjectNotFoundException {

        listpage.deliverylist.waitForExists(NETWORK_TIMEOUT);
        return listpage.deliverylist.getChildCount();
    }

    /**
     * gets delivery description and address of a particulat item in delivery list
     *
     * @param index : delivery list index
     * @return dinfo[0]: delivery description
     * dinfo[1]: address
     * @throws UiObjectNotFoundException when UiObject is not found
     */
    String[] get_deliveryitem_info(int index) throws UiObjectNotFoundException {

        UiObject deliveryitem = listpage.deliverylist.getChild(
                new UiSelector().className("android.view.ViewGroup").index(index));
        String dinfo[] = new String[2];
        dinfo[0] = deliveryitem.getChild(
                new UiSelector().resourceId(
                        "com.lalamove.techchallenge:id/textView_description")).getText();
        dinfo[1] = deliveryitem.getChild(
                new UiSelector().resourceId(
                        "com.lalamove.techchallenge:id/textView_address")).getText();
        return dinfo;

    }

    /**
     * to get all child objects of chosen index of delivery list item
     *
     * @param i: delivery list index
     * @return a Map of all child objects
     * @throws UiObjectNotFoundException when Ui elements are not found
     */
    Map getdeliveryitemobjects(int i) throws UiObjectNotFoundException {

        UiObject deliveryitem = getdeliveryitem(i);

        Map<String, UiObject> ditem = new HashMap<>();
        ditem.put("picon", deliveryitem.getChild(
                new UiSelector().resourceId("com.lalamove.techchallenge:id/simpleDraweeView")));
        ditem.put("dmsg", deliveryitem.getChild(
                new UiSelector().resourceId("com.lalamove.techchallenge:id/textView_description")));
        ditem.put("licon", deliveryitem.getChild(
                new UiSelector().resourceId("com.lalamove.techchallenge:id/imageView")));
        ditem.put("address", deliveryitem.getChild(
                new UiSelector().resourceId("com.lalamove.techchallenge:id/textView_address")));
        return ditem;
    }

    /**
     * UiObject of selected delivery item
     *
     * @param index : delivery list index
     * @return delivery list item
     * @throws UiObjectNotFoundException when Ui elements are not found
     */
    UiObject getdeliveryitem(int index) throws UiObjectNotFoundException {

        return listpage.deliverylist.getChild(
                new UiSelector().className("android.view.ViewGroup").index(index));
    }

    /**
     * Opens Delivery detail page for a randomly selected delivery list item
     *
     * @return an array with 2 values
     * str[0] is delivery description of the item clicked
     * str[1] is the address of the item clicked
     * @throws UiObjectNotFoundException on not finding delivery list
     */
    String[] opendeliverydetails(int index) throws UiObjectNotFoundException {

        wait_for_deliveryList();
        UiObject deliveryitem = getdeliveryitem(index);
        String dinfo[] = get_deliveryitem_info(index);
        deliveryitem.click();
        return dinfo;
    }

    /**
     * waits for delivery list to load
     *
     * @return count to delivery list items visible
     */
    Boolean waitfordeliverydetailspage() {
        UiObject deliverydetails = phonedevice.findObject(new UiSelector().text("Delivery Detail"));
        return deliverydetails.waitForExists(NETWORK_TIMEOUT);
    }

    /**
     * get a UiObject for map pin on delivery details page
     *
     * @return UiObject for map pin
     * @throws UiObjectNotFoundException when Ui elements are not found
     */
    UiObject getmappin() throws UiObjectNotFoundException {
        return detailspage.mapview.getChild(new UiSelector().className("android.view.View"));
    }

    /**
     * randomly selects an index from the delivery list
     *
     * @return an integer within the index visible
     * @throws UiObjectNotFoundException when UI elements are not found
     */
    int getrandomlistindex() throws UiObjectNotFoundException {
        Random rand = new Random();
        return rand.nextInt(wait_for_deliveryList() - 1);
    }


}

