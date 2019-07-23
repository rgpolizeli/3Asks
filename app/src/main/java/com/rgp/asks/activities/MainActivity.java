package com.rgp.asks.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.R;
import com.rgp.asks.adapters.EpisodesRecyclerViewAdapter;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.dialogs.HelpInfoDialog;
import com.rgp.asks.dialogs.NewEpisodeDialog;
import com.rgp.asks.messages.CreatedEpisodeEvent;
import com.rgp.asks.viewmodel.MainViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private RecyclerView episodesRecyclerView;
    private EpisodesRecyclerViewAdapter episodesRecyclerViewAdapter;
    private MainViewModel mainViewModel;
    private NewEpisodeDialog newEpisodeDialog;
    private HelpInfoDialog helpInfoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        episodesRecyclerView = this.findViewById(R.id.episodesRecyclerView);
        RecyclerView.LayoutManager episodesRecyclerViewLayoutManager = new LinearLayoutManager(this);
        episodesRecyclerView.setLayoutManager(episodesRecyclerViewLayoutManager);
        episodesRecyclerViewAdapter = new EpisodesRecyclerViewAdapter();
        episodesRecyclerView.setAdapter(episodesRecyclerViewAdapter);

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        helpInfoDialog = createHelpInfoDialog();
        newEpisodeDialog = createNewEpisodeDialog();

        mainViewModel.getAllEpisodes().observe(this, episodes -> {
            episodesRecyclerViewAdapter.setEpisodes(episodes);
            showViews();
        });

        hideViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private NewEpisodeDialog createNewEpisodeDialog() {
        NewEpisodeDialog newEpisodeDialog = new NewEpisodeDialog();
        newEpisodeDialog.createNewEpisodeDialogView(getLayoutInflater(), R.layout.dialog_new_episode);
        newEpisodeDialog.setupDateOnClickListener();
        newEpisodeDialog.setupPeriodOnTouchListener();
        newEpisodeDialog.createAlertDialog(this, this.mainViewModel);
        return newEpisodeDialog;
    }

    private HelpInfoDialog createHelpInfoDialog() {
        HelpInfoDialog helpInfoDialog = new HelpInfoDialog();
        helpInfoDialog.createHelpInfoDialogView(getLayoutInflater(), R.layout.dialog_help_info);
        helpInfoDialog.setupChildrenViewsOnClickListener();
        helpInfoDialog.createAlertDialog(this);
        return helpInfoDialog;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.rgp.asks.R.menu.menu_episodes, menu);
        return true;
    }

    /**
     * Handle click on newEpisodeFloatingActionButton and show newEpisodeDialog.
     *
     * @param newEpisodeFloatingActionButton clicked.
     */
    public void showNewEpisodeDialog(View newEpisodeFloatingActionButton) {
        this.newEpisodeDialog.show();
    }

    private void showHelpInfoDialog() {
        this.helpInfoDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_help) showHelpInfoDialog();
        return true;
    }

    /**
     * Invoked after the creation of a new Episode.
     *
     * @param event contains information about the created episode.
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreatedEpisodeEvent(CreatedEpisodeEvent event) {
        startEditEpisodeActivity(event.episodeId);
    }

    private void startEditEpisodeActivity(int episodeId) {
        Intent intent = new Intent(this, AsksActivity.class);
        intent.putExtra(Constants.ARG_EPISODE, episodeId);
        startActivity(intent);
    }

    private void hideViews() {
        findViewById(com.rgp.asks.R.id.toolbar).setVisibility(View.GONE);
        findViewById(com.rgp.asks.R.id.episodesRecyclerView).setVisibility(View.GONE);
    }

    private void showViews() {
        findViewById(com.rgp.asks.R.id.indeterminateBar).setVisibility(View.GONE);
        findViewById(com.rgp.asks.R.id.toolbar).setVisibility(View.VISIBLE);
        findViewById(com.rgp.asks.R.id.episodesRecyclerView).setVisibility(View.VISIBLE);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        this.newEpisodeDialog.dismiss();
        this.helpInfoDialog.dismiss();
    }

}
