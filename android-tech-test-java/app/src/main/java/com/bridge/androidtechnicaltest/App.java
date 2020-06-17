package com.bridge.androidtechnicaltest;

import android.app.Application;

import com.bridge.androidtechnicaltest.di.ApplicationComponent;
import com.bridge.androidtechnicaltest.di.ApplicationModule;
import com.bridge.androidtechnicaltest.di.DaggerApplicationComponent;

public class App extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
