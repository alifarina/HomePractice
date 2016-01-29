package com.poject.dalithub.screens;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.kelltontech.utils.ConnectivityUtils;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.poject.dalithub.Utils.AppConstants;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.R;
import com.poject.dalithub.application.AppController;
import com.poject.dalithub.listener.VolleyErrorListener;
import com.poject.dalithub.models.DalitHubBaseModel;
import com.poject.dalithub.models.SignUpModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SignUpActivity extends DalitHubBaseActivity implements LocationListener {
    private LinearLayout editBox_layout;
    private EditText emailBox, fNameBox, lastNameBox, passwordBox;
    private UserPreferences pref;

    private String TAG = "jsonRequestTag";
    private int DIALOG_YES_BUTTON = -1;
    private int DIALOG_NO_BUTTON = -2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        pref = new UserPreferences(this);

        setContentView(R.layout.signup_screen);
        initViews();

        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                this);
    }

    @SuppressLint("NewApi")
    private void initViews() {
        editBox_layout = (LinearLayout) findViewById(R.id.editBox_layout);
        editBox_layout.setAlpha(0.5f);

        emailBox = (EditText) findViewById(R.id.email_id);
        fNameBox = (EditText) findViewById(R.id.f_name);
        lastNameBox = (EditText) findViewById(R.id.username);
        passwordBox = (EditText) findViewById(R.id.password);

    }

    public void signupClick(View v) {

        String email_id = emailBox.getText().toString();
        String firstName = fNameBox.getText().toString();
        String lastName = lastNameBox.getText().toString();
        String password = passwordBox.getText().toString();

        if (TextUtils.isEmpty(email_id) || TextUtils.isEmpty(firstName)
                || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(password)) {

            AppUtils.showToast(this, "all fields need to be filled");
            return;
        }
        if (!AppUtils.isEmailAddress(email_id)) {
            AppUtils.showToast(this, "Please enter a valid email id");
            return;
        }


        if (!showGPSAlert())
            return;

        getData(AppConstants.actions.DO_SIGNUP);//makeRequestCall(email_id, firstName, username, password);

        // Intent in = new Intent(this, LandingScreenActivity.class);
        // in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
        // | Intent.FLAG_ACTIVITY_NEW_TASK);
        // startActivity(in);
        // this.finish();
    }

    private boolean showGPSAlert() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!statusOfGPS) {
            ((AppController) getApplication()).showGPSDialog(SignUpActivity.this, new DialogInterface.OnClickListener() {
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


    private void makeRequestCall(final String emailId, final String fname,
                                 final String lname, final String pass) {

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

		/*JsonObjectRequest signupReq = new JsonObjectRequest(Method.POST,
                getRegistrationUrl(emailId, fname, lname, pass), "",
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						pDialog.hide();
						handleResponse(response);

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						pDialog.hide();
					}
				}) {*/

        // @Override
        // protected Map<String, String> getParams() {
        // Map<String, String> params = new HashMap<String, String>();
        // params.put("emailId", emailId);
        // params.put("fname", fname);
        // params.put("lname", lname);
        // params.put("Password", pass);
        // params.put("type_of_registration", "1");
        // params.put("uniqueId", "");
        // params.put("userLong", "00.0000");
        // params.put("userLat", "00.0000");
        // params.put("deviceToken", "");
        // params.put("deviceUniqueId", "");
        // params.put("source", "2");
        //
        // return params;
        // }
        //};

        //AppController.getInstance().addToRequestQueue(signupReq, "signup_req");
    }

    public String getRegistrationUrl() {
        String email_id = emailBox.getText().toString();
        String firstName = fNameBox.getText().toString();
        String lastName = lastNameBox.getText().toString();
        String password = passwordBox.getText().toString();

        StringBuffer finalUrl = new StringBuffer();
        AppController appController = (AppController) getApplication();
        Location currentLocation = appController.getmLastLocation();
        if (currentLocation == null) {
            AppUtils.showToast(this, "unable to get location.Try again.");
            return null;
        }
        Log.d(TAG, "getRegistrationUrl LOCATION---->" + currentLocation.getLatitude() + "===" + currentLocation.getLongitude());
        try {

            finalUrl.append(AppConstants.baseUrl + "Registration?emailId="
                    + email_id
                    + "&fname="
                    + URLEncoder.encode(firstName, "UTF-8")
                    + "&lname="
                    + URLEncoder.encode(lastName, "UTF-8")
                    + "&Password="
                    + password
                    + "&type_of_registration=1&uniqueId=&userLong=" + currentLocation.getLongitude() + "&userLat=" + currentLocation.getLatitude() + "&deviceToken=&deviceUniqueId=&source=2");


            Log.d(TAG, "getRegistrationUrl--->" + finalUrl.toString());

            return finalUrl.toString();

            /*return AppConstants.baseUrl
                    + "Registration?emailId="
                    + email_id
                    + "&fname="
                    + URLEncoder.encode(firstName, "UTF-8")
                    + "&lname="
                    + URLEncoder.encode(lastName, "UTF-8")
                    + "&Password="
                    + password
                    + "&type_of_registration=1&uniqueId=&userLong=00.0000&userLat=00.0000&deviceToken=&deviceUniqueId=&source=2";
*/
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void handleResponse(JSONObject response) {
        try {
            if (response != null) {
                Log.d(TAG, response.toString());

                int resCode = response.getInt("responseCode");
                if (resCode == 103) {
                    // success
                    AppUtils.showToast(SignUpActivity.this,
                            response.getString("responseDescription"));

                    pref.saveUserId(response.getString("UserId"));

                    Intent in = new Intent(SignUpActivity.this,
                            LandingScreenActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);

                } else {

                    AppUtils.showToast(SignUpActivity.this,
                            response.getString("responseDescription"));
                }

            } else {
                AppUtils.showToast(SignUpActivity.this,
                        "something went wrong. Try again.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    double latitude, longitude;

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        System.out.println("SignUpActivity.onLocationChanged()");
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
        System.out.println("SignUpActivity.onStatusChanged()");
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        System.out.println("SignUpActivity.onProviderEnabled()");
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
        System.out.println("SignUpActivity.onProviderDisabled()");
    }

    @Override
    public void getData(final int actionID, String... s) {
        if (!ConnectivityUtils.isNetworkEnabled(SignUpActivity.this)) {
            showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_internet));
            return;
        }
        AppController appController = (AppController) getApplication();
        Location currentLocation = appController.getmLastLocation();
        if (currentLocation == null) {
            AppUtils.showToast(this, "unable to get location.Try again.");
            return;
        }
        showProgressDialog(getResources().getString(R.string.loading));

        switch (actionID) {
            case AppConstants.actions.DO_SIGNUP:
                RequestManager.addRequest(new GsonObjectRequest<SignUpModel>(
                        getRegistrationUrl(), null, SignUpModel.class,
                        new VolleyErrorListener(SignUpActivity.this, actionID)) {
                    @Override
                    protected void deliverResponse(SignUpModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        SignUpActivity.this.updateUi(true, actionID, response);
                    }
                });
                break;
            default:
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
        } else if (!((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("103")) {
            showAlert(getResources().getString(R.string.err_msg), ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            Log.e(TAG, "Message from Server : " + ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            return;
        }

        switch (action) {
            case AppConstants.actions.DO_SIGNUP: {
                Log.d(TAG, "response is success..........................");
                try {
                    SignUpModel response = (SignUpModel) serviceResponse;

                    AppUtils.showToast(SignUpActivity.this,
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());


                    pref.saveUserId(response.getUserId());

//                    Intent in = new Intent(SignUpActivity.this,
//                            LandingScreenActivity.class);
//                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
//                            | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(in);

                    Intent in = new Intent(SignUpActivity.this,
                            AccountVerification.class);
//                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
//                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    this.finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }
}