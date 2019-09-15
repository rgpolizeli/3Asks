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
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.interfaces.OnFloatingActionButtonClickListener;
import com.rgp.asks.viewmodel.EpisodeViewModel;
import com.rgp.asks.views.DisableSwipeViewPager;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AsksFragment extends Fragment {

    private EpisodeViewModel model;
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
        return inflater.inflate(R.layout.fragment_asks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragmentView, Bundle savedInstanceState) {
        this.floatingActionButton = getFloatingActionButton();
        initViewModel();
        this.model.setEpisodeId(requireArguments().getInt(Constants.ARG_ID));
        initTabs(fragmentView);
    }

    private void initViewModel() {
        this.model = ViewModelProviders.of(this).get(EpisodeViewModel.class);
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) requireActivity().getSystemService(INPUT_METHOD_SERVICE);
        View focusedView = requireActivity().getCurrentFocus();
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private AlertDialog createUnsavedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder
                .setMessage(this.getString(R.string.episode_save_dialog_title))
                .setPositiveButton(this.getString(R.string.episode_save_dialog_save_button), (dialog, id) -> {
                    this.model.checkedSaveEpisode(getWhenFragmentFromFragmentManager().getOnUpdatedEntityListener());
                })
                .setNegativeButton(this.getString(R.string.episode_save_dialog_discard_button), (dialog, id) -> {
                    finish();
                })
        ;
        return builder.create();
    }

    private void finish() {
        Navigation.findNavController(requireActivity().findViewById(R.id.nav_host_fragment)).navigateUp();
    }

    private void openUnsavedDialog() {
        AlertDialog unsavedDialog = createUnsavedDialog();
        if (model.episodeWasChanged()) {
            unsavedDialog.show();
        } else {
            finish();
        }
    }

    private void initTabs(View fragmentView) {
        fragmentView.findViewById(R.id.indeterminateBar).setVisibility(View.GONE);
        fragmentView.findViewById(R.id.tabs).setVisibility(View.VISIBLE);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        DisableSwipeViewPager disableSwipeViewPager = fragmentView.findViewById(R.id.disableSwipeViewPager);
        disableSwipeViewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = fragmentView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(disableSwipeViewPager);

        disableSwipeViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(disableSwipeViewPager) {
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
        showRightFab(disableSwipeViewPager.getCurrentItem());
    }

    private void showRightFab(int tabPosition) {
        switch (tabPosition) {
            case 0:
                setupFloatingActionButton(
                        ColorStateList.valueOf(getResources().getColor(R.color.secondaryColor)),
                        R.drawable.ic_save_white_24dp,
                        v -> {
                            Fragment f = getWhenFragmentFromFragmentManager();
                            if (f != null) {
                                OnFloatingActionButtonClickListener listener = (OnFloatingActionButtonClickListener) f;
                                listener.onFloatingActionButtonClick();
                            }
                        }
                );
                break;
            case 1:
                setupFloatingActionButton(
                        ColorStateList.valueOf(getResources().getColor(R.color.secondaryColor)),
                        R.drawable.ic_add_white_24dp,
                        v -> {
                            Fragment f = getWhatFragmentFromFragmentManager();
                            if (f != null) {
                                OnFloatingActionButtonClickListener listener = (OnFloatingActionButtonClickListener) f;
                                listener.onFloatingActionButtonClick();
                            }
                        }
                );
                break;
            case 2:
                setupFloatingActionButton(
                        ColorStateList.valueOf(getResources().getColor(R.color.secondaryColor)),
                        R.drawable.ic_add_white_24dp,
                        v -> {
                            Fragment f = getWhyFragmentFromFragmentManager();
                            if (f != null) {
                                OnFloatingActionButtonClickListener listener = (OnFloatingActionButtonClickListener) f;
                                listener.onFloatingActionButtonClick();
                            }
                        }
                );
                break;
        }
    }

    private WhatFragment getWhatFragmentFromFragmentManager() {
        for (Fragment f : getChildFragmentManager().getFragments()) {
            if (f instanceof WhatFragment) {
                return (WhatFragment) f;
            }
        }
        return null;
    }

    private WhenFragment getWhenFragmentFromFragmentManager() {
        for (Fragment f : getChildFragmentManager().getFragments()) {
            if (f instanceof WhenFragment) {
                return (WhenFragment) f;
            }
        }
        return null;
    }

    private WhyFragment getWhyFragmentFromFragmentManager() {
        for (Fragment f : getChildFragmentManager().getFragments()) {
            if (f instanceof WhyFragment) {
                return (WhyFragment) f;
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        // tab titles
        private String[] titles = new String[]{
                getResources().getString(R.string.tab_when),
                getResources().getString(R.string.tab_what),
                getResources().getString(R.string.tab_why)
        };

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            Fragment f;
            switch (position) {
                default:
                    f = new WhenFragment();
                    break;
                case 1:
                    f = new WhatFragment();
                    break;
                case 2:
                    f = new WhyFragment();
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
            return titles[position];
        }
    }
}
