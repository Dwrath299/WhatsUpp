package edu.byui.whatsupp;

import com.facebook.AccessToken;

/**
 * Created by MikeyG on 2/26/2018.
 *
 */

public class User {
    String firstName;
    String lastName;
    boolean admin;
    String facebookUrl;
    String uid;
    String email;
    String gender;
    UserActivity userActivity;
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

    //Provide just the uid and it will fetch the rest of the data
    public User(String uid) {
        this.uid = uid;
        userActivity = new UserActivity();
        userActivity.getUserInfo(this);

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
