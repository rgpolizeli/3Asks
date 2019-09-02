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
        setMargins(recyclerView, 0, 0, 0, 0);
        actionBar.show();
    }

    private void modifyLayoutBecauseOpenedSearch() {
        actionBar.hide();
        setMargins(recyclerView, 0, searchLayoutHeight, 0, 0);
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

    public void closeSearch() {
        searchLayout.closeSearch();
    }
}
