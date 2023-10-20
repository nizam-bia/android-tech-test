package com.bridge.androidtechnicaltest.rx

import androidx.lifecycle.Observer
import com.bridge.androidtechnicaltest.db.Pupil
import com.bridge.androidtechnicaltest.db.PupilList
import com.bridge.androidtechnicaltest.model.PupilUi
import com.bridge.androidtechnicaltest.model.Resource
import com.bridge.androidtechnicaltest.model.Status
import com.bridge.androidtechnicaltest.repository.rx.IPupilRxRepository
import com.bridge.androidtechnicaltest.ui.pupillist.PupilListRxViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class PupilViewModelTest {

    private lateinit var pupilViewModel: PupilListRxViewModel
    private lateinit var pupilRepository: IPupilRxRepository

    private val syncStatusObserver: Observer<Status> = Observer { }
    private val pupilListObserver: Observer<Resource<List<PupilUi>>> = Observer { }

    @Rule
    @JvmField
    val testSchedulerRule = RxSchedulerRule()

    @Before
    fun setup() {
        pupilRepository = mockk(relaxed = true)
        pupilViewModel = spyk(PupilListRxViewModel(pupilRepository))
    }

    @Test
    fun testLoadPupilsSuccess() {
        val pupil1 = Pupil(1L, "Test1", "UK", "image", 23.08, 24.79)
        val pupil2 = Pupil(2L, "Test2", "UK", "image", 23.08, 24.79)
        val pupilList = PupilList(mutableListOf(pupil1, pupil2))
        pupilViewModel.pupilListLiveData.observeForever(pupilListObserver)

        every { pupilRepository.getOrFetchPupils() } returns Single.just(pupilList)

        pupilViewModel.loadPupils()

        Mockito.verify(pupilListObserver).onChanged(Resource.Success(pupilList.getPupilsForUi()))

        pupilViewModel.pupilListLiveData.removeObserver(pupilListObserver)
    }

    @Test
    fun testLoadPupilsError() {
        val error = Exception()
        pupilViewModel.pupilListLiveData.observeForever(pupilListObserver)

        every { pupilRepository.getOrFetchPupils() } returns Single.error(error)

        pupilViewModel.loadPupils()

        Mockito.verify(pupilListObserver).onChanged(Resource.Error(error))

        pupilViewModel.pupilListLiveData.removeObserver(pupilListObserver)
    }

    @Test
    fun testSyncDataSuccess() {
        val pupilList = PupilList(mutableListOf())
        pupilViewModel.pupilSyncStatusLiveData.observeForever(syncStatusObserver)
        every { pupilRepository.syncData() } returns Completable.complete()
        every { pupilRepository.getOrFetchPupils() } returns Single.just(pupilList)

        pupilViewModel.loadPupils()

        Mockito.verify(syncStatusObserver).onChanged(Status.SUCCESS)
        pupilViewModel.pupilSyncStatusLiveData.removeObserver(syncStatusObserver)
    }

    @Test
    fun testSyncDataError() {
        val error: Throwable = Exception()
        pupilViewModel.pupilSyncStatusLiveData.observeForever(syncStatusObserver)
        every { pupilRepository.syncData() } returns Completable.error(error)
        pupilViewModel.loadPupils()
        Mockito.verify(syncStatusObserver).onChanged(Status.ERROR)
        pupilViewModel.pupilSyncStatusLiveData.removeObserver(syncStatusObserver)
    }

}