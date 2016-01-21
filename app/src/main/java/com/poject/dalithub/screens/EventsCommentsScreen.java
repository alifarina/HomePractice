package com.poject.dalithub.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.widget.UserSettingsFragment;
import com.kelltontech.utils.ConnectivityUtils;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.poject.dalithub.R;
import com.poject.dalithub.Utils.AppConstants;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.Utils.DialogsCustom;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.adapters.CommentsAdapter;
import com.poject.dalithub.adapters.EventCommentsAdapter;
import com.poject.dalithub.listener.VolleyErrorListener;
import com.poject.dalithub.models.DalitHubBaseModel;
import com.poject.dalithub.models.EventCommentModel;
import com.poject.dalithub.models.EventsCommentsBaseModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Farina on 12/28/2015.
 */
public class EventsCommentsScreen extends DalitHubBaseActivity implements View.OnClickListener {
    private ImageView leftTopButton, rightTopButton;
    private TextView headerTitle;
    ListView listview_comments;
    private EditText commentBox;
    private List<EventCommentModel> commentsList;
    private UserPreferences mPref;
    private String event_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_layout);

        mPref = new UserPreferences(this);
        event_id = getIntent().getStringExtra("eventId");

        //getting comments from event details
        getData(AppConstants.actions.GET_EVENT_COMMENT, mPref.getUserId());
        // commentsList = EventsDetailScreen.commentsListToForward;
        initViews();

        setScreenListeners();


    }

    private EventCommentsAdapter adapter;

    private void setAdapter(List<EventCommentModel> list) {
        adapter = new EventCommentsAdapter(this, list);
        listview_comments.setAdapter(adapter);
    }

    private void initViews() {

        leftTopButton = (ImageView) findViewById(R.id.left_button);
        rightTopButton = (ImageView) findViewById(R.id.right_button);
        headerTitle = (TextView) findViewById(R.id.title);
        listview_comments = (ListView) findViewById(R.id.comment_list);

        commentBox = (EditText) findViewById(R.id.comment);

        rightTopButton.setVisibility(View.GONE);
        headerTitle.setText("COMMENTS");
    }

    private void setScreenListeners() {
        leftTopButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_button:
                goBackScreen();
                break;
            case R.id.send_button:
                sendCommentForEvent();
                break;
            default:
                break;
        }
    }

    private void sendCommentForEvent() {
        String comment = commentBox.getText().toString();
        if (comment == null || TextUtils.isEmpty(comment)) {
            AppUtils.showToast(this, "comment cannot be blank.");
            return;
        }

        try {
            getData(AppConstants.actions.POST_COMMENT, event_id, mPref.getUserId(), URLEncoder.encode(comment, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private String getPostCommentUrl(String eve_id, String u_id, String comment) {
        return AppConstants.baseUrl + "postcommentforevent?event_id=" + eve_id + "&userid=" + u_id + "&comment=" + comment;
    }

    private String TAG = "CommentseventsTag";

    @Override
    public void getData(final int actionID, String... params) {
        if (!ConnectivityUtils.isNetworkEnabled(EventsCommentsScreen.this)) {
            showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_internet));
            return;
        }
        showProgressDialog(getResources().getString(R.string.loading));

        switch (actionID) {
            case AppConstants.actions.POST_COMMENT:
                RequestManager.addRequest(new GsonObjectRequest<DalitHubBaseModel>(
                        getPostCommentUrl(params[0], params[1], params[2]), null, DalitHubBaseModel.class,
                        new VolleyErrorListener(EventsCommentsScreen.this, actionID)) {
                    @Override
                    protected void deliverResponse(DalitHubBaseModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        EventsCommentsScreen.this.updateUi(true, actionID, response);
                    }
                });
                break;
            case AppConstants.actions.DEL_EVENT_COMMENT:
                RequestManager.addRequest(new GsonObjectRequest<DalitHubBaseModel>(
                        getDelCommentUrl(params[0]), null, DalitHubBaseModel.class,
                        new VolleyErrorListener(EventsCommentsScreen.this, actionID)) {
                    @Override
                    protected void deliverResponse(DalitHubBaseModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        EventsCommentsScreen.this.updateUi(true, actionID, response);
                    }
                });
                break;
            case AppConstants.actions.GET_EVENT_COMMENT:
                RequestManager.addRequest(new GsonObjectRequest<EventsCommentsBaseModel>(
                        getAllCommentsUrl(params[0]), null, EventsCommentsBaseModel.class,
                        new VolleyErrorListener(EventsCommentsScreen.this, actionID)) {
                    @Override
                    protected void deliverResponse(EventsCommentsBaseModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        EventsCommentsScreen.this.updateUi(true, actionID, response);
                    }
                });
                break;
        }
    }

    private String getDelCommentUrl(String commId) {
        return AppConstants.baseUrl + "DeleteCommment?type=2&commentId=" + commId;
    }

    private String getAllCommentsUrl(String user_id) {
        return AppConstants.baseUrl + "GetEventComments?event_id=" + event_id + "&userid=" + user_id;
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
        } else if (!((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("119")
                && !((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("122")
                && !((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("116")) {
            showAlert(getResources().getString(R.string.err_msg), ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            Log.e(TAG, "Message from Server : " + ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            return;
        }

        switch (action) {
            case AppConstants.actions.POST_COMMENT: {
                Log.d(TAG, "response is success..........................");
                try {
                    DalitHubBaseModel response = (DalitHubBaseModel) serviceResponse;

                    AppUtils.showToast(EventsCommentsScreen.this,
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());

                    commentBox.setText(null);
                    getData(AppConstants.actions.GET_EVENT_COMMENT, mPref.getUserId());


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
            case AppConstants.actions.DEL_EVENT_COMMENT: {
                Log.d(TAG, "response is success..........................");
                try {
                    DalitHubBaseModel response = (DalitHubBaseModel) serviceResponse;

                    AppUtils.showToast(EventsCommentsScreen.this,
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());
                    getData(AppConstants.actions.GET_EVENT_COMMENT, mPref.getUserId());


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
            case AppConstants.actions.GET_EVENT_COMMENT: {
                Log.d(TAG, "response is success..........................");
                try {
                    EventsCommentsBaseModel response = (EventsCommentsBaseModel) serviceResponse;

                    AppUtils.showToast(EventsCommentsScreen.this,
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());
                    commentsList = response.getEventcomments();
                    if (commentsList != null)
                        setAdapter(commentsList);


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

    public void deleteComment(final String commentId) {
        final AlertDialog dialog = DialogsCustom.promptDialog(this, "Delete Comment", "Do you want to delete this comment", "Yes", "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == AlertDialog.BUTTON_POSITIVE) {
                    getData(AppConstants.actions.DEL_EVENT_COMMENT, commentId);
                } else {
                }
            }
        });

        dialog.show();
    }
}
