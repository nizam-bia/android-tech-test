package com.bridge.androidtechnicaltest.ui.pupillist;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.bridge.androidtechnicaltest.db.IPupilRepository;

import javax.inject.Inject;

public class PupilListViewModelFactory implements ViewModelProvider.Factory {

    private final IPupilRepository pupilRepository;

    @Inject
    public PupilListViewModelFactory(IPupilRepository pupilRepository) {
        this.pupilRepository = pupilRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new PupilListViewModel(pupilRepository);
    }
}
