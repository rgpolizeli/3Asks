package com.rpolizeli.asks.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.rpolizeli.asks.adapters.ReactionRVAdapter;
import com.rpolizeli.asks.messages.CreatingReactionEvent;
import com.rpolizeli.asks.messages.OpenEditReactionDialogEvent;
import com.rpolizeli.asks.persistence.entity.Reaction;
import com.rpolizeli.asks.viewmodel.EpisodeViewModel;

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

        View rootView = inflater.inflate(com.rpolizeli.asks.R.layout.fragment_what_ask, container, false);

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
        FloatingActionButton reactionFab = coordinatorLayout.findViewById(com.rpolizeli.asks.R.id.addReactionFab);
        reactionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewReactionDialog();
            }
        });
    }

    private void setupRecyclerView(View rootView){
        reactionsRecyclerView = rootView.findViewById(com.rpolizeli.asks.R.id.reactionsRecyclerView);
        LinearLayoutManager reactionsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
        //GridLayoutManager reactionsRecyclerViewLayoutManager = new GridLayoutManager(rootView.getContext(),2);
        reactionsRecyclerView.setLayoutManager(reactionsRecyclerViewLayoutManager);
        reactionsRecyclerViewAdapter = new ReactionRVAdapter();
        reactionsRecyclerView.setAdapter(reactionsRecyclerViewAdapter);
    }

    private AlertDialog createEditReactionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(com.rpolizeli.asks.R.layout.dialog_new_reaction, null))
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

        builder.setView(inflater.inflate(com.rpolizeli.asks.R.layout.dialog_new_reaction, null))
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
                TextInputEditText reactionEditText = dialog.findViewById(com.rpolizeli.asks.R.id.reactionEditText);
                Spinner reactionClassSpinner = dialog.findViewById(com.rpolizeli.asks.R.id.reactionClassSpinner);

                String newReaction = reactionEditText.getText().toString();
                String newReactionClass = reactionClassSpinner.getSelectedItem().toString();

                if(newReaction.isEmpty()){
                    TextInputLayout inputLayout = dialog.findViewById(com.rpolizeli.asks.R.id.reactionTextInputLayout);
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
        final TextInputEditText reactionEditText = dialog.findViewById(com.rpolizeli.asks.R.id.reactionEditText);
        final Spinner reactionClassSpinner = dialog.findViewById(com.rpolizeli.asks.R.id.reactionClassSpinner);

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
                    TextInputLayout inputLayout = dialog.findViewById(com.rpolizeli.asks.R.id.reactionTextInputLayout);
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
        TextInputLayout inputLayout = dialog.findViewById(com.rpolizeli.asks.R.id.reactionTextInputLayout);
        EditText reactionEditText = dialog.findViewById(com.rpolizeli.asks.R.id.reactionEditText);
        Spinner reactionClassSpinner = dialog.findViewById(com.rpolizeli.asks.R.id.reactionClassSpinner);

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
