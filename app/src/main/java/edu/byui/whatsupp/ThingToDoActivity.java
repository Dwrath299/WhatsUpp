package edu.byui.whatsupp;

import android.app.Activity;
import android.app.Fragment;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>ThingToDo Activity</h1>
 * The porthole for activites that deal with ThingsToDo
 * to retrieve information from firebase.
 *
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */
public class ThingToDoActivity {
    ThingToDoPresenter thingToDoPresenter;
    List<ThingToDo> things;


    /**
     * Constructor that sets up thingToDoPresenter
     */
    public ThingToDoActivity() {
        thingToDoPresenter = new ThingToDoPresenter();

    }

    /**
     * The basis of the whole app, calls the presenter for
     * the list of things to do for the homepage.
     * @param activity
     */
    public void displayThingsToDo(Activity activity) {
        thingToDoPresenter.getListThings(activity);

    }

    /**
     * This gets the things to for a group, so their
     * private ones.
     * @param activity
     * @param groupTitle
     */
    public void getThingsToDo(Activity activity, String groupTitle) {
        thingToDoPresenter.getActivityListThings(activity, groupTitle);
    }

    /**
     * For a specific ThingToDo, for ThingToDoView
     * @param activity
     * @param title
     */
    public void displayThingToDo(Activity activity, String title) {
        thingToDoPresenter.getThing(activity, title);

    }

    /**
     * Retrieves the information for a ThingToDo for the
     * ThingToDoForm
     * @param activity
     * @param title
     */
    public void getThingToEdit(Activity activity, String title) {
        thingToDoPresenter.getThingToEdit(activity, title);
    }

    /**
     * Removes a ThingToDo from that database.
     * @param docRef
     */
    public void deleteThing(String docRef) {
        thingToDoPresenter.deleteThingDocument(docRef);

    }




}
