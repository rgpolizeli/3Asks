package com.example.myapplication.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.persistence.entity.Belief;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.viewmodel.EpisodeViewModel;
import com.example.myapplication.R;
import com.example.myapplication.fragments.WhatFragment;
import com.example.myapplication.fragments.WhenFragment;
import com.example.myapplication.fragments.WhyFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import android.arch.lifecycle.Observer;
import android.view.View;

public class AsksActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private EpisodeViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asks_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        model = ViewModelProviders.of(this).get(EpisodeViewModel.class);
        model.loadEpisode(this.getIntent().getIntExtra(Constants.ARG_EPISODE,-1));
        model.getEpisode().observe(this, new Observer<Episode>() {
            @Override
            public void onChanged(@Nullable final Episode episode) {
                //this way I'm sure the episode has already been loaded from the database
                // before user click in a tab
                if (model.getEpisodeIsLoaded() == false && episode!=null){
                    model.setEpisodeIsLoaded(true);

                    findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
                    findViewById(R.id.tabs).setVisibility(View.VISIBLE);

                    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                    mViewPager = (ViewPager) findViewById(R.id.container);
                    mViewPager.setAdapter(mSectionsPagerAdapter);

                    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

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

                    model.initModifiableEpisodeCopy();

                } else{
                    if (model.getEpisodeIsLoaded() == true && episode!=null){
                        finish();
                    }
                }
            }
        });

    }

    private void showRightFab(int tabPosition) {

        FloatingActionButton reactionsFab;
        FloatingActionButton beliefsFab;

        reactionsFab = (FloatingActionButton) findViewById(R.id.addReactionFab);
        beliefsFab = (FloatingActionButton) findViewById(R.id.addBeliefFab);

        switch (tabPosition) {
            case 1:
                beliefsFab.hide();
                reactionsFab.show();
                break;

            case 2:
                beliefsFab.show();
                reactionsFab.hide();
                break;

            default:
                beliefsFab.hide();
                reactionsFab.hide();
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
            case R.id.action_save_episode:
                this.model.saveEpisode();
                return true;
            case R.id.action_delete_episode:
                return true;
            default: return true;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        switch(requestCode){
            case Constants.REQUEST_NEW_BELIEF:
                if(resultCode == Constants.RESULT_NEW_BELIEF){
                    try {
                        JSONObject newBeliefJSON = new JSONObject(data.getStringExtra(Constants.ARG_NEW_BELIEF));
                        processNewBelief(newBeliefJSON);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                switch(resultCode){
                    case Constants.RESULT_NEW_BELIEF:

                        break;

                }
                break;

            case Constants.REQUEST_EDIT_BELIEF:
                switch(resultCode){
                    case Constants.RESULT_BELIEF_DELETE:
                        break;
                    case Constants.RESULT_BELIEF_EDIT:
                        break;
                }
                break;

            case Constants.REQUEST_NEW_REACTION:

                if (resultCode == Constants.RESULT_NEW_REACTION) {
                    WhatFragment f = (WhatFragment)getFragmentOfClass(getSupportFragmentManager().getFragments(), WhatFragment.class);
                    try {
                        JSONObject newReactionJSON = new JSONObject(data.getStringExtra(Constants.ARG_NEW_REACTION));
                        f.processNewReaction(newReactionJSON);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;

            case Constants.REQUEST_EDIT_REACTION:
                switch(resultCode){
                    case Constants.RESULT_REACTION_DELETE:
                        break;
                    case Constants.RESULT_REACTION_EDIT:
                        String reactionJSONString = data.getStringExtra(Constants.ARG_REACTION);
                        if(reactionJSONString != null){
                            WhatFragment f = (WhatFragment)getFragmentOfClass(getSupportFragmentManager().getFragments(), WhatFragment.class);
                            try {
                                JSONObject reactionJSON = new JSONObject(data.getStringExtra(Constants.ARG_REACTION));
                                f.processEditReaction(reactionJSON, data.getIntExtra(Constants.ARG_REACTION_POSITION,-1));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }
                break;
        }
    }

    private Fragment getFragmentOfClass(List<Fragment> fragments, Class c){
        for(Fragment f : fragments){
            if (f.getClass().equals(c)) {
                return f;
            }
        }

        return null;
    }


    private void processNewBelief(JSONObject newBelief){
        mSectionsPagerAdapter.getItem(2);
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
}
