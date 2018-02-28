package edu.byui.whatsupp;

import com.google.firebase.storage.StorageReference;

/**
 * Created by Dallin's PC on 2/16/2018.
 */

public class ThingToDo {
    private StorageReference storageRef;
    private String title;
    private String address;
    private String city;
    private int zipCode;
    private String description;
    public boolean approved;

    public ThingToDo(StorageReference ref, String t, String a, String c, int z, String d) {
        storageRef = ref;
        title = t;
        address = a;
        city = c;
        zipCode = z;
        description = d;
        approved = true;
    }
}
