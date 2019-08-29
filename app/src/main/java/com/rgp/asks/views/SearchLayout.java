package com.rgp.asks.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.rgp.asks.R;

public class SearchLayout extends LinearLayout {

    @Nullable
    private OnBackButtonClickListener onBackButtonClickListener;
    @Nullable
    private OnQueryListener onQueryListener;

    private boolean pausedOnQueryListener;

    public SearchLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_search, this, true);

        SearchView searchView = getSearchView();
        ImageButton backButton = getBackButton();

        searchView.setSaveEnabled(false);
        searchView.setSaveFromParentEnabled(false);

        backButton.setOnClickListener(v -> {
            pausedOnQueryListener = true;
            getSearchView().setQuery("", false);
            if (this.onBackButtonClickListener != null) {
                this.onBackButtonClickListener.onBackButtonClick();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                if (!pausedOnQueryListener) {
                    if (onQueryListener != null) {
                        onQueryListener.onQuery(query);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!pausedOnQueryListener) {
                    if (onQueryListener != null && !pausedOnQueryListener) {
                        onQueryListener.onQuery(query);
                    }
                }
                return true;
            }
        });
        pausedOnQueryListener = true;
    }

    public void openSearchLayout() {
        pausedOnQueryListener = false;
    }

    public void setOnBackButtonClickListener(OnBackButtonClickListener onBackButtonClickListener) {
        this.onBackButtonClickListener = onBackButtonClickListener;
    }

    public void setOnQueryListener(OnQueryListener onQueryListener) {
        this.onQueryListener = onQueryListener;
    }

    private SearchView getSearchView() {
        LinearLayout rootLinearLayout = (LinearLayout) getChildAt(0);
        LinearLayout parentLinearLayout = (LinearLayout) rootLinearLayout.getChildAt(0);
        return (SearchView) parentLinearLayout.getChildAt(1);
    }

    private ImageButton getBackButton() {
        LinearLayout rootLinearLayout = (LinearLayout) getChildAt(0);
        LinearLayout parentLinearLayout = (LinearLayout) rootLinearLayout.getChildAt(0);
        return (ImageButton) parentLinearLayout.getChildAt(0);
    }


    public void restoreQuery(String query) throws NullPointerException {
        if (onQueryListener == null || onBackButtonClickListener == null) {
            throw new NullPointerException("onQueryListener or onBackButtonClickListener is null");
        } else {
            pausedOnQueryListener = false;
            getSearchView().setQuery(query, false);
        }
    }

    public interface OnBackButtonClickListener {
        void onBackButtonClick();
    }

    public interface OnQueryListener {
        void onQuery(String query);
    }
}
