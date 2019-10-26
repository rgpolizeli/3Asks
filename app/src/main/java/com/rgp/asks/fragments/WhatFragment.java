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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rgp.asks.R;
import com.rgp.asks.activities.MainActivity;
import com.rgp.asks.adapters.ReactionRecyclerViewAdapter;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.auxiliaries.Searcher;
import com.rgp.asks.interfaces.OnFloatingActionButtonClickListener;
import com.rgp.asks.interfaces.OnInsertedEntityListener;
import com.rgp.asks.persistence.entity.Reaction;
import com.rgp.asks.viewmodel.EpisodeViewModel;

import java.util.List;

public class WhatFragment extends Fragment implements OnFloatingActionButtonClickListener, OnInsertedEntityListener {

    private Observer<List<Reaction>> observer;
    private ReactionRecyclerViewAdapter recyclerViewAdapter;
    private EpisodeViewModel model;
    private Searcher searcher;
    private OnInsertedEntityListener onInsertedEntityListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createObserver();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragmentView, Bundle savedInstanceState) {
        setupRecyclerView(fragmentView);
        this.searcher = new Searcher(
                ((MainActivity) requireActivity()).getSupportActionBar(),
                requireParentFragment().requireView().findViewById(R.id.disableSwipeViewPager),
                requireParentFragment().requireView().findViewById(R.id.tabs),
                getFloatingActionButton(),
                recyclerViewAdapter,
                fragmentView.findViewById(R.id.search),
                fragmentView.findViewById(R.id.searchHeaderTextView)
        );
        this.searcher.setSearchHeader(getString(R.string.search_header_reactions));
        initViewModel();
        int episodeIdToLoad = model.getEntityId();
        if (episodeIdToLoad != -1) {
            this.model.getReactionsForEpisode().removeObservers(this);
            this.model.getReactionsForEpisode().observe(getViewLifecycleOwner(), this.observer);
        } else {
            //todo: err: episode not loaded
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        this.onInsertedEntityListener = this;
    }

    @Override
    public void onStop() {
        super.onStop();
        this.onInsertedEntityListener = null;
    }

    private void createObserver() {
        this.observer = observed -> {
            recyclerViewAdapter.setData(observed);
            searcher.restoreSearchIfNecessary();
        };
    }

    private void startEditFragment(int id) {
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putInt(Constants.ARG_ID, id);
        navigateDown(R.id.nav_host_fragment, R.id.action_asksActivity_to_reactionFragment, argumentsBundle);
    }

    @Override
    public void onFloatingActionButtonClick() {
        if (this.model != null) {
            insertNewReaction();
        }
    }

    private void insertNewReaction() {
        Reaction newReaction = new Reaction(
                "",
                getResources().getStringArray(R.array.reaction_categories_array)[0],
                model.getEntityId()
        );
        this.model.insertReaction(newReaction, this.onInsertedEntityListener);
    }

    @NonNull
    private FloatingActionButton getFloatingActionButton() {
        return requireActivity().findViewById(R.id.floatingActionButton);
    }

    private void initViewModel() {
        this.model = ViewModelProviders.of(requireParentFragment()).get(EpisodeViewModel.class);
    }

    private void setupRecyclerView(@NonNull View rootView) {
        RecyclerView reactionsRecyclerView = rootView.findViewById(com.rgp.asks.R.id.recyclerView);
        LinearLayoutManager reactionsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
        reactionsRecyclerView.setLayoutManager(reactionsRecyclerViewLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(reactionsRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        reactionsRecyclerView.addItemDecoration(dividerItemDecoration);
        recyclerViewAdapter = new ReactionRecyclerViewAdapter(getString(R.string.destination_asks_unnamed_reaction), createOnItemRecyclerViewClickListener());
        reactionsRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private View.OnClickListener createOnItemRecyclerViewClickListener() {
        return v -> {
            RecyclerView recyclerView = (RecyclerView) v.getParent();
            int position = recyclerView.getChildAdapterPosition(v);
            Reaction reaction = ((ReactionRecyclerViewAdapter) recyclerView.getAdapter()).getItem(position);
            if (reaction != null) {
                startEditFragment(reaction.getId());
            } else {
                Toast.makeText(requireParentFragment().requireContext(), "This reaction don't exist!", Toast.LENGTH_SHORT).show();
            }
        };
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_search, menu);
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

    @Override
    public void onInsertedEntity(int id) {
        startEditFragment(id);
    }
}