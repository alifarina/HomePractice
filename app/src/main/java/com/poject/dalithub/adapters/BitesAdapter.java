package com.poject.dalithub.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.poject.dalithub.R;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.models.Member;
import com.poject.dalithub.models.NewBite;
import com.poject.dalithub.screens.BitesFullScreenView;
import com.poject.dalithub.screens.LandingScreenActivity;
import com.poject.dalithub.screens.MemberDetailScreen;
import com.poject.dalithub.subFragments.DalitHubBasefragment;
import com.poject.dalithub.subFragments.MyBitesFragment;
import com.squareup.picasso.Picasso;


public class BitesAdapter extends BaseAdapter {
    List<NewBite> myBitesList;
    Context mContext;
    DalitHubBasefragment fragContext;
    UserPreferences mPref;

    public BitesAdapter(DalitHubBasefragment frag, Context con, List<NewBite> list) {
        myBitesList = list;
        mContext = con;
        fragContext = frag;
        mPref = new UserPreferences((LandingScreenActivity) mContext);
        AppUtils.updateDeviceResolution(frag.getLandingActivityContext());
    }

    @Override
    public int getCount() {
        return myBitesList.size();
    }

    @Override
    public Object getItem(int position) {
        return myBitesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = null;
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.list_item_bites, null);

            holder = new ViewHolder();
            holder.numComments = (TextView) convertView
                    .findViewById(R.id.num_comments);
            holder.biteMessage = (TextView) convertView
                    .findViewById(R.id.bite_msg);
            holder.numLikes = (TextView) convertView
                    .findViewById(R.id.num_likes);
            holder.postImage = (ImageView) convertView
                    .findViewById(R.id.post_image);
            holder.uName = (TextView) convertView
                    .findViewById(R.id.username);
            holder.timeDisplay = (TextView) convertView.findViewById(R.id.time_display);
            holder.profile_image = (ImageView) convertView.findViewById(R.id.profile_image);
            holder.userLocation = (TextView) convertView.findViewById(R.id.location);
            holder.spamClick = (TextView) convertView.findViewById(R.id.spam);
            holder.deleteClick = (TextView) convertView.findViewById(R.id.delete);
            holder.moreIcon = (ImageView) convertView.findViewById(R.id.moreIcon);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final NewBite biteData = myBitesList.get(position);
        System.out.println("========" + biteData.getLikecount());
        holder.biteMessage.setText(Html.fromHtml(biteData.getBiteMessage()));
        holder.numLikes.setText("" + biteData.getLikecount());
        holder.numComments.setText("" + biteData.getCommentcount());
        holder.uName.setText("" + biteData.getUserName());
        Log.d("getTime ", biteData.getTime());
        holder.timeDisplay.setText(" " + biteData.getTime());
        holder.userLocation.setText("" + biteData.getUserLocation());


//        holder.deleteClick.setVisibility(View.GONE);
        Log.d("posted id " + biteData.getPostedUserId(), " user id " + mPref.getUserId());

        holder.uName.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, MemberDetailScreen.class);
                i.putExtra("MemberId", biteData.getPostedUserId());
                mContext.startActivity(i);
            }
        });

        if (biteData.getLikestatus().equals("1")) {
            Log.d("Tag", "getLikeStatus === 1");
            holder.numLikes.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.like), null, null, null);
        } else {
            Log.d("Tag", "getLikeStatus === 0");
            holder.numLikes.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.dislike), null, null, null);
        }
        holder.postImage.setVisibility(View.GONE);
        if (biteData.getImageUrl() != null && !biteData.getImageUrl().equals("")) {
            LinearLayout.LayoutParams postImageParams = (LinearLayout.LayoutParams) holder.postImage.getLayoutParams();
            postImageParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            postImageParams.height = (int) (AppUtils.DEVICE_HEIGHT * 0.3f);
            holder.postImage.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(biteData.getImageUrl()).placeholder(-1).into(holder.postImage);
        }

        if (biteData.getProfileImage() != null && !biteData.getProfileImage().equals("")) {

            int profileParams = (int) (AppUtils.DEVICE_HEIGHT * 0.1f);

            Picasso.with(mContext).load(biteData.getProfileImage()).resize(profileParams, profileParams).into(holder.profile_image);
        }

        holder.numComments.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ((MyBitesFragment) fragContext).goToCommentsSCreen(biteData.getBiteid());
            }
        });
        holder.numLikes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyBitesFragment) fragContext).setLikesForThisPost(biteData.getBiteid(), position, (int) view.getY());
            }
        });
//        holder.spamClick.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((MyBitesFragment) fragContext).markContentAsSpam(biteData.getBiteid(), "3");
//            }
//        });
//        holder.deleteClick.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((MyBitesFragment) fragContext).markContentAsSpam(biteData.getBiteid(), "2");
//            }
//        });
        holder.postImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, BitesFullScreenView.class);
                intent.putExtra("PostName", biteData.getUserName());
                intent.putExtra("PostUrl", biteData.getImageUrl());
                intent.putExtra("PostId", biteData.getBiteid());
                mContext.startActivity(intent);
            }
        });

        holder.moreIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MyBitesFragment) fragContext).showDeleteSelectionDialog(biteData.getPostedUserId(), biteData.getBiteid());
            }
        });

        return convertView;
    }

    class ViewHolder {
        private TextView numComments;
        private TextView biteMessage;
        private TextView numLikes;
        private TextView spamClick;
        private TextView deleteClick;

        private TextView uName;
        private ImageView postImage, profile_image, moreIcon;
        private TextView timeDisplay, userLocation;
    }

}
