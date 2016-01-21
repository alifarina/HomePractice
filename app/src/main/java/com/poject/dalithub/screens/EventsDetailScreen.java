package com.poject.dalithub.screens;

import com.kelltontech.utils.ConnectivityUtils;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.poject.dalithub.R;
import com.poject.dalithub.Utils.AppConstants;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.Utils.DialogsCustom;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.listener.VolleyErrorListener;
import com.poject.dalithub.models.DalitHubBaseModel;
import com.poject.dalithub.models.EventAttendee;
import com.poject.dalithub.models.EventCommentModel;
import com.poject.dalithub.models.EventDetail;
import com.poject.dalithub.models.EventDetailBaseClass;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.regex.Pattern;

public class EventsDetailScreen extends DalitHubBaseActivity implements OnClickListener {
    private ImageView leftTopButton, rightTopButton, eventImageview;
    private TextView headerTitle, eventlabel, description, address;
    private UserPreferences mPref;
    private LinearLayout attendeesLayout;
    private RelativeLayout rsvpLayout;
    private ImageView weblinkClick;
    private TextView view_more_members;

    public EventDetailBaseClass getResponse() {
        return response;
    }

    public void setResponse(EventDetailBaseClass response) {
        this.response = response;
    }

    private EventDetailBaseClass response;
    String eve_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_detailscreen);

        mPref = new UserPreferences(this);
        eve_id = getIntent().getStringExtra("event_id");

        initViews();
        setScreenListeners();

        getData(AppConstants.actions.GET_EVENT_DETAIL, eve_id, mPref.getUserId());


    }

    private String TAG = "eventDetailTag";

    @Override
    public void getData(final int actionID, String... params) {
        if (!ConnectivityUtils.isNetworkEnabled(EventsDetailScreen.this)) {
            showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_internet));
            return;
        }
        showProgressDialog(getResources().getString(R.string.loading));
        switch (actionID) {
            case AppConstants.actions.GET_EVENT_DETAIL:
                RequestManager.addRequest(new GsonObjectRequest<EventDetailBaseClass>(
                        getEventDetailUrl(params[0], params[1]), null, EventDetailBaseClass.class,
                        new VolleyErrorListener(EventsDetailScreen.this, actionID)) {
                    @Override
                    protected void deliverResponse(EventDetailBaseClass response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        EventsDetailScreen.this.updateUi(true, actionID, response);
                    }
                });
                break;
            case AppConstants.actions.ATTEND_EVENT:
                RequestManager.addRequest(new GsonObjectRequest<DalitHubBaseModel>(
                        getAttendEventUrl(params[0], params[1]), null, DalitHubBaseModel.class,
                        new VolleyErrorListener(EventsDetailScreen.this, actionID)) {
                    @Override
                    protected void deliverResponse(DalitHubBaseModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        EventsDetailScreen.this.updateUi(true, actionID, response);
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
        } else if (!((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("118")
                && !((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("121")) {
            showAlert(getResources().getString(R.string.err_msg), ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            Log.e(TAG, "Message from Server : " + ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            return;
        }

        switch (action) {
            case AppConstants.actions.GET_EVENT_DETAIL: {
                Log.d(TAG, "response is success..........................");
                try {
                    EventDetailBaseClass response = (EventDetailBaseClass) serviceResponse;

                    if (response != null) {
                        setResponse(response);
                        setScreenData(response);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
            case AppConstants.actions.ATTEND_EVENT: {
                Log.d(TAG, "response is success..........................");
                try {
                    DalitHubBaseModel response = (DalitHubBaseModel) serviceResponse;

                    AppUtils.showToast(EventsDetailScreen.this, response.getResponseDescription());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
        }
    }

    private void setScreenData(EventDetailBaseClass response) {
        List<EventDetail> details = response.getEventDetails();
        EventDetail detail = details.get(0);
        eventlabel.setText("" + detail.getEventName());
        description.setText("" + detail.getDescription());
        address.setText("" + detail.getEventAddress());

        if (detail.getImagePath() != null) {
            eventImageview.setVisibility(View.VISIBLE);
            Picasso.with(EventsDetailScreen.this).load(detail.getImagePath()).placeholder(R.drawable.profile_pic).into(eventImageview);
        }

        //parsing event date
        String date = detail.getEventDate();
        String[] dateArr = date.split(" ");
        String dateString = dateArr[0];
        dateString = dateString.substring(0, dateString.lastIndexOf("/"));
        Log.d("Details======", detail.getImagePath() + " " + dateString);

        LayoutInflater inflater = getLayoutInflater();
        List<EventAttendee> attendees = response.getEventAttendees();
        if (attendees != null && attendees.size() > 0) {

            //clear any previour child
            attendeesLayout.removeAllViews();
            int count = 0;
            for (EventAttendee attendee : attendees) {

                if (count >= 4)
                    break;
                count++;

                View attendee_item = inflater.inflate(R.layout.attendee_item_layout, null);

                //setting values
                ImageView iv_attendee = (ImageView) attendee_item.findViewById(R.id.image_attendee);
                TextView tv_attendee = (TextView) attendee_item.findViewById(R.id.name_attendee);
                if (attendee.getProfileImage() != null && attendee.getProfileImage().length() > 1)
                    Picasso.with(EventsDetailScreen.this).load(attendee.getProfileImage()).placeholder(R.drawable.img_member).into(iv_attendee);
                tv_attendee.setText(attendee.getUserName());
                Log.d("name ", "" + attendee.getUserName());

                attendeesLayout.addView(attendee_item);
            }
        }
    }

    private void setScreenListeners() {
        leftTopButton.setOnClickListener(this);
        rsvpLayout.setOnClickListener(this);
        weblinkClick.setOnClickListener(this);
        view_more_members.setOnClickListener(this);
    }

    private void initViews() {
        leftTopButton = (ImageView) findViewById(R.id.left_button);
        rightTopButton = (ImageView) findViewById(R.id.right_button);
        headerTitle = (TextView) findViewById(R.id.title);
        eventlabel = (TextView) findViewById(R.id.label_text);
        description = (TextView) findViewById(R.id.description);
        address = (TextView) findViewById(R.id.locationText);
        eventImageview = (ImageView) findViewById(R.id.eventImage);
        attendeesLayout = (LinearLayout) findViewById(R.id.attendee_layout);
        view_more_members = (TextView) findViewById(R.id.view_more_members);

        rsvpLayout = (RelativeLayout) findViewById(R.id.rsvp_layout);
        weblinkClick = (ImageView) findViewById(R.id.weblink);

        rightTopButton.setVisibility(View.GONE);
        headerTitle.setText("EVENT");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_button:
                goBackScreen();
                break;
            case R.id.rsvp_layout:
                showRsvpDialog();
                break;
            case R.id.weblink:
                goToEventWeblink();
                break;
            case R.id.view_more_members:
                showMembersListing();
                break;
            default:
                break;
        }

    }

    private void showMembersListing() {

        Intent memberIntent = new Intent(this, MembersListActivity.class);
        memberIntent.putExtra("eventId", eve_id);
        memberIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(memberIntent);

    }

    private void goToEventWeblink() {
        if (response != null) {
            List<EventDetail> detail = response.getEventDetails();
            String webUrl = detail.get(0).getWebLink();

            if (webUrl != null && AppUtils.isValidUrl(webUrl)) {
                Intent web = new Intent(Intent.ACTION_VIEW);
                web.setData(Uri.parse(webUrl));
                startActivity(web);
            } else {
                AppUtils.showToast(EventsDetailScreen.this, "url is either emmpty or invalid.");
            }
        }

    }

    private void showRsvpDialog() {
        final AlertDialog dialog = DialogsCustom.promptDialog(this, "Attend Event", "Do you want to attend this event ?", "Yes", "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == DialogInterface.BUTTON_POSITIVE) {
                    getData(AppConstants.actions.ATTEND_EVENT, eve_id, mPref.getUserId());
                } else {
                    dialogInterface.cancel();
                }
            }
        });
        dialog.show();
    }

    public static List<EventCommentModel> commentsListToForward;

    public void displayComments(View v) {

        if (response != null) {

            List<EventCommentModel> commentsList = response.getEventcomments();
            commentsListToForward = commentsList;
            Intent intent = new Intent(this, EventsCommentsScreen.class);
            intent.putExtra("eventId", eve_id);
            startActivity(intent);
        }
    }

    private void goBackScreen() {
        this.finish();
    }

    private String getEventDetailUrl(String eve_id, String user_id) {
        Log.d("event id " + eve_id, "user id " + user_id);
        return AppConstants.baseUrl + "geteventdetail?event_id=" + eve_id + "&userid=" + user_id;
    }

    private String getAttendEventUrl(String eveId, String userId) {
        return AppConstants.baseUrl + "AttendEvent?event_id=" + eveId + "&userid=" + userId;
    }
}
