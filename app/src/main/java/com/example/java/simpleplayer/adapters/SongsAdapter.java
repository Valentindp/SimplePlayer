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
 * Created by Valentin on 06.12.2016.
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsViewHolder> {

    private List<Song> mDataSource = null;

    private View.OnClickListener mOnItemClickListener = null;

    private View.OnLongClickListener mOnLongClickListener = null;


    public View.OnLongClickListener getOnLongClickListener() {
        return mOnLongClickListener;
    }

    public void setOnItemLongClickListener(View.OnLongClickListener onItemLongClickListener) {
        this.mOnLongClickListener = onItemLongClickListener;
    }


    public View.OnClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setDataSource(List<Song> dataSource) {
        this.mDataSource = dataSource;
        notifyDataSetChanged();
    }



    @Override
    public SongsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View view = layoutInflater.inflate(R.layout.item_main_song, parent, false);
        return new SongsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SongsViewHolder holder, int position) {
        Song song = mDataSource.get(position);
        holder.bindView(song);
        holder.itemView.setOnClickListener(mOnItemClickListener);
        holder.itemView.setOnLongClickListener(mOnLongClickListener);
    }



    @Override
    public int getItemCount() {
        return mDataSource == null ? 0 : mDataSource.size();
    }

    public List<Song> getDataSource() {
        return mDataSource;
    }


    public static class SongsViewHolder extends RecyclerView.ViewHolder {

        private Song mSong;

        @BindView(R.id.cover_image_view)
        protected ImageView mCoverImageView;
        @BindView(R.id.title_text_view)
        protected TextView mTitleTextView;
        @BindView(R.id.artist_text_view)
        protected TextView mArtistTextView;

        private SongsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindView(@NonNull Song song) {
            mSong = song;
            mTitleTextView.setText(song.getTitle());
            mArtistTextView.setText(song.getArtistName());
            String cover = SongRepository.getAlbumCover(itemView.getContext(), song.getAlbumId());
            Glide
                    .with(itemView.getContext())
                    .load(cover)
                    .centerCrop()
                    .placeholder(new ColorDrawable(Color.GRAY))
                    .crossFade()
                    .into(mCoverImageView);
        }

        public Song getSong(){
            return mSong;

        }

    }

}
