package com.example.myapplication.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.auxiliaries.Constants;
import com.example.myapplication.fragments.BeliefArgumentsFragment;
import com.example.myapplication.fragments.BeliefDetailsFragment;
import com.example.myapplication.fragments.BeliefObjectionsFragment;
import com.example.myapplication.messages.DeletedBeliefEvent;
import com.example.myapplication.messages.DeletedEpisodeEvent;
import com.example.myapplication.messages.SavedEditedBeliefEvent;
import com.example.myapplication.persistence.entity.Belief;
import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.persistence.entity.ThinkingStyle;
import com.example.myapplication.viewmodel.BeliefViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class AddNewBeliefActivity extends AppCompatActivity {

    private BeliefPagerAdapter mBeliefPagerAdapter;
    private ViewPager mViewPager;
    private BeliefViewModel beliefViewModel;
    private FloatingActionButton saveBeliefFab;
    private FloatingActionButton argumentsFab;
    private FloatingActionButton objectionsFab;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_belief);

        EventBus.getDefault().register(this);

        toolbar = findViewById(R.id.belief_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Belief");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadFABs();

        beliefViewModel = ViewModelProviders.of(this).get(BeliefViewModel.class);

        if(beliefViewModel!= null && beliefViewModel.getBeliefIsLoaded() && beliefViewModel.getSelectedThinkingStylesIsLoaded()){
            initTabs();
        }

        beliefViewModel.loadBelief(this.getIntent().getIntExtra(Constants.ARG_BELIEF,-1));
        beliefViewModel.getBelief().observe(this, new Observer<Belief>() {
            @Override
            public void onChanged(@Nullable Belief belief) {

                setBeliefNameInToolbar(belief);

                if (!beliefViewModel.getBeliefIsLoaded() && belief!=null){
                    beliefViewModel.initModifiableBeliefCopy();
                    beliefViewModel.setBeliefIsLoaded(true);
                    if (beliefViewModel.getSelectedThinkingStylesIsLoaded()){
                        initTabs();
                    }
                }
                /*else{
                    if (beliefViewModel.getBeliefIsLoaded() && belief!=null){
                        initTabs();
                    }
                }*/


            }
        });
        beliefViewModel.getSelectedThinkingStyles().observe(this, new Observer<List<ThinkingStyle>>() {
            @Override
            public void onChanged(@Nullable List<ThinkingStyle> selectedThinkingStyles) {
                if (!beliefViewModel.getSelectedThinkingStylesIsLoaded() && selectedThinkingStyles!=null){
                    beliefViewModel.initModifiableSelectedThinkingStylesCopy();
                    beliefViewModel.setSelectedThinkingStylesIsLoaded(true);
                    if(beliefViewModel.getBeliefIsLoaded()){
                        initTabs();
                    }

                } else{

                }



                /*
                beliefViewModel.initModifiableSelectedThinkingStylesCopy();
                beliefViewModel.setSelectedThinkingStylesIsLoaded(true);
                if (beliefViewModel.getBeliefIsLoaded() && beliefViewModel.getSelectedThinkingStylesIsLoaded()){
                    initTabs();
                }
                */
            }
        });
    }

    private void loadFABs(){
        saveBeliefFab = findViewById(R.id.saveBeliefFab);
        argumentsFab = findViewById(R.id.addArgumentFab);
        objectionsFab = findViewById(R.id.addObjectionFab);
    }

    private void setBeliefNameInToolbar(final Belief b){
        if (b != null){
            toolbar.setTitle(b.getBelief());
        }
    }

    private void initTabs(){
        findViewById(R.id.indeterminateBar2).setVisibility(View.GONE);
        findViewById(R.id.beliefTabs).setVisibility(View.VISIBLE);

        mBeliefPagerAdapter = new BeliefPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mBeliefPagerAdapter);

        tabLayout = findViewById(R.id.beliefTabs);

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
        showRightFab(mViewPager.getCurrentItem());
    }

    private void showRightFab(int tabPosition) {

        switch (tabPosition) {
            case 1:
                saveBeliefFab.hide();
                objectionsFab.hide();
                argumentsFab.show();
                break;

            case 2:
                saveBeliefFab.hide();
                argumentsFab.hide();
                objectionsFab.show();

                break;

            case 0:
                objectionsFab.hide();
                argumentsFab.hide();
                saveBeliefFab.show();
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
            case R.id.action_delete_belief:
                beliefViewModel.removeBelief();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default: return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
            return 3;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSavedEditedBeliefEvent(SavedEditedBeliefEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
        setBeliefNameInToolbar(beliefViewModel.getModifiableBeliefCopy());
        //this.finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeletedBeliefEvent(DeletedBeliefEvent event) {

        if (event.result){
            Belief currentBelief = beliefViewModel.getBelief().getValue();
            if (currentBelief == null || currentBelief.getId() == event.deletedBeliefId){
                Toast.makeText(this, Constants.DELETED_BELIEF_MESSAGE, Toast.LENGTH_SHORT).show();
                this.finish();

            }
        } else{
            Toast.makeText(this, "Failed to delete the belief", Toast.LENGTH_SHORT).show();
        }
    }

}

