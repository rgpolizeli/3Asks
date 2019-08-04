package com.rgp.asks.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.rgp.asks.R;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.fragments.BeliefArgumentsFragment;
import com.rgp.asks.fragments.BeliefDetailsFragment;
import com.rgp.asks.fragments.BeliefObjectionsFragment;
import com.rgp.asks.messages.DeletedBeliefEvent;
import com.rgp.asks.messages.SavedEditedBeliefEvent;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.persistence.entity.ThinkingStyle;
import com.rgp.asks.viewmodel.BeliefViewModel;

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
        setContentView(com.rgp.asks.R.layout.activity_add_belief);

        EventBus.getDefault().register(this);

        toolbar = findViewById(com.rgp.asks.R.id.belief_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Belief");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        loadFABs();

        beliefViewModel = ViewModelProviders.of(this).get(BeliefViewModel.class);

        if (beliefViewModel != null && beliefViewModel.getBeliefIsLoaded() && beliefViewModel.getSelectedThinkingStylesIsLoaded()) {
            initTabs();
        }

        beliefViewModel.loadBelief(this.getIntent().getIntExtra(Constants.ARG_BELIEF, -1));
        beliefViewModel.getBelief().observe(this, new Observer<Belief>() {
            @Override
            public void onChanged(@Nullable Belief belief) {

                setBeliefNameInToolbar(belief);

                if (!beliefViewModel.getBeliefIsLoaded() && belief != null) {
                    beliefViewModel.initModifiableBeliefCopy();
                    beliefViewModel.setBeliefIsLoaded(true);
                    if (beliefViewModel.getSelectedThinkingStylesIsLoaded()) {
                        initTabs();
                    }
                }

            }
        });
        beliefViewModel.getSelectedThinkingStyles().observe(this, new Observer<List<ThinkingStyle>>() {
            @Override
            public void onChanged(@Nullable List<ThinkingStyle> selectedThinkingStyles) {
                if (!beliefViewModel.getSelectedThinkingStylesIsLoaded() && selectedThinkingStyles != null) {
                    beliefViewModel.initModifiableSelectedThinkingStylesCopy();
                    beliefViewModel.setSelectedThinkingStylesIsLoaded(true);
                    if (beliefViewModel.getBeliefIsLoaded()) {
                        initTabs();
                    }

                } else {

                }
            }
        });
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void loadFABs() {
        saveBeliefFab = findViewById(com.rgp.asks.R.id.saveBeliefFab);
        argumentsFab = findViewById(com.rgp.asks.R.id.addArgumentFab);
        objectionsFab = findViewById(com.rgp.asks.R.id.addObjectionFab);
    }

    private void setBeliefNameInToolbar(final Belief b) {
        if (b != null) {
            toolbar.setTitle(b.getBelief());
        }
    }

    private void initTabs() {
        findViewById(com.rgp.asks.R.id.indeterminateBar2).setVisibility(View.GONE);
        findViewById(com.rgp.asks.R.id.beliefTabs).setVisibility(View.VISIBLE);

        mBeliefPagerAdapter = new BeliefPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(com.rgp.asks.R.id.asksViewPager);
        mViewPager.setAdapter(mBeliefPagerAdapter);

        tabLayout = findViewById(com.rgp.asks.R.id.beliefTabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                hideKeyboard();
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
        getMenuInflater().inflate(com.rgp.asks.R.menu.menu_belief, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        hideKeyboard();
        switch (id) {
            case com.rgp.asks.R.id.action_delete_belief:
                beliefViewModel.removeBelief();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSavedEditedBeliefEvent(SavedEditedBeliefEvent event) {
        Toast.makeText(this, event.message, Toast.LENGTH_SHORT).show();
        setBeliefNameInToolbar(beliefViewModel.getModifiableBeliefCopy());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeletedBeliefEvent(DeletedBeliefEvent event) {
        if (event.result) {
            Belief currentBelief = beliefViewModel.getBelief().getValue();
            if (currentBelief == null || currentBelief.getId() == event.deletedBeliefId) {
                Toast.makeText(this, this.getString(R.string.toast_deleted_belief), Toast.LENGTH_SHORT).show();
                this.finish();

            }
        } else {
            Toast.makeText(this, this.getString(R.string.toast_error_deleted_belief), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        openUnsavedDialog();
    }

    private AlertDialog createUnsavedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setMessage(this.getString(R.string.belief_save_dialog_title))
                .setPositiveButton(this.getString(R.string.belief_save_dialog_save_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        beliefViewModel.checkedSaveBelief();
                        finish();
                    }
                })
                .setNegativeButton(this.getString(R.string.belief_save_dialog_discard_button), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //nothing
                        finish();
                    }
                })
        ;

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void openUnsavedDialog() {
        AlertDialog unsavedDialog = createUnsavedDialog();
        if (beliefViewModel.beliefWasChanged()) {
            unsavedDialog.show();
        } else {
            finish();
        }
    }

    public class BeliefPagerAdapter extends FragmentPagerAdapter {

        public BeliefPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment f = null;

            switch (position) {
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

}

