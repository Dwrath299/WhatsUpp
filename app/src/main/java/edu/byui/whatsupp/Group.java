package edu.byui.whatsupp;

import java.util.ArrayList;

/**
 * Created by Dallin's PC on 2/26/2018.
 */

public class Group {
    GroupPresenter groupPresenter;

    ArrayList<User> memberList;
    int numMembers;
    String title;

    public Group() {

    }

    public String getTitle() {
        return title;
    }

}
