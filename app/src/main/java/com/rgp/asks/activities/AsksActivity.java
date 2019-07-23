package com.rgp.asks.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.fragments.WhatFragment;
import com.rgp.asks.fragments.WhenFragment;
import com.rgp.asks.fragments.WhyFragment;
import com.rgp.asks.messages.DeletedEpisodeEvent;
import com.rgp.asks.messages.SavedEditedEpisodeEvent;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.viewmodel.EpisodeViewModel;

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
        setContentView(com.rgp.asks.R.layout.activity_asks);

        EventBus.getDefault().register(this);

        toolbar = findViewById(com.rgp.asks.R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Episode");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadFABs();

        model = ViewModelProviders.of(this).get(EpisodeViewModel.class);
        model.loadEpisode(this.getIntent().getIntExtra(Constants.ARG_EPISODE, -1));
        model.getEpisode().observe(this, new Observer<Episode>() {
            @Override
            public void onChanged(@Nullable final Episode episode) {

                setEpisodeNameInToolbar(episode);

                //this way I'm sure the episode has already been loaded from the database
                // before user click in a tab
                if (!model.getEpisodeIsLoaded() && episode != null) {
                    model.setEpisodeIsLoaded(true);
                    initTabs();
                    model.initModifiableEpisodeCopy();
                } else {
                    if (model.getEpisodeIsLoaded() && episode != null) {
                        initTabs();
                    }
                }

            }
        });

    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onBackPressed() {
        openUnsavedDialog();
    }

    private AlertDialog createUnsavedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setMessage("Save changes in the details of the episode?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        model.checkedSaveEpisode();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //nothing
                        finish();
                    }
                })
        ;

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void openUnsavedDialog() {
        AlertDialog unsavedDialog = createUnsavedDialog();
        if (model.episodeWasChanged()) {
            unsavedDialog.show();
        } else {
            finish();
        }
    }

    private void loadFABs() {
        saveEpisodeFab = findViewById(com.rgp.asks.R.id.saveEpisodeFab);
        reactionsFab = findViewById(com.rgp.asks.R.id.addReactionFab);
        beliefsFab = findViewById(com.rgp.asks.R.id.addBeliefFab);
    }

    private void setEpisodeNameInToolbar(final Episode e) {
        if (e != null) {
            toolbar.setTitle(e.getEpisode());
        }
    }

    private void initTabs() {
        findViewById(com.rgp.asks.R.id.indeterminateBar).setVisibility(View.GONE);
        findViewById(com.rgp.asks.R.id.tabs).setVisibility(View.VISIBLE);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(com.rgp.asks.R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(com.rgp.asks.R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                hideKeyboard();
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
        getMenuInflater().inflate(com.rgp.asks.R.menu.menu_asks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case com.rgp.asks.R.id.action_delete_episode:
                model.removeEpisode();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSavedEditedEpisodeEvent(SavedEditedEpisodeEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
        setEpisodeNameInToolbar(model.getModifiableEpisodeCopy());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeletedEpisodeEvent(DeletedEpisodeEvent event) {

        if (event.result) {
            Episode currentEpisode = model.getEpisode().getValue();
            if (currentEpisode == null || currentEpisode.getId() == event.deletedEpisodeId) {
                Toast.makeText(this, Constants.DELETED_EPISODE_MESSAGE, Toast.LENGTH_SHORT).show();
                this.finish();
            }
        } else {
            Toast.makeText(this, "Failed to delete the episode", Toast.LENGTH_SHORT).show();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment f = null;

            switch (position) {
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
