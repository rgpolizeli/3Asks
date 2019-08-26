package com.rgp.asks.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.rgp.asks.R;

public class HelpInfoFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help_info, container, false);
        setupChildrenViewsOnClickListener(rootView);
        return rootView;
    }

    /**
     * Create and set listeners to children views.
     *
     * @throws NullPointerException
     */
    private void setupChildrenViewsOnClickListener(View helpInfoFragmentView) throws NullPointerException {
        TextView websiteTextView = helpInfoFragmentView.findViewById(R.id.websiteTextView);
        TextView howTextView = helpInfoFragmentView.findViewById(R.id.howItWorksTextView);
        TextView exampleTextView = helpInfoFragmentView.findViewById(R.id.exampleOfUseTextView);
        TextView termsTextView = helpInfoFragmentView.findViewById(R.id.termsOfUseTextView);
        TextView privacyTextView = helpInfoFragmentView.findViewById(R.id.privacyPolicyTextView);
        TextView contactTextView = helpInfoFragmentView.findViewById(R.id.contactTextView);

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
}
