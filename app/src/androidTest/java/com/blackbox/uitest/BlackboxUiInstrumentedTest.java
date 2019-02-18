package com.blackbox.uitest;


import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Random;
import static java.lang.Math.round;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BlackboxUiInstrumentedTest {
    private UiDevice phonedevice;


    /**
     * Launches app before each test.
     */
    @Before
    public void launchapp() throws UiObjectNotFoundException {
        phonedevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        phonedevice.pressHome();
        UiObject allAppsButton = phonedevice
                .findObject(new UiSelector().descriptionContains("Apps"));
        allAppsButton.clickAndWaitForNewWindow();
        UiScrollable appViews = new UiScrollable(new UiSelector().scrollable(true));
        appViews.scrollIntoView(new UiSelector().text("TechChallenge"));
        phonedevice.findObject(new UiSelector().text("TechChallenge")).clickAndWaitForNewWindow();
    }


    /**
     * Exits app after each test.
     */
    @After
    public void exitapp() {
        UiObject currentapp = phonedevice.findObject(
                new UiSelector().packageName("com.lalamove.techchallenge"));
        while (currentapp.exists()) {
            phonedevice.pressBack();
        }
    }


    /**
     * Test:
     * [Image-1A] Landing page of the app shows Delivery List
     */
    @Test
    public void func1_image1A_landingpagelayout() {

        UiObject titlebar = phonedevice.findObject(new UiSelector().text("Delivery List"));
        UiScrollable deliverylist = new UiScrollable(new UiSelector().scrollable(true));
        assertTrue("Delivery List textview not found!", titlebar.waitForExists(2000));
        assertTrue("Delivery List not getting Loaded", deliverylist.waitForExists(10000));
    }

    /**
     * Gets Count of delivery list.
     * @return cont of items loaded in delivery list
     */
    public int getdeliverylistcount() {
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
     * Test:
     * [Image-1A] Landing page of the app shows Delivery List with maximum 20 records when first opened.
     */
    @Test
    public void func1_image1A_initialdeliverylistcount() {

        int cnt = getdeliverylistcount();
        assertTrue("Delivery list has more than 20 records.", 20>=cnt);

    }


    /**Test:
     * [Image-1A]
            Scrolling up will retrieve more items,
             each time 20 records are appended at the end of the list.*/
    @Test
    public void func2_image1_deliverylistappending() {

       getdeliverylistcount();
        UiObject progressbar = phonedevice.findObject(
                new UiSelector().className("android.widget.ProgressBar"));
        progressbar.waitUntilGone(10);

        assertEquals("Number of items appended in delivery list is not 20", 20, getdeliverylistcount());
    }

    /**Test:
     * [Image-1A]
     * Shows icon, description and location. Parcels are for Leviero and documents are for Andrio.
        */

    @Test
    public void func3_checkdeliverylistitemslayout() throws UiObjectNotFoundException {

        UiScrollable deliverylist = new UiScrollable(new UiSelector().scrollable(true));
        deliverylist.waitForExists(10000);
        int deliverylistcount = deliverylist.getChildCount();
        for (int i = 0; i < deliverylistcount - 1; i++) {
            UiObject deliveryitem = deliverylist.getChild(
                    new UiSelector().className("android.view.ViewGroup").index(i));
            UiObject picon = deliveryitem.getChild(
                    new UiSelector().resourceId("com.lalamove.techchallenge:id/simpleDraweeView"));
            UiObject dmsg = deliveryitem.getChild(
                    new UiSelector().resourceId("com.lalamove.techchallenge:id/textView_description"));
            UiObject licon = deliveryitem.getChild(
                    new UiSelector().resourceId("com.lalamove.techchallenge:id/imageView"));
            UiObject loc = deliveryitem.getChild(
                    new UiSelector().resourceId("com.lalamove.techchallenge:id/textView_address"));
            assertTrue("Picture icon missing for delivery item" + Integer.toString(i + 1), picon.exists());
            assertTrue("Delivery description missing for delivery item" + Integer.toString(i + 1), dmsg.exists());
            assertTrue("Location icon missing for delivery item" + Integer.toString(i + 1), licon.exists());
            assertTrue("Address missing for delivery item" + Integer.toString(i + 1), loc.exists());
            String dmsg_txt = dmsg.getText();
            String loc_txt = loc.getText();
            assertTrue("Delivery Description does not match the standard pattern for item" + Integer.toString(i + 1),
                    dmsg_txt.matches("Deliver (.*) to (.*)"));
            assertNotNull("Address Missing for item" + Integer.toString(i + 1)
                    , loc_txt);
            if (dmsg_txt.contains("documents")) {
                assertEquals("Documents not delivered to Andrio!", "Deliver documents to Andrio", dmsg_txt);
            } else if (dmsg_txt.contains("parcel")) {
                assertEquals("Not delivering parcel to Leviero!", "Deliver parcel to Leviero", dmsg_txt);
            }
        }
    }


    /**
     * Test:
     * [Image-1A]Long press on an item, and it will be deleted.
     */
    @Test
    public void func4_longpressdelete() throws UiObjectNotFoundException {

        UiScrollable deliverylist = new UiScrollable(new UiSelector().scrollable(true));
        deliverylist.waitForExists(10000);
        int deliverylistcount = deliverylist.getChildCount();
        Random rand = new Random();
        int x = rand.nextInt(deliverylistcount - 1);
        UiObject deliveryitem = deliverylist.getChild(
                new UiSelector().className("android.view.ViewGroup").index(x));
        String dinfo = deliveryitem.getChild(
                new UiSelector().resourceId(
                        "com.lalamove.techchallenge:id/textView_description")).getText()
                + deliveryitem.getChild(
                new UiSelector().resourceId(
                        "com.lalamove.techchallenge:id/textView_address")).getText();
        deliveryitem.dragTo(deliveryitem, 100);
        String dinfo2 = deliveryitem.getChild(
                new UiSelector().resourceId(
                        "com.lalamove.techchallenge:id/textView_description")).getText()
                + deliveryitem.getChild(
                new UiSelector().resourceId(
                        "com.lalamove.techchallenge:id/textView_address")).getText();
        assertNotEquals("Delivery Item Not deleted!", dinfo, dinfo2);

    }

    /**
     * Test:
     * [Image-2A]        Click any item from Delivery List and Delivery Details page shows.
     * @throws UiObjectNotFoundException on not finding Uiobjects
     */
    @Test
    public void func5_clickindeliveriitem() throws UiObjectNotFoundException {

        opendeliverydetails();
        UiObject deliverydetails = phonedevice.findObject(new UiSelector().text("Delivery Detail"));
        assertTrue("Delivery detail page did not open!", deliverydetails.waitForExists(10000));

    }

    /**
     * Test:
     * [Image-2A] Information should be shown correctly based on the item details from [Image-1A]
     * @throws UiObjectNotFoundException on not finding Uiobjects
     */
    @Test
    public void func5_ddpagedetails() throws UiObjectNotFoundException {

        String str[] = opendeliverydetails();
        UiObject deliverydetails = phonedevice.findObject(new UiSelector().text("Delivery Detail"));
        assertTrue("Delivery detail page did not open!", deliverydetails.waitForExists(10000));
        UiObject dd_picon = phonedevice.findObject(new UiSelector().resourceId("com.lalamove.techchallenge:id/ivDelivery"));
        UiObject dd_desc = phonedevice.findObject(new UiSelector().resourceId("com.lalamove.techchallenge:id/tvDescription"));
        UiObject dd_add = phonedevice.findObject(new UiSelector().resourceId("com.lalamove.techchallenge:id/textView_address"));
        UiObject dd_licon = phonedevice.findObject(new UiSelector().resourceId("com.lalamove.techchallenge:id/imageView"));
        assertTrue("Picture icon not found on detail page!", dd_picon.waitForExists(2000));
        assertTrue("Delivery description not found on detail page!", dd_desc.waitForExists(2000));
        assertTrue("Location icon not found on detail page!", dd_licon.waitForExists(2000));
        assertTrue("Address not found on detail page!", dd_add.waitForExists(2000));
        assertEquals("Delivery description on delivery list does not match description on detail page!", str[0], dd_desc.getText());
        assertEquals("Address on delivery list does not match address on detail page!", str[1], dd_add.getText());
    }

    /**
     * Opens Delivery detail page for a randomly selected delivery list item
     * @return an array with 2 values
     *          str[0] is delivery description of the item clicked
     *          str[1] is the address of the item clicked
     * @throws UiObjectNotFoundException on not finding delivery list
     */
    public String[] opendeliverydetails() throws UiObjectNotFoundException {
        UiScrollable deliverylist = new UiScrollable(new UiSelector().scrollable(true));
        deliverylist.waitForExists(10000);
        int deliverylistcount = deliverylist.getChildCount();
        Random rand = new Random();
        int x = rand.nextInt(deliverylistcount - 1);
        String str[] = new String[2];
        UiObject deliveryitem = deliverylist.getChild(
                new UiSelector().className("android.view.ViewGroup").index(x));
        str[0] = deliveryitem.getChild(
                new UiSelector().resourceId(
                        "com.lalamove.techchallenge:id/textView_description")).getText();
        str[1] = deliveryitem.getChild(
                new UiSelector().resourceId(
                        "com.lalamove.techchallenge:id/textView_address")).getText();

        deliveryitem.click();

        return str;
    }


    /**
     * Test:
     * [Image-2A]
     Map pin shows and centred to that pin for that delivery.*/
    @Test
    public void func6_checkmappin() throws UiObjectNotFoundException {

        opendeliverydetails();
        UiObject mapview = phonedevice.findObject(new UiSelector().description("Google Map"));
        UiObject mappin = mapview.getChild(new UiSelector().className("android.view.View"));
        assertTrue("Map not loaded!", mapview.waitForExists(10000));

        assertEquals("map pin is Horizontally not centered!",
                round(mapview.getBounds().exactCenterX()), round(mappin.getBounds().exactCenterX()));
        assertEquals("map pin is Vertically not centered!",
                round(mapview.getBounds().exactCenterY()), round(mappin.getBounds().bottom));

    }


    /**
     * [Image-2B] Shows when clicking the 14th record from Delivery List.*/
    @Test
    public void func8_check14deliverydetail() {

        UiScrollable deliverylist = new UiScrollable(new UiSelector().scrollable(true));
        assertTrue("Delivery List not getting Loaded", deliverylist.waitForExists(10000));
        for (int i = 0; i < 14; i++) {
            phonedevice.pressDPadDown();
        }
        phonedevice.pressDPadCenter();
        UiObject weatherinfo = phonedevice.findObject(
                new UiSelector().text("Gale or storm force wind is expected or blowing generally in Hong Kong near sea level, with a sustained wind speed of 63–117 km/h from the quarter indicated and gusts which may exceed 180 km/h, and the wind condition is expected to persist."));
        assertTrue("Weather information on 14th item not found!", weatherinfo.waitForExists(10000));


    }

}
