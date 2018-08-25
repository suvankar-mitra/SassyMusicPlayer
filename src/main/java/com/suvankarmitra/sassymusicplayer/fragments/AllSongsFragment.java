package com.suvankarmitra.sassymusicplayer.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.suvankarmitra.sassymusicplayer.R;
import com.suvankarmitra.sassymusicplayer.models.Song;
import com.suvankarmitra.sassymusicplayer.models.SongListHolder;
import com.suvankarmitra.sassymusicplayer.models.adapter.SongListAdapter;
import com.suvankarmitra.sassymusicplayer.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllSongsFragment extends Fragment {

    private static final String TAG = "AllSongsFragment";
    private String SPECIAL_CHAR;

    private RecyclerView mSongsRecyclerView;
    private SongListAdapter mSongListAdapter;
    private LinearLayout mTopBar;
    private ImageButton mSortButton;

    // song lists
    private List<Song> sortedSongList;
    private List<Song> reverseSortedSongList;
    private boolean songListAlphabetic = true;

    public AllSongsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_songs, container, false);

        SPECIAL_CHAR = getString(R.string.special_char_for_division);
        sortedSongList = new ArrayList<>();
        reverseSortedSongList = new ArrayList<>();

        // init All view variables
        initViews(view);

        // prepare recycler view
        prepareRecyclerViewComponents();

        // init other objects
        init();

        return view;
    }

    private void init() {
        new SortSongListTask().execute();
    }

    private void initViews(View v) {
        mTopBar = v.findViewById(R.id.all_songs_topbar);

        mSongsRecyclerView = v.findViewById(R.id.all_songs_recycler_view);
        final VerticalRecyclerViewFastScroller fastScroller = v.findViewById(R.id.fast_scroller);
        // Connect the recycler to the scroller (to let the scroller scroll the list)
        fastScroller.setRecyclerView(mSongsRecyclerView);
        // Connect the scroller to the recycler (to let the recycler scroll the scroller's handle)
        mSongsRecyclerView.addOnScrollListener(fastScroller.getOnScrollListener());
        mSongsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(recyclerView.getScrollState()==RecyclerView.SCROLL_STATE_DRAGGING) {
                    if (dy >= 0) {
                        mTopBar.animate().translationY(mTopBar.getHeight());
                    } else {
                        mTopBar.animate().translationY(0);
                    }
                }
            }
        });

        TextView indicator = v.findViewById(R.id.all_songs_tv);
        // fade out the text ALL TRACKS after 2 secs
        AlphaAnimation fadeOut = new AlphaAnimation( 1.0f , 0.0f );
        fadeOut.setDuration(2000);
        fadeOut.setRepeatCount(1);
        fadeOut.setStartOffset(1000);
        fadeOut.setFillAfter(true);
        indicator.setAnimation(fadeOut);

        mSortButton = v.findViewById(R.id.all_songs_sort);
        mSortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(songListAlphabetic) {
                    mSongListAdapter.setSortedSongList(reverseSortedSongList);
                    mSortButton.setImageResource(R.drawable.sort_descending);
                } else {
                    mSongListAdapter.setSortedSongList(sortedSongList);
                    mSortButton.setImageResource(R.drawable.sort_ascending);
                }
                songListAlphabetic = !songListAlphabetic;
                mSongListAdapter.notifyDataSetChanged();
                mSongsRecyclerView.scrollToPosition(0);
            }
        });
    }

    private void prepareRecyclerViewComponents() {
        /* Song list */
        // set a LinearLayoutManager with default orientation
        mSongsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // By the time the fragment is created, we assume that the SongScannerService
        // is done and song list is prepared
        mSongListAdapter = new SongListAdapter(getContext(), sortedSongList);
        mSongsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSongsRecyclerView.setAdapter(mSongListAdapter);
    }

    private class SortSongListTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            // Sort the list in alphabetic order
            sortedSongList = new ArrayList<>(SongListHolder.songList.size());
            sortedSongList.addAll(SongListHolder.songList);
            Collections.sort(sortedSongList, new Comparator<Song>() {
                @Override
                public int compare(Song o1, Song o2) {
                    return o1.getTITLE().trim().compareToIgnoreCase(o2.getTITLE().trim());
                }
            });
            sortedSongList = Util.addDividerCharactersToList(sortedSongList, SPECIAL_CHAR);
            // Sort the list in reverse alphabetic order
            reverseSortedSongList = new ArrayList<>(SongListHolder.songList.size());
            reverseSortedSongList.addAll(SongListHolder.songList);
            Collections.sort(reverseSortedSongList, new Comparator<Song>() {
                @Override
                public int compare(Song o1, Song o2) {
                    return o2.getTITLE().trim().compareToIgnoreCase(o1.getTITLE().trim());
                }
            });
            reverseSortedSongList = Util.addDividerCharactersToList(reverseSortedSongList, SPECIAL_CHAR);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mSongListAdapter.setSortedSongList(sortedSongList);
            mSongListAdapter.notifyDataSetChanged();
        }
    }
}