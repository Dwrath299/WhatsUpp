package edu.byui.whatsupp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static edu.byui.whatsupp.HomePage.EXTRA_MESSAGE;

public class ViewEvent extends AppCompatActivity {

    private String message;
    private EventActivity ea;
    private FirebaseAuth mAuth;
    User currentUser;
    ListView listView;
    Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        ea = new EventActivity(this);
        Intent intent = getIntent();
        mAuth = FirebaseAuth.getInstance();
        currentUser = new User(AccessToken.getCurrentAccessToken().getUserId());
        // Message is the event title
        message = intent.getStringExtra(HomePage.EXTRA_MESSAGE);
        ea.displayEvent((edu.byui.whatsupp.ViewEvent)this, message);

    }

    public void displayEvent(Event item) {
        this.event = item;
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.eventViewProgressBar);
        progressBar.setVisibility(View.GONE);
        ImageView imageView = (ImageView) findViewById(R.id.eventPicView);

        TextView description = (TextView) findViewById(R.id.eventDescriptionView);
        Picasso.with(this).load(event.getUrl()).into(imageView);
        description.setText(event.getDescription());
        imageView.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);

        if(event.getCreator() != null) { //If the creator he can edit, need to make a way of seeing if the user is admin
            String uid = currentUser.getUid();
            if(event.getCreator().equals( uid)) {
                Button editButton = (Button) findViewById(R.id.editButton);
                editButton.setVisibility(View.VISIBLE);
            }
        }
    }

    public void displayAttendees(List<User> users) {
        //Check to see if the current user has already said they are coming
        // If so, make the join button say leave event
        boolean joined = false;
        for(int i = 0; i < users.size(); i++){
            if (currentUser.getUid().equals(users.get(i).getUid())){
                joined = true;
            }
        }
        Button joinButton = (Button) this.findViewById(R.id.attendee_btn);
        if (joined == false) {
            joinButton.setText("Join Event");
        } else {
            joinButton.setText("Leave Event");
        }
        joinButton.setVisibility(View.VISIBLE);

        UserAdapter userAdapter = new UserAdapter(this, users, this);
        listView = (ListView) this.findViewById(R.id.attendees_list);
        listView.setAdapter(userAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                Object o = listView.getItemAtPosition(position);
                User user = (User) o;
                Intent intent = new Intent(ViewEvent.this, Profile.class);
                intent.putExtra(EXTRA_MESSAGE, user.getUid());
                Log.i("Intent", "Send User to a User Profile");
                startActivity(intent);
            }
        });

    }


}
