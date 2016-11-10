package com.lohika.ovashchenko.radiot;

/**
 * Created by ovashchenko on 11/8/16.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RadioStation.Song> songs;
    private Context context;

    public RecyclerAdapter(RadioStation station) {
        this.songs = station.getSongs();
    }

    public RecyclerAdapter(List<RadioStation.Song> songs) {
        this.songs = songs;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(this.context).inflate(R.layout.recycler_item, parent, false);
        return RecyclerItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
        String itemText = songs.get(position).getName();
        Drawable image = songs.get(position).getImage(this.context);
        holder.mItemTextView.setText(itemText);
        holder.mImageView.setImageDrawable(image);
    }

    @Override
    public int getItemCount() {
        if (songs == null) {
            return 0;
        }

        return songs.size();
    }

    private static class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView mItemTextView;
        private final ImageView mImageView;

        public RecyclerItemViewHolder(final View parent) {
            super(parent);
            mItemTextView = (TextView) parent.findViewById(R.id.itemTextView);
            mImageView = (ImageView) parent.findViewById(R.id.song_image);
        }

        public static RecyclerItemViewHolder newInstance(View parent) {
            return new RecyclerItemViewHolder(parent);
        }
    }


}
