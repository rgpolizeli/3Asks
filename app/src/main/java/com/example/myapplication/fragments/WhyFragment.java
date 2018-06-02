package com.example.myapplication.fragments;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.adapters.BeliefRVAdapter;
import com.example.myapplication.adapters.ReactionRVAdapter;
import com.example.myapplication.persistence.entity.Belief;
import com.example.myapplication.persistence.entity.Reaction;
import com.example.myapplication.viewmodel.EpisodeViewModel;
import com.example.myapplication.R;
import com.example.myapplication.activities.AddNewBeliefActivity;
import com.example.myapplication.auxiliaries.Constants;

import java.util.List;

public class WhyFragment extends Fragment {

    private RecyclerView beliefsRecyclerView;
    private BeliefRVAdapter beliefsRecyclerViewAdapter;
    private EpisodeViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.why_ask_fragment, container, false);

        beliefsRecyclerView = (RecyclerView) rootView.findViewById(R.id.beliefsRecyclerView);
        beliefsRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        beliefsRecyclerViewAdapter = new BeliefRVAdapter();
        beliefsRecyclerView.setAdapter(beliefsRecyclerViewAdapter);

        CoordinatorLayout coordinatorLayout2 = (CoordinatorLayout)container.getParent();
        FloatingActionButton beliefsFab = (FloatingActionButton) coordinatorLayout2.findViewById(R.id.addBeliefFab);
        beliefsFab.setOnClickListener(new OnClickListenerFABBeliefs());

        model = ViewModelProviders.of(this.getActivity()).get(EpisodeViewModel.class);
        model.getBeliefs().observe(this, new Observer<List<Belief>>() {
            @Override
            public void onChanged(@Nullable final List<Belief> beliefs) {
                beliefsRecyclerViewAdapter.setBeliefs(beliefs);
            }
        });

        return rootView;
    }

    private class OnClickListenerFABBeliefs implements View.OnClickListener {

        public OnClickListenerFABBeliefs(){
        }

        @Override
        public void onClick(View v) {

            model.createBelief();

            //Intent intent = new Intent(v.getContext(), AddNewBeliefActivity.class);
            //((Activity)((ContextWrapper) v.getContext()).getBaseContext()).startActivityForResult(intent, Constants.REQUEST_NEW_BELIEF);
        }

    }
}
