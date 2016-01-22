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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SettingsActivity extends DalitHubBaseActivity implements OnClickListener {
    private ImageView leftTopButton, rightTopButton;
    private TextView headerTitle;
    private Dialog feedsDialog, notification_dialog, privacy_dialog;
    private String TAG = "settingTag";
    private UserPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        mPref = new UserPreferences(this);
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
                if (feedsDialog != null) {
                    sendFeedback((View)v.getParent());
                    feedsDialog.cancel();
                }
                if (notification_dialog != null)
                    notification_dialog.cancel();
                if (privacy_dialog != null)
                    privacy_dialog.cancel();
                break;
            case R.id.cancel_button:
                if (feedsDialog != null)
                    feedsDialog.cancel();
                if (notification_dialog != null)
                    notification_dialog.cancel();
                if (privacy_dialog != null)
                    privacy_dialog.cancel();
                break;
            default:
                break;
        }

    }

    private void sendFeedback(View v) {

        EditText feedBox = (EditText) v.findViewById(R.id.feedBox);
        String feed = feedBox.getText().toString();

        if (feed != null && !feed.equals("")) {
            try {
                getData(AppConstants.actions.SEND_FEEDBACK, mPref.getUserId(), URLEncoder.encode(feed, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
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

        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);

        privacy_dialog.show();

    }

    private void loggoutFromApp() {

        //removing user credentials
        new UserPreferences(this).saveUserId("");

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

        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);

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
        }
    }

    @Override
    public void updateUi(boolean status, int action, Object serviceResponse) {
        super.updateUi(status, action, serviceResponse);
    }

    private String updatePrivacyUrl() {
        return AppConstants.baseUrl+"updateuserprivacy?userId=2&showMobile=1&showeMail=1&showCompanyDetails=1&showOtherDetails=1";
    }
}
