package com.bridge.androidtechnicaltest;

import android.arch.lifecycle.Observer;

import com.bridge.androidtechnicaltest.db.IPupilRepository;
import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.db.PupilList;
import com.bridge.androidtechnicaltest.model.PupilUi;
import com.bridge.androidtechnicaltest.model.Resource;
import com.bridge.androidtechnicaltest.model.Status;
import com.bridge.androidtechnicaltest.ui.pupillist.PupilListViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@RunWith(JUnit4.class)
public class PupilListViewModelTest {

    @Rule
    public RxSchedulerRule schedulerRule = new RxSchedulerRule();

    @Mock
    private IPupilRepository pupilRepository;
    private PupilListViewModel pupilListViewModel;

    private final Observer<Status> syncStatusObserver = status -> { };
    private final Observer<Resource<List<PupilUi>>> pupilListObserver = listResource -> { };

    @Before
    public void setup() {
        pupilListViewModel = new PupilListViewModel(pupilRepository);
    }

    @Test
    public void testLoadPupilsSuccess() {
        Pupil pupil1 = new Pupil(1L, "Test1","UK","image", 23.08, 24.79);
        Pupil pupil2 = new Pupil(2L, "Test2","UK","image", 23.08, 24.79);
        PupilList pupilList = new PupilList(Arrays.asList(pupil1, pupil2));
        pupilListViewModel.getPupilListLiveData().observeForever(pupilListObserver);
        Mockito.when(pupilRepository.getOrFetchPupils()).thenReturn(Single.just(pupilList));

        pupilListViewModel.loadPupils();

        Resource<List<PupilUi>> resourceSuccess = Resource.success(pupilList.getPupilsForUi());

        Mockito.verify(pupilListObserver).onChanged(resourceSuccess);

        pupilListViewModel.getPupilListLiveData().removeObserver(pupilListObserver);
    }

    @Test
    public void testLoadPupilsError() {
        Throwable error = new Exception();
        pupilListViewModel.getPupilListLiveData().observeForever(pupilListObserver);
        Mockito.when(pupilRepository.getOrFetchPupils()).thenReturn(Single.error(error));

        pupilListViewModel.loadPupils();

        Resource<List<PupilUi>> resourceError = Resource.error(error);

        Mockito.verify(pupilListObserver).onChanged(resourceError);

        pupilListViewModel.getPupilListLiveData().removeObserver(pupilListObserver);
    }

    @Test
    public void testSyncDataSuccess() {
        PupilList pupilList = new PupilList(Collections.emptyList());
        pupilListViewModel.getSyncStatusLiveData().observeForever(syncStatusObserver);

        Mockito.when(pupilRepository.syncData()).thenReturn(Completable.complete());
        Mockito.when(pupilRepository.getOrFetchPupils()).thenReturn(Single.just(pupilList));

        pupilListViewModel.loadPupils();

        Mockito.verify(syncStatusObserver).onChanged(Status.SUCCESS);

        pupilListViewModel.getSyncStatusLiveData().removeObserver(syncStatusObserver);
    }

    @Test
    public void testSyncDataError() {
        Throwable error = new Exception();
        pupilListViewModel.getSyncStatusLiveData().observeForever(syncStatusObserver);

        Mockito.when(pupilRepository.syncData()).thenReturn(Completable.error(error));

        pupilListViewModel.loadPupils();

        Mockito.verify(syncStatusObserver).onChanged(Status.ERROR);

        pupilListViewModel.getSyncStatusLiveData().removeObserver(syncStatusObserver);
    }
}
