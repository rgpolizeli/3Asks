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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rgp.asks.R;
import com.rgp.asks.activities.MainActivity;
import com.rgp.asks.adapters.ReactionRecyclerViewAdapter;
import com.rgp.asks.auxiliaries.Searcher;
import com.rgp.asks.dialogs.ReactionDialog;
import com.rgp.asks.interfaces.ReactionDialogListener;
import com.rgp.asks.persistence.entity.Reaction;
import com.rgp.asks.viewmodel.EpisodeViewModel;

public class WhatFragment extends Fragment implements ReactionDialogListener {

    private ReactionRecyclerViewAdapter reactionsRecyclerViewAdapter;
    private EpisodeViewModel model;
    private ReactionDialog reactionDialog;
    private Searcher searcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragmentView, Bundle savedInstanceState) {
        setupFAB();
        setupRecyclerView(fragmentView);
        this.searcher = new Searcher(
                ((MainActivity) requireActivity()).getSupportActionBar(),
                requireParentFragment().requireView().findViewById(R.id.disableSwipeViewPager),
                requireParentFragment().requireView().findViewById(R.id.tabs),
                reactionsRecyclerViewAdapter,
                fragmentView.findViewById(R.id.search)
        );
        initDialogs();
        initViewModel();
        int episodeIdToLoad = model.getEpisodeId();
        if (episodeIdToLoad != -1) {
            this.model.getReactionsForEpisode().observe(this, reactions -> {
                reactionsRecyclerViewAdapter.setReactions(reactions);
                searcher.restoreSearchIfNecessary();
            });
        } else {
            //todo: err: episode not loaded
        }
    }

    /**
     * Handle click on addReactionButtonView and open ReactionDialog.
     *
     */
    private void setupFAB() {
        FloatingActionButton reactionFab = requireParentFragment().requireView().findViewById(R.id.addReactionFab);
        reactionFab.setOnClickListener(v -> showReactionDialogInCreateMode());
    }

    private void initDialogs() {
        reactionDialog = createReactionDialog();
        reactionDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    private void initViewModel() {
        this.model = ViewModelProviders.of(requireParentFragment()).get(EpisodeViewModel.class);
    }

    private void setupRecyclerView(@NonNull View rootView) {
        RecyclerView reactionsRecyclerView = rootView.findViewById(com.rgp.asks.R.id.recyclerView);
        LinearLayoutManager reactionsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
        reactionsRecyclerView.setLayoutManager(reactionsRecyclerViewLayoutManager);
        reactionsRecyclerViewAdapter = new ReactionRecyclerViewAdapter(createOnItemRecyclerViewClickListener());
        reactionsRecyclerView.setAdapter(reactionsRecyclerViewAdapter);
    }

    private View.OnClickListener createOnItemRecyclerViewClickListener() {
        return v -> {
            RecyclerView recyclerView = (RecyclerView) v.getParent();
            int position = recyclerView.getChildAdapterPosition(v);
            Reaction reaction = ((ReactionRecyclerViewAdapter) recyclerView.getAdapter()).getItem(position);

            if (reaction != null) {
                this.showReactionDialogInEditMode(reaction.getId(), reaction.getReaction(), reaction.getReactionCategory());
            } else {
                Toast.makeText(requireParentFragment().requireContext(), "This reaction don't exist!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private ReactionDialog createReactionDialog() {
        return new ReactionDialog();
    }

    private void showReactionDialogInCreateMode() {
        this.reactionDialog.showInCreateMode(getChildFragmentManager());
    }

    private void showReactionDialogInEditMode(int reactionId, @NonNull String reaction, @NonNull String reactionClass) {
        this.reactionDialog.showInEditMode(getChildFragmentManager(), reactionId, reaction, reactionClass);
    }

    @Override
    public void onReactionDialogCreateButtonClick(@NonNull String newReaction, @NonNull String newReactionClass) {
        this.model.createReaction(newReaction, newReactionClass);
    }

    @Override
    public void onReactionDialogSaveButtonClick(int reactionId, @NonNull String newReaction, @NonNull String newReactionClass) {
        Reaction reactionToEdit = new Reaction(reactionId, newReaction, newReactionClass, model.getEpisodeId());
        model.editReactionForEpisode(reactionToEdit);
    }

    @Override
    public void onReactionDialogDeleteButtonClick(int reactionId) {
        Reaction reactionToDelete = new Reaction(reactionId, "", "", model.getEpisodeId());
        model.removeReactionForEpisode(reactionToDelete);
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
}