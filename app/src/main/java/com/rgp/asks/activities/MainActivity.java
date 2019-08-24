package com.rgp.asks.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.rgp.asks.R;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.fragments.EpisodesFragment;
import com.rgp.asks.interfaces.EpisodeDialogListener;
import com.rgp.asks.messages.CreatedEpisodeEvent;
import com.rgp.asks.viewmodel.MainViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements EpisodeDialogListener {

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewModel();

        if (getSupportFragmentManager().findFragmentByTag("EpisodesFragment") == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mainFrameLayout, new EpisodesFragment(), "EpisodesFragment")
                    .commit();
        }
    }

    private void initViewModel() {
        this.mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    /**
     * Invoked after the creation of a new Episode.
     *
     * @param event contains information about the created episode.
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreatedEpisodeEvent(CreatedEpisodeEvent event) {
        startEditEpisodeActivity(event.episodeId, event.episodeName);
    }

    private void startEditEpisodeActivity(int episodeId, String episodeName) {
        Intent intent = new Intent(this, AsksActivity.class);
        intent.putExtra(Constants.ARG_EPISODE_ID, episodeId);
        intent.putExtra(Constants.ARG_EPISODE_TITLE, episodeName);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onEpisodeDialogCreateButtonClick(@NonNull String newEpisode, @NonNull String newDate, @NonNull String newPeriod) {
        mainViewModel.createEpisode(newEpisode, newDate, newPeriod);
    }
}
