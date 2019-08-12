package com.rgp.asks.interfaces;

import androidx.annotation.NonNull;

public interface ArgumentDialogListener {
    void onArgumentDialogCreateButtonClick(@NonNull String newArgument);

    void onArgumentDialogSaveButtonClick(int argumentId, @NonNull String newArgument);

    void onArgumentDialogDeleteButtonClick(int argumentId);
}
