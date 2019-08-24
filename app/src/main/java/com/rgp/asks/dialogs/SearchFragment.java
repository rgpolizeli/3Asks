package com.rgp.asks.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.rgp.asks.R;
import com.rgp.asks.activities.MainActivity;

public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.search_layout, container, false);

        Toolbar myToolbar = rootView.findViewById(R.id.searchFragmentToolbar);
        MainActivity mainActivity = ((MainActivity) getActivity());
        mainActivity.setSupportActionBar(myToolbar);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        } else {
            //todo: err load action bar
        }

        return rootView;
    }

}
