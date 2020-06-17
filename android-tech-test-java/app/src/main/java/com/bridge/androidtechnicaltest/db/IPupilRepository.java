package com.bridge.androidtechnicaltest.db;

import io.reactivex.Single;

public interface IPupilRepository {
    Single<PupilList> getOrFetchPupils();
}
