package com.rgp.asks.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.rgp.asks.R;
import com.rgp.asks.ad.AderClient;
import com.rgp.asks.ad.AderCreator;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.interfaces.OnFloatingActionButtonClickListener;
import com.rgp.asks.viewmodel.BeliefViewModel;
import com.rgp.asks.views.DisableSwipeViewPager;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AddNewBeliefFragment extends Fragment implements AderClient {

    private BeliefViewModel model;
    private FloatingActionButton floatingActionButton;

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
        requestToShowAd((AderCreator) requireActivity());
        this.floatingActionButton = getFloatingActionButton();
        initViewModel();
        this.model.setEntityId(getArguments().getInt(Constants.ARG_ID));
        initTabs(fragmentView);
    }

    private void initViewModel() {
        this.model = ViewModelProviders.of(this).get(BeliefViewModel.class);
    }

    private void finish() {
        Navigation.findNavController(requireActivity().findViewById(R.id.nav_host_fragment)).navigateUp();
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) requireActivity().getSystemService(INPUT_METHOD_SERVICE);
        View focusedView = requireActivity().getCurrentFocus();
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void initTabs(View fragmentView) {
        fragmentView.findViewById(com.rgp.asks.R.id.indeterminateBar2).setVisibility(View.GONE);
        fragmentView.findViewById(com.rgp.asks.R.id.tabs).setVisibility(View.VISIBLE);

        BeliefPagerAdapter mBeliefPagerAdapter = new BeliefPagerAdapter(getChildFragmentManager());
        DisableSwipeViewPager mViewPager = fragmentView.findViewById(com.rgp.asks.R.id.disableSwipeViewPager);
        mViewPager.setAdapter(mBeliefPagerAdapter);

        TabLayout tabLayout = fragmentView.findViewById(com.rgp.asks.R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager) {
            @Override
            public void onTabSelected(@NonNull TabLayout.Tab tab) {
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
            case 0:
                setupFloatingActionButton(
                        ColorStateList.valueOf(getResources().getColor(R.color.secondaryColor)),
                        R.drawable.ic_save_white_24dp,
                        v -> {
                            Fragment f = getBeliefDetailsFragmentFromFragmentManager();
                            if (f != null) {
                                OnFloatingActionButtonClickListener listener = (OnFloatingActionButtonClickListener) f;
                                listener.onFloatingActionButtonClick();
                            }
                        }
                );
                break;
            case 1:
                setupFloatingActionButton(
                        ColorStateList.valueOf(getResources().getColor(R.color.secondFloatingActionButtonColor)),
                        R.drawable.ic_add_white_24dp,
                        v -> {
                            Fragment f = getBeliefArgumentsFragmentFromFragmentManager();
                            if (f != null) {
                                OnFloatingActionButtonClickListener listener = (OnFloatingActionButtonClickListener) f;
                                listener.onFloatingActionButtonClick();
                            }
                        }
                );
                break;
            case 2:
                setupFloatingActionButton(
                        ColorStateList.valueOf(getResources().getColor(R.color.thirdFloatingActionButtonColor)),
                        R.drawable.ic_add_white_24dp,
                        v -> {
                            Fragment f = getBeliefObjectionsFragmentFromFragmentManager();
                            if (f != null) {
                                OnFloatingActionButtonClickListener listener = (OnFloatingActionButtonClickListener) f;
                                listener.onFloatingActionButtonClick();
                            }
                        }
                );
                break;
        }
    }

    private BeliefDetailsFragment getBeliefDetailsFragmentFromFragmentManager() {
        for (Fragment f : getChildFragmentManager().getFragments()) {
            if (f instanceof BeliefDetailsFragment) {
                return (BeliefDetailsFragment) f;
            }
        }
        return null;
    }

    private BeliefArgumentsFragment getBeliefArgumentsFragmentFromFragmentManager() {
        for (Fragment f : getChildFragmentManager().getFragments()) {
            if (f instanceof BeliefArgumentsFragment) {
                return (BeliefArgumentsFragment) f;
            }
        }
        return null;
    }

    private BeliefObjectionsFragment getBeliefObjectionsFragmentFromFragmentManager() {
        for (Fragment f : getChildFragmentManager().getFragments()) {
            if (f instanceof BeliefObjectionsFragment) {
                return (BeliefObjectionsFragment) f;
            }
        }
        return null;
    }

    @NonNull
    private FloatingActionButton getFloatingActionButton() {
        return requireActivity().findViewById(R.id.floatingActionButton);
    }

    private void setupFloatingActionButton(@NonNull ColorStateList backgroundTintList, int imageResourceId, @NonNull View.OnClickListener onClickListener) {
        floatingActionButton.setBackgroundTintList(backgroundTintList);
        floatingActionButton.setImageResource(imageResourceId);
        floatingActionButton.setOnClickListener(onClickListener);
        floatingActionButton.setVisibility(View.VISIBLE);
    }

    private AlertDialog createUnsavedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder
                .setMessage(this.getString(R.string.belief_save_dialog_title))
                .setPositiveButton(this.getString(R.string.belief_save_dialog_save_button), (dialog, id) -> {
                    model.updateBelief(true, getBeliefDetailsFragmentFromFragmentManager().getOnUpdatedEntityListener());
                })
                .setNegativeButton(this.getString(R.string.belief_save_dialog_discard_button), (dialog, id) -> {
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

        // tab titles
        private String[] tabTitles = new String[]{
                getResources().getString(R.string.tab_belief_details),
                getResources().getString(R.string.tab_arguments),
                getResources().getString(R.string.tab_objections)
        };

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

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }
}