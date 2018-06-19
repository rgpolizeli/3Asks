package com.example.myapplication.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import android.support.design.widget.AppBarLayout;

import com.example.myapplication.R;
import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.fragments.WhatFragment;
import com.example.myapplication.fragments.WhenFragment;
import com.example.myapplication.fragments.WhyFragment;
import com.example.myapplication.messages.DeletedEpisodeEvent;
import com.example.myapplication.messages.SavedEditedEpisodeEvent;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.viewmodel.EpisodeViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class AsksActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private EpisodeViewModel model;
    private Toolbar toolbar;

    private FloatingActionButton saveEpisodeFab;
    private FloatingActionButton reactionsFab;
    private FloatingActionButton beliefsFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asks);

        EventBus.getDefault().register(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Episode");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadFABs();

        model = ViewModelProviders.of(this).get(EpisodeViewModel.class);
        model.loadEpisode(this.getIntent().getIntExtra(Constants.ARG_EPISODE,-1));
        model.getEpisode().observe(this, new Observer<Episode>() {
            @Override
            public void onChanged(@Nullable final Episode episode) {

                setEpisodeNameInToolbar(episode);

                //this way I'm sure the episode has already been loaded from the database
                // before user click in a tab
                if (!model.getEpisodeIsLoaded() && episode!=null){
                    model.setEpisodeIsLoaded(true);
                    initTabs();
                    model.initModifiableEpisodeCopy();
                }
                else{
                    if (model.getEpisodeIsLoaded() && episode!=null){
                        initTabs();
                    }
                }

            }
        });

    }

    private void loadFABs(){
        saveEpisodeFab = findViewById(R.id.saveEpisodeFab);
        reactionsFab = findViewById(R.id.addReactionFab);
        beliefsFab = findViewById(R.id.addBeliefFab);
    }

    private void setEpisodeNameInToolbar(final Episode e){
        if (e != null){
            toolbar.setTitle(e.getEpisode());
        }
    }

    private void initTabs(){
        findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
        findViewById(R.id.tabs).setVisibility(View.VISIBLE);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

       TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                showRightFab(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });
        showRightFab(mViewPager.getCurrentItem());
    }

    private void showRightFab(int tabPosition) {

        switch (tabPosition) {
            case 0:
                beliefsFab.hide();
                reactionsFab.hide();
                saveEpisodeFab.show();
                break;
            case 1:
                saveEpisodeFab.hide();
                beliefsFab.hide();
                reactionsFab.show();
                break;

            case 2:
                saveEpisodeFab.hide();
                reactionsFab.hide();
                beliefsFab.show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_asks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_delete_episode:
                model.removeEpisode();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default: return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment f = null;

            switch(position){
                case 0:
                    f = new WhenFragment();
                    break;
                case 1:
                    f = new WhatFragment();
                    break;
                case 2:
                    f = new WhyFragment();
                    break;
            }

            return f;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSavedEditedEpisodeEvent(SavedEditedEpisodeEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
        setEpisodeNameInToolbar(model.getModifiableEpisodeCopy());
        //this.finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeletedEpisodeEvent(DeletedEpisodeEvent event) {

        if (event.result){
            Episode currentEpisode = model.getEpisode().getValue();
            if (currentEpisode == null || currentEpisode.getId() == event.deletedEpisodeId){
                Toast.makeText(this, Constants.DELETED_EPISODE_MESSAGE, Toast.LENGTH_SHORT).show();
                this.finish();
            }
        } else{
            Toast.makeText(this, "Failed to delete the episode", Toast.LENGTH_SHORT).show();
        }
    }
}
