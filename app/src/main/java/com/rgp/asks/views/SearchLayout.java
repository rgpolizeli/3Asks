package com.rgp.asks.views;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;

import com.rgp.asks.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SearchLayout extends LinearLayout {

    @Nullable
    private OnBackButtonClickListener onBackButtonClickListener;
    @Nullable
    private OnQueryListener onQueryListener;
    @Nullable
    private String query;

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
            setQuery(null);
            closeSearch();
            if (this.onBackButtonClickListener != null) {
                this.onBackButtonClickListener.onBackButtonClick();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                if (onQueryListener != null) {
                    setQuery(query);
                    onQueryListener.onQuery(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (onQueryListener != null) {
                    setQuery(query);
                    onQueryListener.onQuery(query);
                    clearFocus();
                }
                return true;
            }
        });

        EditText searchEditText = searchView.findViewById(R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.black));
    }

    public void openSearch() {
        setVisibility(VISIBLE);
        requestFocus();
    }

    public void closeSearch() {
        clearFocus();
        setVisibility(GONE);
    }

    private void showKeyboard() {
        ((InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(getSearchView().findFocus(), InputMethodManager.SHOW_IMPLICIT);
    }

    @Nullable
    public String getQuery() {
        return this.query;
    }

    public void setQuery(@Nullable String newQuery) {
        this.query = newQuery;
    }

    public void initSearch() {
        openSearch();
        setQuery("");
        getSearchView().setQuery("", false);
        showKeyboard();
    }

    public void setOnBackButtonClickListener(@Nullable OnBackButtonClickListener onBackButtonClickListener) {
        this.onBackButtonClickListener = onBackButtonClickListener;
    }

    public void setOnQueryListener(@Nullable OnQueryListener onQueryListener) {
        this.onQueryListener = onQueryListener;
    }

    public SearchView getSearchView() {
        return (SearchView) findViewWithTag("searchView");
    }

    public ImageButton getBackButton() {
        return (ImageButton) findViewWithTag("searchBackButton");
    }

    public boolean restoreSearchIfNecessary() throws NullPointerException {
        if (onQueryListener == null || onBackButtonClickListener == null) {
            throw new NullPointerException("onQueryListener or onBackButtonClickListener is null");
        } else {
            if (this.query == null) {
                return false;
            } else {
                openSearch();
                getSearchView().setQuery(getQuery(), true);
                setQuery(query);
                onQueryListener.onQuery(query);
                showKeyboard();
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
