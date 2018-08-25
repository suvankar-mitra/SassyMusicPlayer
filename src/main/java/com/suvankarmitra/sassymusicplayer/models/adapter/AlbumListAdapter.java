package com.suvankarmitra.sassymusicplayer.models.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.suvankarmitra.sassymusicplayer.R;
import com.suvankarmitra.sassymusicplayer.models.Album;
import com.suvankarmitra.sassymusicplayer.utils.Util;

import java.util.List;

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AlbumListViewHolder> {

    private List<Album> albumList;
    private Context context;

    public AlbumListAdapter(Context context, List<Album> albumList) {
        this.albumList = albumList;
        this.context = context;
    }

    @NonNull
    @Override
    public AlbumListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_list_row, parent, false);
        return new AlbumListAdapter.AlbumListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumListViewHolder holder, int position) {
        Album album = albumList.get(position);
        holder.albumArt.setImageBitmap(Util.getAlbumBitmap(context, album.getALBUM_ART()));
        holder.album.setText(album.getALBUM());
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class AlbumListViewHolder extends RecyclerView.ViewHolder {
        public TextView album;
        public ImageView albumArt;

        public AlbumListViewHolder(View itemView) {
            super(itemView);
            album = itemView.findViewById(R.id.album_list_row_text);
            albumArt = itemView.findViewById(R.id.album_list_row_image);
        }
    }
}
