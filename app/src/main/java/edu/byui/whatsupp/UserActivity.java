package edu.byui.whatsupp;

import com.facebook.AccessToken;
import com.facebook.login.LoginResult;

/**
 * Created by Dallin's PC on 2/26/2018.
 */

public class UserActivity {
    UserPresenter userPresenter;

    public UserActivity() {

    }

    public void detectNewUser(AccessToken token, LoginResult loginResult) {
        token.getUserId();

    }

    public void addUser() {

    }
}
