package com.example.java.simpleplayer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Valentin on 06.12.2016.
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsViewHoldwe> {
    @Override
    public SongsViewHoldwe onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(SongsViewHoldwe holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class SongsViewHoldwe extends RecyclerView.ViewHolder {

        public SongsViewHoldwe(View itemView) {
            super(itemView);
        }
    }

}
