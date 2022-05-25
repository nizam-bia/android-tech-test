package com.bridge.androidtechnicaltest.network

import com.bridge.androidtechnicaltest.model.PupilDataApi
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PupilApi {
    @GET("pupils")
    fun getPupils(@Query("page") page: Int = 1): Single<PupilDataApi>
}

interface PupilService {
    @GET("pupils")
    suspend fun getPupils(@Query("page") page: Int = 1): Response<PupilDataApi>
}