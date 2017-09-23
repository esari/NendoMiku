package com.gromanu.nendomiku;

import android.app.Application;

import com.gromanu.nendomiku.controller.DataController;
import com.gromanu.nendomiku.controller.ImageController;

public class MikuApplication extends Application {

    //private static String DOMAIN = "http://192.168.0.13";
    private static String DOMAIN = "http://gromanu.solarlogic.net";

    private final DataController dataController = new DataController(DOMAIN);

    private final ImageController imageController = new ImageController(DOMAIN);

    public DataController getDataController() {
        return dataController;
    }

    public ImageController getImageController() {
        return imageController;
    }
}
