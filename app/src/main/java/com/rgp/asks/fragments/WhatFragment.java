package com.rgp.asks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.rgp.asks.R;
import com.rgp.asks.adapters.ReactionRVAdapter;
import com.rgp.asks.dialogs.ReactionDialog;
import com.rgp.asks.interfaces.ReactionDialogListener;
import com.rgp.asks.messages.CreatingReactionEvent;
import com.rgp.asks.persistence.entity.Reaction;
import com.rgp.asks.viewmodel.EpisodeViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class WhatFragment extends Fragment implements ReactionDialogListener {

    private RecyclerView reactionsRecyclerView;
    private ReactionRVAdapter reactionsRecyclerViewAdapter;
    private EpisodeViewModel model;
    private ReactionDialog reactionDialog;
    private AlertDialog editReactionDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(com.rgp.asks.R.layout.fragment_what_ask, container, false);

        EventBus.getDefault().register(this);

        setupFAB(container);

        setupRecyclerView(rootView);

        initDialogs();

        initViewModel();

        return rootView;
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initDialogs() {
        reactionDialog = createNewReactionDialog();
        reactionDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        //editReactionDialog = createEditReactionDialog();
    }

    private void initViewModel() {
        model = ViewModelProviders.of(getActivity()).get(EpisodeViewModel.class);
        model.getReactions().observe(this, new Observer<List<Reaction>>() {
            @Override
            public void onChanged(@Nullable final List<Reaction> reactions) {
                reactionsRecyclerViewAdapter.setReactions(reactions);
            }
        });
    }

    private void setupFAB(ViewGroup container) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) container.getParent();
        FloatingActionButton reactionFab = coordinatorLayout.findViewById(com.rgp.asks.R.id.addReactionFab);
        reactionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewReactionDialog();
            }
        });
    }

    private void setupRecyclerView(View rootView) {
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
                this.showEditReactionDialog(reaction);
            } else {
                Toast.makeText(this.getActivity(), "This reaction don't exist!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private AlertDialog createEditReactionDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View alertDialogView = inflater.inflate(com.rgp.asks.R.layout.dialog_new_reaction, null);

        builder.setView(alertDialogView)
                //.setPositiveButton(this.getString(R.string.reaction_dialog_save_button), null)
                //.setNegativeButton(this.getString(R.string.reaction_dialog_cancel_button), null)
                //.setNeutralButton(this.getString(R.string.reaction_dialog_delete_button), null)
                .setTitle(this.getString(R.string.reaction_dialog_edit_title));

        AlertDialog dialog = builder.create();

        Spinner reactionSpinner = alertDialogView.findViewById(com.rgp.asks.R.id.reactionClassSpinner);
        reactionSpinner.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return false;
        });

        return dialog;
    }

    private ReactionDialog createNewReactionDialog() {
        /*
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();

        View alertDialogView = inflater.inflate(com.rgp.asks.R.layout.dialog_new_reaction, null);
        builder.setView(alertDialogView)
                //.setPositiveButton(this.getString(R.string.reaction_dialog_create_button), null)
                //.setNegativeButton(this.getString(R.string.reaction_dialog_cancel_button), null)
                .setTitle(this.getString(R.string.reaction_dialog_create_title));

        AlertDialog dialog = builder.create();

        Spinner reactionSpinner = alertDialogView.findViewById(com.rgp.asks.R.id.reactionClassSpinner);
        reactionSpinner.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return false;
        });


        return dialog;
        */
        return new ReactionDialog();
    }

    private void showNewReactionDialog() {
        this.reactionDialog.showInCreateMode(getChildFragmentManager());

        /*
        this.reactionDialog.show();
        final AlertDialog dialog = this.reactionDialog;

        this.reactionDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                TextInputEditText reactionEditText = dialog.findViewById(com.rgp.asks.R.id.reactionEditText);
                Spinner reactionClassSpinner = dialog.findViewById(com.rgp.asks.R.id.reactionClassSpinner);

                String newReaction = reactionEditText.getText().toString();
                String newReactionClass = reactionClassSpinner.getSelectedItem().toString();

                if (newReaction.isEmpty()) {
                    TextInputLayout inputLayout = dialog.findViewById(com.rgp.asks.R.id.reactionTextInputLayout);
                    inputLayout.setError(dialog.getContext().getString(R.string.reaction_dialog_error_empty_reaction)); // show error
                } else {
                    model.createReaction(newReaction, newReactionClass);
                    clearReactionDialog(dialog);
                    dialog.dismiss();
                }
            }
        });
        this.reactionDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                clearReactionDialog(dialog);
                dialog.cancel();
            }
        });
        */
    }

    private void showEditReactionDialog(final Reaction reaction) {
        this.reactionDialog.showInEditMode(getChildFragmentManager(), reaction.getReaction(), reaction.getReactionCategory());

        /*
        this.editReactionDialog.show();
        final AlertDialog dialog = this.editReactionDialog;
        final TextInputEditText reactionEditText = dialog.findViewById(com.rgp.asks.R.id.reactionEditText);
        final Spinner reactionClassSpinner = dialog.findViewById(com.rgp.asks.R.id.reactionClassSpinner);

        reactionEditText.setText(reaction.getReaction());
        reactionClassSpinner.setSelection(getIndex(reactionClassSpinner, reaction.getReactionCategory()));

        this.editReactionDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                String newReaction = reactionEditText.getText().toString();
                String newReactionClass = reactionClassSpinner.getSelectedItem().toString();

                if (newReaction.isEmpty()) {
                    TextInputLayout inputLayout = dialog.findViewById(com.rgp.asks.R.id.reactionTextInputLayout);
                    inputLayout.setError(dialog.getContext().getString(R.string.reaction_dialog_error_empty_reaction)); // show error
                } else {
                    if (!newReaction.equals(reaction.getReaction()) || !newReactionClass.equals(reaction.getReactionCategory())) {
                        reaction.setReaction(newReaction);
                        reaction.setReactionCategory(newReactionClass);
                        model.editReaction(reaction);
                        clearReactionDialog(dialog);
                        dialog.dismiss();
                    }

                }
            }
        });
        this.editReactionDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                clearReactionDialog(dialog);
                dialog.cancel();
            }
        });
        this.editReactionDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                model.removeReaction(reaction);
                clearReactionDialog(dialog);
                dialog.dismiss();
            }
        });
        */
    }


    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    private void clearReactionDialog(AlertDialog dialog) {
        TextInputLayout inputLayout = dialog.findViewById(com.rgp.asks.R.id.reactionTextInputLayout);
        EditText reactionEditText = dialog.findViewById(com.rgp.asks.R.id.reactionEditText);
        Spinner reactionClassSpinner = dialog.findViewById(com.rgp.asks.R.id.reactionClassSpinner);

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

    @Override
    public void onReactionDialogCreateButtonClick(@NonNull String newReaction, @NonNull String newReactionClass) {

    }

    @Override
    public void onReactionDialogSaveButtonClick(int reactionId, @NonNull String newReaction, @NonNull String newReactionClass) {

    }

    @Override
    public void onReactionDialogDeleteButtonClick(int reactionId) {

    }
}
