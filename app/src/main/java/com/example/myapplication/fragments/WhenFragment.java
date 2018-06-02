package com.example.myapplication.fragments;

import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import java.text.DateFormat;

import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.viewmodel.EpisodeViewModel;
import com.example.myapplication.R;

import java.util.Calendar;
import java.util.Date;

public class WhenFragment extends Fragment {

    private EditText episodeEditText;
    private EditText episodeDescriptionEditText;
    private EditText episodeDateEditText;
    private Spinner episodePeriodSpinner;

    private EpisodeViewModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        model = ViewModelProviders.of(this.getActivity()).get(EpisodeViewModel.class);

        View rootView = inflater.inflate(R.layout.when_ask_fragment, container, false);

        episodeEditText = (EditText)rootView.findViewById(R.id.episode_name_edit_view);
        episodeDescriptionEditText = (EditText)rootView.findViewById(R.id.episode_description_edit_view);
        episodeDateEditText = (EditText)rootView.findViewById(R.id.episode_date_edit_text);
        episodePeriodSpinner = (Spinner)rootView.findViewById(R.id.episode_period_spinner);

        Episode e = model.getEpisode().getValue();

        if(e == null){
            new Exception();
        } else{
            loadFragmentFromViewModel(e);
            setupViewListeners();
        }

        return rootView;
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
        episodeDateEditText.setOnClickListener(new EpisodeDateOnClick(model));
        episodePeriodSpinner.setOnItemSelectedListener(new OnItemSelectedListenerEpisodePeriod(model));
    }



/*
    private void setupEpisodeDescription(View rootView,EpisodeViewModel model){



        if (!episodeDescription.isEmpty()){
            episodeDescriptionEditText.setText(episodeDescription);
        }

        episodeDescriptionEditText.addTextChangedListener(new EpisodeDescriptionTextWatcher(model));
    }

    private void setupEpisodeDate(View rootView,EpisodeViewModel model){

        String episodeDate = model.getDate();

        if (!episodeDate.isEmpty()){
            episodeDateEditText.setText(episodeDate);
        }

        episodeDateEditText.addTextChangedListener(new EpisodeDateTextWatcher(model));
    }

    private void setupEpisodePeriod(View rootView,EpisodeViewModel model){

        String episodePeriod = model.getPeriod();

        if (!episodePeriod.isEmpty()){
            episodePeriodSpinner.setSelection(((ArrayAdapter)episodePeriodSpinner.getAdapter()).getPosition(episodePeriod));
        }

        episodePeriodSpinner.setOnItemSelectedListener(new OnItemSelectedListenerEpisodePeriod(model));
    }
    */

    //
    // LISTENERS
    //

    private class EpisodeTextWatcher implements TextWatcher {

        private EpisodeViewModel model;

        public EpisodeTextWatcher(EpisodeViewModel model){
            this.model = model;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            Episode e = model.getEpisode().getValue();
            e.setEpisode(s.toString());
            model.setModifiableEpisodeCopy(e);
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
        }

        @Override
        public void afterTextChanged(Editable s) {
            Episode e = model.getEpisode().getValue();
            e.setDescription(s.toString());
            model.setModifiableEpisodeCopy(e);
        }
    }

    private class EpisodeDateOnDateSetListener implements DatePickerDialog.OnDateSetListener{
        EpisodeViewModel model;

        EpisodeDateOnDateSetListener(EpisodeViewModel model){
            this.model = model;
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Episode e = model.getEpisode().getValue();
            Calendar c = Calendar.getInstance();
            c.set(year,month,dayOfMonth);
            e.setDate(c.getTime());
            model.setModifiableEpisodeCopy(e);
            episodeDateEditText.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime()));
        }
    }

    private class EpisodeDateOnClick implements View.OnClickListener {

        EpisodeViewModel model;

        EpisodeDateOnClick(EpisodeViewModel model){
            this.model = model;
        }

        @Override
        public void onClick(View v) {
            final Calendar c = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new EpisodeDateOnDateSetListener(this.model), c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
    }


    /*
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
        }

        @Override
        public void afterTextChanged(Editable s) {
            Episode e = model.getEpisode().getValue();
            e.setDate(new Date(s.toString()));
            model.setModifiableEpisodeCopy(e);
        }
    }
    */

    private class OnItemSelectedListenerEpisodePeriod implements AdapterView.OnItemSelectedListener {

        private EpisodeViewModel model;

        public OnItemSelectedListenerEpisodePeriod(EpisodeViewModel model){
            this.model = model;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String period = parent.getItemAtPosition(position).toString();
            Episode e = model.getEpisode().getValue();
            e.setPeriod(period);
            model.setModifiableEpisodeCopy(e);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    }

}
