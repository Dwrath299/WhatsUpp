package edu.byui.whatsupp;

import android.app.Activity;

import com.facebook.AccessToken;
import com.facebook.login.LoginResult;

/**
 * <h1>User Activity</h1>
 * A porthole to the User Presenter, so each activity that
 * uses something with users can use this.
 *
 *
 *
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */
public class UserActivity {
    UserPresenter userPresenter;

    public UserActivity() {
        userPresenter = new UserPresenter();
    }

    public void detectNewUser(AccessToken token, LoginResult loginResult) {
        userPresenter.isNewUser(token, loginResult);

    }

    /**
     * Retrieves the user info from firebase
     * @param activity Activity class from the Profile class
     * @param uid a string of the user ID of the one we need
     *            to retrieve.
     */
    public void getUserInfo(Activity activity, String uid) {
        userPresenter.requestUserData(activity, uid);

    }


}
