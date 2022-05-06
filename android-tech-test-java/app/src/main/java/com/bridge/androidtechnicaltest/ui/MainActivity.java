package com.bridge.androidtechnicaltest.ui;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bridge.androidtechnicaltest.App;
import com.bridge.androidtechnicaltest.R;
import com.bridge.androidtechnicaltest.db.PupilDao;
import com.bridge.androidtechnicaltest.network.PupilService;
import com.bridge.androidtechnicaltest.ui.pupillist.PupilListFragment;
import com.bridge.androidtechnicaltest.ui.pupillist.PupilListViewModel;
import com.bridge.androidtechnicaltest.ui.pupillist.PupilListViewModelFactory;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();

    @Inject
    PupilDao pupilDao;
    @Inject
    PupilService pupilService;
    @Inject
    PupilListViewModelFactory pupilListViewModelFactory;
    private PupilListViewModel pupilListViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getApplicationComponent().inject(this);
        setContentView(R.layout.activity_main);
        initViewModel();

        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .add(R.id.container, new PupilListFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_reset) {
            resetApiData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetApiData() {
        pupilListViewModel.syncPupils();
    }

    private void initViewModel() {
        pupilListViewModel =  pupilListViewModelFactory.create(PupilListViewModel.class);
    }

    private void onDataResetFailed() {
        Snackbar.make(findViewById(R.id.main_layout),
                R.string.data_reset_failed, Snackbar.LENGTH_SHORT).show();
    }

    private void onDataReset() {
        Snackbar.make(findViewById(R.id.main_layout),
                R.string.data_reset, Snackbar.LENGTH_SHORT).show();
    }

    public PupilListViewModel getPupilListViewModel() {
        return pupilListViewModel;
    }
}
