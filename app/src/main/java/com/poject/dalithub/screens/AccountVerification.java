package com.poject.dalithub.screens;

import android.content.Intent;
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

public class AccountVerification extends DalitHubBaseActivity implements OnClickListener {
    private ImageView leftTopButton, rightTopButton;
    private TextView headerTitle;
    private String TAG = "InviteMEmber";
    private EditText emailBox;
    private UserPreferences mPref;
    private TextView subInstruction;
    private boolean isLoginRequired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_layout);
        mPref = new UserPreferences(this);
        initViews();
        isLoginRequired = getIntent().getBooleanExtra("LoginRequired", false);
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
        headerTitle.setText("Account Verification");

        emailBox = (EditText) findViewById(R.id.email_id_box);
        subInstruction = (TextView) findViewById(R.id.subInstruction);
        subInstruction.setText("Check you email id for verification code.");
        emailBox.setHint("Verification code");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_button:
                goBackScreen();
                break;

            default:
                break;
        }

    }

    public void sendInvite(View v) {

        String verifyCode = emailBox.getText().toString();
        if (verifyCode != null && !verifyCode.equals("")) {
            getData(AppConstants.actions.VERIFY_ACCOUNT, mPref.getUserId(), verifyCode);

        } else {
            AppUtils.showToast(this, "Please enter your verification code.");
        }
    }

    @Override
    public void getData(final int actionID, String... params) {
        if (!ConnectivityUtils.isNetworkEnabled(this)) {
            showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_internet));
            return;
        }
        showProgressDialog(getResources().getString(R.string.loading));

        switch (actionID) {
            case AppConstants.actions.VERIFY_ACCOUNT:
                RequestManager.addRequest(new GsonObjectRequest<DalitHubBaseModel>(
                        getVerificationUrl(params[0], params[1]), null, DalitHubBaseModel.class,
                        new VolleyErrorListener(AccountVerification.this, actionID)) {
                    @Override
                    protected void deliverResponse(DalitHubBaseModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        AccountVerification.this.updateUi(true, actionID, response);
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
        } else if (((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("114")) {
            showAlert(getResources().getString(R.string.err_msg), ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            Log.e(TAG, "Message from Server : " + ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            return;
        }

        switch (action) {
            case AppConstants.actions.VERIFY_ACCOUNT: {
                Log.d(TAG, "response is success..........................");
                try {

                    DalitHubBaseModel response = (DalitHubBaseModel) serviceResponse;

                    AppUtils.showToast(AccountVerification.this,
                            response.getResponseDescription());
                    mPref.setAccountVerified(true);

                    if (isLoginRequired) {
                        this.finish();
                    } else {
                        Intent in = new Intent(this,
                                LandingScreenActivity.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                        this.finish();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
        }
    }

    private void goBackScreen() {
        this.finish();
    }

    private String getVerificationUrl(String userId, String code) {
        return AppConstants.baseUrl + "verifyuser?userId=" + userId + "&code=" + code + "&type=1";

    }
}
