package com.example.myapplication.adapters;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.listeners.EditListener;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.persistence.entity.Reaction;

import java.util.ArrayList;
import java.util.List;

public class ReactionRVAdapter extends RecyclerView.Adapter<ReactionRVAdapter.ViewHolder> {
    private List<Reaction> reactions;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView reactionTextView;
        public TextView reactionClassTextView;
        public ViewHolder(View v) {
            super(v);
            reactionTextView = (TextView) v.findViewById(R.id.reaction_text_view);
            reactionClassTextView = (TextView) v.findViewById(R.id.reaction_class_text_view);
        }
    }


    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
        notifyDataSetChanged();
    }

    public ReactionRVAdapter() {
    }

    @Override
    public ReactionRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reaction_item_recycler_view, parent, false);

        setItemListeners(v);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Reaction reaction = this.reactions.get(position);
        holder.reactionTextView.setText(reaction.getReaction());
        //holder.reactionClassTextView.setText(reaction.getReactionClass());
    }

    @Override
    public int getItemCount() {
        if (this.reactions == null)
            return 0;
        else
            return reactions.size();
    }

    public Reaction getItem(int position) {
        if (this.reactions == null){
            return null;
        } else{
            return reactions.get(position);
        }
    }

    public void addReaction(String reaction, String reactionClass){
        int position = this.reactions.size() + 1;
        //this.reactions.add(new Reaction(reaction,reactionClass));
        notifyItemInserted(position);
    }

    public void editReaction(String reaction, String reactionClass, int position){
        Reaction r = this.reactions.get(position);
        r.setReaction(reaction);
        //r.setReactionClass(reactionClass);
        notifyItemChanged(position);
    }

    private void setItemListeners(View v){
        //ConstraintLayout cl = (ConstraintLayout) v.findViewById(R.id.reaction_constraintLayout);
        EditListener editListener = new EditListener();
        editListener.setReactions(this.reactions);
        v.setOnClickListener(editListener);
    }
}
