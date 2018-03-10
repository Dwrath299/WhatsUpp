package edu.byui.whatsupp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.facebook.login.widget.ProfilePictureView;

public class Profile extends AppCompatActivity {
    String message;
    User profileUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        message = intent.getStringExtra(HomePage.EXTRA_MESSAGE);
        profileUser = new User(message);

        ProfilePictureView profilePictureView;
        profilePictureView = (ProfilePictureView) findViewById(R.id.profilePicture);
        profilePictureView.setProfileId(message);
        // -4 in facebook's twisted mind is large, -3 = normal, -2 = small, -1 = custom.
        profilePictureView.setPresetSize(-4);
    }

    public void groupWith(View view) {

    }
}
