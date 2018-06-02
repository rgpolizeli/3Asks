package com.example.myapplication.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.adapters.EpisodesRVAdapter;
import com.example.myapplication.R;
import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.viewmodel.MainViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView episodesRecyclerView;
    private EpisodesRVAdapter episodesRecyclerViewAdapter;
    private RecyclerView.LayoutManager episodesRecyclerViewLayoutManager;
    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        episodesRecyclerView = (RecyclerView) this.findViewById(R.id.episodesRecyclerView);

        episodesRecyclerViewLayoutManager = new LinearLayoutManager(this);
        episodesRecyclerView.setLayoutManager(episodesRecyclerViewLayoutManager);
        episodesRecyclerViewAdapter = new EpisodesRVAdapter();
        episodesRecyclerView.setAdapter(episodesRecyclerViewAdapter);

        model = ViewModelProviders.of(this).get(MainViewModel.class);

        model.getAllEpisodes().observe(this, new Observer<List<Episode>>() {
            @Override
            public void onChanged(@Nullable final List<Episode> episodes) {

                switch(model.getEpisodeListState()){
                    case MainViewModel.NOT_LOADED:
                        episodesRecyclerViewAdapter.setEpisodes(episodes);
                        model.setEpisodeListState(MainViewModel.LOADED);
                        break;
                    case MainViewModel.LOADED:
                        episodesRecyclerViewAdapter.setEpisodes(episodes);
                        break;
                        case MainViewModel.INSERTING_ITEM:
                            episodesRecyclerViewAdapter.setEpisodes(episodes);
                            model.setEpisodeListState(MainViewModel.LOADED);

                            getInsertedItem(episodes);
                            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(null);
                            diffResult.
                            startEditEpisodeActivity();
                            break;
                }


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_episodes, menu);
        MenuItem item = menu.findItem(R.id.action_add_episode);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_add_episode:
                model.createEpisode();
                model.setEpisodeListState(MainViewModel.INSERTING_ITEM);
                //addEpisode();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Episode getInsertedItem(List<Episode> newEpisodes){

    }


    private void startEditEpisodeActivity(Episode newEpisode){
        if (newEpisode != null){
            Intent intent = new Intent(this, AsksActivity.class);
            intent.putExtra(Constants.ARG_EPISODE, newEpisode.getId());
            startActivity(intent);
        }
    }
}
