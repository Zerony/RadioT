package com.lohika.ovashchenko.radiot;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class PlayMusicActivity extends AppCompatActivity {

    private Handler handler;
    private PagerAdapter pagerAdapter;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        //startService(new Intent(this, PlayService.class));
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

        if (!RadioApplication.getInstance().isSynced()) {
            Thread thread = new Thread(new RadioConnector(handler, RadioApplication.getInstance().getRadioStationData().getStation("http://feeds.rucast.net/radio-t")));
            thread.start();
        }

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

}
