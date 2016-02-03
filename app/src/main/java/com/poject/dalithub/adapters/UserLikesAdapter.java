package com.poject.dalithub.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.code.linkedinapi.schema.Like;
import com.poject.dalithub.R;
import com.poject.dalithub.Utils.AppUtils;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.models.LikesBean;
import com.poject.dalithub.models.NewBite;
import com.poject.dalithub.screens.BitesFullScreenView;
import com.poject.dalithub.screens.LandingScreenActivity;
import com.poject.dalithub.screens.MemberDetailScreen;
import com.poject.dalithub.subFragments.DalitHubBasefragment;
import com.poject.dalithub.subFragments.MyBitesFragment;
import com.squareup.picasso.Picasso;

import java.util.List;


public class UserLikesAdapter extends BaseAdapter {
    List<LikesBean> myBitesList;
    Context mContext;
    DalitHubBasefragment fragContext;
    UserPreferences mPref;

    public UserLikesAdapter(Context con, List<LikesBean> list) {
        myBitesList = list;
        mContext = con;
        mPref = new UserPreferences((Activity) mContext);
        AppUtils.updateDeviceResolution((Activity) con);
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
                    R.layout.list_item_members, null);

            holder = new ViewHolder();
            holder.profile_image = (ImageView) convertView.findViewById(R.id.profile_image);
            holder.uName = (TextView) convertView.findViewById(R.id.bookItem_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final LikesBean likesData = myBitesList.get(position);
        holder.uName.setText(likesData.getUserName());
        String imageUrl = likesData.getProfileImage();
        if (imageUrl != null && !imageUrl.equals("")) {
            Picasso.with(mContext).load(imageUrl).placeholder(R.drawable.img_member).into(holder.profile_image);
        }


        return convertView;
    }

    class ViewHolder {

        private TextView uName;
        private ImageView profile_image;
    }

}
