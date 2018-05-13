package com.example.myapplication;

import android.arch.lifecycle.ViewModelProviders;
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

import java.util.ArrayList;

public class AddNewBeliefActivity extends AppCompatActivity {

    private AddNewBeliefActivity.BeliefPagerAdapter mBeliefPagerAdapter;
    private FloatingActionButton argumentsFab;
    private FloatingActionButton objetctionsFab;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private BeliefViewModel beliefViewModel;


    private final String BELIEFS_LIST = "BELIEFS_LIST";
    private final String REACTIONS_LIST = "REACTIONS_LIST";

   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_belief);

        Toolbar activityToolBar = (Toolbar) findViewById(R.id.belief_toolbar);
        setSupportActionBar(activityToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        argumentsFab = (FloatingActionButton) findViewById(R.id.addArgumentFab);
        objetctionsFab = (FloatingActionButton) findViewById(R.id.addObjectionFab);

        argumentsFab.hide();
        objetctionsFab.hide();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        //load lists
        ArrayList<String> beliefsList = new ArrayList<>();
        ArrayList<String> reactiosList = new ArrayList<>();

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
                objetctionsFab.hide();
                argumentsFab.show();
                break;

            case 2:
                objetctionsFab.show();
                argumentsFab.hide();
                break;

            default:
                objetctionsFab.hide();
                argumentsFab.hide();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_belief, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_save_belief:
                beliefViewModel.saveBelief();
                return true;
            case R.id.action_delete_belief:
                return true;
            default: return true;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class BeliefPagerAdapter extends FragmentPagerAdapter {

        public BeliefPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a BeliefFragment (defined as a static inner class below).
            return new BeliefFragment().newInstance((position + 1));
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

}

