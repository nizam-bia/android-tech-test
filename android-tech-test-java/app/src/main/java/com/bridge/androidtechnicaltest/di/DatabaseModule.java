package com.bridge.androidtechnicaltest.di;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.bridge.androidtechnicaltest.db.AppDatabase;
import com.bridge.androidtechnicaltest.db.IPupilRepository;
import com.bridge.androidtechnicaltest.db.PupilDao;
import com.bridge.androidtechnicaltest.db.PupilRepository;
import com.bridge.androidtechnicaltest.network.PupilService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {
    private static final String APP_DATABASE = "bridgetest.db";

    @Provides
    @Singleton
    public AppDatabase provideLocalGameDatabase(Context applicationContext) {
        return Room.databaseBuilder(applicationContext, AppDatabase.class, APP_DATABASE).build();
    }

    @Provides
    public PupilDao providePupilDao(AppDatabase localGameDatabase) {
        return localGameDatabase.getPupilDao();
    }


    @Provides
    public IPupilRepository providePupilRepository(AppDatabase database, PupilService pupilApi) {
        return new PupilRepository(database, pupilApi);
    }

}
