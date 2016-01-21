//package com.poject.dalithub.adapters;
//
//import java.util.ArrayList;
//
//import com.poject.dalithub.R;
//import com.poject.dalithub.Utils.Item;
//import com.poject.dalithub.models.NearMeLessThan1;
//import com.poject.dalithub.subFragments.MembersFragment;
//import com.squareup.picasso.Picasso;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//public class MembersAdapter extends BaseAdapter {
//    ArrayList<Item> membersList;
//    Context mContext;
//
//    public MembersAdapter(Context con, ArrayList<Item> list) {
//        membersList = list;
//        mContext = con;
//    }
//
//    @Override
//    public int getCount() {
//        return membersList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return membersList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = null;
//        ViewHolder holder = null;
//        if (convertView == null) {
//
//            Item listItem = membersList.get(position);
//            if (listItem.isSection()) {
//                convertView = LayoutInflater.from(mContext).inflate(
//                        R.layout.members_section_header, null);
//                holder.nameText = (TextView) convertView.findViewById(R.id.sectionText);
//
//            } else {
//                convertView = LayoutInflater.from(mContext).inflate(
//                        R.layout.list_item_members, null);
//                holder.nameText = (TextView) convertView.findViewById(R.id.name_display);
//                holder.profImage = (ImageView) convertView.findViewById(R.id.profile_image);
//            }
//
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        Item listItem = membersList.get(position);
//        if (listItem.isSection()) {
//            MembersFragment.SectionItem entry = (MembersFragment.SectionItem) listItem;
//            holder.sectionText.setText("" + entry.getSectionTitle());
//        } else {
//            MembersFragment.EntryItem entry = (MembersFragment.EntryItem) listItem;
//            NearMeLessThan1 model = entry.getModelItem();
//            holder.nameText.setText("" + model.getUserName());
//
//            if (model.getProfileImage() != null) {
//                Picasso.with(mContext).load(model.getProfileImage()).resize(100, 100).into(holder.profImage);
//            }
//        }
//
//        return convertView;
//    }
//
//    class ViewHolder {
//        TextView nameText, sectionText;
//        ImageView profImage;
//    }
//}
