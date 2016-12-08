package com.lohika.ovashchenko.radiot;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;

import com.lohika.ovashchenko.radiot.parser.RadioTParser;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ovashchenko on 11/8/16.
 */

public class RadioApplication extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}