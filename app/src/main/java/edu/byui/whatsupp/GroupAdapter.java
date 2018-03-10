package edu.byui.whatsupp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by MikeyG on 3/9/2018.
 */

public class GroupAdapter {
    private Context mContext;
    private List<Group> groups;
    edu.byui.whatsupp.GroupsView activity;

    private LayoutInflater l_Inflater;

    public GroupAdapter(Context c, List<Group> t, Activity a) {
        mContext = c;
        groups = t;
        activity = (edu.byui.whatsupp.GroupsView) a;
        l_Inflater = LayoutInflater.from(c);
    }

    public int getCount() {
        return groups.size();
    }

    public void setList(List<Group> t) {
        groups = t;
    }

    public Object getItem(int position) {
        return groups.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageButton for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        edu.byui.whatsupp.GroupAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.frontview, null);
            holder = new edu.byui.whatsupp.GroupAdapter.ViewHolder();
            holder.Image = (ImageView) convertView.findViewById(R.id.eventpic1);
            holder.MsgType = (TextView) convertView.findViewById(R.id.msgtype1);

            convertView.setTag(holder);
        } else {
            holder = (edu.byui.whatsupp.GroupAdapter.ViewHolder) convertView.getTag();
        }
        Group tempGroup = groups.get(position);
        String imageUrl = tempGroup.getUrl();
        Picasso.with(mContext).load(imageUrl).into(holder.Image); // Sets the group image
        holder.MsgType.setText(tempGroup.getTitle());

        return convertView;
    }

    public void displayGroup() {

    }

    // holder view for views
    static class ViewHolder {
        ImageView Image;
        TextView MsgType;
    }


}
