package edu.byui.whatsupp;

import java.util.ArrayList;

/**
 * Created by Dallin's PC on 2/26/2018.
 */

public class Group {
    ArrayList<String> memberList;
    int numMembers;
    String title;
    String url;
    String creator;
    ArrayList<User> members;

    public Group(String title, ArrayList<User> members, String url) {
        this.title = title;
        this.members = members;
        this.url = url;
    }

    public ArrayList<String> getMemberList() {
        return memberList;
    }

    public void setMemberList(ArrayList<String> memberList) {
        numMembers = memberList.size();
        this.memberList = memberList;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
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
