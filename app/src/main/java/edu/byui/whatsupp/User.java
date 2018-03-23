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
    String docRef;
    UserActivity userActivity;
    /* You can get the profile pic by
    ProfilePictureView profilePictureView;

    profilePictureView = (ProfilePictureView) findViewById(R.id.profilePic);

    profilePictureView.setProfileId(userId);

    Get relationship status
    You just need to get an access token with user_relationships,
    call https://graph.facebook.com/YOURFRIEND-ID and check "relationship_status" in the returned JSON string.
    Just have in mind that if your friend didn't filled his relationship data into his profile,
    there will be no "relationship_status" at the returned user info.
     */


    /**
     * Constructor.
     * @param first
     * @param last
     * @param e
     * @param gen
     * @param uid
     */
    public User(String first, String last, String e, String gen, String uid) {
        //utilize Facebook API's methods
       this.firstName = first;
       lastName = last;
       email = e;
       gender = gen;
       this.uid = uid;

    }

    public User(String uid) {
        this.uid = uid;
    }

    /**
     * Getters and Setters for User class.
     */

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
