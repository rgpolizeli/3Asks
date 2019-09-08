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
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rgp.asks.R;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.ThinkingStyle;
import com.rgp.asks.viewmodel.BeliefViewModel;
import com.rgp.asks.views.TextInputLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class BeliefDetailsFragment extends Fragment {

    private BeliefViewModel model;
    private TextInputLayout beliefTextInputLayout;
    private Map<String, CheckBox> thinkingStylesCheckBoxes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_belief_details, container, false);
    }

    /*
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        AppBarLayout appBarLayout = ((MainActivity)requireActivity()).findViewById(R.id.appbar);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams)appBarLayout.getLayoutParams();
        params.setScrollFlags(SCROLL_FLAG_SCROLL);
        params.setScrollFlags(SCROLL_FLAG_ENTER_ALWAYS);
    }

     */

    @Override
    public void onViewCreated(@NonNull View fragmentView, Bundle savedInstanceState) {
        setupFAB();
        initViewModel();
        int beliefId = this.model.getBeliefId();
        if (beliefId != -1) {
            model.getThinkingStylesLiveData().observe(this, thinkingStyles -> {
                initThinkingStylesViews(fragmentView);
                if (this.model.isThinkingStylesFirstLoad()) {
                    loadFragmentThinkingStylesViewsFromViewModel(thinkingStyles);
                    this.model.initModifiableSelectedThinkingStylesCopy(thinkingStyles);
                    this.model.setIsThinkingStylesFirstLoad(false);
                }
                setupThinkingStylesViewsListeners();
            });
            model.getBeliefLiveData().observe(this, belief -> {
                initBeliefViews(fragmentView);
                if (this.model.isBeliefFirstLoad()) {
                    loadFragmentBeliefViewsFromViewModel(belief);
                    this.model.initModifiableBeliefCopy(belief);
                    this.model.setIsBeliefFirstLoad(false);
                }
                setupBeliefViewsListeners();
            });
        } else {
            //todo: err
        }
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
            this.thinkingStylesCheckBoxes
                    .get(thinkingStyle.getThinkingStyle())
                    .setChecked(true);
            ;
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
                Belief b = model.getModifiableBeliefCopy();
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

    private void initViewModel() {
        this.model = ViewModelProviders.of(requireParentFragment()).get(BeliefViewModel.class);
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void setupFAB() {
        FloatingActionButton saveBeliefFab = requireParentFragment().requireView().findViewById(com.rgp.asks.R.id.saveBeliefFab);
        saveBeliefFab.setOnClickListener(v -> {
            hideKeyboard(v);
            saveBelief();
        });
    }

    private void saveBelief() {
        model.uncheckedSaveBelief();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_belief, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete_belief) {
            model.removeBelief();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
