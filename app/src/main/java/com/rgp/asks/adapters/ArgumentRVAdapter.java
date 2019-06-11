package com.rgp.asks.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rgp.asks.listeners.EditListener;
import com.rgp.asks.persistence.entity.Argument;

import java.util.List;

public class ArgumentRVAdapter extends RecyclerView.Adapter<ArgumentRVAdapter.ViewHolder> {

    private List<Argument> arguments;

    @Override
    public ArgumentRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.rgp.asks.R.layout.item_recycler_view_argument, parent, false);
        setItemListeners(v);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
        notifyDataSetChanged();
    }


    public ArgumentRVAdapter() {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView argumentTextView;

        public ViewHolder(View v) {
            super(v);
            argumentTextView = v.findViewById(com.rgp.asks.R.id.argumentTextView);
        }
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Argument argument = this.arguments.get(position);
        holder.argumentTextView.setText(argument.getArgument());
    }

    @Override
    public int getItemCount() {
        if (this.arguments == null)
            return 0;
        else
            return arguments.size();
    }

    public Argument getItem(int position) {
        if (this.arguments == null){
            return null;
        } else{
            return arguments.get(position);
        }
    }

    private void setItemListeners(View v){
        EditListener editListener = new EditListener();
        editListener.setArguments(this.arguments);
        v.setOnClickListener(editListener);
    }


}
