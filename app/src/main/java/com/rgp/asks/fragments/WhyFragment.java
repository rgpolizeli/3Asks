package com.rgp.asks.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.rgp.asks.R;
import com.rgp.asks.activities.AddNewBeliefActivity;
import com.rgp.asks.adapters.BeliefRVAdapter;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.messages.CreatedBeliefEvent;
import com.rgp.asks.messages.CreatingBeliefEvent;
import com.rgp.asks.messages.OpenEditBeliefActivityEvent;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.viewmodel.EpisodeViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class WhyFragment extends Fragment {

    private RecyclerView beliefsRecyclerView;
    private BeliefRVAdapter beliefsRecyclerViewAdapter;
    private EpisodeViewModel model;
    private AlertDialog newBeliefDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(com.rgp.asks.R.layout.fragment_why_ask, container, false);

        EventBus.getDefault().register(this);

        beliefsRecyclerView = rootView.findViewById(com.rgp.asks.R.id.beliefsRecyclerView);
        beliefsRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        beliefsRecyclerViewAdapter = new BeliefRVAdapter();
        beliefsRecyclerView.setAdapter(beliefsRecyclerViewAdapter);

        newBeliefDialog = createNewBeliefDialog();

        CoordinatorLayout coordinatorLayout2 = (CoordinatorLayout) container.getParent();
        FloatingActionButton beliefsFab = coordinatorLayout2.findViewById(com.rgp.asks.R.id.addBeliefFab);
        beliefsFab.setOnClickListener(v -> showNewBeliefDialog());

        model = ViewModelProviders.of(this.getActivity()).get(EpisodeViewModel.class);
        model.getBeliefs().observe(this, beliefs -> beliefsRecyclerViewAdapter.setBeliefs(beliefs));

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private AlertDialog createNewBeliefDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_new_belief, null))
                .setPositiveButton(this.getString(R.string.belief_dialog_create_button), null)
                .setNegativeButton(this.getString(R.string.belief_dialog_cancel_button), null)
                .setTitle(this.getString(R.string.belief_dialog_title));

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void showNewBeliefDialog() {
        this.newBeliefDialog.show();
        final AlertDialog dialog = this.newBeliefDialog;

        this.newBeliefDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            hideKeyboard(v);
            EditText beliefEditText = dialog.findViewById(R.id.thoughtEditText);
            String newBelief = beliefEditText.getText().toString();

            if (newBelief.isEmpty()) {
                TextInputLayout inputLayout = dialog.findViewById(R.id.newEpisodeNameTextInputLayout);
                inputLayout.setError(dialog.getContext().getString(R.string.belief_dialog_error_empty_thought)); // show error
            } else {
                model.createBelief(newBelief);
                clearEpisodeDialog(dialog);
                dialog.dismiss();
            }
        });
        this.newBeliefDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> {
            hideKeyboard(v);
            clearEpisodeDialog(dialog);
            dialog.cancel();
        });
    }


    private void clearEpisodeDialog(AlertDialog dialog) {
        TextInputLayout inputLayout = dialog.findViewById(com.rgp.asks.R.id.newEpisodeNameTextInputLayout);
        EditText beliefEditText = dialog.findViewById(com.rgp.asks.R.id.thoughtEditText);

        inputLayout.setError(null);
        beliefEditText.setText("");
    }

    /*
    private void createUnsavedDialog(@NonNull final int beliefPositionInRecyclerView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        builder
                .setMessage("Save changes in the details of the episode?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        model.checkedSaveEpisode();
                        startEditBeliefActivity(beliefPositionInRecyclerView);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startEditBeliefActivity(beliefPositionInRecyclerView);
                    }
                })
        ;

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    */


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreatingBeliefEvent(CreatingBeliefEvent event) {
        Toast.makeText(this.getActivity(), event.message, Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreatedBeliefEvent(CreatedBeliefEvent event) {
        //startEditBeliefActivity(event.beliefId);
    }

    private void startEditBeliefActivity(@NonNull final int beliefPositionInRecyclerView) {

        Belief belief = ((BeliefRVAdapter) beliefsRecyclerView.getAdapter()).getItem(beliefPositionInRecyclerView);

        if (belief != null) {
            Intent intent = new Intent(this.getActivity(), AddNewBeliefActivity.class);
            intent.putExtra(Constants.ARG_BELIEF, belief.getId());
            startActivity(intent);
        } else {
            //err
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOpenEditBeliefActivityEvent(OpenEditBeliefActivityEvent event) {

        //if (model.episodeWasChanged()){
        //    createUnsavedDialog(event.beliefPositionInRecyclerView);
        //} else{
        startEditBeliefActivity(event.beliefPositionInRecyclerView);
        //}
    }
}
