package com.suvankarmitra.sassymusicplayer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.suvankarmitra.sassymusicplayer.R;
import com.suvankarmitra.sassymusicplayer.models.Song;
import com.suvankarmitra.sassymusicplayer.models.SongListHolder;
import com.suvankarmitra.sassymusicplayer.utils.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicPlayerFragment extends Fragment {

    private Song song;
    private final String TAG = "MusicPlayerFragment";

    private TextView title;
    private TextView artist;
    private ImageView albumArt;
    private TextView currDur;
    private TextView totalDur;
    private TextView genre;

    public MusicPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_player, container, false);

        // we assume that this is populated already
        song = SongListHolder.currentSelectedSong;

        initViews(view);

        return view;
    }

    private void initViews(View view) {
        title = view.findViewById(R.id.music_player_title);
        artist= view.findViewById(R.id.music_player_artist);
        albumArt = view.findViewById(R.id.music_player_album_art);
        currDur = view.findViewById(R.id.music_player_current_duration);
        totalDur = view.findViewById(R.id.music_player_total_duration);
        genre = view.findViewById(R.id.music_player_genre);

        if(song!=null) {
            Log.d(TAG, "initViews: song genre = "+song.getGENRE());
            title.setText(song.getTITLE().trim());
            artist.setText(song.getARTIST());
            Picasso.get().load("file://"+song.getALBUM_ART())
                    .error(R.drawable.no_cover)
                    .into(albumArt);
            genre.setText(song.getGENRE());
            try {
                int duration = Integer.parseInt(song.getDURATION());
                String dur = Util.getMinSec(duration/1000);
                totalDur.setText(dur);
            } catch (Exception ignored){}
        }
    }

}
