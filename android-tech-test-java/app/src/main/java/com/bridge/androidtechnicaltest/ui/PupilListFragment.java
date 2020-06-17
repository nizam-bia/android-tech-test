package com.bridge.androidtechnicaltest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bridge.androidtechnicaltest.R;

public class PupilListFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pupillist, container, false);
        recyclerView = view.findViewById(R.id.pupil_list);

        return view;
    }
}
