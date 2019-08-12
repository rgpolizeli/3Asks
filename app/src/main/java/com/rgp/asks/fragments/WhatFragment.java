package com.rgp.asks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rgp.asks.R;
import com.rgp.asks.adapters.ReactionRVAdapter;
import com.rgp.asks.dialogs.ReactionDialog;
import com.rgp.asks.interfaces.ReactionDialogListener;
import com.rgp.asks.persistence.entity.Reaction;
import com.rgp.asks.viewmodel.EpisodeViewModel;

public class WhatFragment extends Fragment implements ReactionDialogListener {

    private RecyclerView reactionsRecyclerView;
    private ReactionRVAdapter reactionsRecyclerViewAdapter;
    private EpisodeViewModel model;
    private ReactionDialog reactionDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(com.rgp.asks.R.layout.fragment_what_ask, container, false);

        setupFAB(container);

        setupRecyclerView(rootView);

        initDialogs();

        initViewModel();

        return rootView;
    }

    private void initDialogs() {
        reactionDialog = createReactionDialog();
        reactionDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    private void initViewModel() {
        model = ViewModelProviders.of(getActivity()).get(EpisodeViewModel.class);
        model.getReactions().observe(this, reactions -> reactionsRecyclerViewAdapter.setReactions(reactions));
    }

    private void setupFAB(@NonNull ViewGroup container) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) container.getParent();
        FloatingActionButton reactionFab = coordinatorLayout.findViewById(com.rgp.asks.R.id.addReactionFab);
        reactionFab.setOnClickListener(v -> showReactionDialogInCreateMode());
    }

    private void setupRecyclerView(@NonNull View rootView) {
        reactionsRecyclerView = rootView.findViewById(com.rgp.asks.R.id.reactionsRecyclerView);
        LinearLayoutManager reactionsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
        reactionsRecyclerView.setLayoutManager(reactionsRecyclerViewLayoutManager);
        reactionsRecyclerViewAdapter = new ReactionRVAdapter(createOnItemRecyclerViewClickListener());
        reactionsRecyclerView.setAdapter(reactionsRecyclerViewAdapter);
    }

    private View.OnClickListener createOnItemRecyclerViewClickListener() {
        return v -> {
            RecyclerView recyclerView = (RecyclerView) v.getParent();
            int position = recyclerView.getChildAdapterPosition(v);
            Reaction reaction = ((ReactionRVAdapter) recyclerView.getAdapter()).getItem(position);

            if (reaction != null) {
                this.showReactionDialogInEditMode(reaction.getId(), reaction.getReaction(), reaction.getReactionCategory());
            } else {
                Toast.makeText(this.getActivity(), "This reaction don't exist!", Toast.LENGTH_SHORT).show();
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
        model.createReaction(newReaction, newReactionClass);
    }

    @Override
    public void onReactionDialogSaveButtonClick(int reactionId, @NonNull String newReaction, @NonNull String newReactionClass) {
        Reaction reactionToEdit = new Reaction(reactionId, newReaction, newReactionClass, model.getEpisode().getValue().getId());
        model.editReaction(reactionToEdit);
    }

    @Override
    public void onReactionDialogDeleteButtonClick(int reactionId) {
        Reaction reactionToDelete = new Reaction(reactionId, "", "", model.getEpisode().getValue().getId());
        model.removeReaction(reactionToDelete);
    }
}