package com.example.myapplication.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.models.EpisodeViewModel;
import com.example.myapplication.R;
import com.example.myapplication.fragments.WhatFragment;
import com.example.myapplication.fragments.WhenFragment;
import com.example.myapplication.fragments.WhyFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AsksActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private EpisodeViewModel model;

    private FloatingActionButton reactionsFab;
    private FloatingActionButton beliefsFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asks_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reactionsFab = (FloatingActionButton) findViewById(R.id.addReactionFab);
        beliefsFab = (FloatingActionButton) findViewById(R.id.addBeliefFab);

        reactionsFab.hide();
        beliefsFab.hide();

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

        model = ViewModelProviders.of(this).get(EpisodeViewModel.class);
    }

    private void showRightFab(int tabPosition) {
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
                //Intent resultIntent = new Intent();
                //resultIntent.putExtra(Constants.ARG_NEW_BELIEF, mountActivityResult().toString());
                //setResult(Constants.RESULT_NEW_BELIEF_ADD, resultIntent);
                //finish();
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
                if (resultCode == Constants.RESULT_NEW_BELIEF_ADD) {
                    try {
                        JSONObject newBeliefJSON = new JSONObject(data.getStringExtra(Constants.ARG_NEW_BELIEF));
                        processNewBelief(newBeliefJSON);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else{
                    if(resultCode == Constants.RESULT_NEW_BELIEF_DELETE){

                    }
                }
                break;
            case Constants.REQUEST_NEW_REACTION:

                WhatFragment f = (WhatFragment)getFragmentOfClass(getSupportFragmentManager().getFragments(), WhatFragment.class);
                if (resultCode == Constants.RESULT_NEW_REACTION_ADD) {

                    try {
                        JSONObject newReactionJSON = new JSONObject(data.getStringExtra(Constants.ARG_NEW_REACTION));
                        f.processNewReaction(newReactionJSON);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else{
                    if(resultCode == Constants.RESULT_NEW_REACTION_DELETE){

                    }
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
