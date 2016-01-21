package com.poject.dalithub.screens;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kelltontech.utils.ConnectivityUtils;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.poject.dalithub.R;
import com.poject.dalithub.Utils.AppConstants;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.Utils.Item;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.adapters.MembersSectionAdapter;
import com.poject.dalithub.adapters.SectionableAdapter;
import com.poject.dalithub.application.AppController;
import com.poject.dalithub.listener.VolleyErrorListener;
import com.poject.dalithub.models.DalitHubBaseModel;
import com.poject.dalithub.models.Member;
import com.poject.dalithub.models.MembersBaseClass;
import com.poject.dalithub.models.MembersItems;
import com.poject.dalithub.models.NearMeLessThan1;
import com.poject.dalithub.subFragments.DalitHubBasefragment;

import java.util.ArrayList;
import java.util.HashMap;

//import com.poject.dalithub.adapters.MembersAdapter;

public class MembersListActivity extends DalitHubBaseActivity implements OnClickListener {
    private ListView mListView;
    //    private MembersAdapter adapter;
    private UserPreferences mPref;
    private int DIALOG_YES_BUTTON = -1;
    private int DIALOG_NO_BUTTON = -2;
    private Location currentLocation;
    private boolean isExecutedOnce = false;
    private TextView num_members;

    private ImageView leftTopButton, rightTopButton;
    private TextView headerTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.member_screen_layout);
        mPref = new UserPreferences(this);

        initViews();

//        ArrayList<String> mylist = new ArrayList<String>();
//        for (int i = 0; i < 30; i++) {
//            mylist.add("");
//        }

        setScreenListeners();
        if (!showGPSAlert())
            return;

        AppController appController = AppController.getInstance();
        currentLocation = appController.getmLastLocation();

        if (currentLocation == null || currentLocation.getLatitude() <= 0) {
            AppUtils.showToast(this, "unable to get location.Try again.");
            return;
        }

        String event_id = getIntent().getStringExtra("eventId");

        getData(AppConstants.actions.GET_ALL_MEMBERS, mPref.getUserId(), String.valueOf(currentLocation.getLatitude()),
                String.valueOf(currentLocation.getLongitude()), event_id);
        isExecutedOnce = true;


    }

    private void initViews() {
        leftTopButton = (ImageView) findViewById(R.id.left_button);
        rightTopButton = (ImageView) findViewById(R.id.right_button);
        headerTitle = (TextView) findViewById(R.id.title);

        mListView = (ListView) findViewById(R.id.sectionedGrid_list);
        num_members = (TextView) findViewById(R.id.num_members);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!isExecutedOnce) {

            if (currentLocation == null || currentLocation.getLatitude() <= 0) {
                AppUtils.showToast(this, "unable to get location.Try again.");
                return;
            }

            getData(AppConstants.actions.GET_ALL_MEMBERS, mPref.getUserId(), String.valueOf(currentLocation.getLatitude()),
                    String.valueOf(currentLocation.getLongitude()));

        }
    }


    private void setScreenListeners() {

        leftTopButton.setOnClickListener(this);
        rightTopButton.setVisibility(View.GONE);
        headerTitle.setText("Event Members");


    }

    private boolean showGPSAlert() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!statusOfGPS) {
            ((AppController) getApplication()).showGPSDialog(this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DIALOG_YES_BUTTON) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    } else if (which == DIALOG_NO_BUTTON) {

                    }
                }
            });
        }

        return statusOfGPS;

    }

    private String TAG = "MembersTag";

    @Override
    public void getData(final int actionID, String... params) {
        if (!ConnectivityUtils.isNetworkEnabled(this)) {
            showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_internet));
            return;
        }
        showProgressDialog(getResources().getString(R.string.loading));

        switch (actionID) {
            case AppConstants.actions.GET_ALL_MEMBERS:
                RequestManager.addRequest(new GsonObjectRequest<MembersBaseClass>(
                        getMembersUrl(params[0], params[1], params[2], params[3]), null, MembersBaseClass.class,
                        new VolleyErrorListener(MembersListActivity.this, actionID)) {
                    @Override
                    protected void deliverResponse(MembersBaseClass response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        MembersListActivity.this.updateUi(true, actionID, response);
                    }
                });
                break;
        }
    }

    @Override
    public void updateUi(boolean status, int action, Object serviceResponse) {

        hideProgressDialog();

        Log.e(TAG, " updateUi ::Service Response : " + serviceResponse);

        //If unable to process the request
        if (!status && serviceResponse instanceof String) {
            showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.request_fail));
            Log.e(TAG, "Problem with Server Response");
            return;
        }

        //If not valid response
        if (!(serviceResponse instanceof DalitHubBaseModel)) {
            showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_unknown));
            Log.e(TAG, "Server Response is not instance of BaseModel");
            return;
        } else if (!((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("124")) {
            showAlert(getResources().getString(R.string.err_msg), ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            Log.e(TAG, "Message from Server : " + ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            return;
        }

        switch (action) {
            case AppConstants.actions.GET_ALL_MEMBERS: {
                Log.d(TAG, "response is success..........................");
                try {
                    MembersBaseClass response = (MembersBaseClass) serviceResponse;

                    AppUtils.showToast(this,
                            "reading successful");
                    setData(response);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
        }
    }

    private void setData(MembersBaseClass response) {
        if (response != null) {

            num_members.setText("" + response.getMembersCount() + " members");

            ArrayList<MembersItems> membersItemsArrayList = response.getItems();
            ArrayList<MembersItems> headerDetailList;
            int listSize = membersItemsArrayList.size();
            HashMap<String, ArrayList<Member>> membersMap = new HashMap<>();
            String[] headers = new String[0];
            if (membersItemsArrayList != null && listSize > 0) {

                headers = new String[listSize];


            }

            for (int i = 0; i < listSize; i++) {
                MembersItems membersItems = membersItemsArrayList.get(i);
                headers[i] = membersItems.getHeaderName();
                membersMap.put(membersItems.getHeaderName(), membersItems.getMembers());

            }

            // Switch between these to see the two different types of resizing available.
            MembersSectionAdapter adapter = new MembersSectionAdapter(this,
                    getLayoutInflater(), R.layout.book_row, R.id.row_layout,
                    R.id.bookRow_itemHolder, SectionableAdapter.MODE_VARY_WIDTHS, membersItemsArrayList, membersMap);
//		MembersSectionAdapter adapter = new MembersSectionAdapter(this,
//				getLayoutInflater(), R.layout.book_row_vary_columns, R.id.bookRow_header,
//				R.id.bookRow_itemHolder, SectionableAdapter.MODE_VARY_COUNT);
            mListView.setAdapter(adapter);
            mListView.setDividerHeight(0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_button:
                goBackScreen();
                break;
        }
    }

    private void goBackScreen() {
        this.finish();
    }


    private String getMembersUrl(String userId, String lat, String longt, String eveId) {
        return AppConstants.baseUrl + "GetMembersList?userId=" + userId + "&latitude=" + lat + "&longitude=" + longt + "&eventId=" + eveId;
    }
}
