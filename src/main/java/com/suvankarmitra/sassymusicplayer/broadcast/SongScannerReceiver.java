package com.suvankarmitra.sassymusicplayer.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.suvankarmitra.sassymusicplayer.activities.MainActivity;

public class SongScannerReceiver extends BroadcastReceiver {

    private MainActivity mainActivity;

    private final String TAG = "SongScannerReceiver";

    public SongScannerReceiver(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d(TAG, "onReceive: Broadcast received");
        /*FragmentManager fragmentManager = ((MainActivity)context).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(android.R.id.content, new PlayListFragment())
                .commit();*/
        mainActivity.notifyViewPagerAdapter();
    }
}
