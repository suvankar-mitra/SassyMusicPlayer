package com.suvankarmitra.sassymusicplayer.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SongListHolder {
    public static List<Song> songList = new ArrayList<>();
    public static Set<Album> albumList = new HashSet<>();
    public static Song currentSelectedSong = null;
}
