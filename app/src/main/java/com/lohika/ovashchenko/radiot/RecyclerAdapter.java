package com.lohika.ovashchenko.radiot;

/**
 * Created by ovashchenko on 11/8/16.
 */


import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerItemViewHolder> {
    private RadioStation station;
    private ImageView lastPlayingSong;

    public void setStation(RadioStation station) {
        this.station = station;
    }

    @Override
    public RecyclerItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return RecyclerItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerItemViewHolder holder, int position) {
        final RadioStation.Song song = station.getSong(position);
        String itemText = song.getName();
        if (TextUtils.isEmpty(song.getImageURL())) {
            song.setImageURL("http://cs8.pikabu.ru/post_img/2016/11/23/5/1479882803155027024.jpg");
        }

        Glide
                .with(holder.mImageView.getContext())
                .load(song.getImageURL())
                .crossFade()
                .into(holder.mImageView);


        holder.mItemTextView.setText(itemText);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            private String status = Constants.PLAY;
            @Override
            public void onClick(View v) {
                if (lastPlayingSong != null && lastPlayingSong != holder.mPlayPause) {
                    lastPlayingSong.setImageResource(R.drawable.play);
                    RadioApplication.getInstance().pausePlaying();
                    holder.mPlayPause.setImageResource(R.drawable.pause);
                    lastPlayingSong = holder.mPlayPause;
                    status = Constants.PAUSE;
                    RadioApplication.getInstance().playSong(song.getLinkToSong());
                } else if (status.equals(Constants.PLAY)) {
                    holder.mPlayPause.setImageResource(R.drawable.pause);
                    lastPlayingSong = holder.mPlayPause;
                    status = Constants.PAUSE;
                    RadioApplication.getInstance().playSong(song.getLinkToSong());
                } else {
                    holder.mPlayPause.setImageResource(R.drawable.play);
                    status = Constants.PLAY;
                    RadioApplication.getInstance().pausePlaying();
                }


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

    static class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
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
