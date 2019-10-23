package com.rgp.asks.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.rgp.asks.R;
import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.interfaces.OnFloatingActionButtonClickListener;
import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.ThinkingStyle;
import com.rgp.asks.viewmodel.BeliefViewModel;
import com.rgp.asks.views.TextInputLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeliefDetailsFragment extends Fragment implements OnFloatingActionButtonClickListener, OnUpdatedEntityListener, OnDeletedEntityListener {

    private Observer<Belief> observerBelief;
    private Observer<List<ThinkingStyle>> observerThinkingStyles;
    private BeliefViewModel model;
    private TextInputLayout beliefTextInputLayout;
    private Map<String, CheckBox> thinkingStylesCheckBoxes;
    private OnUpdatedEntityListener onUpdatedEntityListener;
    private OnDeletedEntityListener onDeletedEntityListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createObserverBelief();
        createObserverThinkingStyles();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_belief_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragmentView, Bundle savedInstanceState) {
        initViewModel();
        int beliefId = this.model.getEntityId();
        if (beliefId != -1) {

            initThinkingStylesViews(fragmentView);
            model.getThinkingStylesLiveData().removeObservers(this);
            model.getThinkingStylesLiveData().observe(this, this.observerThinkingStyles);

            initBeliefViews(fragmentView);
            model.getBelief().removeObservers(this);
            model.getBelief().observe(this, this.observerBelief);

        } else {
            //todo: err
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        this.onUpdatedEntityListener = this;
        this.onDeletedEntityListener = this;
    }

    @Override
    public void onStop() {
        super.onStop();
        this.onUpdatedEntityListener = null;
        this.onDeletedEntityListener = null;
    }

    private void createObserverBelief() {
        this.observerBelief = result -> {
            if (this.model.isEntityInFirstLoad()) {
                loadFragmentBeliefViewsFromViewModel(result);
                this.model.setModifiableEntityCopy(result.copy());
                this.model.setIsEntityInFirstLoad(false);
            }
            setupBeliefViewsListeners();
        };
    }

    private void createObserverThinkingStyles() {
        this.observerThinkingStyles = result -> {
            //it is necessary to ever reload checkboxes when this fragment is showed. See issue #
            loadFragmentThinkingStylesViewsFromViewModel(result);
            if (this.model.isThinkingStylesFirstLoad()) {
                this.model.initModifiableSelectedThinkingStylesCopy(result);
                this.model.setIsThinkingStylesFirstLoad(false);
            }
            setupThinkingStylesViewsListeners();
        };
    }

    private void initBeliefViews(@NonNull View rootView) {
        this.beliefTextInputLayout = rootView.findViewById(R.id.beliefTextInputLayout);
    }

    private void initThinkingStylesViews(@NonNull View rootView) {
        this.thinkingStylesCheckBoxes = new HashMap<>();
        this.thinkingStylesCheckBoxes.put(getString(R.string.unhelpful_thinking_style_black_white_label), rootView.findViewById(R.id.radicalisationCheckBox));
        this.thinkingStylesCheckBoxes.put(getString(R.string.unhelpful_thinking_style_catastrophising_label), rootView.findViewById(R.id.catastrophisingCheckBox));
        this.thinkingStylesCheckBoxes.put(getString(R.string.unhelpful_thinking_style_magnification_minimisation_label), rootView.findViewById(R.id.comparationCheckBox));
        this.thinkingStylesCheckBoxes.put(getString(R.string.unhelpful_thinking_style_mental_filter_label), rootView.findViewById(R.id.negativeFocusingCheckBox));
        this.thinkingStylesCheckBoxes.put(getString(R.string.unhelpful_thinking_style_overgeneralisation_label), rootView.findViewById(R.id.generalisationCheckBox));
        this.thinkingStylesCheckBoxes.put(getString(R.string.unhelpful_thinking_style_predictive_thinking_label), rootView.findViewById(R.id.fortuneTellingCheckBox));
        this.thinkingStylesCheckBoxes.put(getString(R.string.unhelpful_thinking_style_shoulding_musting_label), rootView.findViewById(R.id.pressuringCheckBox));
        this.thinkingStylesCheckBoxes.put(getString(R.string.unhelpful_thinking_style_others_empowerment_label), rootView.findViewById(R.id.othersEmpowermentCheckBox));
        this.thinkingStylesCheckBoxes.put(getString(R.string.unhelpful_thinking_style_personalisation_label), rootView.findViewById(R.id.personalisationCheckBox));
        this.thinkingStylesCheckBoxes.put(getString(R.string.unhelpful_thinking_style_mind_reading_label), rootView.findViewById(R.id.mindReadingCheckBox));
        this.thinkingStylesCheckBoxes.put(getString(R.string.unhelpful_thinking_style_labeling_label), rootView.findViewById(R.id.labelingCheckBox));
    }

    private void loadFragmentBeliefViewsFromViewModel(@NonNull Belief belief) {
        this.beliefTextInputLayout.setValue(belief.getBelief());
    }

    private void loadFragmentThinkingStylesViewsFromViewModel(@NonNull List<ThinkingStyle> thinkingStyles) {
        for (ThinkingStyle thinkingStyle : thinkingStyles) {
            CheckBox checkBox = this.thinkingStylesCheckBoxes.get(thinkingStyle.getThinkingStyle());
            checkBox.setChecked(true);
            checkBox.jumpDrawablesToCurrentState();
        }
    }

    private void setupBeliefViewsListeners() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Belief b = model.getModifiableEntityCopy();
                b.setBelief(s.toString());
            }
        };
        this.beliefTextInputLayout.addTextChangedListener(textWatcher);
    }

    private void setupThinkingStylesViewsListeners() {
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = (buttonView, isChecked) -> {
            CheckBox checkBox = (CheckBox) buttonView;
            String thinkingStyleAsString = checkBox.getText().toString();
            ThinkingStyle thinkingStyle = new ThinkingStyle(thinkingStyleAsString);
            if (isChecked) {
                this.model.addUnhelpfulThinkingStyle(thinkingStyle);
            } else {
                this.model.removeUnhelpfulThinkingStyle(thinkingStyle);
            }
        };
        for (CheckBox checkBox : this.thinkingStylesCheckBoxes.values()) {
            checkBox.setOnCheckedChangeListener(onCheckedChangeListener);
        }
    }

    private void finish() {
        Navigation.findNavController(requireActivity().findViewById(R.id.nav_host_fragment)).navigateUp();
    }

    private void initViewModel() {
        this.model = ViewModelProviders.of(requireParentFragment()).get(BeliefViewModel.class);
    }

    @Override
    public void onFloatingActionButtonClick() {
        ((AddNewBeliefFragment) requireParentFragment()).hideKeyboard();
        updateBelief();
    }

    private void updateBelief() {
        if (model.beliefWasChanged()) {
            this.model.updateBelief(false, this.onUpdatedEntityListener);
        } else {
            onUpdatedEntity(false, 1);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_delete, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            this.model.deleteBelief(this.onDeletedEntityListener);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDeletedEntity(int numberOfDeletedRows) {
        if (numberOfDeletedRows == 1) {
            Toast.makeText(requireActivity(), "Deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireActivity(), "Error in delete!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    public void onUpdatedEntity(boolean finishSignal, int numberOfUpdatedRows) {
        if (numberOfUpdatedRows > 0) {
            Toast.makeText(requireActivity(), "Saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireActivity(), "Error in save!", Toast.LENGTH_SHORT).show();
        }
        if (finishSignal) {
            finish();
        }
    }

    public OnUpdatedEntityListener getOnUpdatedEntityListener() {
        return this.onUpdatedEntityListener;
    }
}
