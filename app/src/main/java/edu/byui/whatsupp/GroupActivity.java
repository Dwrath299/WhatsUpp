package edu.byui.whatsupp;

import android.app.Activity;

/**
 * Created by Dallin's PC on 2/26/2018.
 */

public class GroupActivity {
    GroupPresenter groupPresenter;

    public GroupActivity() {
        groupPresenter = new GroupPresenter();
    }

    public void getUsersGroups(Activity activity, String uid) {
        groupPresenter.getListThings(activity, uid);
    }
}
