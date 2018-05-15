package com.example.myapplication.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.myapplication.models.EpisodeViewModel;
import com.example.myapplication.R;

public class WhenFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        EpisodeViewModel model = ViewModelProviders.of(this.getActivity()).get(EpisodeViewModel.class);
        View rootView = inflater.inflate(R.layout.when_ask_fragment, container, false);
        setupEpisodeName(rootView,model);
        setupEpisodeDescription(rootView,model);
        setupEpisodeDate(rootView,model);
        setupEpisodePeriod(rootView,model);

        return rootView;
    }

    private void setupEpisodeName(View rootView,EpisodeViewModel model){
        EditText episodeNameEditText = (EditText)rootView.findViewById(R.id.episode_name_edit_view);
        String episodeName = model.getEpisodeName();

        if (!episodeName.isEmpty()){
            episodeNameEditText.setText(episodeName);
        }

        episodeNameEditText.addTextChangedListener(new EpisodeNameTextWatcher(model));
    }

    private void setupEpisodeDescription(View rootView,EpisodeViewModel model){
        EditText episodeDescriptionEditText = (EditText)rootView.findViewById(R.id.episode_description_edit_view);
        String episodeDescription = model.getEpisodeDescription();

        if (!episodeDescription.isEmpty()){
            episodeDescriptionEditText.setText(episodeDescription);
        }

        episodeDescriptionEditText.addTextChangedListener(new EpisodeDescriptionTextWatcher(model));
    }

    private void setupEpisodeDate(View rootView,EpisodeViewModel model){
        EditText episodeDateEditText = (EditText)rootView.findViewById(R.id.episode_date_edit_text);
        String episodeDate = model.getEpisodeDate();

        if (!episodeDate.isEmpty()){
            episodeDateEditText.setText(episodeDate);
        }

        episodeDateEditText.addTextChangedListener(new EpisodeDateTextWatcher(model));
    }

    private void setupEpisodePeriod(View rootView,EpisodeViewModel model){
        Spinner episodePeriodSpinner = (Spinner)rootView.findViewById(R.id.episode_period_spinner);
        String episodePeriod = model.getEpisodePeriod();

        if (!episodePeriod.isEmpty()){
            episodePeriodSpinner.setSelection(((ArrayAdapter)episodePeriodSpinner.getAdapter()).getPosition(episodePeriod));
        }

        episodePeriodSpinner.setOnItemSelectedListener(new OnItemSelectedListenerEpisodePeriod(model));
    }

    //
    // LISTENERS
    //

    private class EpisodeNameTextWatcher implements TextWatcher {

        private EpisodeViewModel model;

        public EpisodeNameTextWatcher(EpisodeViewModel model){
            this.model = model;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            model.setEpisodeName(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private class EpisodeDescriptionTextWatcher implements TextWatcher {

        private EpisodeViewModel model;

        public EpisodeDescriptionTextWatcher(EpisodeViewModel model){
            this.model = model;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            model.setEpisodeDescription(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private class EpisodeDateTextWatcher implements TextWatcher {

        private EpisodeViewModel model;

        public EpisodeDateTextWatcher(EpisodeViewModel model){
            this.model = model;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            model.setEpisodeDate(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    private class OnItemSelectedListenerEpisodePeriod implements AdapterView.OnItemSelectedListener {

        private EpisodeViewModel model;

        public OnItemSelectedListenerEpisodePeriod(EpisodeViewModel model){
            this.model = model;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String period = parent.getItemAtPosition(position).toString();
            model.setEpisodePeriod(period);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
