package com.rpolizeli.asks.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.rpolizeli.asks.activities.AddNewBeliefActivity;
import com.rpolizeli.asks.adapters.BeliefRVAdapter;
import com.rpolizeli.asks.auxiliaries.Constants;
import com.rpolizeli.asks.messages.CreatedBeliefEvent;
import com.rpolizeli.asks.messages.CreatingBeliefEvent;
import com.rpolizeli.asks.messages.OpenEditBeliefActivityEvent;
import com.rpolizeli.asks.persistence.entity.Belief;
import com.rpolizeli.asks.viewmodel.EpisodeViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class WhyFragment extends Fragment {

    private RecyclerView beliefsRecyclerView;
    private BeliefRVAdapter beliefsRecyclerViewAdapter;
    private EpisodeViewModel model;
    private AlertDialog newBeliefDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(com.rpolizeli.asks.R.layout.fragment_why_ask, container, false);

        EventBus.getDefault().register(this);

        beliefsRecyclerView = rootView.findViewById(com.rpolizeli.asks.R.id.beliefsRecyclerView);
        beliefsRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        beliefsRecyclerViewAdapter = new BeliefRVAdapter();
        beliefsRecyclerView.setAdapter(beliefsRecyclerViewAdapter);

        newBeliefDialog = createNewEpisodeInputDialog();

        CoordinatorLayout coordinatorLayout2 = (CoordinatorLayout)container.getParent();
        FloatingActionButton beliefsFab = coordinatorLayout2.findViewById(com.rpolizeli.asks.R.id.addBeliefFab);
        beliefsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewBeliefDialog();
            }
        });

        model = ViewModelProviders.of(this.getActivity()).get(EpisodeViewModel.class);
        model.getBeliefs().observe(this, new Observer<List<Belief>>() {
            @Override
            public void onChanged(@Nullable final List<Belief> beliefs) {
                beliefsRecyclerViewAdapter.setBeliefs(beliefs);
            }
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private AlertDialog createNewEpisodeInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(com.rpolizeli.asks.R.layout.dialog_new_belief, null))
                .setPositiveButton("Create", null)
                .setNegativeButton("Cancel", null)
                .setTitle("Register a belief");

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void showNewBeliefDialog(){
        this.newBeliefDialog.show();
        final AlertDialog dialog = this.newBeliefDialog;

        this.newBeliefDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                EditText beliefEditText = dialog.findViewById(com.rpolizeli.asks.R.id.thoughtEditText);
                String newBelief = beliefEditText.getText().toString();

                if(newBelief.isEmpty()){
                    TextInputLayout inputLayout = dialog.findViewById(com.rpolizeli.asks.R.id.newEpisodeNameTextInputLayout);
                    inputLayout.setError("Thought is required!"); // show error
                } else{
                    model.createBelief(newBelief);
                    clearEpisodeDialog(dialog);
                    dialog.dismiss();
                }
            }
        });
        this.newBeliefDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                clearEpisodeDialog(dialog);
                dialog.cancel();
            }
        });
    }


    private void clearEpisodeDialog(AlertDialog dialog){
        TextInputLayout inputLayout = dialog.findViewById(com.rpolizeli.asks.R.id.newEpisodeNameTextInputLayout);
        EditText beliefEditText = dialog.findViewById(com.rpolizeli.asks.R.id.thoughtEditText);

        inputLayout.setError(null);
        beliefEditText.setText("");
    }

    private void createUnsavedDialog(@NonNull final int beliefPositionInRecyclerView){
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreatingBeliefEvent(CreatingBeliefEvent event) {
        Toast.makeText(this.getActivity(), event.message, Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreatedBeliefEvent(CreatedBeliefEvent event) {
        //startEditBeliefActivity(event.beliefId);
    }

    private void startEditBeliefActivity(@NonNull final int beliefPositionInRecyclerView){

        Belief belief = ((BeliefRVAdapter)beliefsRecyclerView.getAdapter()).getItem(beliefPositionInRecyclerView);

        if (belief != null){
            Intent intent = new Intent(this.getActivity(), AddNewBeliefActivity.class);
            intent.putExtra(Constants.ARG_BELIEF, belief.getId());
            startActivity(intent);
        }else{
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
