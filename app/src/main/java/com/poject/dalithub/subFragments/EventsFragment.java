package com.poject.dalithub.subFragments;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kelltontech.utils.ConnectivityUtils;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.poject.dalithub.Utils.AppConstants;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.adapters.EventsAdapter;
import com.poject.dalithub.listener.VolleyErrorListener;
import com.poject.dalithub.models.DalitHubBaseModel;
import com.poject.dalithub.models.EventBaseClass;
import com.poject.dalithub.models.EventModel;
import com.poject.dalithub.screens.CreateEventsActivity;
import com.poject.dalithub.screens.EventsDetailScreen;
import com.poject.dalithub.screens.LandingScreenActivity;
import com.poject.dalithub.R;

public class EventsFragment extends DalitHubBasefragment implements OnClickListener {
    EventsAdapter adapter;
    ListView list_events;
    private TextView monthView;
    LandingScreenActivity activity;
    UserPreferences mPref;
    private int currentMonth;

    public int getCurrentYear() {
        return currentYear;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    private int currentYear;
    private ImageView nextButton, prevButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.events_layout, null);
        initViews(view);


        setScreenListeners();
        getPresentCalenderDate();

        int callingMonth = currentMonth + 1;
        getData(AppConstants.actions.GET_ALL_EVENTS, currentYear + "-" + callingMonth + "-1", mPref.getUserId());
        // getData(AppConstants.actions.GET_ALL_EVENTS, "2015-12-14", "2");

        return view;
    }


    private void initViews(View view) {
        list_events = (ListView) view.findViewById(R.id.list_events);
        mPref = new UserPreferences(activity);
        monthView = (TextView) view.findViewById(R.id.month);
        prevButton = (ImageView) view.findViewById(R.id.prev_click);
        nextButton = (ImageView) view.findViewById(R.id.next_click);

    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        if (activity != null && activity instanceof LandingScreenActivity) {
            this.activity = (LandingScreenActivity) activity;
        }
    }

    private void setScreenListeners() {
        nextButton.setOnClickListener(this);
        prevButton.setOnClickListener(this);
        activity.setHeaderButtonListeners(new OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.left_button:
                        System.out.println("left click");
                        int callingMonth = currentMonth + 1;
                        getData(AppConstants.actions.GET_ALL_EVENTS, currentYear + "-" + callingMonth + "-1", mPref.getUserId());
                        break;
                    case R.id.right_button:
                        System.out.println("right click");
                        startActivityForResult(new Intent(mLandingActivity, CreateEventsActivity.class), AppConstants.EVENT_CREATED);
                        break;

                }


            }
        });
        list_events.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                startActivity(new Intent(getActivity(),
                        EventsDetailScreen.class));
            }
        });
    }

    private void getPresentCalenderDate() {
        Calendar cal = Calendar.getInstance();
        int currMonth = cal.get(Calendar.MONTH);
        int currYear = cal.get(Calendar.YEAR);
        Log.d("getPresentMonth", "" + currMonth + " " + currYear);
        setCalenderMonth(currMonth);
        setCurrentYear(currYear);
    }

    private void setCalenderMonth(int index) {
        String[] monthsArr = mLandingActivity.getResources().getStringArray(R.array.events_month);
        monthView.setText(monthsArr[index]);
        setCurrentMonth(index);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.EVENT_CREATED && resultCode == AppConstants.EVENT_CREATED) {
            Log.d("onActivityResult", "Event successfully created");
        }
    }

    private String TAG = "showAllEventsTag";

    @Override
    public void getData(int actionID, String... params) {
        final int actId = actionID;
        if (!ConnectivityUtils.isNetworkEnabled(mLandingActivity)) {
            mLandingActivity.showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_internet));
            return;
        }
        mLandingActivity.showProgressDialog(getLandingActivityContext().getResources().getString(R.string.loading));

        switch (actionID) {
            case AppConstants.actions.GET_ALL_EVENTS:
                RequestManager.addRequest(new GsonObjectRequest<EventBaseClass>(
                        getAllEventsUrl(params[0], params[1]), null, EventBaseClass.class,
                        new VolleyErrorListener(EventsFragment.this, actionID)) {
                    @Override
                    protected void deliverResponse(EventBaseClass response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "getAllBites response json--->" + data);
                        }

                        EventsFragment.this.updateUi(true, actId, response);
                    }
                });
                break;
        }
    }

    @Override
    public void updateUi(boolean status, int actionID, Object serviceResponse) {
        mLandingActivity.hideProgressDialog();

        Log.e(TAG, " updateUi ::Service Response : " + serviceResponse);

        //If unable to process the request
        if (!status && serviceResponse instanceof String) {
            mLandingActivity.showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.request_fail));
            Log.e(TAG, "Problem with Server Response");
            return;
        }

        //If not valid response
        if (!(serviceResponse instanceof DalitHubBaseModel)) {
            mLandingActivity.showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_unknown));
            Log.e(TAG, "Server Response is not instance of BaseModel");
            return;
        } else if (!((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("118")) {
            mLandingActivity.showAlert(getResources().getString(R.string.err_msg), ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            Log.e(TAG, "Message from Server : " + ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            return;
        }

        switch (actionID) {
            case AppConstants.actions.GET_ALL_EVENTS: {
                Log.d(TAG, "response is success..........................");
                try {
                    EventBaseClass response = (EventBaseClass) serviceResponse;

                    System.out.println("bites response >>>>> " + response.toString());
                    AppUtils.showToast(mLandingActivity,
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());

                    List<EventModel> eventsList = response.getEvents();

                    if (eventsList == null || eventsList.size() < 0)
                        return;

                    adapter = new EventsAdapter(this, getActivity(), eventsList);
                    list_events.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
        }
    }

    private String getAllEventsUrl(String date, String user_id) {
        String url = AppConstants.baseUrl + "getallevents?Date=" + date + "&user_id=" + user_id;
        Log.d("getAllEventsUrl", "" + url);
        return url;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prev_click:
                nextButton.setEnabled(true);
                int myMonth = getCurrentMonth();
                if (myMonth == 0) {
                    view.setEnabled(false);
                } else {
                    int actMonth = myMonth - 1;
                    setCalenderMonth(actMonth);
                    Log.d("act month>>>>", "" + actMonth);
                    getData(AppConstants.actions.GET_ALL_EVENTS, currentYear + "-" + (actMonth + 1) + "-1", mPref.getUserId());
                }
                break;
            case R.id.next_click:
                prevButton.setEnabled(true);
                int month = getCurrentMonth();
                if (month == 11) {
                    view.setEnabled(false);
                } else {
                    int actMonth = month + 1;
                    setCalenderMonth(actMonth);
                    Log.d("act month>>>>", "" + actMonth);
                    getData(AppConstants.actions.GET_ALL_EVENTS, currentYear + "-" + (actMonth + 1) + "-1", mPref.getUserId());
                }
                break;
        }
    }

//    public void dateNavigator(View view) {
//        switch (view.getId()) {
//            case R.id.prev_click:
//                if (getCurrentMonth() == 0) {
//                    view.setEnabled(false);
//                    nextButton.setEnabled(true);
//                } else {
//                    setCalenderMonth(getCurrentMonth() - 1);
//                }
//                break;
//            case R.id.next_click:
//                if (getCurrentMonth() == 11) {
//                    view.setEnabled(false);
//                } else {
//                    prevButton.setEnabled(true);
//                    setCalenderMonth(getCurrentMonth() + 1);
//                }
//                break;
//        }
//    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }
}
