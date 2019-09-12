package com.rgp.asks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.rgp.asks.R;
import com.rgp.asks.interfaces.OnFloatingActionButtonClickListener;
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

public class WhenFragment extends Fragment implements OnFloatingActionButtonClickListener {

    private TextInputLayout episodeNameTextInputLayout;
    private TextInputLayout episodeDescriptionTextInputLayout;
    private DateInputLayout episodeDateInputLayout;
    private SpinnerInputLayout episodePeriodSpinnerInputLayout;
    private EpisodeViewModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_when_ask, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragmentView, Bundle savedInstanceState) {
        initViewModel();
        int episodeIdToLoad = model.getEpisodeId();
        if (episodeIdToLoad != -1) {
            this.model.getEpisodeById().observe(this, episode -> {
                initViews(fragmentView);
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
    }

    @Override
    public void onFloatingActionButtonClick() {
        if (this.model != null) {
            ((AsksFragment) requireParentFragment()).hideKeyboard();
            saveEpisode();
        }
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
        this.model.uncheckedSaveEpisode();
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_delete, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            this.model.removeEpisode();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
