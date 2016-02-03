package com.poject.dalithub.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.kelltontech.utils.ConnectivityUtils;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.poject.dalithub.R;
import com.poject.dalithub.Utils.AppConstants;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.listener.VolleyErrorListener;
import com.poject.dalithub.models.DalitHubBaseModel;
import com.poject.dalithub.models.Member;
import com.poject.dalithub.models.UserInfoModel;
import com.squareup.picasso.Picasso;

//import com.loopj.android.http.RequestHandle;
//import com.loopj.android.http.RequestParams;
//import com.loopj.android.http.SyncHttpClient;
//import com.loopj.android.http.TextHttpResponseHandler;

//import cz.msebera.android.httpclient.Header;

public class MemberDetailScreen extends DalitHubBaseActivity implements OnClickListener {
    private ImageView leftTopButton, rightTopButton, image_bites;
    private TextView headerTitle;
    private UserPreferences mPref;
    private EditText content_bite;
    private String encodedImageString;
    String imgDecodableString;
    private TextView TextComp, TextBio, TextSkills, TextAdd, TextPhone, TextSite, TextEmail;
    private String memberId;
    private ImageView profile_pic;
    private TextView txt_lastname;
    private TextView txt_firstname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        mPref = new UserPreferences(this);

        setContentView(R.layout.member_profile_screen);
        initViews();
        setScreenListeners();

        memberId = getIntent().getStringExtra("MemberId");

        getData(AppConstants.actions.GET_MEMBER_DETAIL, memberId);
    }

    private void setScreenListeners() {
        leftTopButton.setOnClickListener(this);
    }

    private void initViews() {
        leftTopButton = (ImageView) findViewById(R.id.left_button);
        rightTopButton = (ImageView) findViewById(R.id.right_button);
        headerTitle = (TextView) findViewById(R.id.title);
        profile_pic = (ImageView) findViewById(R.id.profile_pic);


        rightTopButton.setVisibility(View.GONE);
        leftTopButton.setImageResource(R.drawable.back);

        txt_lastname = (TextView) findViewById(R.id.txt_lastname);
        txt_firstname = (TextView) findViewById(R.id.txt_firstname);
        TextComp = (TextView) findViewById(R.id.txt_company);
        TextBio = (TextView) findViewById(R.id.txt_bio);
        TextSkills = (TextView) findViewById(R.id.txt_skills);
        TextAdd = (TextView) findViewById(R.id.txt_address);
        TextSite = (TextView) findViewById(R.id.txt_website);
        TextPhone = (TextView) findViewById(R.id.txt_phone);
        TextEmail = (TextView) findViewById(R.id.txt_email);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_button:
                MemberDetailScreen.this.finish();
                break;
            default:
                break;
        }

    }


    private String TAG = "MemberDetailTag";

    @Override
    public void getData(final int actionID, final String... params) {
        if (!ConnectivityUtils.isNetworkEnabled(MemberDetailScreen.this)) {
            showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_internet));
            return;
        }
        showProgressDialog(getResources().getString(R.string.loading));

        switch (actionID) {
            case AppConstants.actions.GET_MEMBER_DETAIL:
                RequestManager.addRequest(new GsonObjectRequest<UserInfoModel>(
                        getMemberDetailUrl(mPref.getUserId(), params[0]), null, UserInfoModel.class,
                        new VolleyErrorListener(MemberDetailScreen.this, actionID)) {
                    @Override
                    protected void deliverResponse(UserInfoModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        MemberDetailScreen.this.updateUi(true, actionID, response);
                    }
                });
//


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
            case AppConstants.actions.GET_MEMBER_DETAIL: {
                Log.d(TAG, "response is success..........................");
                try {
                    UserInfoModel info = (UserInfoModel) serviceResponse;

                    AppUtils.showToast(MemberDetailScreen.this,
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());

                    txt_firstname.setText(info.getFirstname());
                    txt_lastname.setText(info.getLastname());
                    TextComp.setText(info.getCompany());
                    TextBio.setText(info.getBioGraphy());
                    TextSkills.setText(info.getSkills());
                    TextAdd.setText(info.getAddress());
                    TextSite.setText(info.getCompanyWebsite());
                    TextPhone.setText(info.getMobile());
                    TextEmail.setText(info.getEmailId());

                    headerTitle.setText(info.getFirstname() + " " + info.getLastname());

                    String profilePic = info.getProfileImage();
                    Log.d("Profile pic ", "" + profilePic);
                    if (profilePic != null && !profilePic.equals("")) {
                        Picasso.with(MemberDetailScreen.this).load(profilePic).resize(200,200).into(profile_pic);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
        }
    }

    private String getMemberDetailUrl(String uId, String memId) {
        return AppConstants.baseUrl + "getMemberDetails?userId=" + uId + "&requestedUserId=" + memId;
    }

    private void goBackScreen() {
        this.finish();
    }
}
