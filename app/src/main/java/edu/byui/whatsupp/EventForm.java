package edu.byui.whatsupp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class EventForm extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String message;
    EventActivity ea;
    TimePicker timePicker;
    DatePicker datePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String thingTitle = extras.getString("EXTRA_THINGTITLE");
        String thingUrl = extras.getString("EXTRA_THINGURL");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        ImageView eventImageView = (ImageView) findViewById(R.id.eventImageView);
        Picasso.with(this).load(thingUrl).into(eventImageView);
        EditText eventTitle = (EditText) findViewById(R.id.editEventTitle);
        eventTitle.setHint(thingTitle + " event");
        ea = new EventActivity(this);
    }

    public void pickTime(View view) {
        hideTimeOrDate();
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setVisibility(View.VISIBLE);
    }

    private void hideTimeOrDate(){
        if(timePicker.getVisibility() == View.VISIBLE) {
            if (getCurrentFocus() != null && getCurrentFocus() instanceof TimePicker) {

                timePicker.setVisibility(View.INVISIBLE);
            }
        }
        if(datePicker.getVisibility() == View.VISIBLE) {
            if (getCurrentFocus() != null && getCurrentFocus() instanceof DatePicker) {

                datePicker.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void pickDate(View view) {
        hideTimeOrDate();
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.setVisibility(View.VISIBLE);
    }

    public void submit(View view) {
        //Event tempEvent = new Event()
    }

    public void addPicture(View view) {

    }
}
