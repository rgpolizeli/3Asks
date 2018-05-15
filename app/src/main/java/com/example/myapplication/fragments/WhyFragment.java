package com.example.myapplication.fragments;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.adapters.BeliefRVAdapter;
import com.example.myapplication.models.EpisodeViewModel;
import com.example.myapplication.R;
import com.example.myapplication.activities.AddNewBeliefActivity;
import com.example.myapplication.auxiliaries.Constants;

public class WhyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        EpisodeViewModel model = ViewModelProviders.of(this.getActivity()).get(EpisodeViewModel.class);
        View rootView = inflater.inflate(R.layout.why_ask_fragment, container, false);
        RecyclerView beliefsRecyclerView = (RecyclerView) rootView.findViewById(R.id.beliefsRecyclerView);
        beliefsRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        beliefsRecyclerView.setAdapter(new BeliefRVAdapter(model.getBeliefs()));

        CoordinatorLayout coordinatorLayout2 = (CoordinatorLayout)container.getParent();
        FloatingActionButton beliefsFab = (FloatingActionButton) coordinatorLayout2.findViewById(R.id.addBeliefFab);
        beliefsFab.setOnClickListener(new OnClickListenerFABBeliefs());

        return rootView;
    }

    private class OnClickListenerFABBeliefs implements View.OnClickListener {

        public OnClickListenerFABBeliefs(){
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), AddNewBeliefActivity.class);
            ((Activity)((ContextWrapper) v.getContext()).getBaseContext()).startActivityForResult(intent, Constants.REQUEST_NEW_BELIEF);
        }

    }
}
