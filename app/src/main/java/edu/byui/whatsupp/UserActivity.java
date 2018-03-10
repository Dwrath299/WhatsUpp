package edu.byui.whatsupp;

import com.facebook.AccessToken;
import com.facebook.login.LoginResult;

/**
 * Created by Dallin's PC on 2/26/2018.
 */

public class UserActivity {
    UserPresenter userPresenter;

    public UserActivity() {
        userPresenter = new UserPresenter();
    }

    public void detectNewUser(AccessToken token, LoginResult loginResult) {
        userPresenter.isNewUser(token, loginResult);

    }

    public void getUserInfo(User user) {
        userPresenter.requestUserData(user);

    }
}
