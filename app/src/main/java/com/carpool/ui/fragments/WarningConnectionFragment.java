package com.carpool.ui.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carpool.R;
import com.carpool.ui.activities.CreationProfilActivity;
import com.carpool.ui.activities.ProfilLoginActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class WarningConnectionFragment extends DialogFragment {

    public WarningConnectionFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.title_dialog_offer);
        builder.setMessage(this.getArguments().getString("textContent"))
                .setPositiveButton(R.string.button_register_dialog_offer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent newActivity = new Intent(getActivity(), CreationProfilActivity.class);
                        startActivity(newActivity);
                        getActivity().finish();
                    }
                })
                .setNeutralButton(R.string.button_login_dialog_offer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent newActivity = new Intent(getActivity(), ProfilLoginActivity.class);
                        startActivity(newActivity);
                        getActivity().finish();
                    }
                })
                .setNegativeButton(R.string.button_cancel_dialog_offer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Title
        final int titleId = getResources().getIdentifier("alertTitle", "id", "android");
        final View title = getDialog().findViewById(titleId);
        if (title != null) {
            ((TextView) title).setTextColor(getResources().getColor(R.color.material_deep_teal_500));
        }
        // Title divider
        final int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
        final View titleDivider = getDialog().findViewById(titleDividerId);
        if (titleDivider != null) {
            titleDivider.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
        }
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }*/


}
