package com.poject.dalithub.screens;

import com.kelltontech.utils.ConnectivityUtils;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.poject.dalithub.R;
import com.poject.dalithub.Utils.AppConstants;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.application.AppController;
import com.poject.dalithub.listener.VolleyErrorListener;
import com.poject.dalithub.models.DalitHubBaseModel;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;

public class CreateEventsActivity extends DalitHubBaseActivity implements OnClickListener {
    private ImageView leftTopButton, rightTopButton;
    private TextView headerTitle;
    private UserPreferences mPref;
    private EditText eventNameBox, eventLocation, eventDate, eventDesc, eventTime, webUrl;
    private int DIALOG_YES_BUTTON = -1;
    private int DIALOG_NO_BUTTON = -2;

    private Calendar cal;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int min;
    private String encodedImageString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        mPref = new UserPreferences(this);

        setContentView(R.layout.create_event_layout);
        initViews();
        setScreenListeners();

        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);

    }

    private void setScreenListeners() {
        leftTopButton.setOnClickListener(this);
    }

    private void initViews() {
        leftTopButton = (ImageView) findViewById(R.id.left_button);
        rightTopButton = (ImageView) findViewById(R.id.right_button);
        headerTitle = (TextView) findViewById(R.id.title);

        eventNameBox = (EditText) findViewById(R.id.editText_name);
        eventLocation = (EditText) findViewById(R.id.edit_location);
        eventDate = (EditText) findViewById(R.id.edit_date);
        eventTime = (EditText) findViewById(R.id.edit_time);
        webUrl = (EditText) findViewById(R.id.edit_weblink);
        eventDesc = (EditText) findViewById(R.id.edit_desc);


        rightTopButton.setVisibility(View.GONE);
        headerTitle.setText("NEW EVENT");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_button:
                goBackScreen();
                break;
            case R.id.ok_button:
                createEvent();
                break;
            case R.id.cancel_button:
                goBackScreen();
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
            String imgDecodableString = cursor.getString(columnIndex);
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


        } else if (requestCode == AppConstants.LOCATION_DATA && resultCode == RESULT_OK && data != null) {
            Log.d("Map data", "--------------result--------");
        }
    }

    private void createEvent() {
        String name = eventNameBox.getText().toString();
        String desc = eventDesc.getText().toString();
        String date = eventDate.getText().toString();
        String location = eventLocation.getText().toString();
        String webLink = webUrl.getText().toString();
        String time = eventTime.getText().toString();

        if (name == null || name.isEmpty()) {
            AppUtils.showToast(this, "Please enter event name");
            return;
        }
        if (desc == null || desc.isEmpty()) {
            AppUtils.showToast(this, "Please enter event description");
            return;
        }
        if (date == null || date.isEmpty()) {
            AppUtils.showToast(this, "Please enter event date");
            return;
        }
        if (!date.contains("-")) {
            AppUtils.showToast(this, "Date format alert!");
            return;
        }
        if (!time.contains(":")) {
            AppUtils.showToast(this, "Time format alert!");
            return;
        }
        if (location == null || location.isEmpty()) {
            AppUtils.showToast(this, "Please enter event location");
            return;
        }
        if (webLink != null && webLink.length() > 0) {
            if (!URLUtil.isValidUrl(webLink)) {
                AppUtils.showToast(CreateEventsActivity.this, "Please enter a valid URL");
                return;
            }
        } else {
            webLink = "";
        }
        AppController appController = (AppController) getApplication();
        currentLocation = appController.getmLastLocation();


        if (!showGPSAlert())
            return;


        if (currentLocation == null || currentLocation.getLatitude() <= 0) {
            AppUtils.showToast(this, "unable to get location.Try again.");
            return;
        }

        getData(AppConstants.actions.CREATE_EVENT, name, desc, location, mPref.getUserId(), date, time, webLink);
    }

    private boolean showGPSAlert() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!statusOfGPS) {
            ((AppController) getApplication()).showGPSDialog(CreateEventsActivity.this, new DialogInterface.OnClickListener() {
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

    Location currentLocation;

    private void goBackScreen() {
        this.finish();

    }

    private String TAG = "createEventTag";

    @Override
    public void getData(final int actionID, String... params) {
        if (!ConnectivityUtils.isNetworkEnabled(this)) {
            showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_internet));
            return;
        }
        showProgressDialog(getResources().getString(R.string.loading));

        switch (actionID) {
            case AppConstants.actions.CREATE_EVENT:
                RequestManager.addRequest(new GsonObjectRequest<DalitHubBaseModel>(
                        getCreateEventUrl(params[0], params[1], params[2], params[3], params[4], params[5], params[6]), null, DalitHubBaseModel.class,
                        new VolleyErrorListener(CreateEventsActivity.this, actionID)) {
                    @Override
                    protected void deliverResponse(DalitHubBaseModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        CreateEventsActivity.this.updateUi(true, actionID, response);
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
        } else if (!((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("117")) {
            showAlert(getResources().getString(R.string.err_msg), ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            Log.e(TAG, "Message from Server : " + ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            return;
        }

        switch (action) {
            case AppConstants.actions.CREATE_EVENT: {
                Log.d(TAG, "response is success..........................");
                try {
                    DalitHubBaseModel response = (DalitHubBaseModel) serviceResponse;

                    AppUtils.showToast(CreateEventsActivity.this,
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());
                    setResult(AppConstants.EVENT_CREATED);
                    CreateEventsActivity.this.finish();


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
        }
    }

    private String getCreateEventUrl(String name, String desc, String location, String u_id, String date, String time, String link) {
        String event_url = null;
        AppController appController = (AppController) getApplication();
        currentLocation = appController.getmLastLocation();
        if (currentLocation == null) {
            AppUtils.showToast(this, "unable to get location.Try again.");
            return null;
        }
        try {
            event_url = AppConstants.baseUrl + "createevent?event_name=" + URLEncoder.encode(name, "UTF-8") + "&event_description=" + URLEncoder.encode(desc, "UTF-8") + "&Location=" + URLEncoder.encode(location, "UTF-8") +
                    "&longtitde=" + currentLocation.getLongitude() + "&latitude=" + currentLocation.getLatitude() + "&userId=" + u_id + "&Web_Link=" + link + "&image_content=&date=" +
                    date + "&time=" + time.replace(" ", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("Create eve url ", event_url);
        return event_url;
    }

    public void pickEventDate(View v) {
        if (v.getId() == R.id.dateClick)
            showDialog(0);
        else
            showDialog(1);
    }

    @Nullable
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 0)
            return new DatePickerDialog(this, datePickerListener, year, month, day);
        else
            return new TimePickerDialog(CreateEventsActivity.this, timePickerListener, hour, min, true);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            eventDate.setText(selectedYear + "-" + (selectedMonth + 1) + "-"
                    + selectedDay);
//            eventDate.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
//                    + selectedYear);
        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int mins) {
            AppUtils.showToast(CreateEventsActivity.this, "hour is " + hour + " mins is " + mins);
            int displayHr = hour > 12 ? hour - 12 : hour;
            String dayLight = hour > 12 ? "PM" : "AM";
            boolean isSingle = displayHr > 9 ? false : true;
            String strHr = isSingle ? "0" + displayHr : String.valueOf(displayHr);
            eventTime.setText(strHr + ":" + mins + " " + dayLight);
        }
    };

    public void pickLocation(View v) {
        Intent locIntent = new Intent(this, PickLocationActivity.class);
        startActivityForResult(locIntent, AppConstants.LOCATION_DATA);
    }
}
