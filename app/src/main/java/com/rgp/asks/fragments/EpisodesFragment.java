package com.rgp.asks.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rgp.asks.R;
import com.rgp.asks.activities.AsksActivity;
import com.rgp.asks.activities.MainActivity;
import com.rgp.asks.adapters.EpisodesRecyclerViewAdapter;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.dialogs.EpisodeDialog;
import com.rgp.asks.dialogs.HelpInfoDialog;
import com.rgp.asks.dialogs.SearchFragment;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.viewmodel.MainViewModel;

public class EpisodesFragment extends Fragment {

    private EpisodesRecyclerViewAdapter episodesRecyclerViewAdapter;
    private MainViewModel mainViewModel;
    private EpisodeDialog episodeDialog;
    private HelpInfoDialog helpInfoDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recycler_view_episodes, container, false);

        setupFAB(rootView);

        setupRecyclerView(rootView);

        initDialogs();

        initViewModel();

        this.mainViewModel.getAllEpisodes().observe(this, episodes -> {
            episodesRecyclerViewAdapter.setEpisodes(episodes);
        });

        Toolbar myToolbar = rootView.findViewById(R.id.episodesFragmentToolbar);
        ((MainActivity) getActivity()).setSupportActionBar(myToolbar);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initViewModel() {
        this.mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    }

    private HelpInfoDialog createHelpInfoDialog() {
        HelpInfoDialog helpInfoDialog = new HelpInfoDialog();
        helpInfoDialog.createHelpInfoDialogView(getLayoutInflater(), R.layout.dialog_help_info);
        helpInfoDialog.setupChildrenViewsOnClickListener();
        helpInfoDialog.createAlertDialog(getActivity());
        return helpInfoDialog;
    }

    private void initDialogs() {
        this.helpInfoDialog = createHelpInfoDialog();
        this.episodeDialog = new EpisodeDialog();
        this.episodeDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    private void setupFAB(@NonNull View rootView) {
        FloatingActionButton episodesFab = rootView.findViewById(com.rgp.asks.R.id.newEpisodeFloatingActionButton);
        episodesFab.setOnClickListener(v -> showNewEpisodeDialog(v));
    }

    private void showHelpInfoDialog() {
        this.helpInfoDialog.show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_episodes, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                if (getActivity().getSupportFragmentManager().findFragmentByTag("SearchFragment") == null) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainFrameLayout, new SearchFragment(), "SearchFragment")
                            .addToBackStack(null)
                            .commit();
                }
                break;
            case R.id.action_help:
                showHelpInfoDialog();
                break;
        }
        return true;
    }

    /**
     * Handle click on newEpisodeFloatingActionButton and show episodeDialog.
     *
     * @param newEpisodeFloatingActionButton clicked.
     */
    private void showNewEpisodeDialog(View newEpisodeFloatingActionButton) {
        if (getChildFragmentManager().findFragmentByTag("episodes") == null) {
            this.episodeDialog.show(getChildFragmentManager(), "episodes");
        }
    }

    private void setupRecyclerView(View rootView) {
        RecyclerView episodesRecyclerView = rootView.findViewById(R.id.episodesRecyclerView);
        RecyclerView.LayoutManager episodesRecyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        episodesRecyclerView.setLayoutManager(episodesRecyclerViewLayoutManager);
        this.episodesRecyclerViewAdapter = new EpisodesRecyclerViewAdapter(createOnItemRecyclerViewClickListener());
        episodesRecyclerView.setAdapter(this.episodesRecyclerViewAdapter);
    }

    private View.OnClickListener createOnItemRecyclerViewClickListener() {
        return v -> {
            RecyclerView recyclerView = (RecyclerView) v.getParent();
            int position = recyclerView.getChildAdapterPosition(v);
            Episode episode = ((EpisodesRecyclerViewAdapter) recyclerView.getAdapter()).getItem(position);

            if (episode != null) {
                startEditEpisodeActivity(episode.getId(), episode.getEpisode());
            } else {
                Toast.makeText(getActivity(), "This episode don't exist!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void startEditEpisodeActivity(int episodeId, String episodeName) {
        Intent intent = new Intent(getActivity(), AsksActivity.class);
        intent.putExtra(Constants.ARG_EPISODE_ID, episodeId);
        intent.putExtra(Constants.ARG_EPISODE_TITLE, episodeName);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        this.helpInfoDialog.dismiss();
    }
}
