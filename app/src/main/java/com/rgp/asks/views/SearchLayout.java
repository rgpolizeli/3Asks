package com.rgp.asks.views;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
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

    private String query;

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
            setQuery("");
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
                        setQuery(query);
                        onQueryListener.onQuery(query);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!pausedOnQueryListener) {
                    if (onQueryListener != null) {
                        setQuery(query);
                        onQueryListener.onQuery(query);
                    }
                }
                return true;
            }
        });
        pausedOnQueryListener = true;
        query = "";
    }

    public String getQuery() {
        return this.query;
    }

    public void setQuery(String newQuery) {
        this.query = newQuery;
    }

    public void continueOnQueryListener() {
        pausedOnQueryListener = false;
    }

    public void setOnBackButtonClickListener(OnBackButtonClickListener onBackButtonClickListener) {
        this.onBackButtonClickListener = onBackButtonClickListener;
    }

    public void setOnQueryListener(OnQueryListener onQueryListener) {
        this.onQueryListener = onQueryListener;
    }

    public SearchView getSearchView() {
        LinearLayout rootLinearLayout = (LinearLayout) getChildAt(0);
        LinearLayout parentLinearLayout = (LinearLayout) rootLinearLayout.getChildAt(0);
        return (SearchView) parentLinearLayout.getChildAt(1);
    }

    public ImageButton getBackButton() {
        LinearLayout rootLinearLayout = (LinearLayout) getChildAt(0);
        LinearLayout parentLinearLayout = (LinearLayout) rootLinearLayout.getChildAt(0);
        return (ImageButton) parentLinearLayout.getChildAt(0);
    }


    public boolean restoreSearchIfNecessary() throws NullPointerException {
        if (onQueryListener == null || onBackButtonClickListener == null) {
            throw new NullPointerException("onQueryListener or onBackButtonClickListener is null");
        } else {
            if (this.query.isEmpty()) {
                return false;
            } else {
                pausedOnQueryListener = false;
                getSearchView().setQuery(getQuery(), false);
                return true;
            }
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.query = this.query;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.query = ss.query;
    }


    public interface OnBackButtonClickListener {
        void onBackButtonClick();
    }

    public interface OnQueryListener {
        void onQuery(String query);
    }

    private static class SavedState extends BaseSavedState {

        public final static Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        String query;

        SavedState(Parcel source) {
            super(source);
            query = source.readString();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeString(query);
        }
    }
}
