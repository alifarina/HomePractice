package com.poject.dalithub.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.View;

/**
 * Created by admin on 12/27/2015.
 */
public class DialogsCustom {

    public static AlertDialog promptDialog(Context con, String title, String message, String posText, String negText, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(con, android.R.style.Theme_DeviceDefault_Dialog);
        builder.setPositiveButton(posText, listener);
        builder.setNegativeButton(negText, listener);
        AlertDialog dialog = builder.create();
        dialog.setTitle(title);
        dialog.setMessage(message);
        return dialog;
    }


}
