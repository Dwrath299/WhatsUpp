package edu.byui.whatsupp;

import android.app.Activity;

import java.util.List;

/**
 * <h1>Event Activity</h1>
 *  A class to handle all the functions that activities need
 *  to call the presenter with. This is the train system.
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */

public class EventActivity {
    EventPresenter eventPresenter;


	/**
     * Event Activity Constructor
	 * Asks for an activity, but each of the methods
	 * also ask for one. Being redundant.
	 * @param activity The activity using this class.
     */
    public EventActivity(Activity activity) {
        eventPresenter = new EventPresenter();
    }

	/**
     * Display Events For Thing
	 * Gets the public events for a Thing To Do
	 * Down the road should implement to get the events that are for that thing
	 * in the groups someone is in.
	 * @param activity The activity using this class.
	 * @param title The title of the place to get events for
     */
    public void displayEventsForThing(Activity activity, String title) {
        eventPresenter.getEventsForThing(activity, title);
    }

	/**
     * Display Event
	 * Gets the event information for a single event. 
	 * Used for the event View page
	 * @param activity The activity using this class.
	 * @param title The title of the event to get event details
     */
    public void displayEvent(Activity activity, String title) {
        eventPresenter.getEvent(activity, title);
    }

	/**
     * Add Attendee
	 * Updates the attending list to add
     * a user to the attendee list for a event.
	 * @param docRef The Reference of the event the user is joining
	 * @param uids A list of strings of UIDs that are attending the event. 
     */
    public void addAttendee(String docRef, List<String> uids) {
        eventPresenter.addAttendee(docRef, uids);

    }

	/**
     * Display Event
	 * Gets the events that a user is going to, to display
	 * on their profile page. Yes, stalkerish
	 * @param activity The activity using this class.
	 * @param uid The uid of the user
     */
    public void displayEventsForProfile(Activity activity, String uid) {
        eventPresenter.getEventsForProfile(activity, uid);
    }

	/**
     * Get Event For Update
	 * Gets the event information for a single event. 
	 * Used to update the event on the EventForm page.
	 * @param activity The activity using this class.
	 * @param title The title of the event to get event details
     */
    public void getEventForUpdate(Activity activity, String title) {
        eventPresenter.getEventToEdit(activity, title);
    }

	/**
     * Display Event
	 * Someone is deleting their event, or the event 
	 * already happened.
	 * @param ref The reference of the event
     */
    public void deleteEvent(String ref) {
        eventPresenter.deleteEventDocument(ref);
    }

	/**
     * Get Events For Group
	 * Gets the events information that are happening in
	 * a group, GroupView Page
	 * @param activity The activity using this class.
	 * @param title The title of the group to get events details
     */
    public void getEventsForGroup(Activity activity, String title) {

    }
}
