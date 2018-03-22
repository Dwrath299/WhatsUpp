package edu.byui.whatsupp;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * <h1>User Adapter</h1>
 * The adapter that sets up the display for the
 * listview displaying the Users. This is for
 * displaying the users attending events.
 * <p>
 *
 *
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */
public class UserAdapter extends BaseAdapter {
    private Context mContext;
    private List<User> users;
    edu.byui.whatsupp.ViewEvent activity;

    private LayoutInflater l_Inflater;

    /**
     * The constructor for the adapter
     * @param c is the Context of the activity
     * @param t A list of Users that are going to be displayed
     * @param a an Activity class
     */
    public UserAdapter(Context c, List<User> t, Activity a) {
        mContext = c;
        users = t;
        activity = (edu.byui.whatsupp.ViewEvent) a;
        l_Inflater = LayoutInflater.from(c);
    }


    /**
     * The count is used to get how many items to display
     */
    public int getCount() {
        return users.size();
    }

    /**
     * Changes the list of users, but I am pretty sure
     * it doesn't update the list, so not sure the point
     * of it.
     * @param t A list of User Objects
     */
    public void setList(List<User> t) {
        users = t;
    }


    /**
     * This is used when the user clicks it will return
     * the selected user to the activity to send to their
     * profile
     * @param position an integer that is the index of the user
     *                 in the list.
     */
    public Object getItem(int position) {
        return users.get(position);
    }

    /**
     * Doesn't do anything right now, nor does
     * it need to.
     */
    public long getItemId(int position) {
        return 0;
    }

    /**
     * This is where the User information is converted into a display
     * for the list view.
     * @param position the index of the user being converted
     * @param convertView A View object, we need it, not sure why
     * @param parent also don't understand this one, but we need it
     *
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.userview, null);
            holder = new ViewHolder();
            holder.Image = (ProfilePictureView) convertView.findViewById(R.id.profilePic1);
            holder.MsgType = (TextView) convertView.findViewById(R.id.name1);
            holder.MsgType2 = (TextView) convertView.findViewById(R.id.msg2);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        User tempUser = users.get(position);
        // Set the image for the profile  pic
        holder.Image.setProfileId(tempUser.getUid());
        holder.Image.setPresetSize(-2);
        holder.MsgType.setText(tempUser.getFirstName());
        // Don't need a second text for users, maybe for creator?
        holder.MsgType2.setVisibility(View.INVISIBLE);




        return convertView;
    }


    /**
     * A holder class to get the display to show how we want it.
     * includes two texts and a pic
     */
    static class ViewHolder {
        ProfilePictureView Image;
        TextView MsgType;
        TextView MsgType2;
    }


}