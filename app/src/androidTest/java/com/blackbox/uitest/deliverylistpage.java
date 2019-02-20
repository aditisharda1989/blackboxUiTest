package com.blackbox.uitest;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

/*
  Class to define UiObjects on delivery list page
 */
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

class deliverylistpage {
    UiScrollable deliverylist = new UiScrollable(new UiSelector().scrollable(true));
    private UiDevice phonedevice = UiDevice.getInstance(getInstrumentation());
    UiObject titlebar = phonedevice.findObject(new UiSelector().text("Delivery List"));
}
