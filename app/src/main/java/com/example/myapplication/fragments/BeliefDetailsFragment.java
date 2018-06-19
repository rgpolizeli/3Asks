package com.example.myapplication.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.persistence.entity.Belief;
import com.example.myapplication.persistence.entity.ThinkingStyle;
import com.example.myapplication.viewmodel.BeliefViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeliefDetailsFragment extends Fragment {

    private BeliefViewModel model;
    private TextInputLayout beliefTextInputLayout;
    private TextInputEditText thoughtEditText;
    private Map<Button,ThinkingStyle> thinkingStyleButtonsMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_belief_details, container, false);

        setupFAB(container);

        this.beliefTextInputLayout = rootView.findViewById(R.id.beliefTextInputLayout);
        this.thoughtEditText = rootView.findViewById(R.id.beliefEditText);
        this.thinkingStyleButtonsMap = getUnhelpfulThinkingStylesButtons(rootView);

        model = ViewModelProviders.of(this.getActivity()).get(BeliefViewModel.class);

        Belief b = model.getModifiableBeliefCopy();
        List<ThinkingStyle> selectedThinkingStyles = model.getModifiableSelectedThinkingStylesCopy();
        if(b == null || selectedThinkingStyles==null){
            new Exception();
        } else{
            loadFragmentFromViewModel(b,selectedThinkingStyles);
        }

        /*
        model.getSelectedThinkingStyles().observe(this, new Observer<List<ThinkingStyle>>() {
            @Override
            public void onChanged(@Nullable List<ThinkingStyle> thinkingStyles) {
                setupSelectUnhelpfulThinkingStyles(getUnhelpfulThinkingStylesButtons(rootView), model);
            }
        });
        model.getBelief().observe(this, new Observer<Belief>() {
            @Override
            public void onChanged(@Nullable Belief belief) {
                setupThought(rootView,model);
            }
        });
        */
        return rootView;
    }

    private void setupFAB(ViewGroup container){
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)container.getParent();
        FloatingActionButton saveBeliefFab = coordinatorLayout.findViewById(R.id.saveBeliefFab);
        saveBeliefFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBelief();
            }
        });
    }

    private void saveBelief(){
        String newBeliefName = thoughtEditText.getText().toString();

        if (!newBeliefName.isEmpty()){
            beliefTextInputLayout.setError(null); // hide error
            model.saveBelief();
        } else{
            beliefTextInputLayout.setError("Belief is required!"); // show error
        }

    }

    private void loadFragmentFromViewModel(@NonNull final Belief b, @NonNull final List<ThinkingStyle> selectedThinkingStyles){
        setupThought(b.getBelief());
        setupSelectUnhelpfulThinkingStyles(selectedThinkingStyles);
    }

    private Map<Button,ThinkingStyle> getUnhelpfulThinkingStylesButtons(View rootView){
        Map<Button,ThinkingStyle> buttonsMap = new HashMap<>();
        List<Button> buttonsList = new ArrayList<>();

        buttonsList.add((Button)rootView.findViewById(R.id.radicalisation_button));
        buttonsList.add((Button)rootView.findViewById(R.id.catastrophising_button));
        buttonsList.add((Button)rootView.findViewById(R.id.comparation_button));
        buttonsList.add((Button)rootView.findViewById(R.id.negative_focusing_button));
        buttonsList.add((Button)rootView.findViewById(R.id.generalisation_button));
        buttonsList.add((Button)rootView.findViewById(R.id.fortune_telling_button));
        buttonsList.add((Button)rootView.findViewById(R.id.pressuring_button));
        buttonsList.add((Button)rootView.findViewById(R.id.others_empowerment_button));
        buttonsList.add((Button)rootView.findViewById(R.id.personalisation_button));
        buttonsList.add((Button)rootView.findViewById(R.id.mind_reading_button));
        buttonsList.add((Button)rootView.findViewById(R.id.labeling_button));

        for(Button button : buttonsList){
            buttonsMap.put(button,new ThinkingStyle(button.getText().toString()));
        }

        return buttonsMap;
    }


    private void setupSelectUnhelpfulThinkingStyles(@NonNull final List<ThinkingStyle> selectedThinkingStyles){

        for (Button button : thinkingStyleButtonsMap.keySet()){
            button.setOnClickListener(new OnClickListenerUnhelpfulThinkingStyleButton(selectedThinkingStyles));

            if(selectedThinkingStyles.contains(thinkingStyleButtonsMap.get(button))){
                button.setSelected(Boolean.TRUE);
            }
        }

    }

    private void setupThought(String thought){
        if (!thought.isEmpty()){
            thoughtEditText.setText(thought);
        }

        thoughtEditText.addTextChangedListener(new ThoughtTextWatcher());
    }

    // //////////
    // LISTENERS //
    // //////////

    private class OnClickListenerUnhelpfulThinkingStyleButton implements View.OnClickListener {

        List<ThinkingStyle> selectedThinkingStyles;

        public OnClickListenerUnhelpfulThinkingStyleButton(@NonNull final List<ThinkingStyle> selectedThinkingStyles){
            this.selectedThinkingStyles = selectedThinkingStyles;
        }

        @Override
        public void onClick(View v) {

            Button button = (Button)v;

            if(!button.isSelected()){
                button.setSelected(Boolean.TRUE);
                model.addUnhelpfulThinkingStyle(thinkingStyleButtonsMap.get(button));
            } else{
                button.setSelected(Boolean.FALSE);
                model.removeUnhelpfulThinkingStyle(thinkingStyleButtonsMap.get(button));
            }
        }

    }


    private class ThoughtTextWatcher implements TextWatcher {

        public ThoughtTextWatcher(){
        }

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
    }
}
