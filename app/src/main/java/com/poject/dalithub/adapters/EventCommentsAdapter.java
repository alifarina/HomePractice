package com.poject.dalithub.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.poject.dalithub.R;
import com.poject.dalithub.Utils.UserPreferences;
import com.poject.dalithub.models.Comment;
import com.poject.dalithub.models.EventCommentModel;
import com.poject.dalithub.screens.CommentsScreen;
import com.poject.dalithub.screens.EventsCommentsScreen;
import com.poject.dalithub.subFragments.MyBitesFragment;

import java.util.List;

public class EventCommentsAdapter extends BaseAdapter {
    List<EventCommentModel> commentsList;
    Context mContext;
    UserPreferences mPref;

    public EventCommentsAdapter(Context con, List<EventCommentModel> list) {
        commentsList = list;
        mContext = con;
        mPref = new UserPreferences((Activity) con);
    }

    @Override
    public int getCount() {
        return commentsList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            v = LayoutInflater.from(mContext).inflate(
                    R.layout.list_item_comments, null);

            holder = new ViewHolder();
            holder.time = (TextView) v.findViewById(R.id.time);
            holder.content = (TextView) v.findViewById(R.id.comment_content);
            holder.deleteIcon = (ImageView) v.findViewById(R.id.deleteComment);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        final EventCommentModel comment = commentsList.get(position);
        holder.content.setText(Html.fromHtml("<B>" + comment.getUserName() + "</B> " + comment.getComment()));
        Log.d("comment.getPostedDate()", comment.getCommentedTime());
        holder.time.setText(comment.getCommentedTime());

        //visibility for delete icon
        holder.deleteIcon.setVisibility(View.GONE);
        if (comment.getUserId().equals(mPref.getUserId()))
            holder.deleteIcon.setVisibility(View.VISIBLE);

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((EventsCommentsScreen) mContext).deleteComment(comment.getEventcommentid());
            }
        });

        return v;
    }

    private class ViewHolder {
        TextView time;
        TextView content;
        ImageView deleteIcon;
    }
}
