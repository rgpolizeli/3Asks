package com.rgp.asks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rgp.asks.listeners.EpisodeDateTextWatcher;
import com.rgp.asks.listeners.EpisodeDescriptionTextWatcher;
import com.rgp.asks.listeners.EpisodeNameTextWatcher;
import com.rgp.asks.listeners.OnItemSelectedListenerEpisodePeriod;
import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.viewmodel.EpisodeViewModel;
import com.rgp.asks.views.DateInputLayout;
import com.rgp.asks.views.SpinnerInputLayout;
import com.rgp.asks.views.TextInputLayout;

import java.text.DateFormat;

public class WhenFragment extends Fragment {

    private TextInputLayout episodeNameTextInputLayout;
    private TextInputLayout episodeDescriptionTextInputLayout;
    private DateInputLayout episodeDateInputLayout;
    private SpinnerInputLayout episodePeriodSpinnerInputLayout;

    private EpisodeViewModel model;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.rgp.asks.R.layout.fragment_when_ask, container, false);

        setupFAB(container);

        initViewModel();

        int episodeIdToLoad = model.getEpisodeId();
        if (episodeIdToLoad != -1) {
            this.model.getEpisodeById().observe(this, episode -> {
                initViews(rootView);
                if (model.isEpisodeInFirstLoad()) {
                    loadFragmentFromViewModel(episode);
                    model.initModifiableEpisodeCopy(episode);
                    model.setIsEpisodeInFirstLoad(false);
                }
                setupViewListeners();
            });
        } else {
            //todo: err
        }

        return rootView;
    }

    /**
     * Handle click on saveEpisodeFloatingActionButton and call saveEpisode.
     *
     * @param container is the viewgroup of this fragment.
     */
    private void setupFAB(@NonNull ViewGroup container) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) container.getParent();
        FloatingActionButton saveEpisodeFab = coordinatorLayout.findViewById(com.rgp.asks.R.id.saveEpisodeFab);
        saveEpisodeFab.setOnClickListener(v -> {
            ((AsksFragment) requireParentFragment()).hideKeyboard();
            saveEpisode();
        });
    }

    private void initViewModel() {
        this.model = ViewModelProviders.of(requireParentFragment()).get(EpisodeViewModel.class);
    }

    private void initViews(View rootView) {
        this.episodeNameTextInputLayout = rootView.findViewById(com.rgp.asks.R.id.episodeNameTextInputLayout);
        this.episodeDescriptionTextInputLayout = rootView.findViewById(com.rgp.asks.R.id.episodeDescriptionTextInputLayout);
        this.episodeDateInputLayout = rootView.findViewById(com.rgp.asks.R.id.episodeDateInputLayout);
        this.episodePeriodSpinnerInputLayout = rootView.findViewById(com.rgp.asks.R.id.episodePeriodSpinnerInputLayout);
    }

    private void saveEpisode() {
        String newEpisodeName = this.episodeNameTextInputLayout.getValue().toString();
        if (!newEpisodeName.isEmpty()) {

            // hide error
            if (this.episodeNameTextInputLayout.hasFocus()) {
                this.episodeNameTextInputLayout.goToState(TextInputLayout.STATE_FOCUSED);
            } else {
                this.episodeNameTextInputLayout.goToState(TextInputLayout.STATE_NORMAL);
            }

            this.model.uncheckedSaveEpisode();

        } else {
            this.episodeNameTextInputLayout.goToState(TextInputLayout.STATE_ERROR); // show error
        }
    }

    private void loadFragmentFromViewModel(@NonNull Episode e) {
        this.episodeNameTextInputLayout.setValue(e.getEpisode());
        this.episodeDescriptionTextInputLayout.setValue(e.getDescription());
        this.episodeDateInputLayout.setValue(DateFormat.getDateInstance(DateFormat.SHORT).format(e.getDate()));
        this.episodePeriodSpinnerInputLayout.setValue(e.getPeriod());
    }

    private void setupViewListeners() {
        episodeNameTextInputLayout.addTextChangedListener(new EpisodeNameTextWatcher(model));
        episodeDescriptionTextInputLayout.addTextChangedListener(new EpisodeDescriptionTextWatcher(model));
        episodeDateInputLayout.addTextChangedListener(new EpisodeDateTextWatcher(model));
        episodePeriodSpinnerInputLayout.setOnItemSelectedListener(new OnItemSelectedListenerEpisodePeriod(model));
    }
}
