package com.example.myapplication.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ObjectionRVAdapter;
import com.example.myapplication.viewmodel.BeliefViewModel;

public class BeliefObjectionsFragment extends Fragment {

    private RecyclerView objectionsRecyclerView;
    private ObjectionRVAdapter objectionsRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        BeliefViewModel model = ViewModelProviders.of(this.getActivity()).get(BeliefViewModel.class);
        View rootView = inflater.inflate(R.layout.objection_recycler_view, container, false);

        objectionsRecyclerView = (RecyclerView) rootView.findViewById(R.id.objectionRV);
        LinearLayoutManager objectionsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
        objectionsRecyclerView.setLayoutManager(objectionsRecyclerViewLayoutManager);

        objectionsRecyclerViewAdapter = new ObjectionRVAdapter();
        objectionsRecyclerView.setAdapter(objectionsRecyclerViewAdapter);

        CoordinatorLayout coordinatorLayout2 = (CoordinatorLayout) container.getParent();
        FloatingActionButton objectionsFab = (FloatingActionButton) coordinatorLayout2.findViewById(R.id.addObjectionFab);
        objectionsFab.setOnClickListener(new OnClickListenerFABObjections(objectionsRecyclerView, objectionsRecyclerViewAdapter, model));

        return rootView;
    }

    // //////////
    // LISTENERS //
    // //////////

    private class OnClickListenerFABObjections implements View.OnClickListener {

        RecyclerView recyclerView;
        ObjectionRVAdapter adapter;
        BeliefViewModel model;

        public OnClickListenerFABObjections(RecyclerView recyclerView, ObjectionRVAdapter adapter, BeliefViewModel model) {
            this.recyclerView = recyclerView;
            this.adapter = adapter;
            this.model = model;
        }

        @Override
        public void onClick(View v) {
            //adapter.addObjection();
            //recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        }

    }
}