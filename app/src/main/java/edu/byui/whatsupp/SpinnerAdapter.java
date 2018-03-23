package edu.byui.whatsupp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.List;

/**
 * Created by MikeyG on 3/14/2018.
 */

public class SpinnerAdapter extends BaseAdapter {
    private Context mContext;
    private List<User> users;
    edu.byui.whatsupp.GroupForm activity;

    private LayoutInflater l_Inflater;
    public SpinnerAdapter(Context c, List<User> t, Activity a) {
        mContext = c;
        users = t;
        activity = (edu.byui.whatsupp.GroupForm) a;
        l_Inflater = LayoutInflater.from(c);
    }

    public int getCount() {
        return users.size();
    }

    public void setList(List<User> t) {
        users = t;
    }

    public Object getItem(int position) {
        return users.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageButton for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        SpinnerAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.simple_spinner_dropdown, null);
            holder = new SpinnerAdapter.ViewHolder();
            holder.Image = (ProfilePictureView) convertView.findViewById(R.id.profilePic2);
            holder.MsgType = (TextView) convertView.findViewById(R.id.nameView);


            convertView.setTag(holder);
        } else {
            holder = (SpinnerAdapter.ViewHolder) convertView.getTag();
        }
        User tempUser = users.get(position);
        // Set the image for the profile  pic
        holder.Image.setProfileId(tempUser.getUid());
        holder.Image.setPresetSize(-2);
        holder.MsgType.setText(tempUser.getFirstName());



        return convertView;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        SpinnerAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.simple_spinner_dropdown, null);
            holder = new SpinnerAdapter.ViewHolder();
            holder.Image = (ProfilePictureView) convertView.findViewById(R.id.profilePic2);
            holder.MsgType = (TextView) convertView.findViewById(R.id.nameView);


            convertView.setTag(holder);
        } else {
            holder = (SpinnerAdapter.ViewHolder) convertView.getTag();
        }
        User tempUser = users.get(position);
        // Set the image for the profile  pic
        holder.Image.setProfileId(tempUser.getUid());
        holder.Image.setPresetSize(-2);
        holder.MsgType.setText(tempUser.getFirstName());





        return convertView;
    }

    public void displayThingToDo() {

    }

    // holder view for views
    static class ViewHolder {
        ProfilePictureView Image;
        TextView MsgType;

    }


}
