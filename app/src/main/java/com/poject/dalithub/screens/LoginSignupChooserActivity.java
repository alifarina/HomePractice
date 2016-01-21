package com.poject.dalithub.screens;

import com.poject.dalithub.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginSignupChooserActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chooser_login_signup);
	}

	public void loginButtonClick(View v) {

		Intent in = new Intent(this, LoginActivity.class);
		startActivity(in);
	}

	public void signupButtonClick(View v) {

		Intent in = new Intent(this, SignUpActivity.class);
		startActivity(in);
	}
}
