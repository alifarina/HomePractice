package com.poject.dalithub.subFragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.kelltontech.ui.fragment.BaseFragment;
import com.poject.dalithub.screens.LandingScreenActivity;

/**
 * Created by admin on 11/29/2015.
 */
public class DalitHubBasefragment extends BaseFragment {

    protected LandingScreenActivity mLandingActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void getData(int actionID, String... s) {
        Log.d("getData", "getData");
    }

    @Override
    public void onEvent(int eventId, Object eventData) {
        Log.d("onEvent", "onEvent");
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        Log.d("updateUi", "updateUi");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (isAdded() && activity != null)
            mLandingActivity = (LandingScreenActivity) activity;

    }

    public LandingScreenActivity getLandingActivityContext() {
        if (mLandingActivity == null) {
            mLandingActivity = (LandingScreenActivity) getActivity();
        }
        return mLandingActivity;
    }
}
