package com.bridge.androidtechnicaltest.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bridge.androidtechnicaltest.R
import com.bridge.androidtechnicaltest.ui.pupillist.PupilListFragment
import com.bridge.androidtechnicaltest.ui.pupillist.PupilListViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    // TODO: Inject PupilListRxViewModel for RxJava implementation or use PupilListViewModel for coroutine
    private val pupilListViewModel by viewModel<PupilListViewModel>()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val fm = supportFragmentManager
            fm.beginTransaction()
                    .add(R.id.container, PupilListFragment())
                    .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_reset) {
            pupilListViewModel.syncPupils()
        }
        return super.onOptionsItemSelected(item)
    }
}