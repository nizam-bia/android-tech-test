package com.bridge.androidtechnicaltest.ui.pupillist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bridge.androidtechnicaltest.R
import com.bridge.androidtechnicaltest.adapter.PupilListAdapter
import com.bridge.androidtechnicaltest.model.PupilUi
import com.bridge.androidtechnicaltest.model.Resource
import com.bridge.androidtechnicaltest.model.Status
import com.bridge.androidtechnicaltest.ui.pupilDetail.PupilDetailFragment
import kotlinx.android.synthetic.main.fragment_pupillist.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class PupilListFragment : Fragment() {

    private val pupilListViewModel by viewModel<PupilListViewModel>()

    private lateinit var pupilListAdapter: PupilListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        initLoadPupils()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_pupillist, container, false)
        initRecyclerView(view)
        observeLiveData()
        return view
    }

    private fun initLoadPupils() {
        pupilListViewModel.loadPupils()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeLiveData() {
        pupilListViewModel.pupilListLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    pupilListAdapter.submitList(emptyList())
                    pupilListAdapter.notifyDataSetChanged()
                    Toast.makeText(
                        requireContext(),
                        "Loading pupils...",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                is Resource.Success -> {
                    pupilListAdapter.submitList(it.data)
                    pupilListAdapter.notifyDataSetChanged()
                }
                is Resource.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Error loading pupils",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        })

        pupilListViewModel.pupilSyncStatusLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                Status.SUCCESS -> pupilListViewModel.loadPupils()
                Status.LOADING -> Toast.makeText(requireContext(), "Syncing...", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(requireContext(), "Error while syncing...", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initRecyclerView(view: View) {
        pupilListAdapter = PupilListAdapter({
            showPupilDetails(it)
        }, {
            // TODO: Delete pupil implementation here
        })
        view.pupil_list.adapter = pupilListAdapter
    }

    private fun showPupilDetails(pupilUi: PupilUi) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, PupilDetailFragment().apply {
                arguments = Bundle().also { it.putParcelable(PupilDetailFragment.EXTRA_PUPIL, pupilUi) }
            })
            .addToBackStack(null)
            .commit()
    }
}