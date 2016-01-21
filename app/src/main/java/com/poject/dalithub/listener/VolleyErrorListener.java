package com.poject.dalithub.listener;

/**
 * Created by admin on 11/28/2015.
 */

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kelltontech.ui.IScreen;

public class VolleyErrorListener implements Response.ErrorListener {
    private final int action;
    private final IScreen screen;

    private String TAG = "VolleyErrorListener";

    public VolleyErrorListener(final IScreen screen, final int action) {
        this.screen = screen;
        this.action = action;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        /* TODO: Log
		 if (error instanceof NoConnectionError) {

		 } else if (error instanceof AuthFailureError) {

		 } else if (error instanceof NetworkError) {

		 } else if (error instanceof ParseError) {

		 } else if (error instanceof ServerError) {

		 } else if (error instanceof TimeoutError) {

		 }*/

        Log.d(TAG, "VolleyErrorListener->" + error.toString() + "==" + screen.getClass().getSimpleName() + "==" + action);
        screen.updateUi(false, action, error.toString());

    }

}

