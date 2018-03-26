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


    public ThingToDoActivity() {
        thingToDoPresenter = new ThingToDoPresenter();
        things = new ArrayList<ThingToDo>();
    }

    public void displayThingsToDo(Activity activity) {
        thingToDoPresenter.getListThings(activity);

    }
    public void getThingsToDo(ThingToDoSelectFragment fragment) {

    }

    public void displayThingToDo(Activity activity, String title) {
        thingToDoPresenter.getThing(activity, title);

    }

    public void getThingToEdit(Activity activity, String title) {
        thingToDoPresenter.getThingToEdit(activity, title);
    }

    public void deleteThing(String docRef) {
        thingToDoPresenter.deleteThingDocument(docRef);

    }




}
