package com.example.myapplication.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ObjectionRVAdapter extends RecyclerView.Adapter<ObjectionRVAdapter.ViewHolder>{
    ArrayList<String> objectionsDataSet;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public EditText mEditText;
        public ImageButton mDeleteButton;
        public MyTextWatcher mTextWatcher;

        public ViewHolder(View v) {
            super(v);
            mEditText = (EditText) v.findViewById(R.id.objectionEditView);
            mDeleteButton = (ImageButton) v.findViewById(R.id.objectionDeleteButton);
            mTextWatcher = new MyTextWatcher(this);

            mEditText.addTextChangedListener(mTextWatcher);

            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    try {
                        objectionsDataSet.remove(position);
                        notifyItemRemoved(position);
                    }catch (ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ObjectionRVAdapter(ArrayList<String> ds) {
        objectionsDataSet = ds;
    }

    public void addObjection(){
        int position = objectionsDataSet.size() + 1;
        objectionsDataSet.add("");
        notifyItemInserted(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ObjectionRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.objection_item_recycler_view, parent, false);
        ObjectionRVAdapter.ViewHolder vh = new ObjectionRVAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ObjectionRVAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (holder.mEditText.hasFocus()){
            holder.mEditText.clearFocus();
        }

        holder.mEditText.removeTextChangedListener(holder.mTextWatcher);
        holder.mEditText.setText(objectionsDataSet.get(position));
        holder.mEditText.addTextChangedListener(holder.mTextWatcher);

        if(objectionsDataSet.get(position).isEmpty()) {
            holder.mEditText.requestFocus();
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return objectionsDataSet.size();
    }


    private class MyTextWatcher implements TextWatcher {

        private ObjectionRVAdapter.ViewHolder vh;

        public MyTextWatcher(ObjectionRVAdapter.ViewHolder viewHolder){
            vh = viewHolder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.i("message","before");
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            objectionsDataSet.set(this.vh.getAdapterPosition(), s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.i("message","after");
        }
    }
}