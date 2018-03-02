package edu.byui.whatsupp;

import com.google.firebase.storage.StorageReference;

import java.util.Map;

/**
 * Created by Dallin's PC on 2/16/2018.
 */

public class ThingToDo {
    private String url;
    private String title;
    private String address;
    private String city;
    private int zipCode;
    private String description;
    //private User creator;
    public boolean approved;

    public ThingToDo(String u, String t, String a, String c, int z, String d) {
        url = u;
        title = t;
        address = a;
        city = c;
        zipCode = z;
        description = d;
        approved = true;
    }

    public String getUrl() {
        return url;
    }

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
    /*public Map<String, Object> toMap() {
        return new ThingToDo.FirebaseValue(this).toMap();
    }*/
}
