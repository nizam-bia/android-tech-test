package com.bridge.androidtechnicaltest.repository.coroutine

import com.bridge.androidtechnicaltest.db.AppDatabase
import com.bridge.androidtechnicaltest.db.PupilList
import com.bridge.androidtechnicaltest.model.PupilDetailApi
import com.bridge.androidtechnicaltest.network.PupilService
import kotlinx.coroutines.delay

interface IPupilRepository {
    suspend fun getOrFetchPupils(): PupilList
    suspend fun syncData()
}

class PupilRepository(
    private val database: AppDatabase,
    private val service: PupilService
) : IPupilRepository {

    override suspend fun getOrFetchPupils(): PupilList {
        return PupilList(database.pupilDao.getAllPupils().toMutableList())
    }

    override suspend fun syncData() {
        val startFromPage = 1
        val listOfPupilDetail = syncPupilsForPage(startFromPage)

        database.pupilDao.clear()
        database.pupilDao.insert(listOfPupilDetail.map { it.mapToPupil() })
    }

    private suspend fun syncPupilsForPage(pageNo: Int): List<PupilDetailApi> {
        val list = mutableListOf<PupilDetailApi>()
        val networkCall = retryIO(times = 3) { service.getPupils(pageNo) }
        val responseBody = networkCall.body()

        if (networkCall.isSuccessful && responseBody != null) {
            list.addAll(responseBody.items)
            return if (pageNo < responseBody.totalPages)
                syncPupilsForPage(pageNo + 1).plus(list)
            else
                list

        } else if (pageNo > 1) return list

        throw RuntimeException("Pupils Not found")
    }

    private suspend fun <T> retryIO(
        times: Int = Int.MAX_VALUE,
        initialDelay: Long = 100, // 0.1 second
        maxDelay: Long = 1000,    // 1 second
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(times - 1) {
            try {
                return block()
            } catch (e: Exception) {
                // log an error
            }
            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }
        return block()
    }
}