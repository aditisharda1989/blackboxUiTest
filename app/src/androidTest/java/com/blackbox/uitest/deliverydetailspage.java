package com.blackbox.uitest;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiSelector;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;


/*
Class to define UiObjects on delivery details page
 */
class deliverydetailspage {
    private UiDevice phonedevice = UiDevice.getInstance(getInstrumentation());
    UiObject dd_picon = phonedevice.findObject(new UiSelector().resourceId("com.lalamove.techchallenge:id/ivDelivery"));
    UiObject dd_desc = phonedevice.findObject(new UiSelector().resourceId("com.lalamove.techchallenge:id/tvDescription"));
    UiObject dd_add = phonedevice.findObject(new UiSelector().resourceId("com.lalamove.techchallenge:id/textView_address"));
    UiObject dd_licon = phonedevice.findObject(new UiSelector().resourceId("com.lalamove.techchallenge:id/imageView"));
    UiObject mapview = phonedevice.findObject(new UiSelector().description("Google Map"));
    UiObject weatherinfo = phonedevice.findObject(
            new UiSelector().text("Gale or storm force wind is expected or blowing generally in Hong Kong near sea level, with a sustained wind speed of 63–117 km/h from the quarter indicated and gusts which may exceed 180 km/h, and the wind condition is expected to persist."));

}
