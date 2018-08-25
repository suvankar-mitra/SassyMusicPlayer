package com.suvankarmitra.sassymusicplayer.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.suvankarmitra.sassymusicplayer.R;
import com.suvankarmitra.sassymusicplayer.activities.MainActivity;
import com.suvankarmitra.sassymusicplayer.models.Album;
import com.suvankarmitra.sassymusicplayer.models.Song;
import com.suvankarmitra.sassymusicplayer.models.SongListHolder;

import java.util.HashMap;
import java.util.Map;

public class SongScannerService extends Service {

    private static final int NOTIFICATION_ID = 9999;
    private final String TAG = "SongScannerService";
    private final String NOTIFICATION_CHANNEL_ID = SongScannerService.class.getCanonicalName();

    public static final String ACTION_STOP_SCAN = "ACTION_STOP_SCAN";

    private Map<String, String> genreIdToGenreNameMap;
    private Map<String, String> songIdToGenreIdMap;

    public SongScannerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        genreIdToGenreNameMap = new HashMap<>();
        songIdToGenreIdMap = new HashMap<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null) {
            String action = intent.getAction();
            if(action!=null) {
                switch (action) {
                    case ACTION_STOP_SCAN:
                        stopForegroundService();
                        break;
                }
            }
        }

        //start foreground service
        runAsForegroundService();

        //Start a Task to prepare song list
        new MyScanTask().execute();

        return super.onStartCommand(intent, flags, startId);
    }

    private void runAsForegroundService() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Add Play button intent in notification.
        Intent stopIntent = new Intent(this, SongScannerService.class);
        stopIntent.setAction(ACTION_STOP_SCAN);
        PendingIntent pendingStopIntent = PendingIntent.getService(this, 0, stopIntent, 0);
        NotificationCompat.Action stopAction = new NotificationCompat.Action(android.R.drawable.ic_media_play, "STOP", pendingStopIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setSmallIcon(R.drawable.ic_library_music_black_24dp)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setContentText("Scanning the song lists")
                .setContentIntent(pendingIntent)
        .addAction(stopAction);
        Notification notification = notificationBuilder.build();
        startForeground(NOTIFICATION_ID,notification);
    }

    private NotificationChannel createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "SongScannerService";
            String description = "SongScannerService";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
            return channel;
        }
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID);
        }
    }

    private void stopForegroundService() {
        stopForeground(true);
        stopSelf();
        sendStopBroadcast();
    }

    private void sendStopBroadcast() {
        Intent broadCastIntent = new Intent();
        broadCastIntent.setAction(MainActivity.BROADCAST_ACTION);
        // uncomment this line if you want to send data
//            broadCastIntent.putExtra("data", "abc");
        sendBroadcast(broadCastIntent);
    }

    private class MyScanTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            listAllMusic();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            stopForegroundService();
        }

        private void listAllMusic() {
            String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

            String[] projection = {
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ALBUM_ID
            };

            Cursor cursor = getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    null,
                    null);

            if (cursor != null) {
                SongListHolder.songList.clear();
                SongListHolder.albumList.clear();
                int id = 0;
                while(cursor.moveToNext()){
                    Song song = new Song(
                            id++,
                            cursor.getString(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7),
                            getAlbumArt(cursor.getString(7))
                    );
                    SongListHolder.songList.add(song);
                }
            }

            // to scan all songs for genre
            scanSongsForGenre();
            //Log.d(TAG, "listAllMusic: "+songIdToGenreIdMap);

            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            for (Song song: SongListHolder.songList) {
                                try {
                                    song.setGENRE(getGenreForSongId(song.get_ID()));
                                } catch (Exception ignored){
                                    ignored.printStackTrace();
                                }
                            }
                        }
                    }).start();
            for (Song song: SongListHolder.songList) {
                SongListHolder.albumList.add(
                        new Album(song.getALBUM(), song.getALBUM_ID(), song.getALBUM_ART())
                );
            }
            Log.d(TAG, "listAllMusic: "+SongListHolder.songList);
        }

        @Nullable
        private String getAlbumArt(String albumId) {
            Cursor cursor = getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                    new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                    MediaStore.Audio.Albums._ID+ "=?",
                    new String[] {String.valueOf(albumId)},
                    null);

            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            }
            return null;
        }

        private String getGenreForSongId(String songId) {
            String genre = genreIdToGenreNameMap.get(songIdToGenreIdMap.get(songId));
            return genre == null ? "<UNKNOWN>" : genre;
        }

        private void scanSongsForGenre() {
            ContentResolver resolver = getContentResolver();
            Cursor cursor;

            Uri genreUri = android.provider.MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
            String GENRE_ID      = MediaStore.Audio.Genres._ID;
            String GENRE_NAME    = MediaStore.Audio.Genres.NAME;
            String SONG_ID       = android.provider.MediaStore.Audio.Media._ID;
            // This is what we'll ask of the genres
            String[] genreColumns = {
                    GENRE_ID,
                    GENRE_NAME
            };
            // Actually querying the genres database
            cursor = resolver.query(genreUri, genreColumns, null, null, null);
            // Iterating through the results and filling the map.
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
                genreIdToGenreNameMap.put(cursor.getString(0), cursor.getString(1));
            cursor.close();

            // For each genre, we'll query the databases to get
            // all songs's IDs that have it as a genre.
            for (String genreID : genreIdToGenreNameMap.keySet()) {

                Uri uri = MediaStore.Audio.Genres.Members.getContentUri("external",
                        Long.parseLong(genreID));

                cursor = resolver.query(uri, new String[] { SONG_ID }, null, null, null);

                // Iterating through the results, populating the map
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                    long currentSongID = cursor.getLong(cursor.getColumnIndex(SONG_ID));

                    songIdToGenreIdMap.put(Long.toString(currentSongID), genreID);
                }
                cursor.close();
            }

        }

    }
}
