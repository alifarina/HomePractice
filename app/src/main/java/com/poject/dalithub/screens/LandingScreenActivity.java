package com.poject.dalithub.screens;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poject.dalithub.Utils.ClickHandler;
import com.poject.dalithub.subFragments.EventsFragment;
import com.poject.dalithub.subFragments.MembersFragment;
import com.poject.dalithub.subFragments.MyBitesFragment;
import com.poject.dalithub.subFragments.ProfileFragment;
import com.poject.dalithub.R;

public class LandingScreenActivity extends DalitHubBaseActivity {
	private ImageView bites_image, events_image, mem_image, profile_image;
	private TextView tv_bites, tv_events, tv_members, tv_profile, headerTitle;
	private LinearLayout frag_container;
	FragmentManager fragManager;
	private ImageView leftTopButton, rightTopButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.landing_screen_layout);
		initViews();
		fragManager = getSupportFragmentManager();
		inflateBitesScreen();

	}

	public void setHeaderButtonListeners(OnClickListener m) {
		leftTopButton.setOnClickListener(m);
		rightTopButton.setOnClickListener(m);
	}

	private void showBothHeaderButtons(int left_status, int right_status) {
		leftTopButton.setVisibility(left_status);
		rightTopButton.setVisibility(right_status);
	}

	private void initViews() {
		bites_image = (ImageView) findViewById(R.id.iv_bites);
		events_image = (ImageView) findViewById(R.id.iv_events);
		mem_image = (ImageView) findViewById(R.id.iv_members);
		profile_image = (ImageView) findViewById(R.id.iv_profile);

		tv_bites = (TextView) findViewById(R.id.tv_bites);
		tv_events = (TextView) findViewById(R.id.tv_events);
		tv_members = (TextView) findViewById(R.id.tv_members);
		tv_profile = (TextView) findViewById(R.id.tv_profile);

		frag_container = (LinearLayout) findViewById(R.id.frag_container);

		leftTopButton = (ImageView) findViewById(R.id.left_button);
		rightTopButton = (ImageView) findViewById(R.id.right_button);
		headerTitle = (TextView) findViewById(R.id.title);
	}

	private void setAllTabItemsFalse() {
		bites_image.setSelected(false);
		events_image.setSelected(false);
		mem_image.setSelected(false);
		profile_image.setSelected(false);

		tv_bites.setSelected(false);
		tv_events.setSelected(false);
		tv_members.setSelected(false);
		tv_profile.setSelected(false);
	}

	public void tabBarClick(View v) {

		// setting all the other tabs to unselected state
		setAllTabItemsFalse();

		switch (v.getId()) {
		case R.id.bites_tab_layout:
			inflateBitesScreen();
			break;
		case R.id.events_tab_layout:
			inflateEventsScreen();
			break;
		case R.id.mem_tab_layout:
			inflateMembersScreen();
			break;
		case R.id.profile_tab_layout:
			inflateProfileScreen();
			break;
		default:
			break;
		}
	}

	private void inflateBitesScreen() {
		bites_image.setSelected(true);
		tv_bites.setSelected(true);

		showBothHeaderButtons(1, 1);
		leftTopButton.setImageResource(R.drawable.refresh);
		rightTopButton.setImageResource(R.drawable.plus);
		headerTitle.setText("BITES");

		MyBitesFragment bitesFrag = new MyBitesFragment();

		FragmentTransaction fTransact = fragManager.beginTransaction();
		fTransact.replace(frag_container.getId(), bitesFrag);
		fTransact.commit();
	}

	private void inflateEventsScreen() {
		events_image.setSelected(true);
		tv_events.setSelected(true);

		showBothHeaderButtons(1, 1);
		leftTopButton.setImageResource(R.drawable.refresh);
		rightTopButton.setImageResource(R.drawable.plus);
		headerTitle.setText("EVENTS");

		EventsFragment bitesFrag = new EventsFragment();

		FragmentTransaction fTransact = fragManager.beginTransaction();
		fTransact.replace(frag_container.getId(), bitesFrag);
		fTransact.commit();
	}

	private void inflateMembersScreen() {
		mem_image.setSelected(true);
		tv_members.setSelected(true);

		showBothHeaderButtons(1, 1);
		leftTopButton.setImageResource(R.drawable.refresh);
		rightTopButton.setImageResource(R.drawable.add_member);
		headerTitle.setText("MEMBER");

		MembersFragment bitesFrag = new MembersFragment();

		FragmentTransaction fTransact = fragManager.beginTransaction();
		fTransact.replace(frag_container.getId(), bitesFrag);
		fTransact.commit();
	}

	private void inflateProfileScreen() {
		profile_image.setSelected(true);
		tv_profile.setSelected(true);

		showBothHeaderButtons(1, 1);
		leftTopButton.setImageResource(R.drawable.refresh);
		rightTopButton.setImageResource(R.drawable.setting);
		headerTitle.setText("PROFILE");

		ProfileFragment bitesFrag = new ProfileFragment();

		FragmentTransaction fTransact = fragManager.beginTransaction();
		fTransact.replace(frag_container.getId(), bitesFrag);
		fTransact.commit();
	}

}
