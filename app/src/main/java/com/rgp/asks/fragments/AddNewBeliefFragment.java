package com.rgp.asks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.rgp.asks.R;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.messages.DeletedBeliefEvent;
import com.rgp.asks.messages.SavedEditedBeliefEvent;
import com.rgp.asks.viewmodel.BeliefViewModel;
import com.rgp.asks.views.DisableSwipeViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AddNewBeliefFragment extends Fragment {

    private BeliefViewModel model;
    private FloatingActionButton saveBeliefFab;
    private FloatingActionButton argumentsFab;
    private FloatingActionButton objectionsFab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                openUnsavedDialog();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_belief, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragmentView, Bundle savedInstanceState) {
        loadFABs(fragmentView);
        initViewModel();
        this.model.setBeliefId(getArguments().getInt(Constants.ARG_BELIEF_ID));
        initToolbarTitle();
        initTabs(fragmentView);
    }

    private void initViewModel() {
        this.model = ViewModelProviders.of(this).get(BeliefViewModel.class);
    }

    private void finish() {
        Navigation.findNavController(requireActivity().findViewById(R.id.nav_host_fragment)).navigateUp();
    }

    private void initToolbarTitle() {
        String toolbarTitle = this.model.getBeliefNameForToolbarTitle();
        if (toolbarTitle == null) {
            toolbarTitle = getArguments().getString(Constants.ARG_BELIEF_TITLE);
        }
        setBeliefNameInToolbar(toolbarTitle);
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) requireActivity().getSystemService(INPUT_METHOD_SERVICE);
        View focusedView = requireActivity().getCurrentFocus();
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void loadFABs(View fragmentView) {
        saveBeliefFab = fragmentView.findViewById(com.rgp.asks.R.id.saveBeliefFab);
        argumentsFab = fragmentView.findViewById(com.rgp.asks.R.id.addArgumentFab);
        objectionsFab = fragmentView.findViewById(com.rgp.asks.R.id.addObjectionFab);
    }

    private void setBeliefNameInToolbar(String beliefInToolbar) {
        if (beliefInToolbar.isEmpty()) {
            beliefInToolbar = getResources().getString(R.string.destination_asks_unnamed_belief);
        }
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(beliefInToolbar);
    }

    private void initTabs(View fragmentView) {
        fragmentView.findViewById(com.rgp.asks.R.id.indeterminateBar2).setVisibility(View.GONE);
        fragmentView.findViewById(com.rgp.asks.R.id.tabs).setVisibility(View.VISIBLE);

        BeliefPagerAdapter mBeliefPagerAdapter = new BeliefPagerAdapter(getChildFragmentManager());
        DisableSwipeViewPager mViewPager = fragmentView.findViewById(com.rgp.asks.R.id.disableSwipeViewPager);
        mViewPager.setAdapter(mBeliefPagerAdapter);

        TabLayout tabLayout = fragmentView.findViewById(com.rgp.asks.R.id.tabs);

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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSavedEditedBeliefEvent(SavedEditedBeliefEvent event) {
        Toast.makeText(requireContext(), getString(R.string.toast_message_belief_saved), Toast.LENGTH_SHORT).show();
        String savedBelief = model.getModifiableBeliefCopy().getBelief();
        model.setBeliefNameForToolbarTitle(savedBelief);
        setBeliefNameInToolbar(savedBelief);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeletedBeliefEvent(DeletedBeliefEvent event) {
        if (event.result) {
            if (this.model.getBeliefId() == event.deletedBeliefId) {
                Toast.makeText(requireContext(), getString(R.string.toast_deleted_belief), Toast.LENGTH_SHORT).show();
                this.finish();
            }
        } else {
            Toast.makeText(requireContext(), this.getString(R.string.toast_error_deleted_belief), Toast.LENGTH_SHORT).show();
        }
    }

    private AlertDialog createUnsavedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder
                .setMessage(this.getString(R.string.belief_save_dialog_title))
                .setPositiveButton(this.getString(R.string.belief_save_dialog_save_button), (dialog, id) -> {
                    model.checkedSaveBelief();
                    finish();
                })
                .setNegativeButton(this.getString(R.string.belief_save_dialog_discard_button), (dialog, id) -> {
                    //nothing
                    finish();
                })
        ;
        return builder.create();
    }

    private void openUnsavedDialog() {
        AlertDialog unsavedDialog = createUnsavedDialog();
        if (model.beliefWasChanged()) {
            unsavedDialog.show();
        } else {
            finish();
        }
    }

    public class BeliefPagerAdapter extends FragmentPagerAdapter {
        BeliefPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment f;
            switch (position) {
                default:
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