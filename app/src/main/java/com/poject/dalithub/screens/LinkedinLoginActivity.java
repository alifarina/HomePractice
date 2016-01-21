package com.poject.dalithub.screens;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.poject.dalithub.R;
import com.poject.dalithub.Utils.AppConstants;

import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

public class LinkedinLoginActivity extends Activity {

    private Response response;
    private static OAuthService oas_linkedin;
    private static Token requestToken;
    private static String authURL;
    private WebView webview;

    String TAG = "LinkedinLogin";

    private static String id;
    private static String email;
    private static String name;
    private static String firstName;
    private static String lastName;
    private static String jsonResponse;
    private static JSONObject jsonResponseObj;

    String SHARED_PREF_FILE_NAME = "user_detail";
    String LINKED_IN_USER_DATA = "user_linkedin_data";

    public static String LINKEDIN_CONSUMER_KEY = "75q03aokm703uq";//"75ovgq6t4ozz2r";
    public static String LINKEDIN_CONSUMER_SECRET = "97d5LSWdMKxHAHE8";//"cBhSPDxPwFDbVcWM";
    public static String scopeParams = "r_basicprofile+r_emailaddress+w_share";

    public static String OAUTH_CALLBACK_SCHEME = "x-oauthflow-linkedin";
    public static String OAUTH_CALLBACK_URL_HOST = "callback";
    public static String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_URL_HOST;
    public static String LINKED_IN_LOGIN_PROBLEM = "x-oauthflow-linkedin://callback?oauth_problem";
    public static final String PROTECTED_RESOURCE_URL = "http://api.linkedin.com/v1/people/~:(id,first-name,last-name,email-address,headline,picture-url,industry,summary,location:(name,country:(code)),public-profile-url,specialties,positions:(id,title,summary,start-date,end-date,is-current,company:(id,name,type,size,industry,ticker)),educations:(id,school-name,field-of-study,start-date,end-date,degree,activities,notes),associations,interests,publications:(id,title,publisher:(name),authors:(id,name),date,url,summary),patents:(id,title,summary,number,status:(id,name),office:(name),inventors:(id,name),date,url),certifications:(id,name,authority:(name),number,start-date,end-date),courses:(id,name,number),honors-awards)?format=json";

    String LinkedInAccessToken = "linkedinaccesstoken";
    String LinkedInStringURL = "linkedinstringurl";
    String LINKEDIN_ACCESS_TOKEN_KEY = "linkedinaccesskey";
    String LINKEDIN_ACCESS_TOKEN_SECRET = "linkedinaccesssecret";

    String linkedInAccessTokenKey;
    String linkedInAccessTokenSecret;

     public boolean setPreference(Context c, String fileName, String key,  String value) {
        SharedPreferences settings = c.getSharedPreferences(fileName, 0);
        settings = c.getSharedPreferences(fileName, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public String getPreference(Context c, String filename, String key) {
        SharedPreferences settings = c.getSharedPreferences(filename, 0);
        settings = c.getSharedPreferences(filename, 0);
        String value = settings.getString(key, "");
        return value;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkedin_login);

        linkedInAccessTokenKey = getPreference(LinkedinLoginActivity.this, SHARED_PREF_FILE_NAME, LINKEDIN_ACCESS_TOKEN_KEY);
        linkedInAccessTokenSecret = getPreference(LinkedinLoginActivity.this, SHARED_PREF_FILE_NAME, LINKEDIN_ACCESS_TOKEN_SECRET);

        Log.e(TAG, "Access Token Secret: " + linkedInAccessTokenKey + " : " + linkedInAccessTokenSecret);

        if ((linkedInAccessTokenKey != null && !linkedInAccessTokenKey.isEmpty()) && (linkedInAccessTokenSecret != null && !linkedInAccessTokenSecret.isEmpty())) {
            Log.e(TAG, "GetUserLinkedinInfo Called");
            new GetUserLinkedinInfo().execute();
        } else {
            Log.e(TAG, "OauthStart Called");
            new OauthStart().execute();
        }

    }



    private class OauthStart extends AsyncTask<Void, Void, WebView> {

        @Override
        protected WebView doInBackground(Void... params) {
            try {

                oas_linkedin = new ServiceBuilder().provider(LinkedInApi.class)
                        .apiKey(LINKEDIN_CONSUMER_KEY)
                        .apiSecret(LINKEDIN_CONSUMER_SECRET)
                        .callback(OAUTH_CALLBACK_URL).build();

                requestToken = oas_linkedin.getRequestToken();
                authURL = oas_linkedin.getAuthorizationUrl(requestToken);

                Log.e(TAG, "Auth URL : " + authURL);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "ScribeError:1 " + e.getMessage());
                Intent intent = new Intent();
                setResult(3, intent);
                finish();
            } finally {
                webview = (WebView) findViewById(R.id.webView);
            }
            return webview;
        }

        @Override
        protected void onPostExecute(final WebView result) {
            result.setWebViewClient(new WebViewClient() {
                @Override
                public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                    super.onReceivedClientCertRequest(view, request);
                }

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.startsWith(OAUTH_CALLBACK_URL)) {
                        result.setVisibility(View.GONE);
                        //showProgressDialog(getResources().getString(R.string.loading));
                        new OauthEnd().execute(url);
                        return true;
                    }
                    return super.shouldOverrideUrlLoading(view, url);
                }
            });

            if (authURL != null) {
                webview.setVisibility(View.VISIBLE);
                result.loadUrl(authURL);
            } else {
                Log.e(TAG, "authURL is null");
            }
            //removeProgressDialog();
            super.onPostExecute(result);
        }
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key.  The default implementation simply finishes the current activity,
     * but you can override this to do whatever you want.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.e(TAG, "onBackPressed Called");
        Intent intent = new Intent();
        setResult(0, intent);
        finish();
    }

    private class OauthEnd extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            final Uri uri = Uri.parse(params[0]);
            final String verifier = uri.getQueryParameter("oauth_verifier");
            Log.d(TAG, "Verifier : " + verifier);
            try {
                final Verifier v = new Verifier(verifier);

                final Token accessToken = oas_linkedin.getAccessToken(requestToken, v);

                final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
                Log.d(TAG, "Request: " + request);
                Log.d(TAG, "Access Token: " + accessToken);
                String accessTokenSecret = accessToken.getSecret();
                String accessTokenKey = accessToken.getToken();
                String testContent = accessToken.getRawResponse();
                Log.e(TAG, "testContent => " + testContent);

                setPreference(LinkedinLoginActivity.this, SHARED_PREF_FILE_NAME, LINKEDIN_ACCESS_TOKEN_KEY, accessTokenKey);
                setPreference(LinkedinLoginActivity.this, SHARED_PREF_FILE_NAME, LINKEDIN_ACCESS_TOKEN_SECRET, accessTokenSecret);

                Token token = new Token("5187756c-a680-4046-9675-05d3a288fd7b", "6b39964b-7b5f-4759-a2ca-28265ba9a92e");
                Log.d(TAG, "Access Token Secret: " + accessTokenSecret + " : " + accessTokenKey);

                oas_linkedin.signRequest(accessToken, request);


                response = request.send();
                Log.e(TAG, "@Jogi: Response From LinkedIn: " + response);
//                StringBuilder response  = new StringBuilder();

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "ScribeError:2 " + e.getMessage());
                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            new XmlWorker().execute(response);
            super.onPostExecute(result);
        }

    }

    private class XmlWorker extends AsyncTask<Response, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Response... params) {

            JSONObject mainResponseObject;
            try {
                mainResponseObject = new JSONObject(params[0].getBody());
                /*email = mainResponseObject.optString("emailAddress");
                firstName = mainResponseObject.optString("firstName");
                lastName = mainResponseObject.optString("lastName");
                id = mainResponseObject.optString("id");*/

                jsonResponse = mainResponseObject.toString();
                //jsonResponseObj = mainResponseObject;

                setPreference(LinkedinLoginActivity.this, SHARED_PREF_FILE_NAME, LINKED_IN_USER_DATA, jsonResponse);

                Log.e(TAG, "XmlWorker -> Linkedin Response : " + jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "ScribeError:3 " + e.getMessage());

                Intent intent = new Intent();
                setResult(Activity.RESULT_CANCELED, intent);
                finish();

            }
            return jsonResponse;
        }

        @Override
        protected void onPostExecute(String result) {

            Intent intent = new Intent();
            intent.putExtra(AppConstants.LINKEDINDATA, result);
            setResult(Activity.RESULT_OK, intent);
            finish();

            super.onPostExecute(result);
        }
    }

    private class GetUserLinkedinInfo extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                oas_linkedin = new ServiceBuilder().provider(LinkedInApi.class)
                        .apiKey(LINKEDIN_CONSUMER_KEY)
                        .apiSecret(LINKEDIN_CONSUMER_SECRET)
                        .callback(OAUTH_CALLBACK_URL).build();

                Log.d(TAG, "Access Token Secret: " + linkedInAccessTokenKey + " : " + linkedInAccessTokenSecret);
                final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
                Token token = new Token(linkedInAccessTokenKey, linkedInAccessTokenSecret);
                oas_linkedin.signRequest(token, request);
                response = request.send();
                Log.e(TAG, "@Jogi: Response From LinkedIn: " + response);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "ScribeError:2 " + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            new XmlWorker().execute(response);
            super.onPostExecute(result);
        }
    }
}
