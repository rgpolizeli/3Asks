package com.example.myapplication.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.listeners.EditListener;
import com.example.myapplication.persistence.entity.Argument;

import java.util.List;

public class ArgumentRVAdapter extends RecyclerView.Adapter<ArgumentRVAdapter.ViewHolder> {

    private List<Argument> arguments;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView argumentTextView;

        public ViewHolder(View v) {
            super(v);
            argumentTextView = v.findViewById(R.id.argumentTextView);
        }
    }

    public void setArguments(List<Argument> arguments) {
        this.arguments = arguments;
        notifyDataSetChanged();
    }


    public ArgumentRVAdapter() {
    }

    @Override
    public ArgumentRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_argument, parent, false);
        setItemListeners(v);
        ViewHolder vh = new ViewHolder(v);
        return vh;
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
