package com.rgp.asks.interfaces;

import androidx.annotation.NonNull;

public interface ReactionDialogListener {
    void onReactionDialogCreateButtonClick(@NonNull String newReaction, @NonNull String newReactionClass);

    void onReactionDialogSaveButtonClick(int reactionId, @NonNull String newReaction, @NonNull String newReactionClass);

    void onReactionDialogDeleteButtonClick(int reactionId);
}
