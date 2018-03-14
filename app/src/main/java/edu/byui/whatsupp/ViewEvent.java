package edu.byui.whatsupp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class ViewEvent extends AppCompatActivity {

    private String message;

    private EventActivity ea;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ListView listView;
    Event event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        ea = new EventActivity(this);
        Intent intent = getIntent();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
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
}
