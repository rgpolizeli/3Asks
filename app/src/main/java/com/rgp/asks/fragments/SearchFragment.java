package com.rgp.asks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.rgp.asks.R;

public class SearchFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        //setupFragmentToolbar(rootView);
        return rootView;
    }

    /*
    private void setupFragmentToolbar(View fragmentView){
        Toolbar toolbar = fragmentView.findViewById(R.id.searchFragmentToolbar);
        ((MainActivity)requireActivity()).setSupportActionBar(toolbar);
        ((MainActivity)requireActivity()).setupActionBarWithToolbar(toolbar);
    }

     */

}
