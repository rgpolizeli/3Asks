package com.rgp.asks.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rgp.asks.R;
import com.rgp.asks.activities.MainActivity;
import com.rgp.asks.adapters.EpisodesRecyclerViewAdapter;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.auxiliaries.Searcher;
import com.rgp.asks.messages.CreatedEpisodeEvent;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.viewmodel.MainViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class EpisodesFragment extends Fragment {

    private Observer<List<Episode>> observer;
    private EpisodesRecyclerViewAdapter recyclerViewAdapter;
    private MainViewModel model;
    private Searcher searcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createObserver();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_episodes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragmentView, Bundle savedInstanceState) {
        FloatingActionButton floatingActionButton = getFloatingActionButton();
        setupFloatingActionButton(floatingActionButton);
        setupRecyclerView(fragmentView);
        this.searcher = new Searcher(
                ((MainActivity) requireActivity()).getSupportActionBar(),
                null,
                null,
                floatingActionButton,
                recyclerViewAdapter,
                fragmentView.findViewById(R.id.search)
        );
        initViewModel();
        model.getAllEpisodes().removeObservers(this);
        model.getAllEpisodes().observe(this, this.observer);
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

    private void createObserver() {
        this.observer = observed -> {
            recyclerViewAdapter.setData(observed);
            searcher.restoreSearchIfNecessary();
        };
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
        navigateDown(R.id.nav_host_fragment, R.id.action_episodesFragment_to_asksActivity, argumentsBundle);
    }

    /**
     * Navigate to a child in NavGraph, applying the necessary layout modifications.
     *
     * @param navHostId   the navigation host's id.
     * @param navActionId the navigation action's id.
     * @param arguments   the arguments to the new destination.
     */
    private void navigateDown(int navHostId, int navActionId, Bundle arguments) {
        searcher.closeSearch();
        Navigation.findNavController(requireActivity(), navHostId).navigate(navActionId, arguments);
    }

    private void initViewModel() {
        this.model = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
    }

    @NonNull
    private FloatingActionButton getFloatingActionButton() {
        return requireActivity().findViewById(R.id.floatingActionButton);
    }

    private void setupFloatingActionButton(@NonNull FloatingActionButton createEpisodeFab) {
        createEpisodeFab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.secondaryColor)));
        createEpisodeFab.setImageResource(R.drawable.ic_add_white_24dp);
        createEpisodeFab.setOnClickListener(v -> createNewEpisode());
        createEpisodeFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_episodes, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            searcher.openSearch();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void createNewEpisode() {
        model.createEpisode(
                "",
                DateFormat.getDateInstance(DateFormat.SHORT).format(Calendar.getInstance().getTime()),
                getResources().getStringArray(R.array.episode_period_array)[0]
        );
    }

    private void setupRecyclerView(@NonNull View rootView) {
        RecyclerView episodesRecyclerView = rootView.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager episodesRecyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        episodesRecyclerView.setLayoutManager(episodesRecyclerViewLayoutManager);
        this.recyclerViewAdapter = new EpisodesRecyclerViewAdapter(getResources().getString(R.string.destination_asks_unnamed_episode), createOnItemRecyclerViewClickListener());
        episodesRecyclerView.setAdapter(this.recyclerViewAdapter);
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