package edu.byui.whatsupp;

import com.facebook.AccessToken;

import java.util.ArrayList;

/**
 * <h1>User</h1>
 * The user class is stored information from the Facebook API
 * of the user when they log in.
 *
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
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
    ArrayList<Group> userGroups;

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
     * The constructor for the user, The user stores the information first
     * obtained by the Facebook API, and then the database.
     * @param first
     * @param last
     * @param e Email
     * @param gen Gender
     * @param uid User ID
     */
    public User(String first, String last, String e, String gen, String uid) {
        //utilize Facebook API's methods
       this.firstName = first;
       lastName = last;
       email = e;
       gender = gen;
       this.uid = uid;
       userGroups = new ArrayList<>();
    }

    /**
     * Constructor that only takes in UID,
     * Mainly used as a filler one. (A fake one)
     * For when one is needed, but nobody is logged in.
     * Bad programming? Probably
     * @param uid User ID
     */
    public User(String uid) {
        this.uid = uid;
    }

    /**
     * Returns just the first name. More compatible with some
     * programming.
     * @return
     */
    @Override
    public String toString() { return firstName; }

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

    /**
     * Not yet implemented into anything, but the potential
     * of giving super powers ;)
     * @return admin boolean
     */
    public boolean isAdmin() {
        return admin;
    }

    /**
     * Make a user an admin, or revoke
     * @param admin
     */
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    /**
     * not implemented, but the potential
     * of linking profile pic to their
     * facebook page.
     * @return facebookUrl
     */
    public String getFacebookUrl() {
        return facebookUrl;
    }

    /**
     * Set facebook URL
     * @param facebookUrl
     */
    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    /**
     * User ID, this is where most the programming
     * is able to determine who the user is, in case
     * of duplicate names.
     * @return uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * Set the User ID if you want to impersonate someone.
     * @param uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     *  Not sure why we would use this, but it's here
     * @return email
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    /**
     * Incase someone changes their gender
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }
}
