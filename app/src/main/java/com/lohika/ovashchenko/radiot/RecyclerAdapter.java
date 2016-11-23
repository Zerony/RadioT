package com.lohika.ovashchenko.radiot;

/**
 * Created by ovashchenko on 11/8/16.
 */


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RadioStation station;

    public void setStation(RadioStation station) {
        this.station = station;
        this.notifyDataSetChanged();
    }

    public String getURL() {
        if (station == null) {
            return "";
        }
        return station.getURL();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return RecyclerItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
        RadioStation.Song song = this.station.getSongs().get(position);
        String itemText = song.getName();
        if (song.getImageURL() != null) {
            Glide
                    .with(holder.mImageView.getContext())
                    .load(song.getImageURL())
                    .crossFade()
                    .into(holder.mImageView);
        }

        holder.mItemTextView.setText(itemText);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        holder.mPlayPause.setOnClickListener(onClickListener);
    }


    @Override
    public int getItemCount() {
        if (this.station == null) {
            return 0;
        }
        if (this.station.getSongs() == null) {
            return 0;
        }

        return this.station.getSongs().size();
    }

    private static class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView mItemTextView;
        private final ImageView mImageView;
        private final ImageButton mPlayPause;

        public RecyclerItemViewHolder(final View parent) {
            super(parent);
            mItemTextView = (TextView) parent.findViewById(R.id.itemTextView);
            mImageView = (ImageView) parent.findViewById(R.id.song_image);
            mPlayPause = (ImageButton)parent.findViewById(R.id.play_pause);
        }

        public static RecyclerItemViewHolder newInstance(View parent) {
            return new RecyclerItemViewHolder(parent);
        }
    }

}
