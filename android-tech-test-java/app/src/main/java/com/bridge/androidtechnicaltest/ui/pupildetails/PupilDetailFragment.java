package com.bridge.androidtechnicaltest.ui.pupildetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bridge.androidtechnicaltest.R;
import com.bridge.androidtechnicaltest.model.PupilUi;

public class PupilDetailFragment extends Fragment {

    public static String EXTRA_PUPIL = "pupilExtra";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pupildetail, container, false);
        if (getArguments() != null) {
            PupilUi pupil = (PupilUi) getArguments().getSerializable(EXTRA_PUPIL);
            if (pupil != null) {
                ((TextView) view.findViewById(R.id.pupil_id)).setText(String.valueOf(pupil.getPupilId()));
                ((TextView) view.findViewById(R.id.pupil_name)).setText(pupil.getPupilName());
                ((TextView) view.findViewById(R.id.country)).setText(pupil.getCountry());


            }
        }
        return view;
    }

}
