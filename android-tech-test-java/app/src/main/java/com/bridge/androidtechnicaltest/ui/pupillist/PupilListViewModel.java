package com.bridge.androidtechnicaltest.ui.pupillist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.bridge.androidtechnicaltest.db.IPupilRepository;
import com.bridge.androidtechnicaltest.model.PupilUi;
import com.bridge.androidtechnicaltest.model.Resource;
import com.bridge.androidtechnicaltest.model.Status;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PupilListViewModel extends ViewModel {

    private final CompositeDisposable disposable;
    private final IPupilRepository pupilRepository;

    public PupilListViewModel(IPupilRepository pupilRepository) {
        disposable = new CompositeDisposable();
        this.pupilRepository = pupilRepository;
    }

    private final MutableLiveData<Resource<List<PupilUi>>> mutablePupilListLiveData = new MutableLiveData<>();
    private final MutableLiveData<Status> mutableSyncStatusLiveData = new MutableLiveData<>();

    public LiveData<Resource<List<PupilUi>>> getPupilListLiveData() {
        return mutablePupilListLiveData;
    }

    public LiveData<Status> getSyncStatusLiveData() {
        return mutableSyncStatusLiveData;
    }

    public void loadPupils() {
        mutablePupilListLiveData.setValue(Resource.loading());
        disposable.add(pupilRepository.getOrFetchPupils()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        pupilList -> {
                            if (pupilList.getPupilsForUi().isEmpty())
                                syncPupils();
                            else
                                mutablePupilListLiveData.setValue(Resource.success(pupilList.getPupilsForUi()));
                        },
                        throwable -> mutablePupilListLiveData.setValue(Resource.error(throwable))
                )
        );
    }

    public void syncPupils() {
        pupilRepository.syncData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mutableSyncStatusLiveData.setValue(Status.LOADING);
                    }

                    @Override
                    public void onComplete() {
                        mutableSyncStatusLiveData.setValue(Status.SUCCESS);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mutableSyncStatusLiveData.setValue(Status.ERROR);
                    }
                });
    }

    @Override
    public void onCleared() {
        disposable.clear();
    }
}
