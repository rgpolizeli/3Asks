package com.rgp.asks.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.rgp.asks.R;
import com.rgp.asks.listeners.EpisodeDateDialogOnClick;

import java.text.DateFormat;
import java.util.Calendar;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class DateInputLayout extends LinearLayout {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_FOCUSED = 1;

    private int textInputLayoutState = STATE_NORMAL;

    private TextView labelTextView;
    private TextView dateTextView;

    private EpisodeDateDialogOnClick episodeDateDialogOnClick;

    public DateInputLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.DateInputLayout, 0, 0);
        String label = a.getString(R.styleable.DateInputLayout_dateLabel);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.date_input_layout, this, true);

        this.labelTextView = getLabelTextView();
        this.labelTextView.setText(label);

        this.dateTextView = getInputDate();
        Calendar c = Calendar.getInstance();
        dateTextView.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime()));
        this.dateTextView.setFocusable(true);
        this.dateTextView.setFocusableInTouchMode(true);
        this.dateTextView.setClickable(true);

        this.episodeDateDialogOnClick = new EpisodeDateDialogOnClick(this.dateTextView);

        this.dateTextView.setOnClickListener(v -> {
            if (textInputLayoutState == STATE_NORMAL) {
                DateInputLayout.this.goToState(STATE_FOCUSED);
            }
            episodeDateDialogOnClick.onClick(v);
        });

        this.dateTextView.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (textInputLayoutState == STATE_FOCUSED) {
                    DateInputLayout.this.goToState(STATE_NORMAL);
                }
            } else {
                v.performClick();
            }
        });
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.state = textInputLayoutState;
        ss.date = dateTextView.getText().toString();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        goToState(ss.state);
        dateTextView.setText(ss.date);
    }

    public TextView getLabelTextView() {
        LinearLayout linearLayout = (LinearLayout) getChildAt(0);
        return (TextView) linearLayout.getChildAt(0);
    }

    public TextView getInputDate() {
        LinearLayout linearLayout = (LinearLayout) getChildAt(0);
        return (TextView) linearLayout.getChildAt(1);
    }

    public void goToState(int textInputLayoutState) {
        this.textInputLayoutState = textInputLayoutState;
        if (this.textInputLayoutState == STATE_FOCUSED) {
            drawStateFocused();
        } else {
            drawStateNormal();
        }
    }

    public void clear() {
        TextView dateTextView = getInputDate();
        Calendar c = Calendar.getInstance();
        dateTextView.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime()));
        goToState(DateInputLayout.STATE_NORMAL);
    }

    public String getValue() {
        TextView dateTextView = getInputDate();
        return dateTextView.getText().toString();
    }

    public void setValue(String date) {
        TextView dateTextView = getInputDate();
        dateTextView.setText(date);
    }

    private void drawStateNormal() {
        this.labelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
        this.dateTextView.setBackgroundResource(R.drawable.edit_text_dialog_multiline_normal);
    }

    private void drawStateFocused() {
        hideKeyboard(this.dateTextView);
        this.labelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent3));
        this.dateTextView.setBackgroundResource(R.drawable.edit_text_dialog_multiline_focused);
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private static class SavedState extends BaseSavedState {

        public final static Creator<SavedState> CREATOR = new Creator<DateInputLayout.SavedState>() {

            @Override
            public DateInputLayout.SavedState createFromParcel(Parcel source) {
                return new DateInputLayout.SavedState(source);
            }

            public DateInputLayout.SavedState[] newArray(int size) {
                return new DateInputLayout.SavedState[size];
            }
        };
        int state;
        String date;

        SavedState(Parcel source) {
            super(source);
            state = source.readInt();
            date = source.readString();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(state);
            out.writeString(date);
        }
    }
}
