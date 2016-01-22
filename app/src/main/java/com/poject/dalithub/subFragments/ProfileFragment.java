package com.poject.dalithub.subFragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.poject.dalithub.models.UserInfoModel;
import com.poject.dalithub.screens.LandingScreenActivity;
import com.poject.dalithub.screens.SettingsActivity;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ProfileFragment extends DalitHubBasefragment implements OnClickListener {
    LandingScreenActivity activity;
    UserPreferences mPref;
    private LinearLayout editCompleteLayout;
    private TextView button_edit, buttonDone, buttonCancel;
    private EditText editTextFirstName, editTextComp, editTextBio, editTextSkills, editTextAdd, editTextPhone, editTextSite, editTextEmail;
    private TextView textFirstName, TextComp, TextBio, TextSkills, TextAdd, TextPhone, TextSite, TextEmail;
    private UserInfoModel userInfo;
    private ImageView profile_pic;
    private EditText editTextLastName;
    private TextView textLastName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_screen, null);
        initViews(view);
        setScreenListeners();

        mPref = new UserPreferences(getLandingActivityContext());
        Log.d("User id ", mPref.getUserId());

        getData(AppConstants.actions.GET_PERSONAL_INFO, mPref.getUserId());

        return view;
    }

    private void initViews(View view) {
        editCompleteLayout = (LinearLayout) view.findViewById(R.id.doneLayout);
        button_edit = (TextView) view.findViewById(R.id.btn_edit);
        buttonDone = (TextView) view.findViewById(R.id.btn_done);
        buttonCancel = (TextView) view.findViewById(R.id.btn_cancel);

        editTextFirstName = (EditText) view.findViewById(R.id.edit_firstname);
        editTextLastName = (EditText) view.findViewById(R.id.edt_lastname);
        editTextComp = (EditText) view.findViewById(R.id.edt_company);
        editTextBio = (EditText) view.findViewById(R.id.edt_bio);
        editTextSkills = (EditText) view.findViewById(R.id.edt_skills);
        editTextAdd = (EditText) view.findViewById(R.id.edt_address);
        editTextSite = (EditText) view.findViewById(R.id.edt_website);
        editTextPhone = (EditText) view.findViewById(R.id.edt_phone);
        editTextEmail = (EditText) view.findViewById(R.id.edt_email);

        textFirstName = (TextView) view.findViewById(R.id.txt_firstname);
        textLastName = (TextView) view.findViewById(R.id.txt_lastname);
        TextComp = (TextView) view.findViewById(R.id.txt_company);
        TextBio = (TextView) view.findViewById(R.id.txt_bio);
        TextSkills = (TextView) view.findViewById(R.id.txt_skills);
        TextAdd = (TextView) view.findViewById(R.id.txt_address);
        TextSite = (TextView) view.findViewById(R.id.txt_website);
        TextPhone = (TextView) view.findViewById(R.id.txt_phone);
        TextEmail = (TextView) view.findViewById(R.id.txt_email);

        profile_pic = (ImageView) view.findViewById(R.id.profile_pic);


    }


    private void setScreenListeners() {
        getLandingActivityContext().setHeaderButtonListeners(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.left_button) {
                    System.out.println("left click");
                    getData(AppConstants.actions.GET_PERSONAL_INFO, mPref.getUserId());
                } else if (v.getId() == R.id.right_button) {
                    System.out.println("right click");

                    startActivity(new Intent(getLandingActivityContext(), SettingsActivity.class));
                }

            }
        });

        button_edit.setOnClickListener(this);
        buttonDone.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    private String TAG = "ProfileInfoTag";

    @Override
    public void getData(final int actionID, String... params) {
        if (!ConnectivityUtils.isNetworkEnabled(getLandingActivityContext())) {
            getLandingActivityContext().showAlert(getLandingActivityContext().getResources().getString(R.string.err_msg),
                    getLandingActivityContext().getResources().getString(R.string.error_internet));
            return;
        }
        getLandingActivityContext().showProgressDialog(getLandingActivityContext().getResources().getString(R.string.loading));

        switch (actionID) {
            case AppConstants.actions.GET_PERSONAL_INFO:
                RequestManager.addRequest(new GsonObjectRequest<UserInfoModel>(
                        getProfileUrl(params[0]), null, UserInfoModel.class,
                        new VolleyErrorListener(ProfileFragment.this, actionID)) {
                    @Override
                    protected void deliverResponse(UserInfoModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        ProfileFragment.this.updateUi(true, actionID, response);
                    }
                });
                break;
            case AppConstants.actions.UPDATE_PERSONAL_INFO:
                RequestManager.addRequest(new GsonObjectRequest<DalitHubBaseModel>(
                        updateInfoUrl(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8]), null, DalitHubBaseModel.class,
                        new VolleyErrorListener(ProfileFragment.this, actionID)) {
                    @Override
                    protected void deliverResponse(DalitHubBaseModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        ProfileFragment.this.updateUi(true, actionID, response);
                    }
                });
                break;
        }
    }

    @Override
    public void updateUi(boolean status, int action, Object serviceResponse) {
        getLandingActivityContext().hideProgressDialog();

        Log.e(TAG, " updateUi ::Service Response : " + serviceResponse);

        //If unable to process the request
        if (!status && serviceResponse instanceof String) {
            getLandingActivityContext().showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.request_fail));
            Log.e(TAG, "Problem with Server Response");
            return;
        }

        //If not valid response
        if (!(serviceResponse instanceof DalitHubBaseModel)) {
            getLandingActivityContext().showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_unknown));
            Log.e(TAG, "Server Response is not instance of BaseModel");
            return;
        } else if (!((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("124")
                && !((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("123")) {
            getLandingActivityContext().showAlert(getResources().getString(R.string.err_msg), ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            Log.e(TAG, "Message from Server : " + ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            return;
        }

        switch (action) {
            case AppConstants.actions.GET_PERSONAL_INFO: {
                Log.d(TAG, "response is success..........................");
                try {
                    UserInfoModel response = (UserInfoModel) serviceResponse;

                    AppUtils.showToast(getLandingActivityContext(),
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());
                    if (response != null) {
                        setDataOnUI(response);
                        setUserInfo(response);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
            case AppConstants.actions.UPDATE_PERSONAL_INFO: {
                Log.d(TAG, "response is success..........................");
                try {
                    DalitHubBaseModel response = (DalitHubBaseModel) serviceResponse;

                    AppUtils.showToast(getLandingActivityContext(),
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());
                    getData(AppConstants.actions.GET_PERSONAL_INFO, mPref.getUserId());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
        }
    }

    private void setDataOnUI(UserInfoModel info) {

        editTextFirstName.setVisibility(View.GONE);
        editTextLastName.setVisibility(View.GONE);
        editTextComp.setVisibility(View.GONE);
        editTextBio.setVisibility(View.GONE);
        editTextSkills.setVisibility(View.GONE);
        editTextAdd.setVisibility(View.GONE);
        editTextSite.setVisibility(View.GONE);
        editTextPhone.setVisibility(View.GONE);
        editTextEmail.setVisibility(View.GONE);

        textFirstName.setVisibility(View.VISIBLE);
        textLastName.setVisibility(View.VISIBLE);
        TextComp.setVisibility(View.VISIBLE);
        TextBio.setVisibility(View.VISIBLE);
        TextSkills.setVisibility(View.VISIBLE);
        TextAdd.setVisibility(View.VISIBLE);
        TextSite.setVisibility(View.VISIBLE);
        TextPhone.setVisibility(View.VISIBLE);
        TextEmail.setVisibility(View.VISIBLE);

        textFirstName.setText(info.getFirstname());
        textLastName.setText(info.getLastname());
        TextComp.setText(info.getCompany());
        TextBio.setText(info.getBioGraphy());
        TextSkills.setText(info.getSkills());
        TextAdd.setText(info.getAddress());
        TextSite.setText(info.getCompanyWebsite());
        TextPhone.setText(info.getMobile());
        TextEmail.setText(info.getEmailId());

        String profilePic = info.getProfileImage();
        if (profilePic != null && !profilePic.equals("")) {
            Picasso.with(getLandingActivityContext()).load(profilePic).into(profile_pic);
        }
    }

    private String getProfileUrl(String user_id) {
        return AppConstants.baseUrl + "getUserDetails?userId=" + user_id;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_edit:
                performEdit();
                break;
            case R.id.btn_cancel:
                cancelEdit();
                break;
            case R.id.btn_done:
                doneProfileEdit();
                break;

        }

    }

    private void cancelEdit() {
        editCompleteLayout.setVisibility(View.GONE);
        button_edit.setVisibility(View.VISIBLE);

        editTextFirstName.setVisibility(View.GONE);
        editTextLastName.setVisibility(View.GONE);
        editTextComp.setVisibility(View.GONE);
        editTextBio.setVisibility(View.GONE);
        editTextSkills.setVisibility(View.GONE);
        editTextAdd.setVisibility(View.GONE);
        editTextSite.setVisibility(View.GONE);
        editTextPhone.setVisibility(View.GONE);
        editTextEmail.setVisibility(View.GONE);

        textFirstName.setVisibility(View.VISIBLE);
        textLastName.setVisibility(View.VISIBLE);
        TextComp.setVisibility(View.VISIBLE);
        TextBio.setVisibility(View.VISIBLE);
        TextSkills.setVisibility(View.VISIBLE);
        TextAdd.setVisibility(View.VISIBLE);
        TextSite.setVisibility(View.VISIBLE);
        TextPhone.setVisibility(View.VISIBLE);
        TextEmail.setVisibility(View.VISIBLE);
    }

    private void doneProfileEdit() {
        editCompleteLayout.setVisibility(View.GONE);
        button_edit.setVisibility(View.VISIBLE);

        //getting updated data
        String fname = editTextFirstName.getText().toString();
        String lname = editTextLastName.getText().toString();
        String company = editTextComp.getText().toString();
        String bio = editTextBio.getText().toString();
        String skills = editTextSkills.getText().toString();
        String address = editTextAdd.getText().toString();
        String site = editTextSite.getText().toString();
        String phone = editTextPhone.getText().toString();
        String email = editTextEmail.getText().toString();

        try {
            getData(AppConstants.actions.UPDATE_PERSONAL_INFO, fname, lname, URLEncoder.encode(company, "utf-8"), site, phone, URLEncoder.encode(bio, "utf-8"),
                    URLEncoder.encode(skills, "utf-8"), URLEncoder.encode(address, "utf-8"), mPref.getUserId());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    private void performEdit() {
        editCompleteLayout.setVisibility(View.VISIBLE);
        button_edit.setVisibility(View.GONE);

        editTextFirstName.setVisibility(View.VISIBLE);
        editTextLastName.setVisibility(View.VISIBLE);
        editTextComp.setVisibility(View.VISIBLE);
        editTextBio.setVisibility(View.VISIBLE);
        editTextSkills.setVisibility(View.VISIBLE);
        editTextAdd.setVisibility(View.VISIBLE);
        editTextSite.setVisibility(View.VISIBLE);
        editTextPhone.setVisibility(View.VISIBLE);
        editTextEmail.setVisibility(View.VISIBLE);

        textFirstName.setVisibility(View.GONE);
        textLastName.setVisibility(View.GONE);
        TextComp.setVisibility(View.GONE);
        TextBio.setVisibility(View.GONE);
        TextSkills.setVisibility(View.GONE);
        TextAdd.setVisibility(View.GONE);
        TextSite.setVisibility(View.GONE);
        TextPhone.setVisibility(View.GONE);
        TextEmail.setVisibility(View.GONE);

        if (userInfo != null)
            setDataForEdit(userInfo);
    }

    private void setDataForEdit(UserInfoModel info) {
        editTextFirstName.setText(info.getFirstname());
        editTextLastName.setText(info.getLastname());
        editTextComp.setText(info.getCompany());
        editTextBio.setText(info.getBioGraphy());
        editTextSkills.setText(info.getSkills());
        editTextAdd.setText(info.getAddress());
        editTextSite.setText(info.getCompanyWebsite());
        editTextPhone.setText(info.getMobile());
        editTextEmail.setText(info.getEmailId());
    }

    public UserInfoModel getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoModel userInfo) {
        this.userInfo = userInfo;
    }

    private String updateInfoUrl(String fname, String lname, String company, String site, String mbl, String bio, String skills, String add, String userId) {
        return AppConstants.baseUrl + "updateuserdetails?firstname=" + fname + "&lastname=" + lname + "&company=" + company + "&companyWebsite=" +
                site + "&mobile=" + mbl + "&biography=" + bio + "&skills=" + skills + "&address=" + add + "&userId=" + userId;
    }
}
