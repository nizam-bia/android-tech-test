package com.bridge.androidtechnicaltest;

import com.bridge.androidtechnicaltest.db.AppDatabase;
import com.bridge.androidtechnicaltest.db.IPupilRepository;
import com.bridge.androidtechnicaltest.db.Pupil;
import com.bridge.androidtechnicaltest.db.PupilDao;
import com.bridge.androidtechnicaltest.db.PupilList;
import com.bridge.androidtechnicaltest.db.PupilRepository;
import com.bridge.androidtechnicaltest.model.PupilDataApi;
import com.bridge.androidtechnicaltest.model.PupilDetailApi;
import com.bridge.androidtechnicaltest.network.PupilService;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

@RunWith(MockitoJUnitRunner.class)
public class PupilRepositoryTest {

    @Mock
    private PupilService pupilService;
    @Mock
    private AppDatabase appDatabase;
    @Mock
    private PupilDao pupilDao;

    private IPupilRepository pupilRepository;

    @Before
    public void setup() {
        pupilRepository = new PupilRepository(appDatabase, pupilService);
        Mockito.when(appDatabase.getPupilDao()).thenReturn(pupilDao);
    }

    @Test
    public void testGetOrFetchPupilSuccess() {
        Pupil pupil1 = new Pupil(1L, "Test1","UK","image", 23.08, 24.79);
        Pupil pupil2 = new Pupil(2L, "Test2","UK","image", 23.08, 24.79);
        Mockito.when(pupilDao.getPupils()).thenReturn(Single.just(Arrays.asList(pupil1, pupil2)));
        TestObserver<PupilList> pupilList = pupilRepository.getOrFetchPupils().test();
        Assert.assertEquals(pupilList.values().get(0).getPupilList(), Arrays.asList(pupil1, pupil2));
    }

    @Test
    public void testSyncSuccessComplete() {
        PupilDetailApi pupilDetailApi1 = new PupilDetailApi(1, "UK", "Test1", "image", 23.2, 24.4);
        PupilDataApi pupilDataApi1 = new PupilDataApi(Collections.singletonList(pupilDetailApi1), 5, 1, 3);

        PupilDetailApi pupilDetailApi2 = new PupilDetailApi(2, "UK", "Test2", "image", 23.2, 24.4);
        PupilDataApi pupilDataApi2 = new PupilDataApi(Collections.singletonList(pupilDetailApi2), 5, 2, 3);

        PupilDetailApi pupilDetailApi3 = new PupilDetailApi(3, "UK", "Test3", "image", 23.2, 24.4);
        PupilDataApi pupilDataApi3 = new PupilDataApi(Collections.singletonList(pupilDetailApi3), 5, 3, 3);

        Mockito.when(pupilService.getPupils(1)).thenReturn(Single.just(pupilDataApi1));
        Mockito.when(pupilService.getPupils(2)).thenReturn(Single.just(pupilDataApi2));
        Mockito.when(pupilService.getPupils(3)).thenReturn(Single.just(pupilDataApi3));

        TestObserver<Void> observer = pupilRepository.syncData().test();

        Mockito.verify(pupilDao, Mockito.times(1)).clear();

        observer.assertNoErrors();
        observer.assertComplete();
    }

    @Test
    public void testSyncError() {
        Throwable error = new RuntimeException();

        Mockito.when(pupilService.getPupils(1)).thenReturn(Single.error(error));

        TestObserver<Void> observer = pupilRepository.syncData().test();

        Mockito.verify(pupilDao, Mockito.times(0)).clear();

        observer.assertError(RuntimeException.class);
        observer.assertNotComplete();
    }
}
