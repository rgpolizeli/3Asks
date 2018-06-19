package com.example.myapplication.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.myapplication.R;
import com.example.myapplication.listeners.EpisodeDateOnClick;
import com.example.myapplication.listeners.EpisodeDescriptionTextWatcher;
import com.example.myapplication.listeners.EpisodeTextWatcher;
import com.example.myapplication.listeners.OnItemSelectedListenerEpisodePeriod;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.viewmodel.EpisodeViewModel;

import java.text.DateFormat;
import java.util.Date;

public class WhenFragment extends Fragment {

    private TextInputLayout episodeTextInputLayout;
    private TextInputEditText episodeEditText;
    private TextInputEditText episodeDescriptionEditText;
    private TextInputEditText episodeDateEditText;
    private Spinner episodePeriodSpinner;

    private EpisodeViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        model = ViewModelProviders.of(this.getActivity()).get(EpisodeViewModel.class);

        View rootView = inflater.inflate(R.layout.fragment_when_ask, container, false);

        setupFAB(container);

        episodeTextInputLayout = rootView.findViewById(R.id.episodeTextInputLayout);
        episodeEditText = rootView.findViewById(R.id.episode_name_edit_view);
        episodeDescriptionEditText = rootView.findViewById(R.id.episode_description_edit_view);
        episodeDateEditText = rootView.findViewById(R.id.episode_date_edit_text);
        episodePeriodSpinner = rootView.findViewById(R.id.episode_period_spinner);

        Episode e = model.getEpisode().getValue();

        if(e == null){
            new Exception();
        } else{
            loadFragmentFromViewModel(e);
            setupViewListeners();
        }

        return rootView;
    }

    private void setupFAB(ViewGroup container){
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)container.getParent();
        FloatingActionButton saveEpisodeFab = coordinatorLayout.findViewById(R.id.saveEpisodeFab);
        saveEpisodeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEpisode();
            }
        });
    }

    private void saveEpisode(){
        String newEpisodeName = episodeEditText.getText().toString();

        if (!newEpisodeName.isEmpty()){
            episodeTextInputLayout.setError(null); // hide error
            model.saveEpisode();
        } else{
            episodeTextInputLayout.setError("Episode is required!"); // show error
        }

    }

    private void loadFragmentFromViewModel(@NonNull Episode e){
        String episode = e.getEpisode();
        String episodeDescription = e.getDescription();
        Date episodeDate = e.getDate();
        String episodePeriod = e.getPeriod();

        episodeEditText.setText(episode);
        episodeDescriptionEditText.setText(episodeDescription);
        episodeDateEditText.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(episodeDate));
        episodePeriodSpinner.setSelection(((ArrayAdapter)episodePeriodSpinner.getAdapter()).getPosition(episodePeriod));

    }

    private void setupViewListeners(){
        episodeEditText.addTextChangedListener(new EpisodeTextWatcher(model));
        episodeDescriptionEditText.addTextChangedListener(new EpisodeDescriptionTextWatcher(model));
        episodeDateEditText.setOnClickListener(new EpisodeDateOnClick(model,episodeDateEditText));
        episodePeriodSpinner.setOnItemSelectedListener(new OnItemSelectedListenerEpisodePeriod(model));
    }
}
