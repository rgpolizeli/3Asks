package com.example.myapplication.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ReactionRVAdapter;
import com.example.myapplication.messages.CreatingReactionEvent;
import com.example.myapplication.messages.OpenEditReactionDialogEvent;
import com.example.myapplication.persistence.entity.Reaction;
import com.example.myapplication.viewmodel.EpisodeViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class WhatFragment extends Fragment {

    private RecyclerView reactionsRecyclerView;
    private ReactionRVAdapter reactionsRecyclerViewAdapter;
    private EpisodeViewModel model;
    private AlertDialog newReactionDialog;
    private AlertDialog editReactionDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_what_ask, container, false);

        EventBus.getDefault().register(this);

        setupFAB(container);

        setupRecyclerView(rootView);

        initDialogs();

        initViewModel();

        return rootView;
    }

    private void initDialogs(){
        newReactionDialog = createNewReactionDialog();
        editReactionDialog = createEditReactionDialog();
    }

    private void initViewModel(){
        model = ViewModelProviders.of(this.getActivity()).get(EpisodeViewModel.class);
        model.getReactions().observe(this, new Observer<List<Reaction>>() {
            @Override
            public void onChanged(@Nullable final List<Reaction> reactions) {
                reactionsRecyclerViewAdapter.setReactions(reactions);
            }
        });
    }

    private void setupFAB(ViewGroup container){
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)container.getParent();
        FloatingActionButton reactionFab = coordinatorLayout.findViewById(R.id.addReactionFab);
        reactionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewReactionDialog();
            }
        });
    }

    private void setupRecyclerView(View rootView){
        reactionsRecyclerView = rootView.findViewById(R.id.reactionsRecyclerView);
        LinearLayoutManager reactionsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
        reactionsRecyclerView.setLayoutManager(reactionsRecyclerViewLayoutManager);
        reactionsRecyclerViewAdapter = new ReactionRVAdapter();
        reactionsRecyclerView.setAdapter(reactionsRecyclerViewAdapter);
    }

    private AlertDialog createEditReactionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_new_reaction, null))
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Delete",null)
                .setTitle("Edit the reaction");

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog createNewReactionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_new_reaction, null))
                .setPositiveButton("Create", null)
                .setNegativeButton("Cancel", null)
                //.setNeutralButton("Delete",null)
                .setTitle("Create a reaction");

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void showNewReactionDialog(){
        this.newReactionDialog.show();
        final AlertDialog dialog = this.newReactionDialog;

        this.newReactionDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                EditText reactionEditText = dialog.findViewById(R.id.reactionEditText);
                Spinner reactionClassSpinner = dialog.findViewById(R.id.reactionClassSpinner);

                String newReaction = reactionEditText.getText().toString();
                String newReactionClass = reactionClassSpinner.getSelectedItem().toString();

                if(newReaction.isEmpty()){
                    TextInputLayout inputLayout = dialog.findViewById(R.id.reactionTextInputLayout);
                    inputLayout.setError("Reaction is required!"); // show error
                } else{
                    model.createReaction(newReaction, newReactionClass);
                    clearReactionDialog(dialog);
                    dialog.dismiss();
                }
            }
        });
        this.newReactionDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                clearReactionDialog(dialog);
                dialog.cancel();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOpenEditReactionDialogEvent(OpenEditReactionDialogEvent event){
        Reaction reaction = reactionsRecyclerViewAdapter.getItem(event.reactionPositionInRecyclerView);
        if (reaction != null){
            this.showEditReactionDialog(reaction);
        } else{
            Toast.makeText(this.getActivity(),"This reaction don't exist!", Toast.LENGTH_SHORT);
        }
    }

    private void showEditReactionDialog(final Reaction reaction){
        this.editReactionDialog.show();
        final AlertDialog dialog = this.editReactionDialog;
        final EditText reactionEditText = dialog.findViewById(R.id.reactionEditText);
        final Spinner reactionClassSpinner = dialog.findViewById(R.id.reactionClassSpinner);

        reactionEditText.setText(reaction.getReaction());
        reactionClassSpinner.setSelection(getIndex(reactionClassSpinner,reaction.getReactionCategory()));

        this.editReactionDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String newReaction = reactionEditText.getText().toString();
                String newReactionClass = reactionClassSpinner.getSelectedItem().toString();

                if(newReaction.isEmpty()){
                    TextInputLayout inputLayout = dialog.findViewById(R.id.reactionTextInputLayout);
                    inputLayout.setError("Reaction is required!"); // show error
                } else{
                    if (!newReaction.equals(reaction.getReaction()) || !newReactionClass.equals(reaction.getReactionCategory())){
                        reaction.setReaction(newReaction);
                        reaction.setReactionCategory(newReactionClass);
                        model.editReaction(reaction);
                        clearReactionDialog(dialog);
                        dialog.dismiss();
                    }

                }
            }
        });
        this.editReactionDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                clearReactionDialog(dialog);
                dialog.cancel();
            }
        });
        this.editReactionDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                model.removeReaction(reaction);
                clearReactionDialog(dialog);
                dialog.dismiss();
            }
        });
    }


    private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    private void clearReactionDialog(AlertDialog dialog){
        TextInputLayout inputLayout = dialog.findViewById(R.id.reactionTextInputLayout);
        EditText reactionEditText = dialog.findViewById(R.id.reactionEditText);
        Spinner reactionClassSpinner = dialog.findViewById(R.id.reactionClassSpinner);

        inputLayout.setError(null);
        reactionEditText.setText("");
        reactionClassSpinner.setSelection(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreatingReactionEvent(CreatingReactionEvent event) {
        Toast.makeText(this.getActivity(), event.message, Toast.LENGTH_SHORT).show();
    }


}
