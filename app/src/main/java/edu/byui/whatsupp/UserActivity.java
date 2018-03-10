package edu.byui.whatsupp;

import com.facebook.AccessToken;

/**
 * Created by Dallin's PC on 2/26/2018.
 */

public class UserActivity {
    UserPresenter userPresenter;

    public UserActivity() {

    }

    public void detectNewUser(AccessToken token) {
        token.getUserId();

    }

    public void addUser() {

    }
}
