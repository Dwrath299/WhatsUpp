package edu.byui.whatsupp;

import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.Map;

/**
 * <h1>ThingToDo/h1>
 * The ThingToDo class is stored information from when
 * a user creates one.
 *
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */
public class ThingToDo implements Serializable {
    private String url;
    private String title;
    private String address;
    private String city;
    private long zipCode;
    private String description;
    private String creator;
    private String reference;
    private String group;
    public boolean approved;

    public ThingToDo(String u, String t, String a, String c, long z, String d) {
        url = u;
        title = t;
        address = a;
        city = c;
        zipCode = z;
        description = d;
        approved = true;
    }

    public ThingToDo(String u, String t, String a, String c, long z, String d, String g) {
        url = u;
        title = t;
        address = a;
        city = c;
        zipCode = z;
        description = d;
        approved = true;
        group = g;
    }
    public ThingToDo(String u) {
        url = u;

    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String u) {url = u;}

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getDescription() {
        return description;
    }

    public long getZipCode() {return zipCode;}

    public String getCreator() {return creator;}

    public void setCreator(String string) {creator = string; }

}
