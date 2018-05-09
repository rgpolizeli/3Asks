package com.example.myapplication;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class ArgumentRVAdapter extends RecyclerView.Adapter<ArgumentRVAdapter.ViewHolder> {
    private String[] argumentsDataSet;

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
            mViewSwitcher = (ViewSwitcher) v.findViewById(R.id.argument_switcher);
            mTextView = (TextView) v.findViewById(R.id.argumentTextView);
            mEditText = (EditText) v.findViewById(R.id.argument_edit_view);
            mEditButton = (ImageButton) v.findViewById(R.id.argumentEditButton);
            mDeleteButton = (ImageButton) v.findViewById(R.id.argumentDeleteButton);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ArgumentRVAdapter(String[] ds) {
        argumentsDataSet = ds;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ArgumentRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.argument_item_recycler_view, parent, false);

        ImageButton editButton = (ImageButton) v.findViewById(R.id.argumentEditButton);
        editButton.setOnClickListener(new OnClickArgumentTextView(v));

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (holder.mViewSwitcher.getNextView().equals(holder.mTextView)){
            holder.mViewSwitcher.showNext();
        }

        holder.mTextView.setText(argumentsDataSet[position]);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return argumentsDataSet.length;
    }

    private class OnClickArgumentTextView implements View.OnClickListener {

        private View rootView;

        public OnClickArgumentTextView(View v){
            rootView = v;
        }

        @Override
        public void onClick(View v) {
            ViewSwitcher switcher = (ViewSwitcher) this.rootView.findViewById(R.id.argument_switcher);
            View nextView = switcher.getNextView();
            View viewOfTextView = switcher.findViewById(R.id.argumentTextView);

            EditText eText = (EditText) switcher.findViewById(R.id.argument_edit_view);
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
