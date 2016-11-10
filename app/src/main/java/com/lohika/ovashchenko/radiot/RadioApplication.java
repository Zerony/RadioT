package com.lohika.ovashchenko.radiot;

import android.app.Application;
import android.content.res.Configuration;

/**
 * Created by ovashchenko on 11/8/16.
 */

public class RadioApplication extends Application {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}