package com.poject.dalithub.screens;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kelltontech.utils.ConnectivityUtils;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.poject.dalithub.Utils.AppConstants;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.R;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.listener.VolleyErrorListener;
import com.poject.dalithub.models.DalitHubBaseModel;
import com.poject.dalithub.models.UserInfoModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SettingsActivity extends DalitHubBaseActivity implements OnClickListener, CompoundButton.OnCheckedChangeListener {
    private ImageView leftTopButton, rightTopButton;
    private TextView headerTitle;
    private Dialog feedsDialog, notification_dialog, privacy_dialog;
    private String TAG = "settingTag";
    private UserPreferences mPref;

    public String getMobileStatus() {
        return mobileStatus;
    }

    public void setMobileStatus(String mobileStatus) {
        this.mobileStatus = mobileStatus;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getCompStatus() {
        return compStatus;
    }

    public void setCompStatus(String compStatus) {
        this.compStatus = compStatus;
    }

    public String getOtherStatus() {
        return otherStatus;
    }

    public void setOtherStatus(String otherStatus) {
        this.otherStatus = otherStatus;
    }

    private String mobileStatus = "0", emailStatus = "0", compStatus = "0", otherStatus = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        mPref = new UserPreferences(this);
        // userInfo = (UserInfoModel) getIntent().getSerializableExtra("user_info");

        AppUtils.updateDeviceResolution(this);
        initViews();
        setScreenListeners();
    }

    private void setScreenListeners() {
        leftTopButton.setOnClickListener(this);
    }

    private void initViews() {
        leftTopButton = (ImageView) findViewById(R.id.left_button);
        rightTopButton = (ImageView) findViewById(R.id.right_button);
        headerTitle = (TextView) findViewById(R.id.title);

        rightTopButton.setVisibility(View.GONE);
        headerTitle.setText("SETTINGS");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_button:
                goBackScreen();
                break;
            case R.id.feedback_click:
                feedsDialog();
                break;
            case R.id.privacy_click:
                showPrivacyDialog();
                //showNotificationDialog();
                break;
            case R.id.change_pass:
                startActivity(new Intent(SettingsActivity.this,
                        ChangePasswordActivity.class));
                break;
            case R.id.logout_click:

                loggoutFromApp();

                break;
            case R.id.ok_button:

                if (notification_dialog != null)
                    notification_dialog.cancel();

                break;
            case R.id.cancel_button:

                if (notification_dialog != null)
                    notification_dialog.cancel();

                break;
            default:
                break;
        }

    }

    private void sendFeedback(View v) {

        EditText feedBox = (EditText) v;
        String feed = feedBox.getText().toString();

        if (feed != null && !feed.equals("")) {
            try {
                getData(AppConstants.actions.SEND_FEEDBACK, mPref.getUserId(), URLEncoder.encode(feed, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            AppUtils.showToast(SettingsActivity.this, "Please give your feedback.");
        }
    }

    private void showPrivacyDialog() {
        privacy_dialog = new Dialog(this);
        privacy_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        privacy_dialog.setCancelable(false);
        privacy_dialog.setContentView(R.layout.privacy_setting_dialog);

        TextView cancel = (TextView) privacy_dialog
                .findViewById(R.id.cancel_button);
        TextView ok = (TextView) privacy_dialog
                .findViewById(R.id.ok_button);

        CheckBox check_mbl = (CheckBox) privacy_dialog.findViewById(R.id.checkbox_mbl);
        CheckBox check_email = (CheckBox) privacy_dialog.findViewById(R.id.checkbox_email);
        CheckBox check_comp = (CheckBox) privacy_dialog.findViewById(R.id.checkbox_comp);
        CheckBox check_other = (CheckBox) privacy_dialog.findViewById(R.id.checkbox_other);

        check_email.setChecked(mPref.showEmailStatus());
        check_mbl.setChecked(mPref.showMobileStatus());
        check_comp.setChecked(mPref.showCompStatus());
        check_other.setChecked(mPref.showOtherStatus());

        check_mbl.setOnCheckedChangeListener(this);
        check_email.setOnCheckedChangeListener(this);
        check_comp.setOnCheckedChangeListener(this);
        check_other.setOnCheckedChangeListener(this);

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                privacy_dialog.dismiss();
            }
        });
        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(AppConstants.actions.UPDATE_PRIVACY, mPref.getUserId(),
                        getMobileStatus(), getEmailStatus(), getCompStatus(), getOtherStatus());
            }
        });

        privacy_dialog.show();

    }

    private void loggoutFromApp() {

        //removing user credentials
        mPref.saveUserId("");
        mPref.setAccountVerified(false);


        Intent newTask = new Intent(SettingsActivity.this,
                LoginSignupChooserActivity.class);
        newTask.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(newTask);
    }

    private void showNotificationDialog() {
        notification_dialog = new Dialog(this);
        notification_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        notification_dialog.setCancelable(false);
        notification_dialog.setContentView(R.layout.notification_dialog);

        TextView cancel = (TextView) notification_dialog
                .findViewById(R.id.cancel_button);
        TextView ok = (TextView) notification_dialog
                .findViewById(R.id.ok_button);

        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);

        notification_dialog.show();

    }

    private void feedsDialog() {
        feedsDialog = new Dialog(this);
        feedsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        feedsDialog.setCancelable(false);
        feedsDialog.setContentView(R.layout.feedback_dialog);

        TextView cancel = (TextView) feedsDialog
                .findViewById(R.id.cancel_button);
        TextView ok = (TextView) feedsDialog.findViewById(R.id.ok_button);

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                feedsDialog.dismiss();
            }
        });
        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback(feedsDialog.findViewById(R.id.feedBox));
            }
        });

        feedsDialog.show();

    }

    // LinearLayout mainLayout = (LinearLayout) feedsDialog
    // .findViewById(R.id.feed_layout_main);
    // RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
    // mainLayout
    // .getLayoutParams();
    // params.width = (int) (AppUtils.DEVICE_WIDTH * 0.6f);

    private void goBackScreen() {
        this.finish();
    }

    private String feedbackUrl(String userId, String feeds) {
        return AppConstants.baseUrl + "sendfeedback?UserId=" + userId + "&feedback=" + feeds;
    }

    @Override
    public void getData(final int actionID, String... params) {
        if (!ConnectivityUtils.isNetworkEnabled(this)) {
            showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_internet));
            return;
        }
        showProgressDialog(getResources().getString(R.string.loading));

        switch (actionID) {
            case AppConstants.actions.SEND_FEEDBACK:
                RequestManager.addRequest(new GsonObjectRequest<DalitHubBaseModel>(
                        feedbackUrl(params[0], params[1]), null, DalitHubBaseModel.class,
                        new VolleyErrorListener(
                                SettingsActivity.this, actionID)) {
                    @Override
                    protected void deliverResponse(DalitHubBaseModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        SettingsActivity.this.updateUi(true, actionID, response);
                    }
                });
                break;
            case AppConstants.actions.UPDATE_PRIVACY:
                RequestManager.addRequest(new GsonObjectRequest<DalitHubBaseModel>(
                        updatePrivacyUrl(params[0], params[1], params[2], params[3], params[4]), null, DalitHubBaseModel.class,
                        new VolleyErrorListener(
                                SettingsActivity.this, actionID)) {
                    @Override
                    protected void deliverResponse(DalitHubBaseModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        SettingsActivity.this.updateUi(true, actionID, response);
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
        } else if (!((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("127") &&
                !((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("123")) {
            showAlert(getResources().getString(R.string.err_msg), ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            Log.e(TAG, "Message from Server : " + ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            return;
        }

        switch (action) {
            case AppConstants.actions.SEND_FEEDBACK: {
                Log.d(TAG, "response is success..........................");
                try {
                    DalitHubBaseModel response = (DalitHubBaseModel) serviceResponse;

                    AppUtils.showToast(SettingsActivity.this,
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());
                    if (feedsDialog != null && feedsDialog.isShowing()) {
                        feedsDialog.cancel();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
            case AppConstants.actions.UPDATE_PRIVACY: {
                Log.d(TAG, "response is success..........................");
                try {
                    DalitHubBaseModel response = (DalitHubBaseModel) serviceResponse;

                    AppUtils.showToast(SettingsActivity.this,
                            response.getResponseDescription());
                    if (privacy_dialog != null && privacy_dialog.isShowing()) {
                        privacy_dialog.cancel();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
        }
    }

    private String updatePrivacyUrl(String userId, String mbl_status, String mail_status, String comp_status, String other_status) {
        return AppConstants.baseUrl + "updateuserprivacy?userId=" + userId + "&showMobile=" + mbl_status + "&showeMail=" + mail_status +
                "&showCompanyDetails=" + comp_status + "&showOtherDetails=" + other_status;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.checkbox_mbl:
                setMobileStatus(isChecked ? "1" : "0");
                mPref.setMobileStatus(isChecked);
                break;
            case R.id.checkbox_other:
                setOtherStatus(isChecked ? "1" : "0");
                mPref.setOtherStatus(isChecked);
                break;
            case R.id.checkbox_comp:
                setCompStatus(isChecked ? "1" : "0");
                mPref.setCompStatus(isChecked);
                break;
            case R.id.checkbox_email:
                setEmailStatus(isChecked ? "1" : "0");
                mPref.setEmailStatus(isChecked);
                break;
        }
    }
}
