package com.poject.dalithub.subFragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
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
//import com.poject.dalithub.adapters.MembersAdapter;
import com.poject.dalithub.adapters.MembersSectionAdapter;
import com.poject.dalithub.adapters.SectionableAdapter;
import com.poject.dalithub.application.AppController;
import com.poject.dalithub.listener.VolleyErrorListener;
import com.poject.dalithub.models.DalitHubBaseModel;
import com.poject.dalithub.models.FarFarAway;
import com.poject.dalithub.models.LessThan5Km;
import com.poject.dalithub.models.Member;
import com.poject.dalithub.models.MembersBaseClass;
import com.poject.dalithub.models.MembersItems;
import com.poject.dalithub.models.NearMeLessThan1;
import com.poject.dalithub.models.WalkableDistanceLessThan500;
import com.poject.dalithub.screens.InviteMemberScreen;
import com.poject.dalithub.screens.LandingScreenActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MembersFragment extends DalitHubBasefragment {
    private ListView mListView;
    //    private MembersAdapter adapter;
    private UserPreferences mPref;
    private int DIALOG_YES_BUTTON = -1;
    private int DIALOG_NO_BUTTON = -2;
    private Location currentLocation;
    private boolean isExecutedOnce = false;
    private TextView num_members;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mPref = new UserPreferences(mLandingActivity);
        View view = inflater.inflate(R.layout.member_frag_layout, null);
        mListView = (ListView) view.findViewById(R.id.sectionedGrid_list);
        num_members = (TextView) view.findViewById(R.id.num_members);

//        ArrayList<String> mylist = new ArrayList<String>();
//        for (int i = 0; i < 30; i++) {
//            mylist.add("");
//        }

        setScreenListeners();
        if (!showGPSAlert())
            return view;

        AppController appController = AppController.getInstance();
        currentLocation = appController.getmLastLocation();

        if (currentLocation == null || currentLocation.getLatitude() <= 0) {
            AppUtils.showToast(mLandingActivity, "unable to get location.Try again.");
            return view;
        }


        getData(AppConstants.actions.GET_ALL_MEMBERS, mPref.getUserId(), String.valueOf(currentLocation.getLatitude()),
                String.valueOf(currentLocation.getLongitude()));
        isExecutedOnce = true;


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isExecutedOnce) {

            if (currentLocation == null || currentLocation.getLatitude() <= 0) {
                AppUtils.showToast(mLandingActivity, "unable to get location.Try again.");
                return;
            }

            getData(AppConstants.actions.GET_ALL_MEMBERS, mPref.getUserId(), String.valueOf(currentLocation.getLatitude()),
                    String.valueOf(currentLocation.getLongitude()));

        }
    }


    private void setScreenListeners() {
        mLandingActivity.setHeaderButtonListeners(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.left_button) {
                    System.out.println("left click");
                    if (!showGPSAlert())
                        return;

                    AppController appController = AppController.getInstance();
                    currentLocation = appController.getmLastLocation();

                    if (currentLocation == null || currentLocation.getLatitude() <= 0) {
                        AppUtils.showToast(mLandingActivity, "unable to get location.Try again.");
                        return;
                    }
                    getData(AppConstants.actions.GET_ALL_MEMBERS, mPref.getUserId(), String.valueOf(currentLocation.getLatitude()),
                            String.valueOf(currentLocation.getLongitude()));
                } else if (v.getId() == R.id.right_button) {
                    System.out.println("right click");

                    startActivity(new Intent(mLandingActivity, InviteMemberScreen.class));
                }

            }
        });
    }

    private boolean showGPSAlert() {
        LocationManager manager = (LocationManager) mLandingActivity.getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!statusOfGPS) {
            ((AppController) mLandingActivity.getApplication()).showGPSDialog(mLandingActivity, new DialogInterface.OnClickListener() {
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
        if (!ConnectivityUtils.isNetworkEnabled(mLandingActivity)) {
            mLandingActivity.showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_internet));
            return;
        }
        mLandingActivity.showProgressDialog(getResources().getString(R.string.loading));

        switch (actionID) {
            case AppConstants.actions.GET_ALL_MEMBERS:
                RequestManager.addRequest(new GsonObjectRequest<MembersBaseClass>(
                        getMembersUrl(params[0], params[1], params[2]), null, MembersBaseClass.class,
                        new VolleyErrorListener(MembersFragment.this, actionID)) {
                    @Override
                    protected void deliverResponse(MembersBaseClass response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        MembersFragment.this.updateUi(true, actionID, response);
                    }
                });
                break;
        }
    }

    @Override
    public void updateUi(boolean status, int action, Object serviceResponse) {

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
        } else if (!((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("124")) {
            mLandingActivity.showAlert(getResources().getString(R.string.err_msg), ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            Log.e(TAG, "Message from Server : " + ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            return;
        }

        switch (action) {
            case AppConstants.actions.GET_ALL_MEMBERS: {
                Log.d(TAG, "response is success..........................");
                try {
                    MembersBaseClass response = (MembersBaseClass) serviceResponse;

                    AppUtils.showToast(mLandingActivity,
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
            MembersSectionAdapter adapter = new MembersSectionAdapter(mLandingActivity,
                    mLandingActivity.getLayoutInflater(), R.layout.book_row, R.id.row_layout,
                    R.id.bookRow_itemHolder, SectionableAdapter.MODE_VARY_WIDTHS, membersItemsArrayList, membersMap);
//		MembersSectionAdapter adapter = new MembersSectionAdapter(this,
//				getLayoutInflater(), R.layout.book_row_vary_columns, R.id.bookRow_header,
//				R.id.bookRow_itemHolder, SectionableAdapter.MODE_VARY_COUNT);
            mListView.setAdapter(adapter);
            mListView.setDividerHeight(0);
        }
    }

    public class SectionItem implements Item {
        private String sectionTitle;

        SectionItem(String sectionText) {
            sectionTitle = sectionText;
        }

        public String getSectionTitle() {
            return sectionTitle;
        }

        @Override
        public boolean isSection() {
            return true;
        }
    }

    public class EntryItem implements Item {
        public NearMeLessThan1 getModelItem() {
            return modelItem;
        }

        private NearMeLessThan1 modelItem;

        public EntryItem(NearMeLessThan1 item) {
            modelItem = item;

        }

        @Override
        public boolean isSection() {
            return false;
        }
    }

//    private void setData(MembersBaseClass response) {
//        if (response != null) {
//
//            ArrayList<MembersItems> items = response.getItems();
//
//
//
//            ArrayList<Item> itemList = new ArrayList<Item>();
//            itemList.add(new SectionItem("Walkable distance < less than 500"));
//            for (int i = 0; i < walkableLsit.size(); i++) {
//                NearMeLessThan1 item = walkableLsit.get(i);
//                itemList.add(new EntryItem(item));
//            }
//
//            itemList.add(new SectionItem("Near me < less than 1 KM"));
//            for (int i = 0; i < nearMeLessThan1List.size(); i++) {
//                NearMeLessThan1 item = nearMeLessThan1List.get(i);
//                itemList.add(new EntryItem(item));
//            }
//
//            itemList.add(new SectionItem("lessthan < 5 KM"));
//            for (int i = 0; i < lessThan5KmList.size(); i++) {
//                NearMeLessThan1 item = lessThan5KmList.get(i);
//                itemList.add(new EntryItem(item));
//            }
//
//            itemList.add(new SectionItem("far far away"));
//            for (int i = 0; i < farFarAwayList.size(); i++) {
//                NearMeLessThan1 item = farFarAwayList.get(i);
//                itemList.add(new EntryItem(item));
//            }
//
//            if (itemList.size() > 0) {
//                adapter = new MembersAdapter(getActivity(), itemList);
//                mGridView.setAdapter(adapter);
//            }
//        }
//
//    }

    //    private setSectionedAdapter(){
//        //Your RecyclerView
//        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.list);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
//
//        //Your RecyclerView.Adapter
//        mAdapter = new SimpleAdapter(getActivity());
//
//        //This is the code to provide a sectioned grid
//        List<SectionedGridRecyclerViewAdapter.Section> sections =
//                new ArrayList<SectionedGridRecyclerViewAdapter.Section>();
//
//        //Sections
//        sections.add(new SectionedGridRecyclerViewAdapter.Section(0,"Section 1"));
//        sections.add(new SectionedGridRecyclerViewAdapter.Section(5,"Section 2"));
//        sections.add(new SectionedGridRecyclerViewAdapter.Section(12,"Section 3"));
//        sections.add(new SectionedGridRecyclerViewAdapter.Section(14,"Section 4"));
//        sections.add(new SectionedGridRecyclerViewAdapter.Section(20,"Section 5"));
//
//        //Add your adapter to the sectionAdapter
//        SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
//        SectionedGridRecyclerViewAdapter mSectionedAdapter = new
//                SectionedGridRecyclerViewAdapter(getActivity(),R.layout.section,R.id.section_text,mRecyclerView,mAdapter);
//        mSectionedAdapter.setSections(sections.toArray(dummy));
//
//        //Apply this adapter to the RecyclerView
//        mRecyclerView.setAdapter(mSectionedAdapter);
//    }
    private String getMembersUrl(String userId, String lat, String longt) {
        return AppConstants.baseUrl + "GetMembersList?userId=" + userId + "&latitude=" + lat + "&longitude=" + longt;
    }
}
