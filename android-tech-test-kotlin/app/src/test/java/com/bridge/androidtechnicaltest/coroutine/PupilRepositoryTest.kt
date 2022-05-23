package com.bridge.androidtechnicaltest.coroutine

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bridge.androidtechnicaltest.db.AppDatabase
import com.bridge.androidtechnicaltest.db.Pupil
import com.bridge.androidtechnicaltest.db.PupilDao
import com.bridge.androidtechnicaltest.model.PupilDataApi
import com.bridge.androidtechnicaltest.model.PupilDetailApi
import com.bridge.androidtechnicaltest.network.PupilService
import com.bridge.androidtechnicaltest.repository.coroutine.IPupilRepository
import com.bridge.androidtechnicaltest.repository.coroutine.PupilRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class PupilRepositoryTest {

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var pupilRepository: IPupilRepository
    private lateinit var database: AppDatabase
    private lateinit var service: PupilService
    private lateinit var pupilDao: PupilDao

    @Before
    fun setup() {
        database = mockk(relaxed = true)
        service = mockk(relaxed = true)
        pupilDao = mockk(relaxed = true)
        pupilRepository = PupilRepository(database, service)

        every { database.pupilDao } returns pupilDao
    }

    @Test
    fun testGetOrFetchPupilsSuccess() = runBlockingTest {
        val pupil1 = Pupil(1L, "Test1", "UK", "image", 23.08, 24.79)
        val pupil2 = Pupil(2L, "Test2", "UK", "image", 23.08, 24.79)
        coEvery { pupilDao.getAllPupils() } returns listOf(pupil1, pupil2)
        val testObserver = pupilRepository.getOrFetchPupils()

        Assert.assertEquals(testObserver.items, listOf(pupil1, pupil2))
    }

    @Test
    fun testSyncSuccessComplete() = runBlockingTest {
        val pupilDetailApi1 = PupilDetailApi(1, "UK", "Test1", "image", 23.2, 24.4)
        val pupilDataApi1 = PupilDataApi(
            listOf(pupilDetailApi1),
            5,
            1,
            3
        )

        val pupilDetailApi2 = PupilDetailApi(2, "UK", "Test2", "image", 23.2, 24.4)
        val pupilDataApi2 = PupilDataApi(
            listOf(pupilDetailApi2),
            5,
            2,
            3
        )

        val pupilDetailApi3 = PupilDetailApi(3, "UK", "Test3", "image", 23.2, 24.4)
        val pupilDataApi3 = PupilDataApi(
            listOf(pupilDetailApi3),
            5,
            3,
            3
        )

        coEvery { service.getPupils(1) } returns Response.success(pupilDataApi1)
        coEvery { service.getPupils(2) } returns Response.success(pupilDataApi2)
        coEvery { service.getPupils(3) } returns Response.success(pupilDataApi3)

        pupilRepository.syncData()

        verify(exactly = 1) { pupilDao.clear() }
    }

    @Test
    fun testSyncError() = runBlockingTest {
        var exceptionThrown = false
        coEvery { service.getPupils(1) } returns Response.error(404, ResponseBody.create(null, "Not found response body"))

        try {
            pupilRepository.syncData()
        } catch (ep: Exception) {
            exceptionThrown = true
        }

        assert(exceptionThrown)

        verify(exactly = 0) { pupilDao.clear() }
    }


}