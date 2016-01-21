package com.poject.dalithub.adapters;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.poject.dalithub.R;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.models.Member;
import com.poject.dalithub.models.MembersItems;
import com.poject.dalithub.screens.MemberDetailScreen;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A sample Adapter that demonstrates one use of the SectionableAdapter class,
 * using a hard-coded two-dimensional array for the data source.
 *
 * @author Velos Mobile
 */
/*
 * Copyright � 2012 Velos Mobile
 */
public class MembersSectionAdapter extends SectionableAdapter implements
        View.OnClickListener {

    // For simplicity, we hard-code the headers and data. In an actual app, this
    // can come from the network, the filesystem, SQLite, or any of the
    // usual suspects.
    final ArrayList<MembersItems> AUTHORS;// new String[]{"Roberto Bola�o",
    //  "David Mitchell", "Haruki Murakami", "Thomas Pynchon"};

//    private static final String[][] BOOKS = new String[][]{
//            {"The Savage Detectives", "2666"},
//            {"Ghostwritten", "number9dream", "Cloud Atlas",
//                    "Black Swan Green", "The Thousand Autumns of Jacob de Zoet"},
//            {"A Wild Sheep Chase",
//                    "Hard-Boiled Wonderland and the End of the World",
//                    "Norwegian Wood", "Dance Dance Dance",
//                    "South of the Border, West of the Sun",
//                    "The Wind-Up Bird Chronicle", "Sputnik Sweetheart",
//                    "Kafka on the Shore", "After Dark", "1Q84"},
//            {"V.", "The Crying of Lot 49", "Gravity's Rainbow", "Vineland",
//                    "Mason & Dixon", "Against the Day", "Inherent Vice"}};

    private Activity activity;

    public MembersSectionAdapter(Activity activity, LayoutInflater inflater,
                                 int rowLayoutID, int headerID, int itemHolderID, int resizeMode, ArrayList<MembersItems> listHeaders, HashMap<String, ArrayList<Member>> memMap) {
        super(activity, inflater, rowLayoutID, headerID, itemHolderID, resizeMode, memMap);
        this.activity = activity;
        AUTHORS = listHeaders;
        AppUtils.updateDeviceResolution(activity);
        //this.membersMap = memMap;

    }

    @Override
    public Object getItem(int position) {
        for (int i = 0; i < membersMap.size(); ++i) {
            if (position < membersMap.get(AUTHORS.get(i).getHeaderName()).size()) {
                return membersMap.get(AUTHORS.get(i).getHeaderName()).get(position);
            }
            position -= membersMap.get(AUTHORS.get(i).getHeaderName()).size();
        }
        // This will never happen.
        return null;
    }

    @Override
    protected int getDataCount() {
        int total = 0;
        for (int i = 0; i < membersMap.size(); ++i) {
            total += membersMap.get(AUTHORS.get(i).getHeaderName()).size();
        }
        return total;
    }

    @Override
    protected int getSectionsCount() {
        // return BOOKS.length;
        Log.d("membersMap.size() ", "" + membersMap.size());
        return membersMap.size();
    }

    @Override
    protected int getCountInSection(int index) {
//        return BOOKS[index].length;
        return membersMap.get(AUTHORS.get(index).getHeaderName()).size();
    }

    @Override
    protected int getTypeFor(int position) {
        int runningTotal = 0;
        int i = 0;
        for (i = 0; i < membersMap.size(); ++i) {
            int sectionCount = membersMap.get(AUTHORS.get(i).getHeaderName()).size();
            if (position < runningTotal + sectionCount)
                return i;
            runningTotal += sectionCount;
        }
        // This will never happen.
        return -1;
    }

    @Override
    protected MembersItems getHeaderForSection(int section) {
        return AUTHORS.get(section);
    }

    @Override
    protected void bindView(View convertView, int position) {
        Member member = (Member) getItem(position);
        TextView label = (TextView) convertView
                .findViewById(R.id.bookItem_title);
        ImageView dPic = (ImageView) convertView.findViewById(R.id.profile_image);
        label.setText(member.getUserName());
        label.setTag(member);

        //setting params for imageview
        LinearLayout.LayoutParams imgParams = (LinearLayout.LayoutParams) dPic.getLayoutParams();
        imgParams.width = imgParams.height = (int) (AppUtils.DEVICE_WIDTH * 0.2f);

        String imageUrl = member.getProfileImage();
        if (imageUrl != null && !imageUrl.equals("")) {
            Picasso.with(activity).load(imageUrl).placeholder(R.drawable.img_member).into(dPic);
        }

        convertView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(activity, MemberDetailScreen.class);
        TextView label = (TextView) v.findViewById(R.id.bookItem_title);
        // String text = label.getText().toString();
        Member member = (Member) label.getTag();
        i.putExtra("MemberId", member.getUserId());
        activity.startActivity(i);
    }

}
