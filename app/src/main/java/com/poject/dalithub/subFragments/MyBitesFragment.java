package com.poject.dalithub.subFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kelltontech.utils.ConnectivityUtils;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.poject.dalithub.R;
import com.poject.dalithub.Utils.AppConstants;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.Utils.DialogsCustom;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.adapters.BitesAdapter;
import com.poject.dalithub.listener.VolleyErrorListener;
import com.poject.dalithub.models.AllBitesModel;
import com.poject.dalithub.models.DalitHubBaseModel;
import com.poject.dalithub.models.NewBite;
import com.poject.dalithub.screens.CommentsScreen;
import com.poject.dalithub.screens.CreateBitesScreen;
import com.poject.dalithub.screens.DisplayLikesScreen;
import com.poject.dalithub.screens.LandingScreenActivity;

import java.util.List;

public class MyBitesFragment extends DalitHubBasefragment implements OnClickListener {
    private BitesAdapter adapter;
    private ListView listView_bites;
    private View view;
    private ImageView leftTopButton, rightTopButton;
    LandingScreenActivity activity;
    private UserPreferences mPref;
    private int yPos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_bites_frag, null);
        listView_bites = (ListView) view.findViewById(R.id.bites_list);
        mPref = new UserPreferences(mLandingActivity);
        Log.d(TAG, "user id ======== " + mPref.getUserId());
        getData(AppConstants.actions.GET_ALL_BITES, mPref.getUserId());
        setScreenListeners();


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void setScreenListeners() {
        mLandingActivity.setHeaderButtonListeners(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.left_button) {
                    System.out.println("left click");

                    getData(AppConstants.actions.GET_ALL_BITES, mPref.getUserId());

                } else if (v.getId() == R.id.right_button) {
                    System.out.println("right click");

                    startActivityForResult(new Intent(mLandingActivity, CreateBitesScreen.class), AppConstants.BITE_POSTED);
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstants.BITE_POSTED && resultCode == AppConstants.BITE_POSTED) {
            getData(AppConstants.actions.GET_ALL_BITES, mPref.getUserId());
        } else if (requestCode == 111 && resultCode == AppConstants.RESULT_USER_COMMENTED) {
            getData(AppConstants.actions.GET_ALL_BITES, mPref.getUserId());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        /*if (activity != null && activity instanceof LandingScreenActivity) {
            this.activity = (LandingScreenActivity) activity;
        }*/
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // leftTopButton = (ImageView) getActivity()
        // .findViewById(R.id.left_button);
        // rightTopButton = (ImageView) getActivity().findViewById(
        // R.id.right_button);

    }

    @Override
    public void onEvent(int eventId, Object eventData) {

    }

    private String TAG = "showAllBitesTag";

    @Override
    public void getData(int actionID, String... params) {
        final int actId = actionID;
        if (!ConnectivityUtils.isNetworkEnabled(mLandingActivity)) {
            mLandingActivity.showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_internet));
            return;
        }
        getLandingActivityContext().showProgressDialog(getLandingActivityContext().getResources().getString(R.string.loading));

        switch (actionID) {
            case AppConstants.actions.GET_ALL_BITES:
                RequestManager.addRequest(new GsonObjectRequest<AllBitesModel>(
                        getAllBitesUrl(params[0]), null, AllBitesModel.class,
                        new VolleyErrorListener(MyBitesFragment.this, actionID)) {
                    @Override
                    protected void deliverResponse(AllBitesModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "getAllBites response json--->" + data);
                        }

                        MyBitesFragment.this.updateUi(true, actId, response);
                    }
                });
                break;
            case AppConstants.actions.LIKE_POST:
                RequestManager.addRequest(new GsonObjectRequest<DalitHubBaseModel>(
                        getLikeUnlikeUrl(mPref.getUserId(), params[0]), null, DalitHubBaseModel.class,
                        new VolleyErrorListener(MyBitesFragment.this, actionID)) {
                    @Override
                    protected void deliverResponse(DalitHubBaseModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        MyBitesFragment.this.updateUi(true, actId, response);
                    }
                });
                break;
            case AppConstants.actions.SET_BITE_STATUS:
                RequestManager.addRequest(new GsonObjectRequest<DalitHubBaseModel>(
                        getStatusUrl(params[0], mPref.getUserId(), params[1]), null, DalitHubBaseModel.class,
                        new VolleyErrorListener(MyBitesFragment.this, actionID)) {
                    @Override
                    protected void deliverResponse(DalitHubBaseModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        MyBitesFragment.this.updateUi(true, actId, response);
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
        } else if (!((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("115") && !((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("112")) {
            mLandingActivity.showAlert(getResources().getString(R.string.err_msg), ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            Log.e(TAG, "Message from Server : " + ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            return;
        }

        switch (actionID) {
            case AppConstants.actions.GET_ALL_BITES: {
                Log.d(TAG, "response is success..........................");
                try {
                    AllBitesModel response = (AllBitesModel) serviceResponse;

                    System.out.println("bites response >>>>> " + response.toString());
                    AppUtils.showToast(mLandingActivity,
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());

                    List<NewBite> newBites = response.getBites();

                    if (newBites == null || newBites.size() < 0)
                        return;

                    adapter = new BitesAdapter(this, getActivity(), newBites);
                    listView_bites.setAdapter(adapter);
                    if (likedBitePos > -1) {
                        listView_bites.setSelectionFromTop(likedBitePos, yPos);
                        likedBitePos = -1;
                        yPos = -1;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
            case AppConstants.actions.LIKE_POST:
                Log.d(TAG, "response is success......like post....................");
                try {
                    DalitHubBaseModel response = (DalitHubBaseModel) serviceResponse;

                    AppUtils.showToast(mLandingActivity,
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());

                    getData(AppConstants.actions.GET_ALL_BITES, mPref.getUserId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case AppConstants.actions.SET_BITE_STATUS:
                Log.d(TAG, "response is success......Status done....................");
                try {
                    DalitHubBaseModel response = (DalitHubBaseModel) serviceResponse;

                    AppUtils.showToast(mLandingActivity,
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());

                    getData(AppConstants.actions.GET_ALL_BITES, mPref.getUserId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private String getAllBitesUrl(String userId) {
        String finalUrl = AppConstants.baseUrl + "readbites?pageid=0&minid=0&maxid=0&userId=" + userId;
        Log.d(TAG, "getAllBitesUrl is --->" + finalUrl);
        return finalUrl;
    }

    private String getLikeUnlikeUrl(String userId, String biteId) {
        return AppConstants.baseUrl + "LikeBite?userId=" + userId + "&biteId=" + biteId;

    }

    private String getStatusUrl(String bite_id, String user_id, String status) {
        return AppConstants.baseUrl + "updatebitestatus?biteid=" + bite_id + "&userid=" + user_id + "&status=" + status;

    }

    private int likedBitePos;

    public void setLikesForThisPost(String biteId, int position, int y) {
        likedBitePos = position;
        yPos = y;
        getData(AppConstants.actions.LIKE_POST, biteId);
    }

    Dialog moreDialog;
    private String selectedBiteId;

    public void showDeleteSelectionDialog(String postedUserId, String biteId) {

        selectedBiteId = biteId;

        moreDialog = new Dialog(mLandingActivity, android.R.style.Theme_DeviceDefault_Dialog);
        moreDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        moreDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        moreDialog.setCancelable(false);
        moreDialog.setContentView(R.layout.more_bites_layout);
        moreDialog.show();

        TextView cancelDialogBtn = (TextView) moreDialog.findViewById(R.id.cancel);
        TextView spamDialogBtn = (TextView) moreDialog.findViewById(R.id.spam);
        TextView delDialogBtn = (TextView) moreDialog.findViewById(R.id.delete);
        TextView displayLikes = (TextView) moreDialog.findViewById(R.id.likes);

        // display delete button only if its owned content
        delDialogBtn.setVisibility(View.GONE);
        if (postedUserId.equals(mPref.getUserId())) {
            delDialogBtn.setVisibility(View.VISIBLE);
        }

        cancelDialogBtn.setOnClickListener(this);
        spamDialogBtn.setOnClickListener(this);
        delDialogBtn.setOnClickListener(this);
        displayLikes.setOnClickListener(this);

    }

    public void markContentAsSpam(final String b_id, final String status) {
        //prompt user for action
        String titlePromt = "", messagePromt = "";
        if (status.equals("2")) {
            titlePromt = "Delete Bite";
            messagePromt = "Do you want to delete this bite?";
        } else {
            titlePromt = "Report Abuse";
            messagePromt = "Do you want to mark this bite as spam?";
        }
        final AlertDialog dialog = DialogsCustom.promptDialog(mLandingActivity, titlePromt, messagePromt, "Yes", "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == AlertDialog.BUTTON_POSITIVE) {
                    getData(AppConstants.actions.SET_BITE_STATUS, b_id, status);
                } else {
                }
            }
        });

        dialog.show();

    }

    public void goToCommentsSCreen(String bite_id) {
        Intent comIntent = new Intent(mLandingActivity,
                CommentsScreen.class);
        comIntent.putExtra("bite_id", bite_id);
        startActivityForResult(comIntent, 111);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                if (moreDialog != null && moreDialog.isShowing())
                    moreDialog.dismiss();
                break;
            case R.id.delete:
                if (moreDialog != null && moreDialog.isShowing())
                    moreDialog.dismiss();
                markContentAsSpam(selectedBiteId, "2");
                break;
            case R.id.spam:
                if (moreDialog != null && moreDialog.isShowing())
                    moreDialog.dismiss();
                markContentAsSpam(selectedBiteId, "3");

                break;
            case R.id.likes:
                if (moreDialog != null && moreDialog.isShowing())
                    moreDialog.dismiss();

                displayLikes();
                break;
        }
    }

    private void displayLikes() {
        Intent intent = new Intent(mLandingActivity, DisplayLikesScreen.class);
        intent.putExtra("bite_id", selectedBiteId);
        startActivity(intent);
    }
}
