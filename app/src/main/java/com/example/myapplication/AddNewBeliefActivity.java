package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class AddNewBeliefActivity extends AppCompatActivity {

    private AddNewBeliefActivity.SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static final int DETAILS_SECTION_NUMBER = 1;
    private static final int ARGUMENTS_SECTION_NUMBER = 2;
    private static final int OBJECTIONS_SECTION_NUMBER = 3;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_belief);

        Toolbar activityToolBar = (Toolbar) findViewById(R.id.belief_toolbar);
        setSupportActionBar(activityToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new AddNewBeliefActivity.SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.beliefTabs);

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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reaction_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_reaction) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        private class OnClickUnhelpfulThinkingStyleButton implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                v.setSelected(!(v.isSelected()));
            }
        }



        public SectionFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static AddNewBeliefActivity.SectionFragment newInstance(int sectionNumber) {
            AddNewBeliefActivity.SectionFragment fragment = new AddNewBeliefActivity.SectionFragment();
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
                mDataset[i] = "Argument #" + i;
            }
            return mDataset;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            final View rootView;

            switch (getArguments().getInt(ARG_SECTION_NUMBER)){
                case DETAILS_SECTION_NUMBER:

                    rootView = inflater.inflate(R.layout.belief_details_fragment, container, false);

                    rootView.findViewById(R.id.radicalisation_button).setOnClickListener(new OnClickUnhelpfulThinkingStyleButton());
                    rootView.findViewById(R.id.catastrophising_button).setOnClickListener(new OnClickUnhelpfulThinkingStyleButton());
                    rootView.findViewById(R.id.comparation_button).setOnClickListener(new OnClickUnhelpfulThinkingStyleButton());
                    rootView.findViewById(R.id.negative_focusing_button).setOnClickListener(new OnClickUnhelpfulThinkingStyleButton());
                    rootView.findViewById(R.id.generalisation_button).setOnClickListener(new OnClickUnhelpfulThinkingStyleButton());
                    rootView.findViewById(R.id.fortune_telling_button).setOnClickListener(new OnClickUnhelpfulThinkingStyleButton());
                    rootView.findViewById(R.id.pressuring_button).setOnClickListener(new OnClickUnhelpfulThinkingStyleButton());
                    rootView.findViewById(R.id.others_empowerment_button).setOnClickListener(new OnClickUnhelpfulThinkingStyleButton());
                    rootView.findViewById(R.id.personalistation_button).setOnClickListener(new OnClickUnhelpfulThinkingStyleButton());
                    rootView.findViewById(R.id.mind_reading_button).setOnClickListener(new OnClickUnhelpfulThinkingStyleButton());

                    break;
                case ARGUMENTS_SECTION_NUMBER:

                    rootView = inflater.inflate(R.layout.argument_recycler_view, container, false);

                    RecyclerView argumentsRecyclerView = (RecyclerView) rootView.findViewById(R.id.argumentRV);

                    LinearLayoutManager argumentsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
                    argumentsRecyclerView.setLayoutManager(argumentsRecyclerViewLayoutManager);

                    ArgumentRVAdapter argumentsRecyclerViewAdapter = new ArgumentRVAdapter(initDataset());
                    argumentsRecyclerView.setAdapter(argumentsRecyclerViewAdapter);
                    break;
                case OBJECTIONS_SECTION_NUMBER:

                    rootView = inflater.inflate(R.layout.objection_recycler_view, container, false);

                    RecyclerView objectionsRecyclerView = (RecyclerView) rootView.findViewById(R.id.objectionRV);

                    LinearLayoutManager objectionsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
                    objectionsRecyclerView.setLayoutManager(objectionsRecyclerViewLayoutManager);

                    ObjectionRVAdapter objectionsRecyclerViewAdapter = new ObjectionRVAdapter(initDataset());
                    objectionsRecyclerView.setAdapter(objectionsRecyclerViewAdapter);

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
            return AddNewBeliefActivity.SectionFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

}

