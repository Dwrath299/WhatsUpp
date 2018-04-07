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

    /**
     * Gets the information regarding the groups that the user is a part of,
     * using the user's id (uid)
     * @param activity
     * @param uid
     */
    public void getUsersGroups(Activity activity, String uid) {
        groupPresenter.getListGroups(activity, uid);
    }

    /**
     * Retrieves group data by use of the group title
     * @param activity
     * @param groupTitle
     */
    public void getGroup(Activity activity, String groupTitle) {
        groupPresenter.getGroup(activity, groupTitle);
    }

    /**
     * Retrieves votes
     * @param a
     * @param voteRef
     */
    public void getVote(Activity a, String voteRef) {

    }
}
