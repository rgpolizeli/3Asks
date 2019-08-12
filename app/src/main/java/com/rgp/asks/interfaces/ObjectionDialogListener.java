package com.rgp.asks.interfaces;

import androidx.annotation.NonNull;

public interface ObjectionDialogListener {
    void onObjectionDialogCreateButtonClick(@NonNull String newObjection);

    void onObjectionDialogSaveButtonClick(int objectionId, @NonNull String newObjection);

    void onObjectionDialogDeleteButtonClick(int objectionId);
}
