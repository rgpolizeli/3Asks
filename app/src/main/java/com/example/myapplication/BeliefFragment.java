package com.example.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A BeliefFragment containing a simple view.
 */
public class BeliefFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private final String ARG_SECTION_NUMBER = "section_number";
    private static final int DETAILS_SECTION_NUMBER = 1;
    private static final int ARGUMENTS_SECTION_NUMBER = 2;
    private static final int OBJECTIONS_SECTION_NUMBER = 3;


    public BeliefFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public BeliefFragment newInstance(int sectionNumber) {
        BeliefFragment fragment = new BeliefFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);

        return fragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView;
        BeliefViewModel model = ViewModelProviders.of(this.getActivity()).get(BeliefViewModel.class);

        switch (getArguments().getInt(ARG_SECTION_NUMBER)){
            case DETAILS_SECTION_NUMBER:
                rootView = inflater.inflate(R.layout.belief_details_fragment, container, false);
                setupThought(rootView,model);
                setupSelectUnhelpfulThinkingStyles(getUnhelpfulThinkingStylesButtons(rootView), model);
                break;
            case ARGUMENTS_SECTION_NUMBER:

                rootView = inflater.inflate(R.layout.argument_recycler_view, container, false);

                final RecyclerView argumentsRecyclerView = (RecyclerView) rootView.findViewById(R.id.argumentRV);
                LinearLayoutManager argumentsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
                argumentsRecyclerView.setLayoutManager(argumentsRecyclerViewLayoutManager);

                final ArgumentRVAdapter argumentsRecyclerViewAdapter = new ArgumentRVAdapter(model.getArguments());
                argumentsRecyclerView.setAdapter(argumentsRecyclerViewAdapter);

                CoordinatorLayout coordinatorLayout = (CoordinatorLayout)container.getParent();
                FloatingActionButton argumentsFab = (FloatingActionButton) coordinatorLayout.findViewById(R.id.addArgumentFab);
                argumentsFab.setOnClickListener(new OnClickListenerFABArguments(argumentsRecyclerView,argumentsRecyclerViewAdapter,model));

                break;
            case OBJECTIONS_SECTION_NUMBER:

                rootView = inflater.inflate(R.layout.objection_recycler_view, container, false);

                final RecyclerView objectionsRecyclerView = (RecyclerView) rootView.findViewById(R.id.objectionRV);
                LinearLayoutManager objectionsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
                objectionsRecyclerView.setLayoutManager(objectionsRecyclerViewLayoutManager);

                final ObjectionRVAdapter objectionsRecyclerViewAdapter = new ObjectionRVAdapter(model.getObjections());
                objectionsRecyclerView.setAdapter(objectionsRecyclerViewAdapter);

                CoordinatorLayout coordinatorLayout2 = (CoordinatorLayout)container.getParent();
                FloatingActionButton objectionsFab = (FloatingActionButton) coordinatorLayout2.findViewById(R.id.addObjectionFab);
                objectionsFab.setOnClickListener(new OnClickListenerFABObjections(objectionsRecyclerView,objectionsRecyclerViewAdapter,model));

                break;
            default:

                rootView = inflater.inflate(R.layout.fragment_asks, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                break;
        }

        return rootView;
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


    private class OnClickListenerFABArguments implements View.OnClickListener {

        RecyclerView recyclerView;
        ArgumentRVAdapter adapter;
        BeliefViewModel model;

        public OnClickListenerFABArguments(RecyclerView recyclerView, ArgumentRVAdapter adapter, BeliefViewModel model){
            this.recyclerView = recyclerView;
            this.adapter = adapter;
            this.model = model;
        }

        @Override
        public void onClick(View v) {
            adapter.addArgument();
            recyclerView.scrollToPosition(adapter.getItemCount()-1);
            model.addArgument();
        }

    }

    private class OnClickListenerFABObjections implements View.OnClickListener {

        RecyclerView recyclerView;
        ObjectionRVAdapter adapter;
        BeliefViewModel model;

        public OnClickListenerFABObjections(RecyclerView recyclerView, ObjectionRVAdapter adapter, BeliefViewModel model){
            this.recyclerView = recyclerView;
            this.adapter = adapter;
            this.model = model;
        }

        @Override
        public void onClick(View v) {
            adapter.addObjection();
            recyclerView.scrollToPosition(adapter.getItemCount()-1);
            model.addObjection();
        }

    }


}