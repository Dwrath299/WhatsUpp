package edu.byui.whatsupp;

import com.facebook.AccessToken;

/**
 * Created by MikeyG on 2/26/2018.
 */

public class User {
    String firstName;
    String lastName;
    String picUrl;
    boolean admin;
    String facebookUrl;
    String uid;

    public User(AccessToken token) {
        //utilize Facebook API's methods
       uid = token.getUserId();
    }
}
