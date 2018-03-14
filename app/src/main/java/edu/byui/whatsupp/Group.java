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
    String url;

    public Group() {

    }

    public String getTitle() {
        return title;
    }

    public int getNumMembers() {
        return numMembers;
    }

    public void setNumMembers(int numMembers) {
        this.numMembers = numMembers;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
