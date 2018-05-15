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

import com.example.myapplication.models.Belief;
import com.example.myapplication.R;
import com.example.myapplication.activities.AddNewBeliefActivity;
import com.example.myapplication.auxiliaries.Constants;

import java.util.ArrayList;

public class BeliefRVAdapter extends RecyclerView.Adapter<BeliefRVAdapter.ViewHolder> {
    private ArrayList<Belief> beliefs;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.beliefTextView);
        }
    }

    public BeliefRVAdapter(ArrayList<Belief> beliefs) {
        this.beliefs = beliefs;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BeliefRVAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.belief_item_recycler_view, parent, false);
        ConstraintLayout cl = (ConstraintLayout) v.findViewById(R.id.beliefConstraintLayout);
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), AddNewBeliefActivity.class);
                ((Activity)((ContextWrapper) v.getContext()).getBaseContext()).startActivityForResult(intent, Constants.REQUEST_NEW_BELIEF);
                //((Activity) parent.getContext()).startActivityForResult(intent,REQUEST_NEW_BELIEF);
            }
        });

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(beliefs.get(position).getThought());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.beliefs.size();
    }
}
