package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private RecyclerView episodesRecyclerView;
    private RecyclerView.Adapter episodesRecyclerViewAdapter;
    private RecyclerView.LayoutManager episodesRecyclerViewLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        episodesRecyclerView = (RecyclerView) this.findViewById(R.id.episodesRecyclerView);

        episodesRecyclerViewLayoutManager = new LinearLayoutManager(this);
        episodesRecyclerView.setLayoutManager(episodesRecyclerViewLayoutManager);

        episodesRecyclerViewAdapter = new EpisodesRVAdapter(initDataset());
        episodesRecyclerView.setAdapter(episodesRecyclerViewAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_episodes, menu);
        MenuItem item = menu.findItem(R.id.action_add_episode);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add_episode:
                addEpisode();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addEpisode(){
        Intent intent = new Intent(this, AsksActivity.class);
        startActivity(intent);
    }


    /*
        * Generates Strings for RecyclerView's adapter. This data would usually come
            * from a local content provider or remote server.
    */
    private String[] initDataset() {

        int datasetLength = 20;

        String [] mDataset = new String[datasetLength];
        for (int i = 0; i < datasetLength; i++) {
            mDataset[i] = "Episode #" + i;
        }
        return mDataset;
    }
}
