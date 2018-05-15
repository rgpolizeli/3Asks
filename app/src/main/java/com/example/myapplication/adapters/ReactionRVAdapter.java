package com.example.myapplication.adapters;

import android.app.Activity;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.Reaction;
import com.example.myapplication.activities.AddNewReactionActivity;
import com.example.myapplication.auxiliaries.Constants;

import java.util.ArrayList;

public class ReactionRVAdapter extends RecyclerView.Adapter<ReactionRVAdapter.ViewHolder> {
    private ArrayList<Reaction> reactions;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView reactionTextView;
        public TextView reactionClassTextView;
        public ViewHolder(View v) {
            super(v);
            reactionTextView = (TextView) v.findViewById(R.id.reaction_text_view);
            reactionClassTextView = (TextView) v.findViewById(R.id.reaction_class_text_view);
        }
    }

    public ReactionRVAdapter(ArrayList<Reaction> reactions) {
        this.reactions = reactions;
    }

    @Override
    public ReactionRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reaction_item_recycler_view, parent, false);
        ConstraintLayout cl = (ConstraintLayout) v.findViewById(R.id.reaction_constraintLayout);
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddNewReactionActivity.class);
                ((Activity)((ContextWrapper) v.getContext()).getBaseContext()).startActivityForResult(intent, Constants.REQUEST_NEW_REACTION);
            }
        });
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reaction reaction = this.reactions.get(position);
        holder.reactionTextView.setText(reaction.getReaction());
        holder.reactionClassTextView.setText(reaction.getReactionClass());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return reactions.size();
    }

    public void addReaction(String reaction, String reactionClass){
        int position = this.reactions.size() + 1;
        this.reactions.add(new Reaction(reaction,reactionClass));
        notifyItemInserted(position);
    }
}
