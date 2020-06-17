package com.bridge.androidtechnicaltest.network;

import com.bridge.androidtechnicaltest.db.PupilList;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PupilService {
    @POST("reset")
    Completable reset();

    @GET("pupils")
    Single<PupilList> getPupils(@Query("page")Integer page);
}
