package com.example.myapplication;


import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

public class ArgumentRVAdapter extends RecyclerView.Adapter<ArgumentRVAdapter.ViewHolder> {

    ArrayList<String> argumentsDataSet;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public EditText mEditText;
        public ImageButton mDeleteButton;
        public MyTextWatcher mTextWatcher;

        public ViewHolder(View v) {
            super(v);
            mEditText = (EditText) v.findViewById(R.id.argumentEditText);
            mDeleteButton = (ImageButton) v.findViewById(R.id.argumentDeleteButton);
            mTextWatcher = new MyTextWatcher(this);

            mEditText.addTextChangedListener(mTextWatcher);

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    try {
                        argumentsDataSet.remove(position);
                        notifyItemRemoved(position);
                    }catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public ArrayList<String> getArgumentsDataSet(){
        return this.argumentsDataSet;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ArgumentRVAdapter(ArrayList<String> ds) {
        argumentsDataSet = ds;
    }

    public void addArgument(){
        int position = argumentsDataSet.size() + 1;
        argumentsDataSet.add("");
        notifyItemInserted(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ArgumentRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.argument_item_recycler_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (holder.mEditText.hasFocus()){
            holder.mEditText.clearFocus();
        }

        holder.mEditText.removeTextChangedListener(holder.mTextWatcher);
        holder.mEditText.setText(argumentsDataSet.get(position));
        holder.mEditText.addTextChangedListener(holder.mTextWatcher);

        if(argumentsDataSet.get(position).isEmpty()) {
            holder.mEditText.requestFocus();
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return argumentsDataSet.size();
    }


    private class MyTextWatcher implements TextWatcher {

        private ViewHolder vh;

        public MyTextWatcher(ViewHolder viewHolder){
            vh = viewHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.i("message","before");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            argumentsDataSet.set(this.vh.getAdapterPosition(), s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.i("message","after");
        }
    }

}
