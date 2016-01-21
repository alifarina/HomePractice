package com.poject.dalithub.screens;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kelltontech.utils.ConnectivityUtils;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.poject.dalithub.Utils.AppConstants;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.Utils.DialogsCustom;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.adapters.CommentsAdapter;
import com.poject.dalithub.R;
import com.poject.dalithub.listener.VolleyErrorListener;
import com.poject.dalithub.models.Comment;
import com.poject.dalithub.models.CommentsModel;
import com.poject.dalithub.models.DalitHubBaseModel;
import com.poject.dalithub.models.LoginModel;

public class CommentsScreen extends DalitHubBaseActivity implements OnClickListener {
    private ImageView leftTopButton, rightTopButton;
    private TextView headerTitle;
    CommentsAdapter adapter;
    ListView listview_comments;
    private EditText commentBox;

    private String bite_id;
    private UserPreferences mPref;
    private int RESULT_USER_COMMENTED = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comments_layout);
        initViews();

        mPref = new UserPreferences(this);
        bite_id = getIntent().getStringExtra("bite_id");
        Log.d("Bite id for comments ", "" + bite_id);


        getData(AppConstants.actions.GET_ALL_COMMENTS, bite_id);

        setScreenListeners();
    }

    private void setScreenListeners() {
        leftTopButton.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_button:
                goBackScreen();
                break;
            case R.id.send_button:
                sendCommentForPost();
                break;
            default:
                break;
        }

    }

    private void sendCommentForPost() {
        String comment = commentBox.getText().toString();
        if (comment == null || TextUtils.isEmpty(comment)) {
            AppUtils.showToast(this, "comment cannot be blank.");
            return;
        }

        try {
            getData(AppConstants.actions.POST_COMMENT, mPref.getUserId(), bite_id, URLEncoder.encode(comment, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private String TAG = "CommentsReqTag";

    @Override
    public void getData(final int actionID, String... params) {
        if (!ConnectivityUtils.isNetworkEnabled(CommentsScreen.this)) {
            showAlert(getResources().getString(R.string.err_msg), getResources().getString(R.string.error_internet));
            return;
        }
        showProgressDialog(getResources().getString(R.string.loading));

        switch (actionID) {
            case AppConstants.actions.POST_COMMENT:
                RequestManager.addRequest(new GsonObjectRequest<DalitHubBaseModel>(
                        getSendCommentUrl(params[0], params[1], params[2]), null, DalitHubBaseModel.class,
                        new VolleyErrorListener(CommentsScreen.this, actionID)) {
                    @Override
                    protected void deliverResponse(DalitHubBaseModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        CommentsScreen.this.updateUi(true, actionID, response);
                    }
                });
                break;
            case AppConstants.actions.GET_ALL_COMMENTS:
                RequestManager.addRequest(new GsonObjectRequest<CommentsModel>(
                        getAllCommentUrl(params[0]), null, CommentsModel.class,
                        new VolleyErrorListener(CommentsScreen.this, actionID)) {
                    @Override
                    protected void deliverResponse(CommentsModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        CommentsScreen.this.updateUi(true, actionID, response);
                    }
                });
                break;
            case AppConstants.actions.DEL_BITE_COMMENT:
                RequestManager.addRequest(new GsonObjectRequest<DalitHubBaseModel>(
                        getDeleteCommentUrl(params[0]), null, DalitHubBaseModel.class,
                        new VolleyErrorListener(CommentsScreen.this, actionID)) {
                    @Override
                    protected void deliverResponse(DalitHubBaseModel response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        CommentsScreen.this.updateUi(true, actionID, response);
                    }
                });
                break;
        }
    }

    private String getSendCommentUrl(String uId, String bId, String coment) {
        return AppConstants.baseUrl + "commentbite?userId=" + uId + "&biteId=" + bId + "&comment=" + coment;

    }

    private String getAllCommentUrl(String bite_id) {
        return AppConstants.baseUrl + "readcomments?biteid=" + bite_id;


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
        } else if (!((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("113") && !((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("116") && !((DalitHubBaseModel) serviceResponse).getResponseCode().equalsIgnoreCase("122")) {
            showAlert(getResources().getString(R.string.err_msg), ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            Log.e(TAG, "Message from Server : " + ((DalitHubBaseModel) serviceResponse).getResponseDescription());
            return;
        }

        switch (action) {
            case AppConstants.actions.POST_COMMENT: {
                Log.d(TAG, "response is success..........................");
                try {
                    DalitHubBaseModel response = (DalitHubBaseModel) serviceResponse;

                    AppUtils.showToast(CommentsScreen.this,
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());

                    getData(AppConstants.actions.GET_ALL_COMMENTS, bite_id);
                    commentBox.setText(null);
                    RESULT_USER_COMMENTED = 123;


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
            case AppConstants.actions.GET_ALL_COMMENTS:
                Log.d(TAG, "response is success..........................");
                try {
                    CommentsModel response = (CommentsModel) serviceResponse;

                    List<Comment> commentList = response.getComments();
                    if (commentList != null && commentList.size() > 0) {
                        adapter = new CommentsAdapter(CommentsScreen.this, commentList);
                        listview_comments.setAdapter(adapter);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case AppConstants.actions.DEL_BITE_COMMENT:
                Log.d(TAG, "response is success..........................");
                try {
                    DalitHubBaseModel response = (DalitHubBaseModel) serviceResponse;

                    AppUtils.showToast(CommentsScreen.this,
                            ((DalitHubBaseModel) serviceResponse).getResponseDescription());

                    getData(AppConstants.actions.GET_ALL_COMMENTS, bite_id);
                    RESULT_USER_COMMENTED = 123;

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
        }
    }

    @Override
    public void onBackPressed() {
        goBackScreen();
    }

    private void goBackScreen() {
        setResult(RESULT_USER_COMMENTED);
        this.finish();
    }

    public void deleteComment(final String commentId) {
        final AlertDialog dialog = DialogsCustom.promptDialog(this, "Delete Comment", "Do you want to delete this comment","Ok","Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == AlertDialog.BUTTON_POSITIVE) {
                    getData(AppConstants.actions.DEL_BITE_COMMENT, commentId);
                } else {
                }
            }
        });

        dialog.show();
    }

    private String getDeleteCommentUrl(String id) {
        return AppConstants.baseUrl + "DeleteCommment?type=1&commentId=" + id;
    }
}
