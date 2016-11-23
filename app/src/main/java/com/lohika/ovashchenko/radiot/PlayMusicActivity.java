package com.lohika.ovashchenko.radiot;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PlayMusicActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private Handler handler;
    private PagerAdapter pagerAdapter;
    private PlayService mService;
    private RadioDB db;
    private boolean mBound = false;


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            PlayService.PlayBinder binder = (PlayService.PlayBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    // <editor-fold desc="ClickListener">  
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.play_pause:
//                    playMusic();
//                    break;
            }

        }
    };
    // </editor-fold>

    @Override
    protected void onStop() {
        super.onStop();
        if (this.mBound) {
            unbindService(this.mConnection);
            this.mBound = false;
        }
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

//        Intent intent = new Intent(this, PlayService.class);
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        db = new RadioDB(this);
        db.open();
        initToolbar();
        initViewPagerAndTabs();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        for (int i=0; i<pagerAdapter.getCount(); i++) {
                            ((SongsFragment)pagerAdapter.getItem(i)).refreshData();
                        }
                        RadioApplication.getInstance().synced();
                        break;
                }
            }
        };

        /*if (!RadioApplication.getInstance().isSynced()) {
            Thread thread = new Thread(new RadioConnector(handler, RadioApplication.getInstance().getRadioStationData().getStation("http://feeds.rucast.net/radio-t")));
            thread.start();
        }*/

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bndl) {
        return new RadioCursorLoader(this, db);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //RadioStation station = RadioApplication.getInstance().getRadioStationData().getStation("http://feeds.rucast.net/radio-t");
        RadioApplication.getInstance().getRadioStationData().addSongsToRadioData(RadioDB.cursorToListSongs(cursor));

        //RadioStation.Song song = station.new Song("SonName", "", "https://pp.vk.me/c637717/v637717670/148ef/HbtJRf52u9g.jpg", new Date(System.currentTimeMillis()));

        //station.addSong(song);
        //for (int i=0; i<pagerAdapter.getCount(); i++) {
               // ((SongsFragment)pagerAdapter.getItem(i)).refreshData();
        //}
        //RadioDB.addSongsToRadioData(RadioDB.cursorToListSongs(cursor));
        if (!RadioApplication.getInstance().isSynced()) {
            Thread thread = new Thread(new RadioConnector(handler, RadioApplication.getInstance().getRadioStationData().getStation("http://feeds.rucast.net/radio-t")));
            thread.start();
        }


        //for (int i=0; i<pagerAdapter.getCount(); i++) {
        //    ((SongsFragment)pagerAdapter.getItem(i)).refreshData();
        //}
        //scAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
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
        RadioApplication application = (RadioApplication) getApplication();

        for (RadioStation itemStation : application.getRadioStationData().getAllRadioStations()) {
            pagerAdapter.addFragment(SongsFragment.createInstance(itemStation), itemStation.getName());
        }

        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }

    static class PagerAdapter extends FragmentPagerAdapter {

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

    static class RadioCursorLoader extends CursorLoader {

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
}
