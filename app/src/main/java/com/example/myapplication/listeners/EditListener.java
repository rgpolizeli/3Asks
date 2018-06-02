package com.example.myapplication.listeners;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activities.AddNewBeliefActivity;
import com.example.myapplication.activities.AddNewReactionActivity;
import com.example.myapplication.activities.AsksActivity;
import com.example.myapplication.adapters.EpisodesRVAdapter;
import com.example.myapplication.adapters.ReactionRVAdapter;
import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.persistence.entity.Belief;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.persistence.entity.Reaction;

import java.util.List;

public class EditListener implements View.OnClickListener{

    private List<Reaction> reactions;
    private List<Belief> beliefs;
    private List<Episode> episodes;

    @Override
    public void onClick(View v) {

        if (reactions != null){
            startEditReactionActivity(v);
        } else{
            if (beliefs != null){
                startEditBeliefActivity(v);
            } else{
                if(episodes != null){
                    startEditEpisodeActivity(v);
                }
            }
        }
    }

    private void startEditReactionActivity(View v){
        RecyclerView recyclerView = (RecyclerView) v.getParent();
        int position = recyclerView.getChildAdapterPosition(v);
        Reaction reaction = ((ReactionRVAdapter)recyclerView.getAdapter()).getItem(position);

        if (reaction != null){
            Intent intent = new Intent(v.getContext(), AddNewReactionActivity.class);
            intent.putExtra(Constants.ARG_REACTION, reaction.getId());
            v.getContext().startActivity(intent);
        }


        /*
        int position;
        Intent intent;

        position = ((RecyclerView)v.getParent()).getChildAdapterPosition(v);
        intent = new Intent(v.getContext(), AddNewReactionActivity.class);
        //intent.putExtra( Constants.ARG_REACTION, reactions.get(position).parseToJSONObject().toString() );
        //intent.putExtra( Constants.ARG_REACTION_POSITION, position );
        ((Activity)v.getContext()).startActivityForResult(intent, Constants.REQUEST_EDIT_REACTION);
        */
    }

    private void startEditBeliefActivity(View v){
        int position;
        Intent intent;

        intent = new Intent(v.getContext(), AddNewBeliefActivity.class);
        ((Activity)((ContextWrapper) v.getContext()).getBaseContext()).startActivityForResult(intent, Constants.REQUEST_NEW_BELIEF);
    }

    private void startEditEpisodeActivity(View v){

        RecyclerView recyclerView = (RecyclerView) v.getParent();
        int position = recyclerView.getChildAdapterPosition(v);
        Episode episode = ((EpisodesRVAdapter)recyclerView.getAdapter()).getItem(position);

        if (episode != null){
            Intent intent = new Intent(v.getContext(), AsksActivity.class);
            intent.putExtra(Constants.ARG_EPISODE, episode.getId());
            v.getContext().startActivity(intent);
        }

    }

    public EditListener(){
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }

    public void setBeliefs(List<Belief> beliefs) {
        this.beliefs = beliefs;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }


}
