package com.example.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class ObjectionRVAdapter extends RecyclerView.Adapter<ObjectionRVAdapter.ViewHolder>{
    private String[] objectionsDataSet;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public ViewSwitcher mViewSwitcher;
        public TextView mTextView;
        public EditText mEditText;
        public ImageButton mEditButton;
        public ImageButton mDeleteButton;


        public ViewHolder(View v) {
            super(v);
            mViewSwitcher = (ViewSwitcher) v.findViewById(R.id.objection_switcher);
            mTextView = (TextView) v.findViewById(R.id.objectionTextView);
            mEditText = (EditText) v.findViewById(R.id.objection_edit_view);
            mEditButton = (ImageButton) v.findViewById(R.id.objectionEditButton);
            mDeleteButton = (ImageButton) v.findViewById(R.id.objectionDeleteButton);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ObjectionRVAdapter(String[] ds) {
        objectionsDataSet = ds;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ObjectionRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.objection_item_recycler_view, parent, false);

        ImageButton editButton = (ImageButton) v.findViewById(R.id.objectionEditButton);
        editButton.setOnClickListener(new ObjectionRVAdapter.OnClickObjectionTextView(v));

        ObjectionRVAdapter.ViewHolder vh = new ObjectionRVAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ObjectionRVAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (holder.mViewSwitcher.getNextView().equals(holder.mTextView)){
            holder.mViewSwitcher.showNext();
        }

        holder.mTextView.setText(objectionsDataSet[position]);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return objectionsDataSet.length;
    }

    private class OnClickObjectionTextView implements View.OnClickListener {

        private View rootView;

        public OnClickObjectionTextView(View v){
            rootView = v;
        }

        @Override
        public void onClick(View v) {
            ViewSwitcher switcher = (ViewSwitcher) this.rootView.findViewById(R.id.objection_switcher);
            View nextView = switcher.getNextView();
            View viewOfTextView = switcher.findViewById(R.id.objectionTextView);

            EditText eText = (EditText) switcher.findViewById(R.id.objection_edit_view);
            TextView textView = (TextView) viewOfTextView;

            if (nextView.equals(viewOfTextView)){ //if next to show is a textView
                switcher.showNext();
                textView.setText(eText.getText());
            } else{
                switcher.showNext();
                eText.setText(textView.getText());
            }
        }
    }
}