package com.example.myapplication.adapters;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.activities.AsksActivity;

public class EpisodesRVAdapter extends RecyclerView.Adapter<EpisodesRVAdapter.ViewHolder> {
    private String[] episodesDataSet;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.episodeNameTextView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EpisodesRVAdapter(String[] ds) {
        episodesDataSet = ds;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public EpisodesRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_text_view, parent, false);

        ConstraintLayout cl = (ConstraintLayout) v.findViewById(R.id.episodeConstraintLayout);
        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AsksActivity.class);
                TextView episodeTextView = (TextView) v.findViewById(R.id.episodeNameTextView);
                intent.putExtra("episode",episodeTextView.getText().toString());
                v.getContext().startActivity(intent);
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
        holder.mTextView.setText(episodesDataSet[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return episodesDataSet.length;
    }
}
