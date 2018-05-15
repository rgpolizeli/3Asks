package com.example.myapplication.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.R;
import com.example.myapplication.models.ReactionViewModel;

import org.json.JSONException;
import org.json.JSONObject;

public class AddNewReactionActivity extends AppCompatActivity {

    private ReactionViewModel reactionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reaction);

        Toolbar activityToolBar = (Toolbar) findViewById(R.id.what_toolbar);
        setSupportActionBar(activityToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reactionViewModel = ViewModelProviders.of(this).get(ReactionViewModel.class);

        setupReaction();
        setupReactionClass();
    }

    private void setupReaction(){
        EditText reactionEditText = (EditText) findViewById(R.id.reaction_edit_view);
        String reaction = reactionViewModel.getReaction();

        if (!reaction.isEmpty()){
            reactionEditText.setText(reaction);
        }

        reactionEditText.addTextChangedListener(new ReactionTextWatcher(reactionViewModel));
    }

    private void setupReactionClass(){
        Spinner reactionClassSpinner = (Spinner)findViewById(R.id.reaction_class_spinner);
        String reactionClass = reactionViewModel.getReactionClass();

        if (!reactionClass.isEmpty()){
            reactionClassSpinner.setSelection(((ArrayAdapter)reactionClassSpinner.getAdapter()).getPosition(reactionClass));
        }

        reactionClassSpinner.setOnItemSelectedListener(new OnItemSelectedListenerReactionClass(reactionViewModel));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reaction_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_save_reaction:
                Intent resultIntent = new Intent();
                resultIntent.putExtra(Constants.ARG_NEW_REACTION, mountActivityResult().toString());
                setResult(Constants.RESULT_NEW_REACTION_ADD, resultIntent);
                finish();
                return true;
            case R.id.action_delete_reaction:
                return true;
            default: return true;
        }
    }

    private JSONObject mountActivityResult(){

        JSONObject resultJSON = new JSONObject();

        try {
            resultJSON.put(Constants.JSON_REACTION, reactionViewModel.getReaction());
            resultJSON.put(Constants.JSON_REACTION_CLASS, reactionViewModel.getReactionClass());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultJSON;
    }

    //
    // LISTENERS
    //

    private class OnItemSelectedListenerReactionClass implements AdapterView.OnItemSelectedListener {

        private ReactionViewModel model;

        public OnItemSelectedListenerReactionClass(ReactionViewModel model){
            this.model = model;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String reactionClass = parent.getItemAtPosition(position).toString();
            model.setReactionClass(reactionClass);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class ReactionTextWatcher implements TextWatcher {

        private ReactionViewModel model;

        public ReactionTextWatcher(ReactionViewModel model){
            this.model = model;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            model.setReaction(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

}

