package com.rgp.asks.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.rgp.asks.R;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class TextInputLayout extends LinearLayout {

    public static final int STATE_NORMAL = 0;
    public static final int STATE_FOCUSED = 1;
    public static final int STATE_ERROR = 2;

    private int textInputLayoutState = STATE_NORMAL;

    private TextView labelTextView;
    private EditText editText;
    private TextView errorMessageTextView;

    public TextInputLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TextInputLayout, 0, 0);
        String label = a.getString(R.styleable.TextInputLayout_label);
        String errorMessage = a.getString(R.styleable.TextInputLayout_errorMessage);
        if (errorMessage == null) {
            errorMessage = "";
        }
        int numberOfLines = a.getInt(R.styleable.TextInputLayout_numberOfLines, 1);
        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.text_input_layout, this, true);

        this.labelTextView = getLabelTextView();
        this.labelTextView.setText(label);

        this.editText = getInputEditText();
        this.editText.setLines(numberOfLines);

        if (numberOfLines == 1) {
            this.editText.setHorizontallyScrolling(true);
        }

        this.editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (this.textInputLayoutState != STATE_ERROR) {
                if (hasFocus) {
                    if (textInputLayoutState == STATE_NORMAL) {
                        goToState(STATE_FOCUSED);
                    }
                } else {
                    if (textInputLayoutState == STATE_FOCUSED) {
                        hideKeyboard(v);
                        goToState(STATE_NORMAL);
                    }
                }
            }
        });

        this.errorMessageTextView = getErrorMessageTextView();
        this.errorMessageTextView.setText(errorMessage);

        goToState(STATE_NORMAL);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.state = textInputLayoutState;
        ss.text = getValue().toString();
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setValue(ss.text);
        goToState(ss.state);
    }

    public TextView getLabelTextView() {
        LinearLayout linearLayout = (LinearLayout) getChildAt(0);
        return (TextView) linearLayout.getChildAt(0);
    }

    public TextView getErrorMessageTextView() {
        LinearLayout linearLayout = (LinearLayout) getChildAt(0);
        return (TextView) linearLayout.getChildAt(2);
    }

    public EditText getInputEditText() {
        LinearLayout linearLayout = (LinearLayout) getChildAt(0);
        return (EditText) linearLayout.getChildAt(1);
    }

    public void goToState(int textInputLayoutState) {
        this.textInputLayoutState = textInputLayoutState;
        switch (this.textInputLayoutState) {
            case STATE_ERROR:
                drawStateError();
                break;
            case STATE_FOCUSED:
                drawStateFocused();
                break;
            default:
                drawStateNormal();
                break;
        }
    }

    public void clear() {
        EditText editText = getInputEditText();
        editText.getText().clear();
        goToState(TextInputLayout.STATE_NORMAL);
        this.clearFocus();
    }

    public Editable getValue() {
        EditText editText = getInputEditText();
        return editText.getText();
    }

    public void setValue(String text) {
        EditText editText = getInputEditText();
        editText.setText(text);
    }

    private void drawStateError() {
        this.labelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.errorColor));
        this.editText.setBackgroundResource(R.drawable.edit_text_dialog_multiline_error);
        this.errorMessageTextView.setVisibility(VISIBLE);
    }

    private void drawStateNormal() {
        this.labelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
        this.editText.setBackgroundResource(R.drawable.edit_text_dialog_multiline_normal);
        this.errorMessageTextView.setVisibility(GONE);
    }

    private void drawStateFocused() {
        this.requestFocus();
        this.labelTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent3));
        this.editText.setBackgroundResource(R.drawable.edit_text_dialog_multiline_focused);
        this.errorMessageTextView.setVisibility(GONE);
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
        int state;
        String text;

        SavedState(Parcel source) {
            super(source);
            state = source.readInt();
            text = source.readString();
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(state);
            out.writeString(text);
        }
    }

    public void addTextChangedListener(@NonNull TextWatcher textWatcher) {
        this.editText.addTextChangedListener(textWatcher);
    }
}
