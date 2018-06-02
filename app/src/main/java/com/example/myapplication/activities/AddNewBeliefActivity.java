package com.example.myapplication.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.R;
import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.fragments.BeliefArgumentsFragment;
import com.example.myapplication.fragments.BeliefDetailsFragment;
import com.example.myapplication.fragments.BeliefObjectionsFragment;
import com.example.myapplication.viewmodel.BeliefViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddNewBeliefActivity extends AppCompatActivity {

    private AddNewBeliefActivity.BeliefPagerAdapter mBeliefPagerAdapter;
    private FloatingActionButton argumentsFab;
    private FloatingActionButton objectionsFab;

    private ViewPager mViewPager;
    private BeliefViewModel beliefViewModel;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_belief);

        Toolbar activityToolBar = (Toolbar) findViewById(R.id.belief_toolbar);
        setSupportActionBar(activityToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        argumentsFab = (FloatingActionButton) findViewById(R.id.addArgumentFab);
        objectionsFab = (FloatingActionButton) findViewById(R.id.addObjectionFab);

        argumentsFab.hide();
        objectionsFab.hide();

        mBeliefPagerAdapter = new AddNewBeliefActivity.BeliefPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mBeliefPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.beliefTabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                showRightFab(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                super.onTabReselected(tab);
            }
        });

        beliefViewModel = ViewModelProviders.of(this).get(BeliefViewModel.class);

    }

    private void showRightFab(int tabPosition) {
        switch (tabPosition) {
            case 1:
                objectionsFab.hide();
                argumentsFab.show();
                break;

            case 2:
                objectionsFab.show();
                argumentsFab.hide();
                break;

            default:
                objectionsFab.hide();
                argumentsFab.hide();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_belief, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_save_belief:
                Intent resultIntent = new Intent();
                resultIntent.putExtra(Constants.ARG_NEW_BELIEF, mountActivityResult().toString());
                setResult(Constants.RESULT_NEW_BELIEF, resultIntent);
                finish();
                return true;
            case R.id.action_delete_belief:
                return true;
            default: return true;
        }
    }

    private JSONObject mountActivityResult(){
        JSONObject resultJSON = new JSONObject();

        JSONArray selectedUnhelpfulThinkingStylesArray = new JSONArray();
        for(String unhelpfulThinkingStyleLabel : beliefViewModel.getSelectedThinkingStyles()){
            selectedUnhelpfulThinkingStylesArray.put(unhelpfulThinkingStyleLabel);
        }

        JSONArray argumentsArray = new JSONArray();
        for(String argumentLabel : beliefViewModel.getArguments()){
            argumentsArray.put(argumentLabel);
        }

        JSONArray objectionsArray = new JSONArray();
        for(String objectionLabel : beliefViewModel.getObjections()){
            objectionsArray.put(objectionLabel);
        }

        try {
            resultJSON.put(Constants.JSON_BELIEF_THOUGHT, beliefViewModel.getBeliefThought());
            resultJSON.put(Constants.JSON_BELIEF_UNHELPFUL_THINKING_STYLES, selectedUnhelpfulThinkingStylesArray);
            resultJSON.put(Constants.JSON_BELIEF_ARGUMENTS, argumentsArray);
            resultJSON.put(Constants.JSON_BELIEF_OBJECTIONS, objectionsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return resultJSON;
    }

    public class BeliefPagerAdapter extends FragmentPagerAdapter {

        public BeliefPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment f = null;

            switch(position){
                case 0:
                    f = new BeliefDetailsFragment();
                    break;
                case 1:
                    f = new BeliefArgumentsFragment();
                    break;
                case 2:
                    f = new BeliefObjectionsFragment();
                    break;
            }

            return f;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

}

