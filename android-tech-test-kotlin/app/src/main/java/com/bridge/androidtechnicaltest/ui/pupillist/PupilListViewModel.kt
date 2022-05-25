package com.bridge.androidtechnicaltest.ui.pupillist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bridge.androidtechnicaltest.model.PupilUi
import com.bridge.androidtechnicaltest.model.Resource
import com.bridge.androidtechnicaltest.model.Status
import com.bridge.androidtechnicaltest.repository.coroutine.IPupilRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PupilListViewModel(private val pupilRepository: IPupilRepository) : ViewModel() {

    private val mutablePupilListLiveData: MutableLiveData<Resource<List<PupilUi>>> = MutableLiveData()
    val pupilListLiveData: LiveData<Resource<List<PupilUi>>> get() = mutablePupilListLiveData

    private val mutableSyncStatusLiveData: MutableLiveData<Status> = MutableLiveData()
    val pupilSyncStatusLiveData: LiveData<Status> get() = mutableSyncStatusLiveData

    fun loadPupils() {
        viewModelScope.launch(Dispatchers.IO) {
            mutablePupilListLiveData.postValue(Resource.Loading())
            try {
                val pupils = pupilRepository.getOrFetchPupils().getPupilsForUi()
                if (pupils.isEmpty()) syncPupils()
                else mutablePupilListLiveData.postValue(Resource.Success(pupils))
            } catch (ep: Exception) {
                mutablePupilListLiveData.postValue(Resource.Error(ep))
            }
        }
    }

    fun syncPupils() {
        viewModelScope.launch(Dispatchers.IO) {
            mutableSyncStatusLiveData.postValue(Status.LOADING)
            try {
                pupilRepository.syncData()
                mutableSyncStatusLiveData.postValue(Status.SUCCESS)
            } catch (ep: Exception) {
                mutableSyncStatusLiveData.postValue(Status.ERROR)
            }
        }
    }
}