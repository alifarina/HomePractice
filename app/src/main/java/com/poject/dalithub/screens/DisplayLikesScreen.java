package com.poject.dalithub.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.code.linkedinapi.schema.Likes;
import com.kelltontech.utils.ConnectivityUtils;
import com.kelltontech.volley.ext.GsonObjectRequest;
import com.kelltontech.volley.ext.RequestManager;
import com.poject.dalithub.R;
import com.poject.dalithub.Utils.AppConstants;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.adapters.UserLikesAdapter;
import com.poject.dalithub.listener.VolleyErrorListener;
import com.poject.dalithub.models.DalitHubBaseModel;
import com.poject.dalithub.models.LikesBean;
import com.poject.dalithub.models.UserLikes;

import java.util.List;

public class DisplayLikesScreen extends DalitHubBaseActivity implements OnClickListener {
    private ImageView leftTopButton, rightTopButton;
    private TextView headerTitle;
    private String TAG = "InviteMEmber";
    private EditText emailBox;
    private UserPreferences mPref;
    private String biteId;
    private GridView mGridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.likes_display);
        mPref = new UserPreferences(this);
        initViews();

        biteId = getIntent().getStringExtra("bite_id");
        setScreenListeners();

        getData(AppConstants.actions.GET_USER_LIES, biteId);
    }

    private void setScreenListeners() {
        leftTopButton.setOnClickListener(this);
    }

    private void initViews() {
        leftTopButton = (ImageView) findViewById(R.id.left_button);
        rightTopButton = (ImageView) findViewById(R.id.right_button);
        headerTitle = (TextView) findViewById(R.id.title);
        mGridview = (GridView) findViewById(R.id.likes_grid);

        rightTopButton.setVisibility(View.GONE);
        headerTitle.setText("BITE LIKES");


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

        String mailId = emailBox.getText().toString();
        if (mailId != null && !mailId.equals("")) {
            if (AppUtils.isEmailAddress(mailId)) {
                getData(AppConstants.actions.INVITE_MEMBER, mPref.getUserId(), mailId);
            } else {
                AppUtils.showToast(this, "email id is not valid.");
            }
        } else {
            AppUtils.showToast(this, "Please enter a email id");
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
            case AppConstants.actions.GET_USER_LIES:
                RequestManager.addRequest(new GsonObjectRequest<UserLikes>(
                        getLikesUrl(params[0]), null, UserLikes.class,
                        new VolleyErrorListener(DisplayLikesScreen.this, actionID)) {
                    @Override
                    protected void deliverResponse(UserLikes response) {
                        if (mResponse != null && mResponse.data != null) {
                            String data = new String(mResponse.data);
                            Log.d(TAG, "response json--->" + data);
                        }

                        DisplayLikesScreen.this.updateUi(true, actionID, response);
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
            case AppConstants.actions.GET_USER_LIES: {
                Log.d(TAG, "response is success..........................");
                try {
                    UserLikes response = (UserLikes) serviceResponse;

                    AppUtils.showToast(DisplayLikesScreen.this,
                            response.getResponseDescription());

                    setAdapter(response);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            break;
        }
    }

    private UserLikesAdapter adapter;

    private void setAdapter(UserLikes response) {
        if (response != null) {
            List<LikesBean> userLikesList = response.getComments();
            adapter = new UserLikesAdapter(this, userLikesList);
            mGridview.setAdapter(adapter);

            mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    LikesBean likesBean = (LikesBean) adapter.getItem(position);
                    String memberId = likesBean.getUserId();
                    Intent intent = new Intent(DisplayLikesScreen.this, MemberDetailScreen.class);
                    intent.putExtra("MemberId", memberId);
                    startActivity(intent);

                }
            });
        }
    }

    private void goBackScreen() {
        this.finish();
    }

    private String getLikesUrl(String biteId) {
        return AppConstants.baseUrl + "readlikes?biteid=" + biteId;
    }
}
