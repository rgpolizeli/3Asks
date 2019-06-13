package com.rgp.asks.dialogs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.rgp.asks.R;

public class HelpInfoDialog {

    @Nullable
    private View helpInfoDialogView;
    @Nullable
    private AlertDialog alertDialog;

    public void createHelpInfoDialogView(@NonNull LayoutInflater inflater, int layoutId) throws InflateException {
        this.helpInfoDialogView = inflater.inflate(layoutId, null);
    }

    /**
     * Create and set listeners to children views.
     *
     * @throws NullPointerException
     */
    public void setupChildrenViewsOnClickListener() throws NullPointerException {
        ImageButton closeButton = helpInfoDialogView.findViewById(R.id.helpInfoDialogCloseImageButton);

        TextView websiteTextView = helpInfoDialogView.findViewById(R.id.websiteTextView);
        TextView howTextView = helpInfoDialogView.findViewById(R.id.howItWorksTextView);
        TextView exampleTextView = helpInfoDialogView.findViewById(R.id.exampleOfUseTextView);
        TextView termsTextView = helpInfoDialogView.findViewById(R.id.termsOfUseTextView);
        TextView privacyTextView = helpInfoDialogView.findViewById(R.id.privacyPolicyTextView);
        TextView contactTextView = helpInfoDialogView.findViewById(R.id.contactTextView);

        closeButton.setOnClickListener(v -> alertDialog.dismiss());

        websiteTextView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://3asksapp.neocities.org/"));
            v.getContext().startActivity(browserIntent);
        });

        howTextView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://3asksapp.neocities.org/howitworks/"));
            v.getContext().startActivity(browserIntent);
        });

        exampleTextView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://3asksapp.neocities.org/example/"));
            v.getContext().startActivity(browserIntent);
        });

        termsTextView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://3asksapp.neocities.org/about/terms.html"));
            v.getContext().startActivity(browserIntent);
        });

        privacyTextView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://3asksapp.neocities.org/about/privacy.html"));
            v.getContext().startActivity(browserIntent);
        });

        contactTextView.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "3asksapp@gmail.com", null));
            v.getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
        });
    }

    /**
     * Create an {@link AlertDialog} and {@link View.OnClickListener}s to positive and negative buttons.
     * It is necessary to configure listeners with function getButton().setOnClickListener() because the function setPositiveButton dismiss the alertDialog when clicked even an input error occurs.
     *
     * @param context
     */

    public void createAlertDialog(@NonNull Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(helpInfoDialogView);
        this.alertDialog = builder.create();
    }

    public void show() {
        if (this.alertDialog != null) this.alertDialog.show();
    }

    public void dismiss() {
        if (this.alertDialog != null) this.alertDialog.dismiss();
    }
}
