package com.lohika.ovashchenko.radiot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SongsFragment extends Fragment {
    private final static String SAVED_RADIO_KEY = "RADIO_KEY";
    private final static String BUNDLE_RADIO_KEY = "STATION";
    private RecyclerAdapter recyclerAdapter;

    public static SongsFragment createInstance(RadioStation station) {
        SongsFragment songsFragment = new SongsFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_RADIO_KEY, station);
        songsFragment.setArguments(bundle);

        return songsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RadioStation station;
        if(savedInstanceState!=null) {
            String radioId = savedInstanceState.getString(SAVED_RADIO_KEY);
            RadioApplication application = (RadioApplication) getActivity().getApplication();
            station = application.getRadioStationData().getStation(radioId);
        } else {
            Bundle bundle = getArguments();
            station = (RadioStation) bundle.getSerializable(BUNDLE_RADIO_KEY);
        }
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_with_songs, container, false);
        setupRecyclerView(recyclerView, station);

        return recyclerView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle bundle = getArguments();
        RadioStation station = (RadioStation) bundle.getSerializable(BUNDLE_RADIO_KEY);

        outState.putString(SAVED_RADIO_KEY, station.getURL());
    }

    private void setupRecyclerView(RecyclerView recyclerView, RadioStation station) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.recyclerAdapter = new RecyclerAdapter();
        recyclerAdapter.setStation(station);
        recyclerView.setAdapter(this.recyclerAdapter);
    }

    public void refreshData() {
        RadioStation station = RadioApplication.getInstance().getRadioStationData().getStation(this.recyclerAdapter.getURL());
        this.recyclerAdapter.setStation(station);
    }
}
