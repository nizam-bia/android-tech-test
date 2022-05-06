package com.bridge.androidtechnicaltest.db;

import com.bridge.androidtechnicaltest.model.PupilDetailApi;
import com.bridge.androidtechnicaltest.network.PupilService;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class PupilRepository implements IPupilRepository {

    PupilService pupilApi;
    AppDatabase database;

    public PupilRepository(AppDatabase database, PupilService pupilApi) {
        this.pupilApi = pupilApi;
        this.database = database;
    }

    @Override
    public Single<PupilList> getOrFetchPupils() {
        return database.getPupilDao().getPupils().map(PupilList::new);
    }

    @Override
    public Completable syncData() {
        return Completable.create(emitter -> {
            int startFromPage = 1;
            try {
                List<PupilDetailApi> listOfPupilDetail = syncPupilsForPage(startFromPage).blockingGet();
                List<Pupil> listOfPupil = convertToPupil(listOfPupilDetail);

                database.getPupilDao().clear();
                database.getPupilDao().insert(listOfPupil);
                emitter.onComplete();
            } catch (Exception ep) {
                emitter.onError(ep);
            }
        });
    }

    private List<Pupil> convertToPupil(List<PupilDetailApi> listOfPupilDetail) {
        List<Pupil> listOfPupil = new ArrayList<>();
        for (PupilDetailApi pupilDetailApi : listOfPupilDetail) {
            listOfPupil.add(pupilDetailApi.mapToPupil());
        }
        return listOfPupil;
    }

    private Single<List<PupilDetailApi>> syncPupilsForPage(int pageNo) {
        List<PupilDetailApi> list = new ArrayList<>();
        return pupilApi.getPupils(pageNo)
                .retry(3)
                .map(pupilDataApi -> {
                    list.addAll(pupilDataApi.getItems());
                    if (pageNo <= pupilDataApi.getTotalPages()) {
                        List<PupilDetailApi> newList = syncPupilsForPage(pageNo + 1).blockingGet();
                        newList.addAll(list);
                        return newList;
                    }
                    return list;
                })
                .onErrorReturn(throwable -> {
                    if (!list.isEmpty()) return list;
                    throw new RuntimeException("No pupils found");
                });
    }
}
