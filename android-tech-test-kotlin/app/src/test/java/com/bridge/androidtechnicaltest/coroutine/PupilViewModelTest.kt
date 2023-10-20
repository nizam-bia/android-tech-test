package com.bridge.androidtechnicaltest.coroutine

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.bridge.androidtechnicaltest.db.Pupil
import com.bridge.androidtechnicaltest.db.PupilList
import com.bridge.androidtechnicaltest.model.Status
import com.bridge.androidtechnicaltest.repository.coroutine.IPupilRepository
import com.bridge.androidtechnicaltest.ui.pupillist.PupilListViewModel
import com.nhaarman.mockitokotlin2.mock
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class PupilViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var pupilViewModel: PupilListViewModel
    private lateinit var pupilRepository: IPupilRepository

    private val syncStatusObserver : Observer<Status> = mock()

    @Before
    fun setup() {
        pupilRepository = mockk(relaxed = true)
        pupilViewModel = spyk(PupilListViewModel(pupilRepository))
    }

    @Test
    fun testLoadPupilsSuccess() = testCoroutineRule.runBlockingTest {
        val pupil1 = Pupil(1L, "Test1", "UK", "image", 23.08, 24.79)
        val pupil2 = Pupil(2L, "Test2", "UK", "image", 23.08, 24.79)
        val pupilList = PupilList(mutableListOf(pupil1, pupil2))

        coEvery { pupilRepository.getOrFetchPupils() } returns pupilList

        pupilViewModel.loadPupils()

        coVerify(exactly = 1) { pupilRepository.getOrFetchPupils() }
    }

    @Test
    fun testSyncDataSuccess() = testCoroutineRule.runBlockingTest {
        val pupilList = PupilList(mutableListOf())
        pupilViewModel.pupilSyncStatusLiveData.observeForever(syncStatusObserver)
        coEvery { pupilRepository.syncData() } just runs
        coEvery { pupilRepository.getOrFetchPupils() } returns pupilList

        pupilViewModel.loadPupils()

        Mockito.verify(syncStatusObserver).onChanged(Status.LOADING)
        delay(200)
        Mockito.verify(syncStatusObserver).onChanged(Status.SUCCESS)
        pupilViewModel.pupilSyncStatusLiveData.removeObserver(syncStatusObserver)
    }

    @Test
    fun testSyncDataError() = testCoroutineRule.runBlockingTest {
        val error: Throwable = Exception()
        pupilViewModel.pupilSyncStatusLiveData.observeForever(syncStatusObserver)

        coEvery { pupilRepository.getOrFetchPupils() } returns PupilList(mutableListOf())
        coEvery { pupilRepository.syncData() } throws error

        pupilViewModel.loadPupils()

        Mockito.verify(syncStatusObserver).onChanged(Status.LOADING)
        delay(200)
        Mockito.verify(syncStatusObserver).onChanged(Status.ERROR)
        pupilViewModel.pupilSyncStatusLiveData.removeObserver(syncStatusObserver)
    }
}