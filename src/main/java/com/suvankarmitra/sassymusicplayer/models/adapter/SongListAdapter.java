package com.suvankarmitra.sassymusicplayer.models.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.suvankarmitra.sassymusicplayer.R;
import com.suvankarmitra.sassymusicplayer.activities.MainActivity;
import com.suvankarmitra.sassymusicplayer.models.Song;
import com.suvankarmitra.sassymusicplayer.models.SongListHolder;

import java.util.List;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongListViewHolder> {

    private final String TAG = "SongListAdapter";
    private List<Song> sortedSongList;
    private Context context;

    public SongListAdapter(Context ctx, List<Song> songs) {
        sortedSongList = songs;
        context = ctx;
    }

    @NonNull
    @Override
    public SongListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_playlist_row, parent, false);
        return new SongListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongListViewHolder holder, int position) {
        Song song = sortedSongList.get(position);

        if(song.get_ID().equals(context.getString(R.string.special_char_for_division))) {
            holder.divider.setVisibility(View.VISIBLE);
            holder.main.setVisibility(View.GONE);
            holder.dividerText.setText(song.getTITLE());
            return;
        }

        holder.divider.setVisibility(View.GONE);
        holder.main.setVisibility(View.VISIBLE);

        holder.title.setText(song.getTITLE());
        CharSequence sequence = song.getARTIST() +" - "+ song.getALBUM();
        holder.artist.setText(sequence);
        try{
            Picasso.get().load("file://"+song.getALBUM_ART()).error(R.drawable.no_cover).into(holder.art);
        } catch (Exception ignored){}

        //holder.art.setImageBitmap(song.getAlbumArtBitmap());
        try {
            Integer dur = Integer.parseInt(song.getDURATION())/ 1000;
            int min = dur / 60 ;
            int sec = dur - (min*60);
            CharSequence durTxt = String.valueOf(min) +":"+ (sec<10 ? "0"+String.valueOf(sec) : String.valueOf(sec));
            holder.duration.setText(durTxt);
        } catch (Exception ignored){}
    }

    @Override
    public int getItemCount() {
        return sortedSongList.size();
    }

    public void setSortedSongList(List<Song> sortedSongList) {
        this.sortedSongList = sortedSongList;
    }

    private Song getItem(int p) {
        return sortedSongList.get(p);
    }

    class SongListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        TextView artist;
        ImageView art;
        TextView duration;
        RelativeLayout divider;
        LinearLayout main;
        TextView dividerText;

        SongListViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.song_playlist_row_title);
            artist = itemView.findViewById(R.id.song_playlist_row_artist);
            art = itemView.findViewById(R.id.song_playlist_row_album_art);
            duration = itemView.findViewById(R.id.song_playlist_row_duration);
            divider = itemView.findViewById(R.id.song_playlist_row_divider);
            main = itemView.findViewById(R.id.song_playlist_row_main);
            dividerText = itemView.findViewById(R.id.song_playlist_row_divider_text);

            main.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            Song song = sortedSongList.get(pos);
            Toast.makeText(context, "I was clicked at "+pos, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onClick: I was clicked "+song.getTITLE());
            SongListHolder.currentSelectedSong = song;
            ((MainActivity)context).setDataForMusicPlayerFragment(song);
        }
    }
}
