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
import com.example.myapplication.adapters.ArgumentRVAdapter;
import com.example.myapplication.viewmodel.BeliefViewModel;

public class BeliefArgumentsFragment extends Fragment {

    private RecyclerView argumentsRecyclerView;
    private ArgumentRVAdapter argumentsRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BeliefViewModel model = ViewModelProviders.of(this.getActivity()).get(BeliefViewModel.class);
        View rootView = inflater.inflate(R.layout.argument_recycler_view, container, false);

        argumentsRecyclerView = (RecyclerView) rootView.findViewById(R.id.argumentRV);
        LinearLayoutManager argumentsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
        argumentsRecyclerView.setLayoutManager(argumentsRecyclerViewLayoutManager);

        argumentsRecyclerViewAdapter = new ArgumentRVAdapter();
        argumentsRecyclerView.setAdapter(argumentsRecyclerViewAdapter);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)container.getParent();
        FloatingActionButton argumentsFab = (FloatingActionButton) coordinatorLayout.findViewById(R.id.addArgumentFab);
        argumentsFab.setOnClickListener(new OnClickListenerFABArguments(argumentsRecyclerView,argumentsRecyclerViewAdapter,model));

        return rootView;
    }



    // //////////
    // LISTENERS //
    // //////////

    private class OnClickListenerFABArguments implements View.OnClickListener {

        RecyclerView recyclerView;
        ArgumentRVAdapter adapter;
        BeliefViewModel model;

        public OnClickListenerFABArguments(RecyclerView recyclerView, ArgumentRVAdapter adapter, BeliefViewModel model){
            this.recyclerView = recyclerView;
            this.adapter = adapter;
            this.model = model;
        }

        @Override
        public void onClick(View v) {
            //adapter.addArgument();
            //recyclerView.scrollToPosition(adapter.getItemCount()-1);
        }

    }
}
