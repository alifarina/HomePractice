package com.poject.dalithub.application;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.kelltontech.application.BaseApplication;
import com.kelltontech.volley.ext.RequestManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.poject.dalithub.R;

import android.location.Location;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TextView;


public class AppController extends BaseApplication implements GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, LocationListener {


	public static final String TAG = AppController.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	private static AppController mInstance;

	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	private Location mLastLocation;

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;

		buildGoogleApiClient();
		createLocationRequest();
		mGoogleApiClient.connect();
	}

	@Override
	protected void initialize() {
		RequestManager.initializeWith(this.getApplicationContext(), new RequestManager.Config("data/data/dalithub/pics", 5242880, 4));
	}

	public static synchronized AppController getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	private synchronized void buildGoogleApiClient() {
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API)
				.build();
	}

	private void createLocationRequest() {
		mLocationRequest = new LocationRequest();
		mLocationRequest.setInterval(1000 * 60);
		mLocationRequest.setFastestInterval(5000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	}

	@Override
	public void onConnected(Bundle bundle) {
		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
				mGoogleApiClient);
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
	}

	@Override
	public void onConnectionSuspended(int i) { }

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) { }

	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "lat = " + location.getLatitude() + "lon = " + location.getLongitude());
		mLastLocation = location;
	}


	public Location getmLastLocation() {
		return mLastLocation;
	}

	public static void showGPSDialog(Activity pActivity, DialogInterface.OnClickListener pDialogClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(pActivity,AlertDialog.THEME_HOLO_LIGHT);
		builder.setMessage("DalitHub needs access to your location. please turn on location access.");
		builder.setTitle("Location services disabled");//pActivity.getString(R.string.app_name));
		builder.setCancelable(false);
		builder.setPositiveButton("Settings", pDialogClickListener);
		builder.setNegativeButton("Ignore", pDialogClickListener);
		AlertDialog dialog = builder.show();

		// Must call show() prior to fetching views
		TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
		messageView.setGravity(Gravity.CENTER);

		TextView titleView = (TextView)dialog.findViewById(pActivity.getResources().getIdentifier("alertTitle", "id", "android"));
		if (titleView != null) {
			titleView.setGravity(Gravity.CENTER);
		}

		Button buttonbackground1 = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
		buttonbackground1.setTextColor(pActivity.getResources().getColor(R.color.blue_ok));

		Button buttonbackground2 = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		buttonbackground2.setTextColor(pActivity.getResources().getColor(R.color.blue_ok));
	}
}
