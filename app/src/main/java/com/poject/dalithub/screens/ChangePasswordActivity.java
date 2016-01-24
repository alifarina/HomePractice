package com.poject.dalithub.screens;

import com.kelltontech.utils.ConnectivityUtils;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.poject.dalithub.R;
import com.poject.dalithub.Utils.AppConstants;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.listener.VolleyErrorListener;
import com.poject.dalithub.models.DalitHubBaseModel;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ChangePasswordActivity extends DalitHubBaseActivity implements OnClickListener {
    private ImageView leftTopButton, rightTopButton;
    private TextView headerTitle;
    private EditText newPassBox, currentPassBox;
    private EditText confirmBox;
    private String TAG = "changePassTag";
    private UserPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pass_layout);
        mPref = new UserPreferences(this);
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

        newPassBox = (EditText) findViewById(R.id.newPassBox);
        confirmBox = (EditText) findViewById(R.id.confirmBox);
        currentPassBox = (EditText) findViewById(R.id.currentPassBox);

        rightTopButton.setVisibility(View.GONE);
        headerTitle.setText("CHANGE PASSWORD");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_button:
                goBackScreen();
                break;
            case R.id.ok_button:
                confirmChangePassword();
                break;
            case R.id.cancel_button:
                goBackScreen();
                break;
            default:
                break;
        }

    }

    private void confirmChangePassword() {

        String newPass = newPassBox.getText().toString();
        String confirmPass = confirmBox.getText().toString();
        String oldPass = currentPassBox.getText().toString();

        if (oldPass == null || oldPass.equals("")) {
            AppUtils.showToast(this, "Please enter your present password");
            return;
        }
        if (newPass == null || newPass.equals("")) {
            AppUtils.showToast(this, "Please enter new password");
            return;
        }
        if (confirmPass == null || confirmPass.equals("")) {
            AppUtils.showToast(this, "Please verify new password");
            return;
        }
        if (!newPass.equals(confirmPass)) {
            AppUtils.showToast(this, "passwords mismatch!!");
            return;
        }

        getData(AppConstants.actions.CHANGE_PASSWORD, mPref.getUserId(), oldPass, newPass);

    }

    private void goBackScreen() {
        this.finish();
    }

    private String changePassUrl(String userId, String currentPass, String newPass) {
        return AppConstants.baseUrl + "changepassword?UserId=" + userId + "&currentPassword="
                + currentPass + "&NewPassword=" + newPass;
    }

    @Override
    public void getData(final int actionID, String... params) {
        if (!ConnectivityUtils.isNetworkEnabled(this)) {
            showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_internet));
            return;
        }
        showProgressDialog(getResources().getString(R.string.loading));

        switch (actionID) {
            case AppConstants.actions.CHANGE_PASSWORD:
                RequestManager.addRequest(new GsonObjectRequest<DalitHubBaseModel>(
                        changePassUrl(params[0], params[1], params[2]), null, DalitHubBaseModel.class,
                        new VolleyErrorListener(ChangePasswordActivity.this, actionID)) {
                    @Override
                    protected void deliverResponse(DalitHubBaseModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        ChangePasswordActivity.this.updateUi(true, actionID, response);
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
        } else if (!((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("126")) {
            showAlert(getResources().getString(R.string.err_msg), ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            Log.e(TAG, "Message from Server : " + ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            return;
        }

        switch (action) {
            case AppConstants.actions.CHANGE_PASSWORD: {
                Log.d(TAG, "response is success..........................");
                try {
                    DalitHubBaseModel response = (DalitHubBaseModel) serviceResponse;

                    AppUtils.showToast(ChangePasswordActivity.this,
                            response.getResponseDescription());
                    ChangePasswordActivity.this.finish();


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
        }
    }
}
