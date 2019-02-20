package com.blackbox.uitest;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static java.lang.Math.round;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BlackboxUiInstrumentedTest {


    private UiDevice phonedevice = UiDevice.getInstance(getInstrumentation());
    private appActions app = new appActions();


    /**
     * Launches app before each test.
     */
    @Before
    public void setup() {

        app.launchapp();
    }


    /**
     * Exits app after each test.
     */
    @After
    public void teardown() {
        app.exitapp();

    }


    /**
     * Test:
     * [Image-1A] Landing page of the app shows Delivery List
     */
    @Test
    public void func1_image1A_landingpagelayout() {

        assertTrue("Delivery List textview not found!", app.listpage.titlebar.waitForExists(appActions.LAUNCH_TIMEOUT));
        assertTrue("Delivery List not getting Loaded", app.listpage.deliverylist.waitForExists(appActions.NETWORK_TIMEOUT));
    }


    /**
     * Test:
     * [Image-1A] Landing page of the app shows Delivery List with maximum 20 records when first opened.
     */
    @Test
    public void func1_image1A_initialdeliverylistcount() throws UiObjectNotFoundException {

        int cnt = app.getdeliverylistcount();
        assertTrue("Delivery list has more than 20 records.", 20 >= cnt);

    }


    /**
     * Test:
     * [Image-1A]
     * Scrolling up will retrieve more items,
     * each time 20 records are appended at the end of the list.
     */
    @Test
    public void func2_image1_deliverylistappending() throws UiObjectNotFoundException {


        int cnt=app.getdeliverylistcount();
        app.listpage.progressbar.waitUntilGone(appActions.NETWORK_TIMEOUT);
        app.listpage.deliverylist.flingBackward();
        int cnt2=app.getdeliverylistcount();
        assertEquals("Number of items appended in delivery list is not 20", 20, cnt2-cnt);
    }

    /**
     * Test:
     * [Image-1A]
     * Shows icon, description and location. Parcels are for Leviero and documents are for Andrio.
     */

    @Test
    public void func3_checkdeliverylistitemslayout() throws UiObjectNotFoundException {

        app.wait_for_deliveryList();
        int deliverylistcount = app.listpage.deliverylist.getChildCount();
        for (int i = 0; i < deliverylistcount - 1; i++) {

            UiObject picon = (UiObject) app.getdeliveryitemobjects(i).get("picon");
            UiObject dmsg = (UiObject) app.getdeliveryitemobjects(i).get("dmsg");
            UiObject licon = (UiObject) app.getdeliveryitemobjects(i).get("licon");
            UiObject loc = (UiObject) app.getdeliveryitemobjects(i).get("address");
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

        int x = app.getrandomlistindex();
        String dinfo[] = app.get_deliveryitem_info(x);
        UiObject deliveryitem = app.getdeliveryitem(x);
        deliveryitem.dragTo(deliveryitem, 100);
        String dinfo2[] = app.get_deliveryitem_info(x);
        assertNotEquals("Delivery Item Not deleted!", dinfo, dinfo2);


    }

    /**
     * Test:
     * [Image-2A]        Click any item from Delivery List and Delivery Details page shows.
     *
     * @throws UiObjectNotFoundException on not finding Uiobjects
     */
    @Test
    public void func5_clickindeliveriitem() throws UiObjectNotFoundException {

        app.opendeliverydetails(app.getrandomlistindex());

        assertTrue("Delivery detail page did not open!", app.waitfordeliverydetailspage());

    }

    /**
     * Test:
     * [Image-2A] Information should be shown correctly based on the item details from [Image-1A]
     *
     * @throws UiObjectNotFoundException on not finding Uiobjects
     */
    @Test
    public void func5_ddpagedetails() throws UiObjectNotFoundException {

        int x = app.getrandomlistindex();
        String str[] = app.opendeliverydetails(x);

        assertTrue("Delivery detail page did not open!", app.waitfordeliverydetailspage());

        assertTrue("Picture icon not found on detail page!",
                app.detailspage.dd_picon.waitForExists(appActions.LAUNCH_TIMEOUT));
        assertTrue("Delivery description not found on detail page!",
                app.detailspage.dd_desc.waitForExists(appActions.LAUNCH_TIMEOUT));
        assertTrue("Location icon not found on detail page!",
                app.detailspage.dd_licon.waitForExists(appActions.LAUNCH_TIMEOUT));
        assertTrue("Address not found on detail page!",
                app.detailspage.dd_add.waitForExists(appActions.LAUNCH_TIMEOUT));
        assertEquals("Delivery description on delivery list does not match description on detail page!",
                str[0], app.detailspage.dd_desc.getText());
        assertEquals("Address on delivery list does not match address on detail page!",
                str[1], app.detailspage.dd_add.getText());
    }


    /**
     * Test:
     * [Image-2A]
     * Map pin shows and centred to that pin for that delivery.
     */
    @Test
    public void func6_checkmappin() throws UiObjectNotFoundException {

        app.opendeliverydetails(app.getrandomlistindex());

        assertTrue("Map not loaded!", app.detailspage.mapview.waitForExists(appActions.NETWORK_TIMEOUT));

        assertEquals("map pin is Horizontally not centered!",
                round(app.detailspage.mapview.getBounds().exactCenterX()), round(app.getmappin().getBounds().exactCenterX()));
        assertEquals("map pin is Vertically not centered!",
                round(app.detailspage.mapview.getBounds().exactCenterY()), round(app.getmappin().getBounds().bottom));

    }


    /**
     * [Image-2B] Shows when clicking the 14th record from Delivery List.
     */
    @Test
    public void func8_check14deliverydetail() throws UiObjectNotFoundException {

        app.wait_for_deliveryList();
        for (int i = 0; i < 14; i++) {
            phonedevice.pressDPadDown();
        }
        phonedevice.pressDPadCenter();
        assertTrue("Weather information on 14th item not found!", app.detailspage.weatherinfo.waitForExists(appActions.NETWORK_TIMEOUT));


    }



}
