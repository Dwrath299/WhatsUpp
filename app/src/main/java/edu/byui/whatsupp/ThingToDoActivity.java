package edu.byui.whatsupp;

import android.app.Activity;
import android.view.View;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dallin's PC on 2/26/2018.
 */

public class ThingToDoActivity {
    ThingToDoPresenter thingToDoPresenter;
    List<ThingToDo> things;
    Activity activity;

    public ThingToDoActivity(Activity a) {
        thingToDoPresenter = new ThingToDoPresenter();
        things = new ArrayList<ThingToDo>();
        activity = a;
    }

    public void displayThingsToDo(Activity activity) {
        thingToDoPresenter.getListThings(activity);

    }

    public void displayThingToDo(Activity activity, String title) {

    }


}
