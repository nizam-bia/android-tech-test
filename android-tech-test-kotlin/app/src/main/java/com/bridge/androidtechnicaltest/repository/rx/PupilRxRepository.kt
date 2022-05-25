package com.bridge.androidtechnicaltest.repository.rx

import com.bridge.androidtechnicaltest.db.AppDatabase
import com.bridge.androidtechnicaltest.db.Pupil
import com.bridge.androidtechnicaltest.db.PupilList
import com.bridge.androidtechnicaltest.model.PupilDetailApi
import com.bridge.androidtechnicaltest.network.PupilApi
import io.reactivex.Completable
import io.reactivex.Single

interface IPupilRxRepository {
    fun getOrFetchPupils() : Single<PupilList>
    fun syncData() : Completable
}
class PupilRxRepository(
    private val database: AppDatabase,
    private val pupilApi: PupilApi
): IPupilRxRepository {

    override fun getOrFetchPupils(): Single<PupilList> {
        return database.pupilDao.getPupils().map { PupilList(it.toMutableList()) }
    }

    override fun syncData(): Completable {
        return Completable.create { emitter ->
            val startFromPage = 1
            try {
                val listOfPupilDetail = syncPupilsForPage(startFromPage).blockingGet()
                val listOfPupil = convertToPupil(listOfPupilDetail)

                database.pupilDao.clear()
                database.pupilDao.insert(listOfPupil)

                emitter.onComplete()
            } catch (ep: Exception) {
                emitter.onError(ep)
            }
        }
    }

    private fun convertToPupil(listOfPupilDetail: List<PupilDetailApi>): List<Pupil> {
        val listOfPupil: MutableList<Pupil> = ArrayList()
        for (pupilDetailApi in listOfPupilDetail) {
            listOfPupil.add(pupilDetailApi.mapToPupil())
        }
        return listOfPupil
    }

    private fun syncPupilsForPage(pageNo: Int): Single<MutableList<PupilDetailApi>> {
        val list: MutableList<PupilDetailApi> = ArrayList()
        return pupilApi.getPupils(pageNo)
            .retry(3)
            .map { pupilDataApi ->
                list.addAll(pupilDataApi.items)
                if (pageNo <= pupilDataApi.totalPages) {
                    val newList =
                        syncPupilsForPage(pageNo + 1).blockingGet()
                    newList.addAll(list)
                    return@map newList
                }
                list
            }
            .onErrorReturn {
                if (list.isNotEmpty()) return@onErrorReturn list
                throw RuntimeException("No pupils found")
            }
    }
}