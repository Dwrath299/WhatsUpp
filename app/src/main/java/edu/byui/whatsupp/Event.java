package edu.byui.whatsupp;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Event</h1>
 *  This is to store all the information needed for an
 *  event to be stored and retrieved from the
 *  database
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */

public class    Event {

    String creator;
    ArrayList<String> attendees;
    String thingToDo;
    String title;
    String description;
    String reference;
    String date;
    String time;
    String group;
    String url;
    boolean isPublic;

	
	/**
     * Event Constructor (Filler Event)
	 *  Mainly for the filler event of "No events" event in the things view.
	 * @param string The title of the event
	 * @param url The url of the picture to be used for the event
     */
    public Event(String string, String url) {
        title = string;
        this.url = url;
    }
    
	/**
     * Event Constructor (Public Event)
	 * If the user creates a public event from a Thing To Do View.
	 * Any user in the app can join this event
	 * @param string The title of the event
	 * @param description The description of the Event
	 * @param date When the event is taking place.
	 * @param time When the event is taking place.
	 * @param thingToDo The place where the event is.
	 * @param url The url of the picture to be used for the event
     */
    public Event(String string, String description, String date, String time, String thingToDo, String url) {
        title = string;
        this.description = description;
        this.date = date;
        this.time = time;
        this.thingToDo = thingToDo;
        this.url = url;
        attendees = new ArrayList<String>();
    }

    /**
     * Event Constructor (Private Event)
	 * If the user creates a private event from a Group View.
	 * Any user in the group can join this event
	 * @param string The title of the event
	 * @param description The description of the Event
	 * @param date When the event is taking place.
	 * @param time When the event is taking place.
	 * @param thingToDo The place where the event is.
	 * @param url The url of the picture to be used for the event
	 * @param group The title of the group that this event is for
     */
    public Event(String string, String description, String date, String time, String thingToDo, String url, String group) {
        title = string;
        this.description = description;
        this.date = date;
        this.time = time;
        this.thingToDo = thingToDo;
        this.url = url;
        this.group = group;
        attendees = new ArrayList<String>();
    }

	/**
     * Get Reference
	 * The string database reference of the event
	 * for easy access.
	 * @return Reference
     */
    public String getReference() {
        return reference;
    }

	/**
     * Set Reference
	 * The string database reference of the event
	 * for easy access.
	 * @param Reference
     */
    public void setRefrence(String reference) {
        this.reference = reference;
    }

	
	/**
     * Get Title
	 * The string of the event title
	 * @return title
     */
    public String getTitle() {
        return title;
    }

	/**
     * Set Title
	 * The string of the event title
	 * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

	
	/**
     * Get Creator
	 * The user that created the event. Store this so that
	 * the user can edit the event if needed.
	 * @return creator The string of the UID of the user
     */
    public String getCreator() {
        return creator;
    }

	/**
     * Set Creator
	 * The user that created the event. Store this so that
	 * the user can edit the event if needed.
	 * @param creator The string of the UID of the user
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

	/**
     * Get Attendees
	 * The list of users that are attending the
	 * event. 
	 * @return attendees A list of strings of the UID of the user.
     */
    public ArrayList<String> getAttendees() {
        return attendees;
    }

	/**
     * Set Attendees
	 * The list of users that are attending the
	 * event. 
	 * @param attendees A list of strings of the UID of the user
     */
    public void setAttendees(ArrayList<String> attendees) {
        this.attendees = attendees;
    }

	/**
     * Add Attendee
	 * Add a single user to the list of users going .
	 * @return attendees A list of strings of the UID of the user.
     */
    public void addAttendee(String uid) {
        attendees.add(uid);
    }

	/**
     * Get Thing To Do
	 * Retrieve the title of the place the event is happening.
	 * @return thingToDo A string of the title.
     */
    public String getThingToDo() {
        return thingToDo;
    }

	/**
     * Set Thing To Do
	 * Set the title of the place the event is happening.
	 * @param thingToDo A string of the title.
     */
    public void setThingToDo(String thingToDo) {
        this.thingToDo = thingToDo;
    }

	/**
     * Get Description
	 * The description of the event, most likey what they will be doing
	 * what to bring, why to come, etc.
	 * @return description 
     */
    public String getDescription() {
        return description;
    }

	/**
     * Set Description
	 * The description of the event, most likey what they will be doing
	 * what to bring, why to come, etc.
	 * @param description 
     */
    public void setDescription(String description) {
        this.description = description;
    }

	/**
     * Get Date
	 * When the event is going to take place.
	 * @return date 
     */
    public String getDate() {
        return date;
    }

	/**
     * Set Date
	 * When the event is going to take place.
	 * @param date 
     */
    public void setDate(String date) {
        this.date = date;
    }

	/**
     * Get Time
	 * When the event is going to take place.
	 * @return time 
     */
    public String getTime() {
        return time;
    }

	/**
     * Set Time
	 * When the event is going to take place.
	 * @param time 
     */
    public void setTime(String time) {
        this.time = time;
    }

	/**
     * Get Group
	 * If the event is public this will be null
	 * If it is private, this is what group this
	 * event belongs to.
	 * @return group The title of the group
     */
    public String getGroup() {
        return group;
    }

	/**
     * Set Group
	 * If the event is public this will be null
	 * If it is private, this is what group this
	 * event belongs to.
	 * @param group The title of the group
     */
    public void setGroup(String group) {
        this.group = group;
    }

	/**
     * Is Public
	 * Not currently implemented.
	 * This would be used if we wanted group events 
	 * to be public so people outside of their group 
	 * wanted to join.
	 * @return isPublic 
     */
    public boolean isPublic() {
        return isPublic;
    }

	/**
     * Is Public
	 * Not currently implemented.
	 * This would be used if we wanted group events 
	 * to be public so people outside of their group 
	 * wanted to join.
	 * @param isPublic 
     */
    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

	/**
     * Get URL
	 * The picture for the event
	 * @return url string of the url for the picture 
     */
    public String getUrl() {
        return url;
    }

	/**
     * Set URL
	 * The picture for the event
	 * @param url string of the url for the picture 
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
