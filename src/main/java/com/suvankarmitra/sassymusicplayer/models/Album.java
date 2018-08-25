package com.suvankarmitra.sassymusicplayer.models;

public class Album {
    private String ALBUM;
    private String ALBUM_ID;
    private String ALBUM_ART;

    public Album(String ALBUM, String ALBUM_ID, String ALBUM_ART) {
        this.ALBUM = ALBUM;
        this.ALBUM_ID = ALBUM_ID;
        this.ALBUM_ART = ALBUM_ART;
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
    public int hashCode() {
        return ALBUM.length()+ALBUM_ID.length();
    }

    @Override
    public boolean equals(Object obj) {
        Album album = (Album) obj;
        return album.ALBUM.equals(this.ALBUM);
    }
}
