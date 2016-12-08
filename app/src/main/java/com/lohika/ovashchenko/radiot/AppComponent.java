package com.lohika.ovashchenko.radiot;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by ovashchenko on 12/8/16.
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(PlayMusicActivity activity);
}
