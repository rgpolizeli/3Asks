package com.example.myapplication;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

public class AsksActivity extends AppCompatActivity {

    final int REQUEST_NEW_REACTION = 1;
    final int REQUEST_NEW_BELIEF = 2;

    final int RESULT_NEW_REACTION_ADD = 100;
    final int RESULT_NEW_REACTION_DELETE = 101;
    final int RESULT_NEW_BELIEF_ADD = 102;
    final int RESULT_NEW_BELIEF_DELETE = 103;

    final String NEW_BELIEF = "NEW_BELIEF";

    private ArrayList<String> beliefsList;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static final int WHEN_SECTION_NUMBER = 1;
    private static final int WHAT_SECTION_NUMBER = 2;
    private static final int WHY_SECTION_NUMBER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asks_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //passar episodio pelo bundler, como json convertido para string, se vazio, sabe-se que é para criar
        //senão é para editar e precisa preencher os views.

        Bundle bundle = getIntent().getExtras();
        bundle.getString("episode");

        //load beliefs list
        beliefsList = new ArrayList<>();


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                invalidateOptionsMenu();
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_asks, menu);

        MenuItem item = menu.findItem(R.id.action_add_reaction);
        int sectionNumber = mViewPager.getCurrentItem()+1;

        if (item.isVisible()){
            if (sectionNumber == WHEN_SECTION_NUMBER){
                item.setVisible(false);
            }
        } else{
            if(sectionNumber != WHEN_SECTION_NUMBER){
                item.setVisible(true);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_add_reaction:
                if (this.mViewPager.getCurrentItem()+1 == WHAT_SECTION_NUMBER){
                    addReaction();
                }
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQUEST_NEW_BELIEF) {
            if (resultCode == RESULT_NEW_BELIEF_ADD) {



                beliefsList = data.getStringArrayListExtra(NEW_BELIEF);
                int position = beliefsList.size() + 1;
                beliefsList.add("");
                //notifyDataSetChanged();

            } else{
                if(resultCode == RESULT_NEW_BELIEF_DELETE){

                }
            }
        }
    }



    private void addReaction(){
        Intent intent = new Intent(this.getApplicationContext(), AddNewReactionActivity.class);
        startActivity(intent);
    }


    /**
     * A SectionFragment containing a simple view.
     */
    public static class SectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public SectionFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static SectionFragment newInstance(int sectionNumber) {
            SectionFragment fragment = new SectionFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            return fragment;
        }

        /*
         * Generates Strings for RecyclerView's adapter. This data would usually come
         * from a local content provider or remote server.
         */
        private String[] initDataset() {

            int datasetLength = 20;

            String [] mDataset = new String[datasetLength];
            for (int i = 0; i < datasetLength; i++) {
                mDataset[i] = "Reaction #" + i;
            }
            return mDataset;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            final View rootView;

            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case WHEN_SECTION_NUMBER:

                    rootView = inflater.inflate(R.layout.when_ask_fragment, container, false);
                    break;
                case WHAT_SECTION_NUMBER:

                    rootView = inflater.inflate(R.layout.what_ask_fragment, container, false);

                    RecyclerView reactionsRecyclerView = (RecyclerView) rootView.findViewById(R.id.reactionsRecyclerView);

                    LinearLayoutManager reactionsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
                    reactionsRecyclerView.setLayoutManager(reactionsRecyclerViewLayoutManager);

                    ReactionRVAdapter reactionsRecyclerViewAdapter = new ReactionRVAdapter(initDataset());
                    reactionsRecyclerView.setAdapter(reactionsRecyclerViewAdapter);
                    break;
                case WHY_SECTION_NUMBER:

                    rootView = inflater.inflate(R.layout.why_ask_fragment, container, false);

                    RecyclerView beliefsRecyclerView = (RecyclerView) rootView.findViewById(R.id.beliefsRecyclerView);

                    beliefsRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

                    beliefsRecyclerView.setAdapter(new BeliefRVAdapter(initDataset()));

                    break;
                    default:

                        rootView = inflater.inflate(R.layout.fragment_asks, container, false);
                        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
                        break;
            }
            
            return rootView;
            
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a SectionFragment (defined as a static inner class below).
            return SectionFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
