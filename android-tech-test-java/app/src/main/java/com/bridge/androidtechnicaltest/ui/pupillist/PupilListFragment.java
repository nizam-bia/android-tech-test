package com.bridge.androidtechnicaltest.ui.pupillist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bridge.androidtechnicaltest.R;
import com.bridge.androidtechnicaltest.adapter.PupilListAdapter;
import com.bridge.androidtechnicaltest.listeners.PupilOnClickListener;
import com.bridge.androidtechnicaltest.listeners.PupilOnDeleteListener;
import com.bridge.androidtechnicaltest.model.PupilUi;
import com.bridge.androidtechnicaltest.model.Status;
import com.bridge.androidtechnicaltest.ui.MainActivity;
import com.bridge.androidtechnicaltest.ui.pupildetails.PupilDetailFragment;

import java.util.Collections;

public class PupilListFragment extends Fragment implements PupilOnClickListener, PupilOnDeleteListener {

    private RecyclerView recyclerView;
    private PupilListAdapter pupilListAdapter;

    private PupilListViewModel pupilListViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        initViewModel();
        initDataLoad();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pupillist, container, false);
        initView(view);
        observeLiveData();
        return view;
    }

    private void initDataLoad() {
        pupilListViewModel.loadPupils();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void observeLiveData() {
        pupilListViewModel.getSyncStatusLiveData().observe(getViewLifecycleOwner(), status -> {
            if (status == Status.SUCCESS)
                pupilListViewModel.loadPupils();
            else if (status == Status.LOADING)
                Toast.makeText(getContext(), "Syncing...", Toast.LENGTH_LONG).show();
            else if (status == Status.ERROR)
                Toast.makeText(getContext(), "Error while syncing...", Toast.LENGTH_LONG).show();
        });

        pupilListViewModel.getPupilListLiveData().observe(getViewLifecycleOwner(), listResource -> {
            if (listResource != null) {
                if (listResource.getStatus() == Status.SUCCESS) {
                    pupilListAdapter.submitList(listResource.getData());
                } else if (listResource.getStatus() == Status.ERROR) {
                    Toast.makeText(getContext(), "Error on PupilList", Toast.LENGTH_SHORT).show();
                } else {
                    pupilListAdapter.submitList(Collections.emptyList());
                    pupilListAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Loading pupils", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.pupil_list);
        pupilListAdapter = new PupilListAdapter(this, this);
        recyclerView.setAdapter(pupilListAdapter);
    }

    private void initViewModel() {
        if (getActivity() instanceof MainActivity)
            pupilListViewModel = ((MainActivity) getActivity()).getPupilListViewModel();
    }

    @Override
    public void onClick(PupilUi pupil) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(PupilDetailFragment.EXTRA_PUPIL, pupil);
        PupilDetailFragment fragment = new PupilDetailFragment();
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDelete() {
        // TODO: delete pupil implementation here
    }
}
