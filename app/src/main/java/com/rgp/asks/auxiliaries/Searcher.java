package com.rgp.asks.auxiliaries;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.google.android.material.tabs.TabLayout;
import com.rgp.asks.interfaces.Filterable;
import com.rgp.asks.views.DisableSwipeViewPager;
import com.rgp.asks.views.SearchLayout;

public class Searcher {

    private ActionBar actionBar;
    private DisableSwipeViewPager viewPager;
    private TabLayout tabLayout;
    private Filterable filterableAdapter;
    private SearchLayout searchLayout;

    public Searcher(ActionBar actionBar, DisableSwipeViewPager viewPager, TabLayout tabLayout, Filterable filterableAdapter, SearchLayout searchLayout) {
        this.actionBar = actionBar;
        this.viewPager = viewPager;
        this.tabLayout = tabLayout;
        this.filterableAdapter = filterableAdapter;
        this.searchLayout = searchLayout;
        setupSearchLayoutListerners();
    }

    public void openSearch() {
        modifyLayoutBecauseOpenedSearch();
        searchLayout.initSearch();
    }

    public void restoreSearchIfNecessary() {
        boolean isRestored = searchLayout.restoreSearchIfNecessary();
        if (isRestored)
            modifyLayoutBecauseOpenedSearch();
    }

    private void setupSearchLayoutListerners() {
        searchLayout.setOnBackButtonClickListener(this::modifyLayoutBecauseClosedSearch);
        searchLayout.setOnQueryListener(this::applyFilter);
    }

    private void modifyLayoutBecauseClosedSearch() {
        removeAllAppliedFilter();
        actionBar.show();
        if (this.tabLayout != null) {
            this.viewPager.setSwipeEnabled(true);
            this.tabLayout.setVisibility(View.VISIBLE);
        }
    }

    private void modifyLayoutBecauseOpenedSearch() {
        actionBar.hide();
        if (this.tabLayout != null) {
            this.viewPager.setSwipeEnabled(false);
            this.tabLayout.setVisibility(View.GONE);
        }
    }

    private void applyFilter(@NonNull String filter) {
        filterableAdapter.getFilter().filter(filter);
    }

    private void removeAllAppliedFilter() {
        filterableAdapter.removeAllAppliedFilter();
    }

    public void closeSearch() {
        searchLayout.closeSearch();
    }
}
