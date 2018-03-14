package edu.byui.whatsupp;

import android.app.Activity;

import java.util.List;

/**
 * Created by Dallin's PC on 2/26/2018.
 */

public class EventActivity {
    EventPresenter eventPresenter;


    public EventActivity(Activity activity) {
        eventPresenter = new EventPresenter();
    }

    public void displayEventsForThing(Activity activity, String title) {
        eventPresenter.getEventsForThing(activity, title);
    }

    public void displayEvent(Activity activity, String title) {
        eventPresenter.getEvent(activity, title);
    }

    public void addAttendee(String docRef, List<String> uids) {
        eventPresenter.addAttendee(docRef, uids);

    }
}
