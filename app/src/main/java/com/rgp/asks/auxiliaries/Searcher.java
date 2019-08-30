package com.rgp.asks.auxiliaries;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import com.rgp.asks.interfaces.Filterable;
import com.rgp.asks.views.SearchLayout;

public class Searcher {

    private ActionBar actionBar;
    private View recyclerView;
    private Filterable filterableAdapter;
    private int searchLayoutHeight;
    private SearchLayout searchLayout;


    public Searcher(ActionBar actionBar, View recyclerView, Filterable filterableAdapter, int searchLayoutHeight, SearchLayout searchLayout) {
        this.actionBar = actionBar;
        this.recyclerView = recyclerView;
        this.filterableAdapter = filterableAdapter;
        this.searchLayoutHeight = searchLayoutHeight;
        this.searchLayout = searchLayout;

        setupSearchLayoutListerners();
    }

    public void openSearch() {
        showSearch();
        searchLayout.continueOnQueryListener();
    }

    public void restoreSearchIfNecessary() {
        boolean isRestored = searchLayout.restoreSearchIfNecessary();
        if (isRestored)
            showSearch();
    }

    private void setupSearchLayoutListerners() {
        searchLayout.setOnBackButtonClickListener(this::hideSearch);
        searchLayout.setOnQueryListener(this::applyFilter);
    }

    private void hideSearch() {
        removeAllAppliedFilter();
        searchLayout.setVisibility(View.GONE);
        searchLayout.getSearchView().clearFocus();
        setMargins(recyclerView, 0, 0, 0, 0);
        actionBar.show();
    }

    private void showSearch() {
        actionBar.hide();
        setMargins(recyclerView, 0, searchLayoutHeight, 0, 0);
        searchLayout.setVisibility(View.VISIBLE);
        searchLayout.getSearchView().requestFocus();
    }

    private void setMargins(@NonNull View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    private void applyFilter(@NonNull String filter) {
        filterableAdapter.getFilter().filter(filter);
    }

    private void removeAllAppliedFilter() {
        filterableAdapter.removeAllAppliedFilter();
    }
}
