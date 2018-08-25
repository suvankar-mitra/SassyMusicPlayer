package com.suvankarmitra.sassymusicplayer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.suvankarmitra.sassymusicplayer.R;
import com.suvankarmitra.sassymusicplayer.models.Song;
import com.suvankarmitra.sassymusicplayer.models.SongListHolder;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static Bitmap getAlbumBitmap(Context context, String albumArtPath) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        if(albumArtPath != null && !albumArtPath.isEmpty()) {
            bitmap = BitmapFactory.decodeFile(albumArtPath, options);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_cover);
        }
        return bitmap;
    }

    public static String getMinSec(int total) {
        int min = total / 60;
        int sec = total - min * 60;
        return min + ":" + (sec<10 ? "0"+sec : sec);
    }

    public static List<Song> addDividerCharactersToList(List<Song> sortedSongList, String specialChar) {
        List<Song> sortedSongListWithDivider = new ArrayList<>(SongListHolder.songList.size());
        char letter = '\0';
        for(Song song : sortedSongList) {
            char charAtZero = Character.toUpperCase(song.getTITLE().trim().charAt(0));
            if(!Character.isAlphabetic(charAtZero)
                    && !Character.isAlphabetic(letter)) {
                letter = '1';
                Song song1 = new Song();
                song1.setTITLE("#");
                song1.set_ID(specialChar);
                sortedSongListWithDivider.add(song1);
            } else {
                if(charAtZero != Character.toUpperCase(letter)) {
                    letter = charAtZero;
                    Song song1 = new Song();
                    song1.setTITLE(String.valueOf(letter));
                    song1.set_ID(specialChar);
                    sortedSongListWithDivider.add(song1);
                }
            }
            sortedSongListWithDivider.add(song);
        }
        return sortedSongListWithDivider;
    }
}
