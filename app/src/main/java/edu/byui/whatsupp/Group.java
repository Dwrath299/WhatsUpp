package edu.byui.whatsupp;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dallin's PC on 2/26/2018.
 */

/**
 * This class contains all the data that is to be stored inside a Group object.
 */
public class Group implements Serializable {
    int numMembers;
    String title;
    String url;
    String creator;
    String reference;
    ArrayList<String> members;

    /**
     * Constructor.
     * @param title
     * @param members
     * @param url
     */
    public Group(String title, ArrayList<String> members, String url) {
        this.title = title;
        this.members = members;
        this.url = url;
    }

    /**
     * Constructor.
     * @param title
     * @param url
     */
    public Group(String title,  String url) {
        this.title = title;
        this.members = members;
        this.url = url;
    }

    /**
     * Getters and Setters.
     * @return
     */
    public ArrayList<String> getMemberList() {
        return members;
    }

    public void setMembers(ArrayList<String> memberList) {
        numMembers = memberList.size();
        this.members = memberList;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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
