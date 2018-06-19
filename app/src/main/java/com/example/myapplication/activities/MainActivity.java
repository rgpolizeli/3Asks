package com.example.myapplication.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.EpisodesRVAdapter;
import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.listeners.EpisodeDateDialogOnClick;
import com.example.myapplication.messages.CreatedEpisodeEvent;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.messages.CreatingEpisodeEvent;
import com.example.myapplication.viewmodel.MainViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView episodesRecyclerView;
    private EpisodesRVAdapter episodesRecyclerViewAdapter;
    private RecyclerView.LayoutManager episodesRecyclerViewLayoutManager;
    private MainViewModel mainViewModel;
    private AlertDialog newEpisodeDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        EventBus.getDefault().register(this);

        setupFAB();

        episodesRecyclerView = this.findViewById(R.id.episodesRecyclerView);

        episodesRecyclerViewLayoutManager = new LinearLayoutManager(this);
        episodesRecyclerView.setLayoutManager(episodesRecyclerViewLayoutManager);
        episodesRecyclerViewAdapter = new EpisodesRVAdapter();
        episodesRecyclerView.setAdapter(episodesRecyclerViewAdapter);

        newEpisodeDialog = createNewEpisodeInputDialog();

        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mainViewModel.getAllEpisodes().observe(this, new Observer<List<Episode>>() {
            @Override
            public void onChanged(@Nullable final List<Episode> episodes) {
                episodesRecyclerViewAdapter.setEpisodes(episodes);
                showViews();
            }
        });

        hideViews();
    }

    private void setupFAB(){
        FloatingActionButton addEpisodeFab = findViewById(R.id.addEpisodeFAB);
        addEpisodeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewEpisodeDialog();
            }
        });
    }

    private AlertDialog createNewEpisodeInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_new_episode, null))
                .setPositiveButton("Create", null)
                .setNegativeButton("Cancel", null)
        .setTitle("Create an episode");

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void setupDialogListeners(AlertDialog dialog){
        EditText episodeDateEditText = dialog.findViewById(R.id.episodeDateEditText);
        episodeDateEditText.setOnClickListener(new EpisodeDateDialogOnClick(episodeDateEditText));
        Calendar c = Calendar.getInstance();
        episodeDateEditText.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_episodes, menu);
        return true;
    }

    private void showNewEpisodeDialog(){
        this.newEpisodeDialog.show();
        setupDialogListeners(this.newEpisodeDialog);
        final AlertDialog dialog = this.newEpisodeDialog;

        this.newEpisodeDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                EditText episodeNameInputEditText = dialog.findViewById(R.id.episodeEditText);
                EditText episodeDateEditText = dialog.findViewById(R.id.episodeDateEditText);
                Spinner episodePeriodSpinner = dialog.findViewById(R.id.episodePeriodSpinner);

                String newEpisodeName = episodeNameInputEditText.getText().toString();
                String newEpisodeDate = episodeDateEditText.getText().toString();
                String newEpisodePeriod = episodePeriodSpinner.getSelectedItem().toString();

                if(newEpisodeName.isEmpty()){
                    TextInputLayout inputLayout = (TextInputLayout) dialog.findViewById(R.id.inputLayout);
                    inputLayout.setError("Episode name is required!"); // show error
                } else{
                    mainViewModel.createEpisode(newEpisodeName, newEpisodeDate, newEpisodePeriod);
                    clearEpisodeDialog(dialog);
                    dialog.dismiss();
                }
            }
        });
        this.newEpisodeDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                clearEpisodeDialog(dialog);
                dialog.cancel();
            }
        });
    }


    private void clearEpisodeDialog(AlertDialog dialog){
        TextInputLayout inputLayout = (TextInputLayout) dialog.findViewById(R.id.inputLayout);
        EditText episodeNameInputEditText = dialog.findViewById(R.id.episodeEditText);
        EditText episodeDateEditText = dialog.findViewById(R.id.episodeDateEditText);
        Spinner episodePeriodSpinner = dialog.findViewById(R.id.episodePeriodSpinner);

        inputLayout.setError(null);
        episodeNameInputEditText.setText("");
        episodeDateEditText.setText(Calendar.getInstance().getTime().toString());
        episodePeriodSpinner.setSelection(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreatingEpisodeEvent(CreatingEpisodeEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreatedEpisodeEvent(CreatedEpisodeEvent event) {
        startEditEpisodeActivity(event.episodeId);
    }

    private void startEditEpisodeActivity(@NonNull int episodeId){
        Intent intent = new Intent(this, AsksActivity.class);
        intent.putExtra(Constants.ARG_EPISODE, episodeId);
        startActivity(intent);
    }

    private void hideViews(){
        findViewById(R.id.toolbar).setVisibility(View.GONE);
        findViewById(R.id.episodesRecyclerView).setVisibility(View.GONE);
    }

    private void showViews(){
        findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
        findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        findViewById(R.id.episodesRecyclerView).setVisibility(View.VISIBLE);
    }

}
