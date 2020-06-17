package com.bridge.androidtechnicaltest.di;

import com.bridge.androidtechnicaltest.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {
        ApplicationModule.class,
        NetworkModule.class,
        DatabaseModule.class,
})
@Singleton
public abstract class ApplicationComponent {
    public abstract void inject(MainActivity mainActivity);
}
