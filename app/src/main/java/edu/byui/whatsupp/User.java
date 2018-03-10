package edu.byui.whatsupp;

import com.facebook.AccessToken;

/**
 * Created by MikeyG on 2/26/2018.
 */

public class User {
    String firstName;
    String lastName;
    boolean admin;
    String facebookUrl;
    String uid;
    String email;
    String gender;
    /* You can get the profile pic by
    ProfilePictureView profilePictureView;

    profilePictureView = (ProfilePictureView) findViewById(R.id.profilePic);

    profilePictureView.setProfileId(userId);
     */

    public User(String first, String last, String e, String gen, String uid) {
        //utilize Facebook API's methods
       this.firstName = first;
       lastName = last;
       email = e;
       gender = gen;
       this.uid = uid;

    }
}
