package com.example.fragmentslearn;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;

public class Dialog extends AppCompatDialogFragment {
    private String countryName;
    private SharedPreferences countryDetail;
    private int position;
    private static final String TAG = "Dialog";
    boolean inList;
    ConstraintLayout constraintLayout;

    public Dialog(String countryName, SharedPreferences countryDetail, int position, ConstraintLayout constraintLayout) {
        this.countryName = countryName;
        this.countryDetail = countryDetail;
        this.position = position;
        this.constraintLayout = constraintLayout;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String message;
        String action;
        if (countryDetail.contains(countryName)) {
            inList = true;
            action = "Delete?";
            message = countryName + " is already added to favorites list";
        } else {
            inList = false;
            message = "Add " + countryName + " to Favorites list?";
            action = "OK";
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Favorites").setMessage(message).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = countryDetail.edit();
                        if (!inList) {
                            editor.putInt(countryName, position);
                            editor.apply();
                            constraintLayout.setBackgroundColor(Color.parseColor("#fc7474"));
                        } else {
                            editor.remove(countryName);
                            editor.apply();
                            constraintLayout.setBackgroundColor(Color.parseColor("#F3E5F5"));
                        }
                    }
                });
        return builder.create();
    }
}
