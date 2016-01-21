package com.poject.dalithub.screens;

import com.poject.dalithub.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class InviteMemberScreen extends Activity implements OnClickListener {
	private ImageView leftTopButton, rightTopButton;
	private TextView headerTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite_layout);
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

		rightTopButton.setVisibility(View.GONE);
		headerTitle.setText("INVITE MEMBER");
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

	private void goBackScreen() {
		this.finish();
	}
}
