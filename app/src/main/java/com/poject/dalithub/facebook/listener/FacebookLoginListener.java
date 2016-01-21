package com.poject.dalithub.facebook.listener;


import com.poject.dalithub.facebook.model.UserInfo;

/**
 * Login Facebook Listener
 * 
 * @author monish.agarwal
 * 
 */
public interface FacebookLoginListener {

	public void doAfterLogin(UserInfo userInfo);

}
