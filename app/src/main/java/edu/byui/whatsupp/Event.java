package edu.byui.whatsupp;

import java.util.List;

/**
 * Created by MikeyG on 2/26/2018.
 */

public class Event {

    String creator;
    List<String> attendees;
    String thingToDo;
    String title;
    String description;
    String date;
    String time;
    String group;
    String url;
    boolean isPublic;

    // Mainly for the filler event of "No events" event in the things view.
    public Event(String string, String url) {
        title = string;
        this.url = url;
    }
    // Public Event for a ThingToDo
    public Event(String string, String description, String date, String time, String thingToDo, String url) {
        title = string;
        this.description = description;
        this.date = date;
        this.time = time;
        this.thingToDo = thingToDo;
        this.url = url;
    }

    // Private or Public Event for a group
    public Event(String string, String description, String date, String time, String thingToDo, String url, String group) {
        title = string;
        this.description = description;
        this.date = date;
        this.time = time;
        this.thingToDo = thingToDo;
        this.url = url;
        this.group = group;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getAttendees() {
        return attendees;
    }

    public void setAttendees(List<String> attendees) {
        this.attendees = attendees;
    }

    public String getThingToDo() {
        return thingToDo;
    }

    public void setThingToDo(String thingToDo) {
        this.thingToDo = thingToDo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
