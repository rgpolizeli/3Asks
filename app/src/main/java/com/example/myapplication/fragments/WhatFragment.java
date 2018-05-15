package com.example.myapplication.fragments;

import android.arch.lifecycle.ViewModelProviders;
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

import com.example.myapplication.models.EpisodeViewModel;
import com.example.myapplication.R;
import com.example.myapplication.adapters.ReactionRVAdapter;
import com.example.myapplication.activities.AddNewReactionActivity;
import com.example.myapplication.auxiliaries.Constants;

import org.json.JSONException;
import org.json.JSONObject;

public class WhatFragment extends Fragment {

    private RecyclerView reactionsRecyclerView;
    private ReactionRVAdapter reactionsRecyclerViewAdapter;
    private EpisodeViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        model = ViewModelProviders.of(this.getActivity()).get(EpisodeViewModel.class);
        View rootView = inflater.inflate(R.layout.what_ask_fragment, container, false);

        reactionsRecyclerView = (RecyclerView) rootView.findViewById(R.id.reactionsRecyclerView);

        LinearLayoutManager reactionsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
        reactionsRecyclerView.setLayoutManager(reactionsRecyclerViewLayoutManager);

        reactionsRecyclerViewAdapter = new ReactionRVAdapter(model.getReactions());
        reactionsRecyclerView.setAdapter(reactionsRecyclerViewAdapter);

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)container.getParent();
        FloatingActionButton reactionFab = (FloatingActionButton) coordinatorLayout.findViewById(R.id.addReactionFab);
        reactionFab.setOnClickListener(new OnClickListenerFABReactions());

        return rootView;
    }

    public void processNewReaction(JSONObject newReaction){

        try {
            reactionsRecyclerViewAdapter.addReaction(newReaction.getString(Constants.JSON_REACTION),newReaction.getString(Constants.JSON_REACTION_CLASS));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private class OnClickListenerFABReactions implements View.OnClickListener {

        public OnClickListenerFABReactions(){
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), AddNewReactionActivity.class);
            getActivity().startActivityForResult(intent,Constants.REQUEST_NEW_REACTION);
        }

    }
}
