package com.example.java.simpleplayer.fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.java.simpleplayer.PreferencesUtility;
import com.example.java.simpleplayer.R;
import com.example.java.simpleplayer.activitys.MusicActivity;
import com.example.java.simpleplayer.interfaces.MenuInteractionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.java.simpleplayer.activitys.MusicActivity.*;


public class MainFragment extends Fragment {

    private MenuInteractionListener mListener = null;

    @BindView(R.id.playerView)
    protected ViewPager playerView;

    @BindView(R.id.btnPlay)
    protected ImageView mPlayPauseButton;

    PreferencesUtility mPreferences;

    private PlayBackInteraction mPlayBackInteraction;
    private SeekBar mSeekBar = null;


    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    private void initPlayBackInteraction() {
        if (getActivity() instanceof MusicActivity) {
            mPlayBackInteraction = ((MusicActivity) getActivity())
                    .getPlayBackInteraction();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MenuInteractionListener) {
            mListener = (MenuInteractionListener) activity;
        }
        initPlayBackInteraction();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = PreferencesUtility.getInstance(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, rootView);


        if (playerView != null) {
            setupViewPager(playerView);
            playerView.setOffscreenPageLimit(2);
        }

        playerView.setCurrentItem(mPreferences.getStartPageIndex());

        mPlayPauseButton.setOnClickListener(view -> {
            if (mPlayBackInteraction == null) {
                initPlayBackInteraction();
            }

        });

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int position, boolean fromUser) {
                if(fromUser) {
                    if(mPlayBackInteraction != null) {
                        mPlayBackInteraction.onUserSeek(position);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        if (playerView != null) {
            setupViewPager(playerView);
            playerView.setOffscreenPageLimit(2);
        }

        mPlayPauseButton.setOnClickListener(iv -> {
            if(mPlayBackInteraction == null) {
                initPlayBackInteraction();
            }
            if(mPlayBackInteraction != null) {
                if(mPlayBackInteraction.isPaused()) {
                    mPlayBackInteraction.play();
                    mPlayBackInteraction
                            .gerDurationObservable()
                            .subscribe(position -> { mSeekBar.setProgress(position); });
                } else {
                    mPlayBackInteraction.pause();
                }
            }
        });
    }




    @Override
    public void onPause() {
        super.onPause();
        if (mPreferences.lastOpenedIsStartPagePreference()) {
            mPreferences.setStartPageIndex(playerView.getCurrentItem());
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(new SongFragment(), this.getString(R.string.songs));
        adapter.addFragment(new AlbumFragment(), this.getString(R.string.albums));
//        adapter.addFragment(new ArtistFragment(), this.getString(R.string.artists));
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }


        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
