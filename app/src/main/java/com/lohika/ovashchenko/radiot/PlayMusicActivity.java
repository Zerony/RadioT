package com.lohika.ovashchenko.radiot;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class PlayMusicActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private PagerAdapter pagerAdapter;
    private RadioDB db;
    private Handler refreshAdapterHandler;
    private Handler requestHandler;
    private AppComponent appComponent;
    @Inject RadioConnector connector;
    @Inject PlayController playController;



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new RadioCursorLoader(this, db);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //RadioApplication.RadioStationData radioStationData = RadioApplication.getInstance().getRadioStationData();
        PlayController.RadioStationData radioStationData = playController.getStationData();
        radioStationData.clearSongs();
        radioStationData.addSongsToRadioData(RadioDB.cursorToListSongs(cursor));

        for (RadioStation item : radioStationData.getAllRadioStations()) {
            item.fillLastSyncedSong();
        }

        if (!playController.isSynced()) {
            connector.setHandler(requestHandler);
            connector.setRadioStation(radioStationData.getAllRadioStations());
            Thread thread = new Thread(connector);
            thread.start();
        }

        refreshAdapterHandler.sendEmptyMessage(0);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        appComponent = ((RadioApplication) getApplication()).getAppComponent();
        appComponent.inject(this);

        requestHandler = new RequestHandler(this);
        refreshAdapterHandler = new RefreshAdapterHandler(this);
        db = new RadioDB(this);
        db.open();
        initToolbar();
        initViewPagerAndTabs();
        if (!playController.isSynced()) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    private void restartLoader() {
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.app_name));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    private void initViewPagerAndTabs() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        //RadioApplication application = (RadioApplication) getApplication();

        for (RadioStation itemStation : playController.getStationData().getAllRadioStations()) {
            pagerAdapter.addFragment(SongsFragment.createInstance(itemStation), itemStation.getName());
        }

        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void refreshAdapter() {
        for (int i=0; i<pagerAdapter.getCount(); i++) {
            ((SongsFragment)pagerAdapter.getItem(i)).refreshData();
        }
    }

    private static class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    private static class RadioCursorLoader extends CursorLoader {
        RadioDB db;

        public RadioCursorLoader(Context context, RadioDB db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            return db.getAllSongs();
        }

    }

    private static class RequestHandler extends Handler {
        private final WeakReference<PlayMusicActivity> mActivity;

        RequestHandler(PlayMusicActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mActivity.get().restartLoader();
                    break;
            }
        }
    }

    private static class RefreshAdapterHandler extends Handler {
        private final WeakReference<PlayMusicActivity> mActivityWeak;

        RefreshAdapterHandler(PlayMusicActivity activity) {
            mActivityWeak = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PlayMusicActivity activity = mActivityWeak.get();
            if (activity!= null) {
                activity.refreshAdapter();
            }

        }
    }
}
