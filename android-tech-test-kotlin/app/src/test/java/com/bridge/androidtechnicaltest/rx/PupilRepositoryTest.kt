package com.bridge.androidtechnicaltest.rx

import com.bridge.androidtechnicaltest.db.AppDatabase
import com.bridge.androidtechnicaltest.db.Pupil
import com.bridge.androidtechnicaltest.db.PupilDao
import com.bridge.androidtechnicaltest.model.PupilDataApi
import com.bridge.androidtechnicaltest.model.PupilDetailApi
import com.bridge.androidtechnicaltest.network.PupilApi
import com.bridge.androidtechnicaltest.repository.rx.IPupilRxRepository
import com.bridge.androidtechnicaltest.repository.rx.PupilRxRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.*

class PupilRepositoryTest {

    private lateinit var pupilRepository: IPupilRxRepository
    private lateinit var database: AppDatabase
    private lateinit var service: PupilApi
    private lateinit var pupilDao: PupilDao


    @Rule
    @JvmField
    var testSchedulerRule = RxSchedulerRule()

    @Before
    fun setup() {
        database = mockk(relaxed = true)
        service = mockk(relaxed = true)
        pupilDao = mockk(relaxed = true)
        pupilRepository = PupilRxRepository(database, service)

        every { database.pupilDao } returns pupilDao
    }

    @Test
    fun testGetOrFetchPupilsSuccess() {
        val pupil1 = Pupil(1L, "Test1", "UK", "image", 23.08, 24.79)
        val pupil2 = Pupil(2L, "Test2", "UK", "image", 23.08, 24.79)
        every { pupilDao.getPupils() } returns Single.just(listOf(pupil1, pupil2))
        val testObserver = pupilRepository.getOrFetchPupils().test()

        Assert.assertEquals(testObserver.values().first().items, listOf(pupil1, pupil2))
    }

    @Test
    fun testSyncSuccessComplete() {
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

        every { service.getPupils(1) } returns Single.just(pupilDataApi1)
        every { service.getPupils(2) } returns Single.just(pupilDataApi2)
        every { service.getPupils(3) } returns Single.just(pupilDataApi3)

        val test = pupilRepository.syncData().test()

        verify(exactly = 1) { pupilDao.clear() }

        test.assertNoErrors()
        test.assertComplete()
    }

    @Test
    fun testSyncError() {
        val error = RuntimeException("No pupils found")

        every { service.getPupils(1) } returns Single.error(error)

        val observer = pupilRepository.syncData().test()

        verify(exactly = 0) { pupilDao.clear() }

        observer.assertNotComplete()
    }
}