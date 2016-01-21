package com.poject.dalithub.facebook.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.gson.Gson;
import com.poject.dalithub.R;
import com.poject.dalithub.facebook.listener.FacebookLoginListener;
import com.poject.dalithub.facebook.listener.FacebookPostListener;

/**
 * 
 * @author
 * 
 */
public class FacebookUtils {
	// private static final List<String> PERMISSIONS = Arrays
	// .asList("publish_actions");
	private static Activity			mActivity;

	static UserInfo					mUserInfo;

	private static PendingAction	pendingAction	= PendingAction.NONE;
    private static String TAG = "FacebookUtils";
    //String TAG = "FacebookUtils";

	// private RestaurantDetailModel mRestaurantDetailModel;

	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	// private static boolean isSubsetOf(Collection<String> subset,
	// Collection<String> superset) {
	// for (String string : subset) {
	// if (!superset.contains(string)) {
	// return false;
	// }
	// }
	// return true;
	// }

	// public static void postOnWall(Activity pActivity, final
	// FacebookPostListener facebookPostListener) {
	// mActivity = pActivity;
	//
	// Session session = Session.getActiveSession();
	// if (session.getState().isOpened()) {
	// if (session != null) {
	//
	// // Check for publish permissions
	// List<String> permissions = session.getPermissions();
	// if (!isSubsetOf(PERMISSIONS, permissions)) {
	//
	// Session.NewPermissionsRequest newPermissionsRequest = new
	// Session.NewPermissionsRequest(mActivity, PERMISSIONS);
	// session.requestNewPublishPermissions(newPermissionsRequest);
	// return;
	// }
	//
	// Bundle postParams = new Bundle();
	// postParams.putString("name", "Facebook SDK for Android");
	// postParams.putString("picture",
	// "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");
	//
	// Request.Callback callback = new Request.Callback() {
	// public void onCompleted(Response response) {
	// Log.d("FACEBOOK--->>", response.toString());
	// JSONObject graphResponse =
	// response.getGraphObject().getInnerJSONObject();
	// String postId = null;
	// try {
	// postId = graphResponse.getString("id");
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// FacebookRequestError error = response.getError();
	//
	// if (error != null) {
	// facebookPostListener.doAfterPostOnWall(true);
	//
	// } else {
	// facebookPostListener.doAfterPostOnWall(false);
	// }
	//
	// }
	// };
	//
	// Request request = new Request(session, "me/feed", postParams,
	// HttpMethod.POST, callback);
	//
	// RequestAsyncTask task = new RequestAsyncTask(request);
	// task.execute();
	// }
	// } else {
	// loginFacebook(mActivity, new FacebookLoginListener() {
	//
	// @Override
	// public void doAfterLogin(UserInfo userInfo) {
	//
	// postOnWall(mActivity, facebookPostListener);
	//
	// }
	// });
	// }
	//
	// }

	public static void postOnWall(Activity pActivity, final FacebookPostListener facebookPostListener, final String title, final String msg, final String downloadURL) {
        mActivity = pActivity;
        Session session = Session.getActiveSession();
        if (session != null && session.getState().isOpened()) {
            if (session != null) {
                Bundle prms = new Bundle();
                prms.putString("name", "Nasscom GDC 2015");
                prms.putString("caption", title);
                prms.putString("link", downloadURL);
                prms.putString("picture","http://www.nasscom.in/sites/all/themes/nasscom_new/images/logo.jpg");
//                prms.putString("link", "http://www.iit-2015.org/wp-content/uploads/2015/01/logotest.png");

//                prms.putString("picture", image);
                Log.d(TAG, "sharingmsg-->"+msg);
                prms.putString("description", msg);

                WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(
                        mActivity, session, prms)).setOnCompleteListener(
                        new OnCompleteListener() {

                            @Override
                            public void onComplete(Bundle values,
                                                   FacebookException error) {
                                if (error == null) {
                                    final String postId = values
                                            .getString("post_id");
                                    if (postId != null) {
                                        System.out.println("post success-->"
                                                + postId);
                                        facebookPostListener
                                                .doAfterPostOnWall(true);
                                    } else {
                                        facebookPostListener
                                                .doAfterPostOnWall(false);
                                    }
                                } else if (error instanceof FacebookOperationCanceledException) {
                                    facebookPostListener
                                            .doAfterPostOnWall(false);
                                } else {
                                    facebookPostListener
                                            .doAfterPostOnWall(false);
                                }
                            }
                        }).build();
                feedDialog.show();
            }
        } else {
            loginFacebook(mActivity, new FacebookLoginListener() {
                @Override
                public void doAfterLogin(UserInfo userInfo) {
                   // postOnWall(mActivity, facebookPostListener,title,msg,downloadURL);
                }
            });
        }
    }

	public static void loginFacebook(Activity pActivity, final FacebookLoginListener facebookListener) {
		mActivity = pActivity;
		Session session = new Session(mActivity);
		Session.OpenRequest request = new Session.OpenRequest(mActivity).setPermissions("email");
		request.setCallback(new Session.StatusCallback() {
			// Put your callback code here
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					Request.newMeRequest(session, new GraphUserCallback() {

                        @Override
                        public void onCompleted(GraphUser user, Response response) {
                            if (user != null) {

                                System.out.println("facebook login success->" + user.getInnerJSONObject());

                                Gson gson = new Gson();

                                UserInfo userInfo = gson.fromJson(user.getInnerJSONObject().toString(), UserInfo.class);

                                facebookListener.doAfterLogin(userInfo);

                            } else {
                                System.out.println("facebook login failed->" + response.getError());
                            }
                        }

                    }).executeAsync();

				}
			}
		});
		Session.setActiveSession(session);
		session.openForRead(request);
	}

	public static void logout() {
		Session session = Session.getActiveSession();
		if (session != null)
			session.closeAndClearTokenInformation();

	}

	public static Session.StatusCallback	callback	= new Session.StatusCallback() {
															@Override
															public void call(Session session, SessionState state, Exception exception) {
																onSessionStateChange(session, state, exception);
															}
														};

	private static void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (pendingAction != PendingAction.NONE && (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
			new AlertDialog.Builder(mActivity).setTitle(R.string.cancelled).setMessage(R.string.permission_not_granted).setPositiveButton(R.string.ok, null).show();
			pendingAction = PendingAction.NONE;
		} else if (state == SessionState.OPENED_TOKEN_UPDATED) {

		}
		// updateUI();
	}

	// public static void updateUI() {// to update UI at time of login and
	// // logout....................
	// Session session = Session.getActiveSession();
	//
	// }

	public static FacebookDialog.Callback	dialogCallback	= new FacebookDialog.Callback() {
		@Override
		public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
			    Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
		}

		@Override
		public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
		    Log.d("HelloFacebook", "Success!" + data.toString());
		}
	};
}
