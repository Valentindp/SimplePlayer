package com.example.java.simpleplayer.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.java.simpleplayer.R;
import com.example.java.simpleplayer.models.Song;
import com.example.java.simpleplayer.presenters.SongRepository;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by java on 18.01.2017.
 */

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlayListItemViewHolder> {

    private List<Song> mDataSource = null;

    private View.OnClickListener mOnItemClickListener = null;

    public View.OnClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(View.OnClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }



    public void setDataSource(List<Song> songs) {
        mDataSource = songs;
        notifyDataSetChanged();
    }

    @Override
    public PlayListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater.inflate(R.layout.item_playlist_song, parent, false);
        return new PlayListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlayListItemViewHolder holder, int position) {
        final Song song = mDataSource.get(position);
        holder.bind(song);
        holder.itemView.setOnClickListener(mOnItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mDataSource == null ? 0 : mDataSource.size();
    }

   public class PlayListItemViewHolder extends RecyclerView.ViewHolder {

        private Song mSong;

        @BindView(R.id.song_text_view_pl)
        protected TextView mTitleTextView;
        @BindView(R.id.artist_text_view_pl)
        protected TextView mArtistTextView;
       @BindView(R.id.duration_view_pl)
       protected TextView mDurationView;



        public PlayListItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(@NonNull Song song) {
            mSong = song;
            mArtistTextView.setText(song.getArtistName());
            mTitleTextView.setText(song.getTitle());
//            mDurationView.setText(song.getDuration());

        }

        public Song getSong() {
            return mSong;
        }

    }

}