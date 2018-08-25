package com.suvankarmitra.sassymusicplayer.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.suvankarmitra.sassymusicplayer.R;
import com.suvankarmitra.sassymusicplayer.broadcast.SongScannerReceiver;
import com.suvankarmitra.sassymusicplayer.fragments.AlbumFragment;
import com.suvankarmitra.sassymusicplayer.fragments.AllSongsFragment;
import com.suvankarmitra.sassymusicplayer.fragments.MusicPlayerFragment;
import com.suvankarmitra.sassymusicplayer.models.Song;
import com.suvankarmitra.sassymusicplayer.services.SongScannerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final String BROADCAST_ACTION = "com.suvankarmitra.sassymusicplayer";
    private static final String TAG = "MainActivity";
    private BroadcastReceiver songScannerReceiver;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        // create receiver
        songScannerReceiver = new SongScannerReceiver(this);

        getPermissionFromUser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcastReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(songScannerReceiver);
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSmoothScrollingEnabled(true);

        setupTabIcons();

        //default tab
        TabLayout.Tab defaultTab = tabLayout.getTabAt(1);
        if (defaultTab != null) {
            defaultTab.select();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new MusicPlayerFragment(), "PLAY");
        viewPagerAdapter.addFragment(new AllSongsFragment(), "ALL TRACKS");
        viewPagerAdapter.addFragment(new AlbumFragment(), "ALBUMS");
        viewPagerAdapter.addFragment(new AlbumFragment(), "ARTISTS");
        viewPagerAdapter.addFragment(new AlbumFragment(), "GENRES");
        viewPagerAdapter.addFragment(new AlbumFragment(), "PLAYLISTS");
        viewPagerAdapter.addFragment(new AlbumFragment(), "DIRECTORIES");
        viewPagerAdapter.addFragment(new AlbumFragment(), "SETTINGS");
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void setupTabIcons() {
        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_music_play);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_library_music_black_24dp);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.ic_album_black_24dp);
        Objects.requireNonNull(tabLayout.getTabAt(3)).setIcon(R.drawable.ic_artist);
        Objects.requireNonNull(tabLayout.getTabAt(4)).setIcon(R.drawable.ic_genre);
        Objects.requireNonNull(tabLayout.getTabAt(5)).setIcon(R.drawable.ic_music_playlist);
        Objects.requireNonNull(tabLayout.getTabAt(6)).setIcon(R.drawable.ic_music_folder);
        Objects.requireNonNull(tabLayout.getTabAt(7)).setIcon(R.drawable.ic_settings_black_24dp);
    }

    public void notifyViewPagerAdapter() {
        viewPagerAdapter.notifyDataSetChanged();
        setupTabIcons();
    }

    /**
     * This method will set data for  {@link MusicPlayerFragment}.
     */
    public void setDataForMusicPlayerFragment(Song song) {
        //Objects.requireNonNull(tabLayout.getTabAt(0)).select();
        new Handler().postDelayed(
                new Runnable() {
                    @Override public void run() {
                        tabLayout.getTabAt(0).select();
                    }
                }, 100);
        notifyViewPagerAdapter();
    }

    private void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        registerReceiver(songScannerReceiver, intentFilter);
    }

    private void getPermissionFromUser() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.d("debug", "not granted");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            } else {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        } else {
            startService(new Intent(this, SongScannerService.class));
        }
    }
    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }
    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permissionName}, permissionRequestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("TAG", "onRequestPermission");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startService(new Intent(this, SongScannerService.class));
                }
            }
        }
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //return mFragmentTitleList.get(position);
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            Fragment fragment = (Fragment) object;
            Log.d(TAG, "getItemPosition: "+(fragment instanceof AllSongsFragment));
            if(fragment instanceof AllSongsFragment)
                return POSITION_UNCHANGED;
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }
    }
}
