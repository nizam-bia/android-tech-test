package com.bridge.androidtechnicaltest.db;

import com.bridge.androidtechnicaltest.network.PupilService;

import java.util.ArrayList;

import io.reactivex.Single;

public class PupilRepository implements IPupilRepository {

    PupilService pupilApi;
    AppDatabase database;

    public PupilRepository(AppDatabase database, PupilService pupilApi) {
        this.pupilApi = pupilApi;
        this.database = database;
    }

    @Override
    public Single<PupilList> getOrFetchPupils() {
        // TODO("Continue with the implementation here")
        return Single.just(new PupilList(new ArrayList<Pupil>()));
    }
}
