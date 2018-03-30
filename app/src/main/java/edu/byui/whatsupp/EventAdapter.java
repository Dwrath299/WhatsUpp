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
 * <h1>Event Adapter</h1>
 * A class to display the events in a list view. This is used by
 * Viewing Thing To Do page, Profile Page, and Group View Page
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */

public class EventAdapter extends BaseAdapter {
    private Context mContext;
    private List<Event> events;
	// Each activity has specific functions to call
    edu.byui.whatsupp.ViewThingToDo thingActivity;
    edu.byui.whatsupp.Profile profileActivity;
    int page;

    private LayoutInflater l_Inflater;
	
	/**
     * Event Adapter Constructor
	 * Specific to the page that is using it
	 * @param c The Context of the activity using it.
	 * @param t A list of events to display in the list View
	 * @param a The activity using this adapter.
	 * @param page To let the adapter know what page is using this adapter.
					1 = View Thing To Do page
					2 = Profile page
					3 = View Group page
     */
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

	/**
     * Get Count
	 * How many events are there?
	 * @return events.size() the number of events.
     */
    public int getCount() {
        return events.size();
    }

	/**
     * Set List
	 * The list of the events
	 * @param t List<Event>
     */
    public void setList(List<Event> t) {
        events = t;
    }

	
	/**
     * Get Item
	 * Returns the event at the position
	 * @param position Index of the event
	 * @return event 
     */
    public Object getItem(int position) {
        return events.get(position);
    }

	/**
     * Get Item ID
	 * This does nothing right now... 
	 * It was here and I didn't touch it.
	 * @param position Index of the event
	 * @return 0
     */
    public long getItemId(int position) {
        return 0;
    }

	/**
     * Get View
	 * When you set list view adapter to this, it will run through
	 * each of the events and set them up for display
	 * 
	 * @param position Index of the event
	 * @param convertView don't know what this is, but it works!
	 * @param parent ditto to convertView
	 * @return convertView  
     */
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

   

    /**
     * View Holder
	 * The holder for each of the things needed to display 
	 * in the event.
	 * 
     */
    static class ViewHolder {
        ImageView Image;
        TextView MsgType;
        TextView MsgType2;
    }


}