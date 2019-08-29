package com.rgp.asks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rgp.asks.R;
import com.rgp.asks.activities.MainActivity;
import com.rgp.asks.adapters.EpisodesRecyclerViewAdapter;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.messages.CreatedEpisodeEvent;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.viewmodel.MainViewModel;
import com.rgp.asks.views.SearchLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.util.Calendar;

public class EpisodesFragment extends Fragment {

    private EpisodesRecyclerViewAdapter episodesRecyclerViewAdapter;
    private MainViewModel mainViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_episodes, container, false);
        setupFAB(rootView);
        setupRecyclerView(rootView);
        setupSearchLayoutListerners(rootView);
        initViewModel();
        this.mainViewModel.getAllEpisodes().observe(this, episodes -> {
            episodesRecyclerViewAdapter.setEpisodes(episodes);
            restoreSearch();
        });
        return rootView;
    }

    private void restoreSearch() {
        String query = this.mainViewModel.getQuery();
        if (!query.isEmpty()) {
            SearchLayout searchLayout = requireView().findViewById(R.id.search);
            searchLayout.restoreQuery(query);
            showSearch(
                    ((MainActivity) requireActivity()).getSupportActionBar(),
                    requireView().findViewById(R.id.episodesRecyclerView),
                    (int) requireView().getResources().getDimension(R.dimen.searchLayoutHeight),
                    searchLayout
            );
        }
    }

    private void setupSearchLayoutListerners(@NonNull View fragmentView) {
        SearchLayout searchLayout = fragmentView.findViewById(R.id.search);
        searchLayout.setOnBackButtonClickListener(() -> {
            this.mainViewModel.setQuery("");
            hideSearch(
                    ((MainActivity) requireActivity()).getSupportActionBar(),
                    fragmentView.findViewById(R.id.episodesRecyclerView),
                    searchLayout
            );
        });
        searchLayout.setOnQueryListener(this::applyFilter);
    }

    private void hideSearch(ActionBar actionBar, View recyclerView, SearchLayout searchLayout) {
        removeAllAppliedFilter();
        searchLayout.setVisibility(View.GONE);
        setMargins(recyclerView, 0, 0, 0, 0);
        actionBar.show();
    }

    private void showSearch(ActionBar actionBar, View recyclerView, int searchLayoutHeight, SearchLayout searchLayout) {
        actionBar.hide();
        setMargins(recyclerView, 0, searchLayoutHeight, 0, 0);
        searchLayout.setVisibility(View.VISIBLE);
    }

    private void setMargins(@NonNull View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    private void applyFilter(@NonNull String filter) {
        this.mainViewModel.setQuery(filter);
        episodesRecyclerViewAdapter.getFilter().filter(filter);
    }

    private void removeAllAppliedFilter() {
        episodesRecyclerViewAdapter.removeAllAppliedFilter();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putInt(Constants.ARG_EPISODE_ID, episodeId);
        argumentsBundle.putString(Constants.ARG_EPISODE_TITLE, episodeName);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_episodesFragment_to_asksActivity, argumentsBundle);
    }

    private void initViewModel() {
        this.mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
    }

    private void setupFAB(@NonNull View rootView) {
        FloatingActionButton episodesFab = rootView.findViewById(com.rgp.asks.R.id.newEpisodeFloatingActionButton);
        episodesFab.setOnClickListener(v -> createNewEpisode());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_episodes, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:
                //searcher.openSearch();
                SearchLayout searchLayout = getView().findViewById(R.id.search);
                showSearch(
                        ((MainActivity) requireActivity()).getSupportActionBar(),
                        requireView().findViewById(R.id.episodesRecyclerView),
                        (int) requireView().getResources().getDimension(R.dimen.searchLayoutHeight),
                        searchLayout
                );
                searchLayout.openSearchLayout();
                break;
        }
        return true;
    }

    private void createNewEpisode() {
        mainViewModel.createEpisode(
                "",
                DateFormat.getDateInstance(DateFormat.SHORT).format(Calendar.getInstance().getTime()),
                getResources().getStringArray(R.array.episode_period_array)[0]
        );
    }

    private void setupRecyclerView(@NonNull View rootView) {
        RecyclerView episodesRecyclerView = rootView.findViewById(R.id.episodesRecyclerView);
        RecyclerView.LayoutManager episodesRecyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        episodesRecyclerView.setLayoutManager(episodesRecyclerViewLayoutManager);
        this.episodesRecyclerViewAdapter = new EpisodesRecyclerViewAdapter(getResources().getString(R.string.destination_asks_unnamed_episode), createOnItemRecyclerViewClickListener());
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
}