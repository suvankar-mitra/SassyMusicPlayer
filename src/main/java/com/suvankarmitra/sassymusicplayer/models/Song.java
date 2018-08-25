package com.suvankarmitra.sassymusicplayer.models;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.io.Serializable;

/*TODO*/
public class Song implements Serializable{
    private Integer id;
    @NonNull
    private String _ID;
    private String ARTIST;
    private String TITLE;
    private String DATA;
    private String DISPLAY_NAME;
    private String DURATION;
    private String ALBUM;
    private String ALBUM_ID;
    private String ALBUM_ART;
    private Bitmap albumArtBitmap;
    private Boolean IS_PLAYING;
    private String GENRE;

    public Song(Integer id, String _ID, String ARTIST, String TITLE, String DATA, String DISPLAY_NAME, String DURATION, String ALBUM, String ALBUM_ID, String ALBUM_ART) {
        this.id = id;
        this._ID = _ID;
        this.ARTIST = ARTIST;
        this.TITLE = TITLE;
        this.DATA = DATA;
        this.DISPLAY_NAME = DISPLAY_NAME;
        this.DURATION = DURATION;
        this.ALBUM = ALBUM;
        this.ALBUM_ID = ALBUM_ID;
        this.ALBUM_ART = ALBUM_ART;
    }

    public Song(){}

    public String getGENRE() {
        return GENRE;
    }

    public void setGENRE(String GENRE) {
        this.GENRE = GENRE;
    }

    public Bitmap getAlbumArtBitmap() {
        return albumArtBitmap;
    }

    public void setAlbumArtBitmap(Bitmap albumArtBitmap) {
        this.albumArtBitmap = albumArtBitmap;
    }

    public Boolean getIS_PLAYING() {
        return IS_PLAYING;
    }

    public void setIS_PLAYING(Boolean IS_PLAYING) {
        this.IS_PLAYING = IS_PLAYING;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getARTIST() {
        return ARTIST;
    }

    public void setARTIST(String ARTIST) {
        this.ARTIST = ARTIST;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    public String getDISPLAY_NAME() {
        return DISPLAY_NAME;
    }

    public void setDISPLAY_NAME(String DISPLAY_NAME) {
        this.DISPLAY_NAME = DISPLAY_NAME;
    }

    public String getDURATION() {
        return DURATION;
    }

    public void setDURATION(String DURATION) {
        this.DURATION = DURATION;
    }

    public String getALBUM() {
        return ALBUM;
    }

    public void setALBUM(String ALBUM) {
        this.ALBUM = ALBUM;
    }

    public String getALBUM_ID() {
        return ALBUM_ID;
    }

    public void setALBUM_ID(String ALBUM_ID) {
        this.ALBUM_ID = ALBUM_ID;
    }

    public String getALBUM_ART() {
        return ALBUM_ART;
    }

    public void setALBUM_ART(String ALBUM_ART) {
        this.ALBUM_ART = ALBUM_ART;
    }

    @Override
    public String toString() {
        return "{"+_ID+","+ARTIST+","+TITLE+","+ALBUM+","+DURATION+"}";
    }

    @Override
    public boolean equals(Object obj) {
        Song s1 = (Song) obj;
        return s1.id == this.id;
    }
}
