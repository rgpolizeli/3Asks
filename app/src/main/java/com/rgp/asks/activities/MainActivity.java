package com.rgp.asks.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.rgp.asks.R;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.messages.CreatedEpisodeEvent;
import com.rgp.asks.viewmodel.MainViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        initViewModel();
    }

    private void setupToolbar() {
        this.toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle(R.string.destination_episodes);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {
                default:
                    this.toolbar.removeView(this.toolbar.findViewById(R.id.search));
                    this.toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    this.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
                    this.toolbar.setContentInsetStartWithNavigation(16);
                    break;
                case R.id.searchFragment:
                    getLayoutInflater().inflate(R.layout.search_view, this.toolbar);
                    this.toolbar.setBackgroundColor(getResources().getColor(R.color.white));
                    this.toolbar.setContentInsetStartWithNavigation(0);
                    break;
            }
        });
    }

    private void initViewModel() {
        ViewModelProviders.of(this).get(MainViewModel.class);
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

    public void startEditEpisodeActivity(int episodeId, String episodeName) {
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putInt(Constants.ARG_EPISODE_ID, episodeId);
        argumentsBundle.putString(Constants.ARG_EPISODE_TITLE, episodeName);
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_episodesFragment_to_asksActivity, argumentsBundle);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if (item.getItemId() == android.R.id.home) {
            this.getOnBackPressedDispatcher().onBackPressed();
        }
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }
}