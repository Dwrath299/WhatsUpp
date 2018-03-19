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

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Dallin's PC on 3/2/2018.
 */

public class EventAdapter extends BaseAdapter {
    private Context mContext;
    private List<Event> events;
    edu.byui.whatsupp.ViewThingToDo thingActivity;
    edu.byui.whatsupp.Profile profileActivity;
    int page;

    private LayoutInflater l_Inflater;
    public EventAdapter(Context c, List<Event> t, Activity a, int page) {
        mContext = c;
        events = t;
        this.page = page;
        if (page == 1)
            thingActivity = (edu.byui.whatsupp.ViewThingToDo) a;
        else if(page == 2)
            profileActivity = (edu.byui.whatsupp.Profile) a;
        l_Inflater = LayoutInflater.from(c);
    }

    public int getCount() {
        return events.size();
    }

    public void setList(List<Event> t) {
        events = t;
    }

    public Object getItem(int position) {
        return events.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageButton for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.frontview, null);
            holder = new ViewHolder();
            holder.Image = (ImageView) convertView.findViewById(R.id.eventpic1);
            holder.MsgType = (TextView) convertView.findViewById(R.id.msgtype1);
            holder.MsgType2 = (TextView) convertView.findViewById(R.id.msgtype2);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Event tempEvent = events.get(position);
        String imageUrl = tempEvent.getUrl();
        Picasso.with(mContext).load(imageUrl).into(holder.Image); // Sets the event image
        holder.MsgType.setText(tempEvent.getTitle());
        if(tempEvent.getTime() != null) {
            String time = tempEvent.getTime() + " " + tempEvent.getDate();
            holder.MsgType2.setText(time);
        } else {
            holder.MsgType2.setText("");
        }


        return convertView;
    }

    public void displayThingToDo() {

    }

    // holder view for views
    static class ViewHolder {
        ImageView Image;
        TextView MsgType;
        TextView MsgType2;
    }


}