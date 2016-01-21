package com.poject.dalithub.adapters;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.poject.dalithub.R;
import com.poject.dalithub.models.Comment;
import com.poject.dalithub.screens.CommentsScreen;

import org.w3c.dom.Text;

public class CommentsAdapter extends BaseAdapter {
    List<Comment> commentsList;
    Context mContext;

    public CommentsAdapter(Context con, List<Comment> list) {
        commentsList = list;
        mContext = con;
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

        final Comment comment = commentsList.get(position);
        holder.content.setText(Html.fromHtml("<B>" + comment.getUserName() + "</B> " + comment.getComment()));
        Log.d("comment.getPostedDate()", comment.getPostedDate());
        holder.time.setText(comment.getPostedDate());

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CommentsScreen) mContext).deleteComment(comment.getBitecommentid());
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
