package com.rgp.asks.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.rgp.asks.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SpinnerInputLayout extends LinearLayout {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_FOCUSED = 1;

    private int textInputLayoutState = STATE_NORMAL;

    private TextView labelTextView;
    private Spinner spinner;

    public SpinnerInputLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.SpinnerInputLayout, 0, 0);
        String label = a.getString(R.styleable.SpinnerInputLayout_spinnerLabel);
        CharSequence[] options = a.getTextArray(R.styleable.SpinnerInputLayout_entries);
        List<String> optionsAsString = new ArrayList<>();
        for (CharSequence charSequence : options) {
            optionsAsString.add(charSequence.toString());
        }
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.spinner_input_layout, this, true);

        this.labelTextView = getLabelTextView();
        this.labelTextView.setText(label);

        this.spinner = getInputSpinner();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, optionsAsString);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);
        this.spinner.setFocusable(true);
        this.spinner.setFocusableInTouchMode(true);

        this.spinner.setOnTouchListener((v, event) -> {
            if (textInputLayoutState == STATE_NORMAL) {
                v.requestFocus();
                SpinnerInputLayout.this.goToState(STATE_FOCUSED);
            }
            return v.performClick();
        });

        this.spinner.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (textInputLayoutState == STATE_FOCUSED) {
                    SpinnerInputLayout.this.goToState(STATE_NORMAL);
                }
            }
        });

        /*
        this.spinner.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus){
                if (textInputLayoutState == STATE_NORMAL) {
                    SpinnerInputLayout.this.goToState(STATE_FOCUSED);
                }
            } else{
                if (textInputLayoutState == STATE_FOCUSED) {
                    SpinnerInputLayout.this.goToState(STATE_NORMAL);
                }
            }
        });
       */
        /*
        this.spinner.setOnTouchListener((v, event) -> {
            if (textInputLayoutState == STATE_NORMAL) {
                SpinnerInputLayout.this.goToState(STATE_FOCUSED);
            }
            return v.performClick();
        });

        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (textInputLayoutState == STATE_FOCUSED) {
                    SpinnerInputLayout.this.goToState(STATE_NORMAL);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.state = textInputLayoutState;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        goToState(ss.state);
    }

    public TextView getLabelTextView() {
        LinearLayout linearLayout = (LinearLayout) getChildAt(0);
        return (TextView) linearLayout.getChildAt(0);
    }

    public Spinner getInputSpinner() {
        LinearLayout linearLayout = (LinearLayout) getChildAt(0);
        return (Spinner) linearLayout.getChildAt(1);
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
        Spinner spinner = getInputSpinner();
        spinner.setSelection(0);
        goToState(SpinnerInputLayout.STATE_NORMAL);
    }

    public String getValue() {
        Spinner spinner = getInputSpinner();
        return spinner.getSelectedItem().toString();
    }

    public void setValue(String reactionClass) {
        Spinner spinner = getInputSpinner();
        spinner.setSelection(getIndex(this.spinner, reactionClass));
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    private void drawStateNormal() {
        this.labelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
        this.spinner.setBackgroundResource(R.drawable.edit_text_dialog_multiline_normal);
    }

    private void drawStateFocused() {
        hideKeyboard(this.spinner);
        this.labelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent3));
        this.spinner.setBackgroundResource(R.drawable.edit_text_dialog_multiline_focused);
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private static class SavedState extends BaseSavedState {

        public final static Creator<SavedState> CREATOR = new Creator<SpinnerInputLayout.SavedState>() {

            @Override
            public SpinnerInputLayout.SavedState createFromParcel(Parcel source) {
                return new SpinnerInputLayout.SavedState(source);
            }

            public SpinnerInputLayout.SavedState[] newArray(int size) {
                return new SpinnerInputLayout.SavedState[size];
            }
        };
        int state;

        SavedState(Parcel source) {
            super(source);
            state = source.readInt();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(state);
        }
    }
}
