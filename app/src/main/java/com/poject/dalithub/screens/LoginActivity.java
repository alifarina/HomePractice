package com.poject.dalithub.screens;
/**
 * Test email ids for login
 * >>>>>email =  user_one@gmail.com
 * >>>>>first name = frist name
 * >>>>>Last name = Last name
 * >>>>>password =  123456
 **/

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Resources.Theme;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.FacebookException;
import com.facebook.UiLifecycleHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;
import com.kelltontech.utils.ConnectivityUtils;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.poject.dalithub.Utils.AppConstants;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.application.AppController;
import com.poject.dalithub.R;
import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Person;
import com.poject.dalithub.facebook.listener.FacebookLoginListener;
import com.poject.dalithub.facebook.model.FacebookUtils;
import com.poject.dalithub.facebook.model.UserInfo;
import com.poject.dalithub.linkedin.LinkedinLoginModel;
import com.poject.dalithub.listener.VolleyErrorListener;
import com.poject.dalithub.models.DalitHubBaseModel;
import com.poject.dalithub.models.LoginModel;
import com.poject.dalithub.models.SignUpModel;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LoginActivity extends DalitHubBaseActivity implements OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private UiLifecycleHelper mUiHelper;

    private UserInfo mUserInfo;


    String linkedinData;
    String firstName, lastName, email;
    String linkedInHeadline, linkedInIndustry, linkedExperience, linkedInSummary;
    String linkedInImageURL, linkedInProfileUri;

    private static final int RC_SIGN_IN = 0;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;

    private ConnectionResult mConnectionResult;

    LinearLayout editBox_layout;

    private EditText emailBox, passwordBox;

    private LinkedInOAuthService oAuthService;
    private LinkedInApiClientFactory factory;
    private LinkedInRequestToken liToken;
    private LinkedInApiClient client;
    private boolean mSignInClicked;
    private UserPreferences mPref;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        mPref = new UserPreferences(LoginActivity.this);

        mUiHelper = new UiLifecycleHelper(this, FacebookUtils.callback);
        mUiHelper.onCreate(savedInstanceState);

        //Google+ SDK initialisation
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();


        setContentView(R.layout.login_screen_layout);
        initViews();

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> listProviders = locationManager.getAllProviders();
        // if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        } else if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
        for (int i = 0; i < listProviders.size(); i++) {

            if (locationManager.isProviderEnabled(listProviders.get(i))) {

                location = locationManager.getLastKnownLocation(listProviders.get(i));
                if (location == null) {
                    // request for a single update, and try again.
                    // Later will request for updates every 10 mins

                }
                break;
            }
        }
        //  location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        // }

    }


    private void initViews() {
        editBox_layout = (LinearLayout) findViewById(R.id.editBox_layout);
        editBox_layout.setAlpha(0.5f);

        emailBox = (EditText) findViewById(R.id.email_id);
        passwordBox = (EditText) findViewById(R.id.password);

//        emailBox.setText("eavinash0816@gmail.com");
//        passwordBox.setText("Avi");

    }

    public void loginButtonClick(View v) {

        String email_id = emailBox.getText().toString();
        String password = passwordBox.getText().toString();

        if (TextUtils.isEmpty(email_id) || TextUtils.isEmpty(password)) {

            AppUtils.showToast(this, "all fields need to be filled");
            return;
        }
        if (!AppUtils.isEmailAddress(email_id)) {
            AppUtils.showToast(this, "Please enter a valid email id");
            return;
        }

        // makeRequestCall(email_id, password);
        getData(AppConstants.actions.DO_LOGIN_MANUAL);
        // this.finish();
    }

    private String TAG = "loginRequestTag";

    private void makeRequestCall(final String emailId, final String pass) {

        final ProgressDialog pDialog = new ProgressDialog(this, ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pDialog.setMessage("Loading...");
        pDialog.show();

		/*JsonObjectRequest loginReq = new JsonObjectRequest(Method.POST,
                getLoginUrl(emailId, pass), "",
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						pDialog.hide();
						LoginResponse(response);

					}

				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						VolleyLog.d(TAG, "Error: " + error.getMessage());
						pDialog.hide();
					}
				}) {

		};*/

        //AppController.getInstance().addToRequestQueue(loginReq, "login_req");
    }

    @Override
    public void getData(final int actionID, String... params) {
        if (!ConnectivityUtils.isNetworkEnabled(LoginActivity.this)) {
            showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_internet));
            return;
        }
        showProgressDialog(getResources().getString(R.string.loading));

        switch (actionID) {
            case AppConstants.actions.DO_LOGIN_MANUAL:
                RequestManager.addRequest(new GsonObjectRequest<LoginModel>(
                        getLoginUrl(), null, LoginModel.class,
                        new VolleyErrorListener(LoginActivity.this, actionID)) {
                    @Override
                    protected void deliverResponse(LoginModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        LoginActivity.this.updateUi(true, actionID, response);
                    }
                });
                break;
            case AppConstants.actions.DO_LOGIN_GOOGLE:
            case AppConstants.actions.DO_LOGIN_FB:
            case AppConstants.actions.DO_LOGIN_LINKEDIN:
                try {
                    RequestManager.addRequest(new GsonObjectRequest<SignUpModel>(
                            getRegistrationUrl(params[0], URLEncoder.encode(params[1], "UTF-8"), URLEncoder.encode(params[2], "UTF-8"), params[3], params[4]), null, SignUpModel.class,
                            new VolleyErrorListener(LoginActivity.this, actionID)) {
                        @Override
                        protected void deliverResponse(SignUpModel response) {
                            if (mResponse != null && mResponse.data != null) {
                                String data = new String(mResponse.data);
                                Log.d(TAG, "response json--->" + data);
                            }

                            LoginActivity.this.updateUi(true, actionID, response);
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                break;
            case AppConstants.actions.FORGOT_PASSWORD:
                RequestManager.addRequest(new GsonObjectRequest<DalitHubBaseModel>(
                        getForgotPassUrl(params[0]), null, DalitHubBaseModel.class,
                        new VolleyErrorListener(LoginActivity.this, actionID)) {
                    @Override
                    protected void deliverResponse(DalitHubBaseModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        LoginActivity.this.updateUi(true, actionID, response);
                    }
                });
                break;
            default:
                break;
        }
    }

    private String getLoginUrl() {
        String email_id = emailBox.getText().toString();
        String password = passwordBox.getText().toString();

        String tempUrl = AppConstants.baseUrl + "Login?emailId=" + email_id + "&Password=" + password;
        if (location != null) {
            tempUrl = tempUrl + "&userLong=" + location.getLongitude() + "&userLat=" +
                    location.getLatitude();
        }
        tempUrl = tempUrl + "&deviceToken=&deviceUniqueId=";
        Log.d("getLoginUrl", tempUrl);
        return tempUrl;

    }

    public String getRegistrationUrl(String email, String firstName, String lastName, String password, String type) {

        try {
            return AppConstants.baseUrl
                    + "Registration?emailId="
                    + email
                    + "&fname="
                    + URLEncoder.encode(firstName, "UTF-8")
                    + "&lname="
                    + URLEncoder.encode(lastName, "UTF-8")
                    + "&Password="
                    + password
                    + "&type_of_registration=" + type + "&uniqueId=&userLong=00.0000&userLat=00.0000&deviceToken=&deviceUniqueId=&source=2";

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
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
        } else if (!((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("110") && !((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("108") && !((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("103")) {
            showAlert(getResources().getString(R.string.err_msg), ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            Log.e(TAG, "Message from Server : " + ((DalitHubBaseModel) serviceResponse).getResponseDescription() + " " + ((DalitHubBaseModel) serviceResponse).getResponseCode());
            return;
        }

        switch (action) {
            case AppConstants.actions.DO_LOGIN_MANUAL: {
                Log.d(TAG, "response is success..........................");
                try {
                    LoginModel response = (LoginModel) serviceResponse;

                    AppUtils.showToast(LoginActivity.this,
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());


                    mPref.saveUserId(response.getUserId());
                    mPref.setIsAdmin(response.getIsAdmin());

                    Intent in = new Intent(LoginActivity.this,
                            LandingScreenActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
            case AppConstants.actions.DO_LOGIN_GOOGLE:
            case AppConstants.actions.DO_LOGIN_LINKEDIN:
            case AppConstants.actions.DO_LOGIN_FB:
                Log.d(TAG, "response is success..........................");
                try {
                    SignUpModel response = (SignUpModel) serviceResponse;

                    AppUtils.showToast(LoginActivity.this,
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());


                    mPref.saveUserId(response.getUserId());

                    Intent in = new Intent(LoginActivity.this,
                            LandingScreenActivity.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case AppConstants.actions.FORGOT_PASSWORD:
                DalitHubBaseModel forgot_pass_response = (DalitHubBaseModel) serviceResponse;
                showAlert(getResources().getString(R.string.err_msg), "A mail has been sent to this mail id. Please relogin");
                break;
        }

    }

    private void LoginResponse(JSONObject response) {

        try {
            if (response != null) {
                Log.d(TAG, response.toString());

                int resCode = response.getInt("responseCode");
                String desc = response.getString("responseDescription");
                int user_id = response.getInt("UserId");
                int isAdmin = response.getInt("IsAdmin");

                AppUtils.showToast(this, "login successful");

                Intent in = new Intent(this, LandingScreenActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);

                // if (resCode == 103) {
                // // success
                //
                // Intent in = new Intent(SignUpActivity.this,
                // LandingScreenActivity.class);
                // in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                // | Intent.FLAG_ACTIVITY_NEW_TASK);
                // startActivity(in);
                //
                // } else {
                //
                // AppUtils.showToast(SignUpActivity.this,
                // response.getString("responseDescription"));
                // }
                //
                // } else {
                // AppUtils.showToast(SignUpActivity.this,
                // "something went wrong. Try again.");
                // }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linkedin:
                Intent intent = new Intent(LoginActivity.this, LinkedinLoginActivity.class);
                startActivityForResult(intent, AppConstants.LINKED_IN_LOGIN_REQUEST_CODE);//signupViaLinkedin();
                break;
            case R.id.gplus:
                signInWithGplus();
                break;
            case R.id.fb:
                signInWithFb();
                break;
            case R.id.forgot_pass:
                forgotPassword();
                break;
            default:
                break;
        }
    }

    /********************************
     * Code by Anoop for fb & google & linkedin starts here
     *********************************/
    private void signInWithFb() {
        FacebookUtils.loginFacebook(LoginActivity.this, new FacebookLoginListener() {

            @Override
            public void doAfterLogin(UserInfo userInfo) {
                //removeProgressDialog();
                mUserInfo = new UserInfo(userInfo);
                System.out.println("facebook data =========== " + mUserInfo);

                String[] arrParams = new String[5];
                arrParams[0] = mUserInfo.getEmail();
                arrParams[1] = mUserInfo.getFirst_name();
                arrParams[2] = mUserInfo.getLast_name();
                arrParams[3] = "";
                arrParams[4] = "3";

                getData(AppConstants.actions.DO_LOGIN_FB, arrParams);
            }
        });
    }

    private void signInWithGplus() {
        connectGooglePlus();
    }

    private void connectGooglePlus() {
        mIntentInProgress = false;
        //showProgressHUD();//DialogUtility.displayProgressDialog(this, R.string.loading);
        mGoogleApiClient.connect();
    }

    /**
     * Method to resolve any signin errors
     */
    private void resolveSignInError() {
        if (mConnectionResult != null && mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                connectGooglePlus();
            }
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        //hideProgressHUD(); //DialogUtility.hideProgressDialog();
        Log.d("TAG", "Google+ on success ");
        mSignInClicked = false;

        getGooglePlusProfileInformation();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        connectGooglePlus();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        //hideProgressHUD(); //DialogUtility.hideProgressDialog();
        Log.d("TAG", "Google+ on connection failed " + result.getErrorCode());

        if (!mIntentInProgress && result.hasResolution()) {
            try {
                mIntentInProgress = true;
                startIntentSenderForResult(result.getResolution().getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                // The intent was canceled before it was sent.  Return to the default
                // state and attempt to connect to get an updated ConnectionResult.

                connectGooglePlus();
            }
        }
    }

    /**
     * Fetching user's information name, email, profile pic
     */
    private void getGooglePlusProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                //GetLogin getLogin = new GetLogin();

                //final PreferenceManager preferenceManager = PreferenceManager.getInstance();
                final com.google.android.gms.plus.model.people.Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                Log.e("Google Sign In", "Name: " + currentPerson.getDisplayName() + ", email: " + email
                        + ", Image: " + currentPerson.getImage().getUrl());

                //call registration service
                String[] arrParams = new String[5];
                arrParams[0] = email;
                arrParams[1] = currentPerson.getName().toString();
                arrParams[2] = currentPerson.getDisplayName().toString();
                arrParams[3] = "";
                arrParams[4] = "2";

                System.out.println("getting params ----------- " + arrParams.length);

                getData(AppConstants.actions.DO_LOGIN_GOOGLE, arrParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("LoginActivity.onActivityResult() " + resultCode);

        mUiHelper.onActivityResult(requestCode, resultCode, data, FacebookUtils.dialogCallback);

        if (requestCode == RC_SIGN_IN) {
            mIntentInProgress = false;
            if (resultCode != RESULT_CANCELED) {
                if (!mGoogleApiClient.isConnecting()) {
                    mGoogleApiClient.connect();
                }
            }
        }

        if (requestCode == AppConstants.LINKED_IN_LOGIN_REQUEST_CODE && data != null && resultCode == Activity.RESULT_OK) {
            linkedinData = data.getStringExtra(AppConstants.LINKEDINDATA);

            if (linkedinData != null) {
                Log.e("TAG", "linked in Data----->" + linkedinData);

                Gson gson = new Gson();
                LinkedinLoginModel linkedinLoginModel = gson.fromJson(linkedinData, LinkedinLoginModel.class);

                firstName = linkedinLoginModel.getFirstName();
                lastName = linkedinLoginModel.getLastName();
                email = linkedinLoginModel.getEmailAddress();

                //call for linkedin signup
                String[] arrParams = new String[5];
                arrParams[0] = email;
                arrParams[1] = firstName;
                arrParams[2] = lastName;
                arrParams[3] = "";
                arrParams[4] = "4";


                getData(AppConstants.actions.DO_LOGIN_LINKEDIN, arrParams);

//                linkedInHeadline = linkedinLoginModel.getHeadline();
//                linkedInIndustry = linkedinLoginModel.getIndustry();
//                linkedInSummary = linkedinLoginModel.getSummary();
//                if (linkedinLoginModel.getPositions().getTotal() > 0) {
//                    linkedExperience = linkedinLoginModel
//                            .getPositions()
//                            .getValues()
//                            .get(0).getTitle() + "\n" +
//                            linkedinLoginModel
//                                    .getPositions()
//                                    .getValues()
//                                    .get(0)
//                                    .getCompany()
//                                    .getName()
//                    ;
//                }
//                linkedInImageURL = linkedinLoginModel.getPictureUrl();
//                linkedInProfileUri = linkedinLoginModel.getPublicProfileUrl();
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        mUiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUiHelper.onDestroy();
        Log.d("destroy", "****************************");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("LoginActivity.onResume()");
        mUiHelper.onResume();
    }

    /*************
     * code by anoop end here
     ***********************************************************/

    private void forgotPassword() {
        AlertDialog.Builder b = new AlertDialog.Builder(this,
                android.R.style.Theme_Holo_Light_Dialog);
        b.setTitle("Forgot Password?");
        b.setMessage("Please enter your email id");
        final EditText input = new EditText(this);
        b.setView(input);
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // SHOULD NOW WORK
                String emailAdd = input.getText().toString();
                if (TextUtils.isEmpty(emailAdd)) {

                    AppUtils.showToast(LoginActivity.this, "Please enter email");
                    return;
                }
                if (!AppUtils.isEmailAddress(emailAdd)) {
                    AppUtils.showToast(LoginActivity.this,
                            "Please enter a valid email id");
                    return;
                }
                makeCallForForgotPassword(emailAdd);
            }
        });
        b.setNegativeButton("CANCEL", null);
        b.create().show();

    }

    private void forgotPassResponse(JSONObject response) {
        try {
            if (response != null) {
                Log.d(TAG, response.toString());

                int resCode = response.getInt("responseCode");
                String desc = response.getString("responseDescription");
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    private void makeCallForForgotPassword(String emailId) {
//        final ProgressDialog pDialog = new ProgressDialog(this,
//                ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
//        pDialog.setMessage("Loading...");
//        pDialog.show();
        getData(AppConstants.actions.FORGOT_PASSWORD, emailId);


    }

    private String getForgotPassUrl(String emailId) {
        return "http://www.dalithub.com/Forgetpassword?emailId=" + emailId;

    }

    private void signupViaLinkedin() {
        /*oAuthService = LinkedInOAuthServiceFactory.getInstance()
                .createLinkedInOAuthService(AppConstants.CONSUMER_KEY,
						AppConstants.CONSUMER_SECRET);
		System.out.println("oAuthService : " + oAuthService);

		factory = LinkedInApiClientFactory.newInstance(
				AppConstants.CONSUMER_KEY, AppConstants.CONSUMER_SECRET);

		liToken = oAuthService
				.getOAuthRequestToken(AppConstants.OAUTH_CALLBACK_URL);

		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(liToken
				.getAuthorizationUrl()));
		startActivityForResult(i, 101);*/
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        System.out.println("LoginActivity.onNewIntent()");
        try {
            linkedInImport(intent);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void linkedInImport(Intent intent) {
        String verifier = intent.getData().getQueryParameter("oauth_verifier");
        System.out.println("liToken " + liToken);
        System.out.println("verifier " + verifier);

        LinkedInAccessToken accessToken = oAuthService.getOAuthAccessToken(
                liToken, verifier);
        client = factory.createLinkedInApiClient(accessToken);

        // client.postNetworkUpdate("LinkedIn Android app test");

        Person profile = client.getProfileForCurrentUser(EnumSet.of(
                ProfileField.ID, ProfileField.FIRST_NAME,
                ProfileField.LAST_NAME, ProfileField.HEADLINE));

        System.out.println("First Name :: " + profile.getFirstName());
        System.out.println("Last Name :: " + profile.getLastName());
        System.out.println("Head Line :: " + profile.getHeadline() + " email ");

    }

    private String getLoginUrl(String email, String pass) {
        return "http://www.dalithub.com/Login?emailId="
                + email
                + "&Password="
                + pass
                + "&userLong=00.0000&userLat=00.0000&deviceToken=&deviceUniqueId=";
    }
}
