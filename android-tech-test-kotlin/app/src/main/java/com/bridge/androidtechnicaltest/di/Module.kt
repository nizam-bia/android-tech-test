package com.bridge.androidtechnicaltest.di

import com.bridge.androidtechnicaltest.repository.coroutine.IPupilRepository
import com.bridge.androidtechnicaltest.repository.coroutine.PupilRepository
import com.bridge.androidtechnicaltest.repository.rx.IPupilRxRepository
import com.bridge.androidtechnicaltest.repository.rx.PupilRxRepository
import com.bridge.androidtechnicaltest.ui.pupillist.PupilListRxViewModel
import com.bridge.androidtechnicaltest.ui.pupillist.PupilListViewModel

import org.koin.dsl.module

val networkModule = module {
    factory { PupilAPIFactory.retrofitRxPupil() }
    factory { PupilAPIFactory.retrofitCoroutinePupil() }
}

val databaseModule = module {
    factory { DatabaseFactory.getDBInstance(get()) }
    single<IPupilRxRepository>{ PupilRxRepository(get(), get()) }
    single<IPupilRepository> { PupilRepository(get(), get()) }
}

val viewModelModule = module {
    single { PupilListRxViewModel(get()) }
    single { PupilListViewModel(get()) }
}

