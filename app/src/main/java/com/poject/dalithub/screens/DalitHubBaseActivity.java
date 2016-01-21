package com.poject.dalithub.screens;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Window;

import com.kelltontech.ui.activity.BaseActivity;
import com.poject.dalithub.R;

public class DalitHubBaseActivity extends BaseActivity {

    private String TAG = "DalitHubBaseActivity";

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    /**
     * Subclass should over-ride this method to update the UI with response,
     * this base class promises to call this method from UI thread.
     *
     * @param status
     * @param action
     * @param serviceResponse
     */
    @Override
    public void updateUi(boolean status, int action, Object serviceResponse) {
        System.out.println("activity update UI");
    }

    /**
     * Subclass should over-ride this method to update the UI on event <br/>
     * Caller should note that it should be called only from UI thread.
     *
     * @param eventId
     * @param eventData
     * @deprecated
     */
    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    @Override
    public void getData(int actionID, String... s) {

    }

    @Override
    public void showProgressDialog(String message) {
        removeProgressDialog();
        Log.d(TAG, "showProgressDialog--DalitHubBaseActivity...........");
        //if (mProgressDialog == null) {

        Log.d(TAG, "showProgressDialog is null--DalitHubBaseActivity...........");

        mProgressDialog = new ProgressDialog(new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog));
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressDialog.setCancelable(false);
        if (message != null) {
            mProgressDialog.setMessage(message);
        } else {
            mProgressDialog.setMessage(getResources().getString(R.string.loading));
        }
        mProgressDialog.show();
        //}
    }

    @Override
    public void removeProgressDialog() {
        Log.d(TAG, "removeProgressDialog--DalitHubBaseActivity...........");
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            Log.d(TAG, "removeProgressDialog is null--DalitHubBaseActivity...........");
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void hideProgressDialog() {
        removeProgressDialog();
    }

    public void showAlert(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(title);
        //alertDialogBuilder.setIcon(R.drawable.notification_template_icon_bg);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.cancel();
            }
        });

        /*alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });*/

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
