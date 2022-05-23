package com.bridge.androidtechnicaltest.ui.pupilDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bridge.androidtechnicaltest.R
import com.bridge.androidtechnicaltest.model.PupilUi
import kotlinx.android.synthetic.main.fragment_pupildetail.view.*

class PupilDetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pupildetail, container, false)
        if (arguments != null) {
            val pupil = arguments?.getParcelable<PupilUi>(EXTRA_PUPIL)
            view.pupil_id.text = pupil?.pupilId.toString()
            view.pupil_name.text = pupil?.pupilName
            view.country.text = pupil?.country
        }
        return view
    }

    companion object {
        const val EXTRA_PUPIL = "pupil_data"
    }
}