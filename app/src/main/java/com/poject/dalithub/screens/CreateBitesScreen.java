package com.poject.dalithub.screens;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kelltontech.utils.ConnectivityUtils;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
//import com.loopj.android.http.RequestHandle;
//import com.loopj.android.http.RequestParams;
//import com.loopj.android.http.SyncHttpClient;
//import com.loopj.android.http.TextHttpResponseHandler;
import com.poject.dalithub.R;
import com.poject.dalithub.Utils.AppConstants;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.listener.VolleyErrorListener;
import com.poject.dalithub.models.DalitHubBaseModel;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

//import cz.msebera.android.httpclient.Header;

public class CreateBitesScreen extends DalitHubBaseActivity implements OnClickListener {
    private ImageView leftTopButton, rightTopButton, image_bites;
    private TextView headerTitle;
    private UserPreferences mPref;
    private EditText content_bite;
    private String encodedImageString;
    String imgDecodableString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        mPref = new UserPreferences(this);

        setContentView(R.layout.new_bites_layout);
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

        content_bite = (EditText) findViewById(R.id.content_view);
        image_bites = (ImageView) findViewById(R.id.addedImage);

        rightTopButton.setVisibility(View.GONE);
        headerTitle.setText("NEW BITES");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                && null != data) {
            // Get the Image from data

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            // Get the cursor
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            byte[] bytes;
            ByteArrayOutputStream output = null;
            try {
                InputStream inputStream = new FileInputStream(imgDecodableString);//You can get an inputStream using any IO API

                byte[] buffer = new byte[8192];
                int bytesRead;
                output = new ByteArrayOutputStream();

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bytes = output.toByteArray();
            encodedImageString = Base64.encodeToString(bytes, Base64.DEFAULT);

            Log.d("photo added.", encodedImageString);
            ((TextView) findViewById(R.id.addImage_button)).setText("Image Added");

            File imageFile = new File(imgDecodableString);
            image_bites.setVisibility(View.VISIBLE);
            Picasso.with(CreateBitesScreen.this).load(imageFile).resize(200, 200).into(image_bites);


        } else {
            Toast.makeText(this, "You haven't picked Image",
                    Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_button:
                goBackScreen();
                break;
            case R.id.send_button:
                postContent();
                break;
            case R.id.addImage_button:
                pickImageFromGallery();
                break;
            default:
                break;
        }

    }

    int RESULT_LOAD_IMG = 1001;

    private void pickImageFromGallery() {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    private void postContent() {

        String content = content_bite.getText().toString();
        if (content == null || TextUtils.isEmpty(content)) {
            AppUtils.showToast(this, "Please add content");
            return;
        }

        try {
            content = URLEncoder.encode(content, "UTF-8");
//            encodedImageString = URLEncoder.encode(encodedImageString, "UTF-8");
            getData(AppConstants.actions.POST_A_BITE, content);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }

    }

    private String TAG = "createBitetTag";

    @Override
    public void getData(final int actionID, final String... params) {
        if (!ConnectivityUtils.isNetworkEnabled(CreateBitesScreen.this)) {
            showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_internet));
            return;
        }
        showProgressDialog(getResources().getString(R.string.loading));

        switch (actionID) {
            case AppConstants.actions.POST_A_BITE:
                RequestManager.addRequest(new GsonObjectRequest<DalitHubBaseModel>(
                        getCreateBiteUrl(mPref.getUserId(), params[0], params[1]), null, DalitHubBaseModel.class,
                        new VolleyErrorListener(CreateBitesScreen.this, actionID)) {
                    @Override
                    protected void deliverResponse(DalitHubBaseModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        CreateBitesScreen.this.updateUi(true, actionID, response);
                    }
                });
//                new AsyncTask<Void, Void, Void>() {
//
//                    @Override
//                    protected Void doInBackground(Void... voids) {
//                        SyncHttpClient client = new SyncHttpClient();
//                        RequestParams reqParams = new RequestParams();
//                        reqParams.put("userId", mPref.getUserId());
//                        reqParams.put("message", params[0]);
//
//                        try {
//                            if (imgDecodableString != null)
//                                reqParams.put("image_content", new File(imgDecodableString));
//                        } catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//
//                        RequestHandle post = client.post(AppConstants.baseUrl + "insertnewbite", reqParams, new TextHttpResponseHandler() {
//                            @Override
//                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                                // error handling
//                                Log.d(TAG, "onFailure--->" + statusCode + "---" + responseString);
//                            }
//
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                                Log.d(TAG, "onSuccess--->" + statusCode + "---" + responseString);
//                            }
//                        });
//                        return null;
//                    }
//                }.execute();


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
        } else if (!((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("111")) {
            showAlert(getResources().getString(R.string.err_msg), ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            Log.e(TAG, "Message from Server : " + ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            return;
        }

        switch (action) {
            case AppConstants.actions.POST_A_BITE: {
                Log.d(TAG, "response is success..........................");
                try {
                    DalitHubBaseModel response = (DalitHubBaseModel) serviceResponse;

                    AppUtils.showToast(CreateBitesScreen.this,
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());
                    setResult(AppConstants.BITE_POSTED);
                    CreateBitesScreen.this.finish();


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
        }
    }

    private String getCreateBiteUrl(String uId, String msg, String base64Image) {
        return AppConstants.baseUrl + "insertnewbite?userId=" + uId + "&message=" + msg + "&image_content=" + base64Image;
    }

    private void goBackScreen() {
        this.finish();
    }
}
