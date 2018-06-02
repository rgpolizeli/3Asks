package com.example.myapplication.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.R;
import com.example.myapplication.viewmodel.BeliefViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeliefDetailsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.belief_details_fragment, container, false);
        BeliefViewModel model = ViewModelProviders.of(this.getActivity()).get(BeliefViewModel.class);

        setupThought(rootView,model);
        setupSelectUnhelpfulThinkingStyles(getUnhelpfulThinkingStylesButtons(rootView), model);

        return rootView;
    }

    private Map<String,Button> getUnhelpfulThinkingStylesButtons(View rootView){
        Map<String,Button> buttonsMap = new HashMap<>();
        List<Button> buttonsList = new ArrayList<>();

        buttonsList.add((Button)rootView.findViewById(R.id.radicalisation_button));
        buttonsList.add((Button)rootView.findViewById(R.id.catastrophising_button));
        buttonsList.add((Button)rootView.findViewById(R.id.comparation_button));
        buttonsList.add((Button)rootView.findViewById(R.id.negative_focusing_button));
        buttonsList.add((Button)rootView.findViewById(R.id.generalisation_button));
        buttonsList.add((Button)rootView.findViewById(R.id.fortune_telling_button));
        buttonsList.add((Button)rootView.findViewById(R.id.pressuring_button));
        buttonsList.add((Button)rootView.findViewById(R.id.others_empowerment_button));
        buttonsList.add((Button)rootView.findViewById(R.id.personalistation_button));
        buttonsList.add((Button)rootView.findViewById(R.id.mind_reading_button));

        for(Button button : buttonsList){
            buttonsMap.put(button.getText().toString(), button);
        }

        return buttonsMap;
    }


    private void setupSelectUnhelpfulThinkingStyles(Map<String,Button> buttonsMap, BeliefViewModel model){

        for (Button button : buttonsMap.values()){
            button.setOnClickListener(new OnClickListenerUnhelpfulThinkingStyleButton(model));
        }

        for(String label : model.getSelectedThinkingStyles()){
            Button button = buttonsMap.get(label);
            button.setSelected(Boolean.TRUE);
        }

    }

    private void setupThought(View rootView,BeliefViewModel model){
        EditText thoughtEditText = (EditText)rootView.findViewById(R.id.belief_edit_view);
        String thoughtString = model.getBeliefThought();

        if (!thoughtString.isEmpty()){
            thoughtEditText.setText(thoughtString);
        }

        thoughtEditText.addTextChangedListener(new ThoughtTextWatcher(model));
    }

    // //////////
    // LISTENERS //
    // //////////

    private class OnClickListenerUnhelpfulThinkingStyleButton implements View.OnClickListener {

        BeliefViewModel model;

        public OnClickListenerUnhelpfulThinkingStyleButton(BeliefViewModel model){
            this.model = model;
        }

        @Override
        public void onClick(View v) {

            Button button = (Button)v;

            if(!button.isSelected()){
                button.setSelected(Boolean.TRUE);
                model.addUnhelpfulThinkingStyle(button.getText().toString());
            } else{
                button.setSelected(Boolean.FALSE);
                model.removeUnhelpfulThinkingStyle(button.getText().toString());
            }
        }

    }


    private class ThoughtTextWatcher implements TextWatcher {

        private BeliefViewModel model;

        public ThoughtTextWatcher(BeliefViewModel model){
            this.model = model;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            model.setBeliefThought(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }
}
