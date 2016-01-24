package com.poject.dalithub.adapters;

import java.util.ArrayList;
import java.util.List;

import com.poject.dalithub.R;
import com.poject.dalithub.models.EventModel;
import com.poject.dalithub.screens.EventsDetailScreen;
import com.poject.dalithub.subFragments.DalitHubBasefragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EventsAdapter extends BaseAdapter {
    List<EventModel> eventsList;
    Context mContext;
    DalitHubBasefragment fragContext;

    public EventsAdapter(DalitHubBasefragment fCon, Context con, List<EventModel> list) {
        eventsList = list;
        mContext = con;
        fragContext = fCon;
    }

    @Override
    public int getCount() {
        return eventsList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.list_item_events, null);
            holder = new ViewHolder();
            holder.eventName = (TextView) convertView.findViewById(R.id.label_text);
            holder.eventTime = (TextView) convertView.findViewById(R.id.time_display);

            convertView.setTag(holder);
        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        final EventModel events = eventsList.get(position);
        holder.eventName.setText("" + events.getEventName());

        //parsing event date
        String date = events.getEventDate();
        String[] dateArr = date.split(" ");
        String dateString = dateArr[0];
        dateString = dateString.substring(0, dateString.lastIndexOf("/"));
        holder.eventTime.setText(events.getDay() + " " + setEventMonth(events.getMonth()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, EventsDetailScreen.class);
                intent.putExtra("event_id", events.getEventId());
                fragContext.startActivity(intent);
            }
        });
        return convertView;
    }

    private String setEventMonth(String indexVal) {
        int index = Integer.parseInt(indexVal);

        String[] monthsArr = mContext.getResources().getStringArray(R.array.events_month_caps);
        return monthsArr[index - 1];
    }

    class ViewHolder {
        TextView eventName;
        TextView eventTime;
    }
}
