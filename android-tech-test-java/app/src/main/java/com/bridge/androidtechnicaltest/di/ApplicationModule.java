package com.bridge.androidtechnicaltest.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final Context applicationContext;

    public ApplicationModule(Context applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Provides
    public Context provideApplicationContext() {
        return applicationContext;
    }
}
