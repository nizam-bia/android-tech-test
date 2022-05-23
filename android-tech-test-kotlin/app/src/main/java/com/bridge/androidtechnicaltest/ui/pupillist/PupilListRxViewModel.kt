package com.bridge.androidtechnicaltest.ui.pupillist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bridge.androidtechnicaltest.repository.rx.IPupilRxRepository
import com.bridge.androidtechnicaltest.db.PupilList
import com.bridge.androidtechnicaltest.model.PupilUi
import com.bridge.androidtechnicaltest.model.Resource
import com.bridge.androidtechnicaltest.model.Status
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class PupilListRxViewModel(private val pupilRxRepository: IPupilRxRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val mutablePupilListLiveData: MutableLiveData<Resource<List<PupilUi>>> = MutableLiveData()
    val pupilListLiveData: LiveData<Resource<List<PupilUi>>> get() = mutablePupilListLiveData

    private val mutableSyncStatusLiveData: MutableLiveData<Status> = MutableLiveData()
    val pupilSyncStatusLiveData: LiveData<Status> get() = mutableSyncStatusLiveData

    fun loadPupils() {
        mutablePupilListLiveData.value = Resource.Loading()
        disposable.add(pupilRxRepository.getOrFetchPupils()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { pupilList: PupilList ->
                    if (pupilList.getPupilsForUi().isEmpty())
                        syncPupils()
                    else
                        mutablePupilListLiveData.value = Resource.Success(
                            pupilList.getPupilsForUi()
                        )
                }
            ) { throwable ->
                mutablePupilListLiveData.value = Resource.Error(throwable)
            }
        )
    }

    fun syncPupils() {
        pupilRxRepository.syncData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    mutableSyncStatusLiveData.value = Status.LOADING
                }

                override fun onComplete() {
                    mutableSyncStatusLiveData.value = Status.SUCCESS
                }

                override fun onError(e: Throwable) {
                    mutableSyncStatusLiveData.value = Status.ERROR
                }
            })
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}