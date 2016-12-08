package com.lohika.ovashchenko.radiot;

import android.app.Application;
import android.content.Context;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ovashchenko on 12/8/16.
 */
@Module
public class AppModule {
    private final Application context;

    AppModule(Application context) {
        this.context = context;
    }

    @Provides
    @Singleton
    static RadioDB provideRadioDb(Context context) {
        return new RadioDB(context);
    }

    @Provides
    @Singleton
    static HttpClient provideHttpClient() {
        return new DefaultHttpClient();
    }

    @Provides
    @Singleton
    static RadioConnector provideRadioConnector(HttpClient client, RadioDB db) {
        return new RadioConnector(client, db);
    }

    @Provides
    @Singleton
    Context provideContext() {
        return context;
    }
}
