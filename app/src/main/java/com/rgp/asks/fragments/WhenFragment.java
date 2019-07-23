package com.rgp.asks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rgp.asks.activities.AsksActivity;
import com.rgp.asks.listeners.EpisodeDateOnClick;
import com.rgp.asks.listeners.EpisodeDescriptionTextWatcher;
import com.rgp.asks.listeners.EpisodeTextWatcher;
import com.rgp.asks.listeners.OnItemSelectedListenerEpisodePeriod;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.viewmodel.EpisodeViewModel;

import java.text.DateFormat;
import java.util.Date;

import static android.content.Context.INPUT_METHOD_SERVICE;

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

        View rootView = inflater.inflate(com.rgp.asks.R.layout.fragment_when_ask, container, false);

        setupFAB(container);

        episodeTextInputLayout = rootView.findViewById(com.rgp.asks.R.id.episodeTextInputLayout);
        episodeEditText = rootView.findViewById(com.rgp.asks.R.id.episode_name_edit_view);
        episodeDescriptionEditText = rootView.findViewById(com.rgp.asks.R.id.episode_description_edit_view);
        episodeDateEditText = rootView.findViewById(com.rgp.asks.R.id.episode_date_edit_text);
        episodePeriodSpinner = rootView.findViewById(com.rgp.asks.R.id.episode_period_spinner);

        Episode e = model.getEpisode().getValue();

        if (e == null) {
            new Exception();
        } else {
            loadFragmentFromViewModel(e);
            setupViewListeners();
        }

        return rootView;
    }

    private void setupFAB(ViewGroup container) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) container.getParent();
        FloatingActionButton saveEpisodeFab = coordinatorLayout.findViewById(com.rgp.asks.R.id.saveEpisodeFab);
        saveEpisodeFab.setOnClickListener(v -> {
            ((AsksActivity) getActivity()).hideKeyboard();
            saveEpisode();
        });
    }

    private void saveEpisode() {
        String newEpisodeName = episodeEditText.getText().toString();

        if (!newEpisodeName.isEmpty()) {
            episodeTextInputLayout.setError(null); // hide error
            model.uncheckedSaveEpisode();
        } else {
            episodeTextInputLayout.setError("Episode is required!"); // show error
        }

    }

    private void loadFragmentFromViewModel(@NonNull Episode e) {
        String episode = e.getEpisode();
        String episodeDescription = e.getDescription();
        Date episodeDate = e.getDate();
        String episodePeriod = e.getPeriod();

        episodeEditText.setText(episode);
        episodeDescriptionEditText.setText(episodeDescription);
        episodeDateEditText.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(episodeDate));

        //episodePeriodSpinner.setSelection(((ArrayAdapter)episodePeriodSpinner.getAdapter()).getPosition(episodePeriod));
        episodePeriodSpinner.setSelection(getPositionInSpinnerAdapter(episodePeriodSpinner.getAdapter(), episodePeriod));

    }

    private int getPositionInSpinnerAdapter(SpinnerAdapter spinnerAdapter, String item) {
        if (spinnerAdapter != null) {
            for (int i = 0; i < spinnerAdapter.getCount(); i++) {
                if (spinnerAdapter.getItem(i).toString().equals(item)) {
                    return i;
                }
            }
        }
        return 0;
    }

    private void setupViewListeners() {
        episodeEditText.addTextChangedListener(new EpisodeTextWatcher(model));
        episodeDescriptionEditText.addTextChangedListener(new EpisodeDescriptionTextWatcher(model));
        episodeDateEditText.setOnClickListener(new EpisodeDateOnClick(model, episodeDateEditText));
        episodePeriodSpinner.setOnItemSelectedListener(new OnItemSelectedListenerEpisodePeriod(model));
        episodePeriodSpinner.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return false;
        });
    }
}
