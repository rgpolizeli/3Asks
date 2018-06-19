package com.example.myapplication.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.AddNewBeliefActivity;
import com.example.myapplication.adapters.BeliefRVAdapter;
import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.messages.CreatedBeliefEvent;
import com.example.myapplication.messages.CreatingBeliefEvent;
import com.example.myapplication.persistence.entity.Belief;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.viewmodel.EpisodeViewModel;

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

        View rootView = inflater.inflate(R.layout.fragment_why_ask, container, false);

        EventBus.getDefault().register(this);

        beliefsRecyclerView = rootView.findViewById(R.id.beliefsRecyclerView);
        beliefsRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        beliefsRecyclerViewAdapter = new BeliefRVAdapter();
        beliefsRecyclerView.setAdapter(beliefsRecyclerViewAdapter);

        newBeliefDialog = createNewEpisodeInputDialog();

        CoordinatorLayout coordinatorLayout2 = (CoordinatorLayout)container.getParent();
        FloatingActionButton beliefsFab = coordinatorLayout2.findViewById(R.id.addBeliefFab);
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

        builder.setView(inflater.inflate(R.layout.dialog_new_belief, null))
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
                EditText beliefEditText = dialog.findViewById(R.id.thoughtEditText);
                String newBelief = beliefEditText.getText().toString();

                if(newBelief.isEmpty()){
                    TextInputLayout inputLayout = dialog.findViewById(R.id.inputLayout);
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
        TextInputLayout inputLayout = dialog.findViewById(R.id.inputLayout);
        EditText beliefEditText = dialog.findViewById(R.id.thoughtEditText);

        inputLayout.setError(null);
        beliefEditText.setText("");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreatingBeliefEvent(CreatingBeliefEvent event) {
        Toast.makeText(this.getActivity(), event.message, Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreatedBeliefEvent(CreatedBeliefEvent event) {
        startEditBeliefActivity(event.beliefId);
    }

    private void startEditBeliefActivity(@NonNull final int beliefId){

        Episode e = model.getEpisode().getValue();
        if (e != null){
            Intent intent = new Intent(this.getActivity(), AddNewBeliefActivity.class);
            intent.putExtra(Constants.ARG_BELIEF, beliefId);
            intent.putExtra(Constants.ARG_EPISODE, e.getId());
            startActivity(intent);
        }else{
            //err
        }

    }
}
