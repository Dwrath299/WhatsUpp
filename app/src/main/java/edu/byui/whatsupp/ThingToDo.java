package edu.byui.whatsupp;

import com.google.firebase.storage.StorageReference;

import java.util.Map;

/**
 * Created by Dallin's PC on 2/16/2018.
 */

/**
 * This class contains all the data needed to be stored inside a thing to do.
 */
public class ThingToDo {
    private String url;
    private String title;
    private String address;
    private String city;
    private long zipCode;
    private String description;
    private String creator;
    private String reference;
    public boolean approved;

    /**
     * Constructor.
     * @param u
     * @param t
     * @param a
     * @param c
     * @param z
     * @param d
     */
    public ThingToDo(String u, String t, String a, String c, long z, String d) {
        url = u;
        title = t;
        address = a;
        city = c;
        zipCode = z;
        description = d;
        approved = true;
    }

    /**
     * Getters and Setters for ThingToDo class.
     * @return
     */
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
