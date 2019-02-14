package com.tierhilfe.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;

import com.tierhilfe.R;

public class Util {


    private static android.support.v7.app.AlertDialog customDialog;

    public static void showLoading(Activity activity) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.loading, null);
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(activity, R.style.CustomDialog);
        dialog.setView(view);
        dialog.create();
        dialog.setCancelable(false);
        customDialog = dialog.show();
    }


    public static void hideLoading() {
        customDialog.dismiss();
    }
}
