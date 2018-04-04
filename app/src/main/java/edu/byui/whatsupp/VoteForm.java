package edu.byui.whatsupp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import static edu.byui.whatsupp.GroupView.EXTRA_MESSAGE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static java.util.Calendar.getInstance;

public class VoteForm extends AppCompatActivity {
    private FirebaseAuth mAuth;
    User currentUser;
    boolean loggedIn;
    Group group;
    ThingToDo option1;
    ThingToDo option2;
    ThingToDo option3;
    int numOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_form);
        Intent intent = getIntent();
        group = (Group) intent.getSerializableExtra("EXTRA_GROUP");
        option1 = (ThingToDo) intent.getSerializableExtra("EXTRA_OPTION1THING");
        option2 = (ThingToDo) intent.getSerializableExtra("EXTRA_OPTION2THING");
        option3 = (ThingToDo) intent.getSerializableExtra("EXTRA_OPTION3THING");
        if(option3.getUrl().equals("null")) {
            numOptions = 2;
        } else {
            numOptions = 3;
        }
        mAuth = FirebaseAuth.getInstance();
        // Get the logged in Status
        loggedIn = AccessToken.getCurrentAccessToken() == null;
        if(!loggedIn){ // For facebook, logged in = false
            loggedIn = true;
            currentUser = new User(AccessToken.getCurrentAccessToken().getUserId());
        } else {
            loggedIn = false;
            currentUser = new User("123");
        }
        setupActionBar();
        displayOptions();
    }

    private void displayOptions() {

        //Set the headers
        TextView header = findViewById(R.id.vote_form_option1_header);
        header.setText("Option 1: " + option1.getTitle());
        header = findViewById(R.id.vote_form_option2_header);
        header.setText("Option 2: " + option2.getTitle());
        header = findViewById(R.id.vote_form_option3_header);
        if(numOptions == 3) {
            header.setText("Option 3: " + option3.getTitle());
        } else {
            header.setVisibility(View.INVISIBLE);
        }

        // Set the images
        ImageView image = findViewById(R.id.vote_form_option1_pic);
        Picasso.with(this).load(option1.getUrl()).into(image);
        image = findViewById(R.id.vote_form_option2_pic);
        Picasso.with(this).load(option2.getUrl()).into(image);
        image = findViewById(R.id.vote_form_option3_pic);
        if(numOptions == 3) {
            Picasso.with(this).load(option3.getUrl()).into(image);
        } else {
            image.setVisibility(View.INVISIBLE);
            EditText text = findViewById(R.id.vote_form_option3_desc);
            text.setVisibility(View.INVISIBLE);
        }

    }

    public void submit(View view) {

        // Get time and date
        TextView tv = findViewById(R.id.voteForm_tv_date);
        String date = tv.getText().toString();
        tv = findViewById(R.id.voteForm_tv_time);
        String time = tv.getText().toString();

        // Option info
        EditText desc = findViewById(R.id.vote_form_option1_desc);
        option1.setDescription(desc.getText().toString());
        desc = findViewById(R.id.vote_form_option2_desc);
        option2.setDescription(desc.getText().toString());
        if(option3.getUrl().equals("null")) {
            numOptions = 2;
        } else {
            desc = findViewById(R.id.vote_form_option3_desc);
            option3.setDescription(desc.getText().toString());
        }
        Vote vote = new Vote(group.getTitle(), group.getMemberList().size(),
               option1, option2, option3, currentUser.getUid(), date, time );
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("votes")
                .add(vote)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(VoteForm.this, "Successfully Created Vote for " + group.getTitle(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                        Toast.makeText(VoteForm.this, "Failure",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        finish();
    }

    /**
     * Show Time Picker Dialog
     * Called by the time picker in the activity.
     * Allows the user to choose the time.
     * @param v
     */
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new VoteForm.TimePickerFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(newFragment)
                .commit();
        newFragment.show(fm, "timePicker");
    }

    /**
     * Time Picker Fragment
     * The interface of the time picker
     *
     */
    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        /**
         * On Create Dialog
         * Sets the interface to default to the current time
         * @param savedInstanceState
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = getInstance();
            int hour = c.get(HOUR_OF_DAY);
            int minute = c.get(MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        /**
         * On Time Set
         * Returns the time information back to the activity
         * @param view
         * @param hourOfDay
         * @param minute
         */
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            ((TextView) getActivity().findViewById(R.id.voteForm_tv_time)).setVisibility(View.VISIBLE);
            ((TextView) getActivity().findViewById(R.id.voteForm_tv_time)).setText(hourOfDay + ":" + minute);

        }
    }

    /**
     * Show Date Picker Dialog
     * Called by the date picker in the activity.
     * Allows the user to choose the date.
     * @param v
     */
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new VoteForm.DatePickerFragment();

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(newFragment)
                .commit();
        newFragment.show(fm, "datePicker");

    }

    /**
     * Date Picker Fragment
     * The interface of the date picker
     *
     */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        /**
         * On Create Dialog
         * Sets the interface to default to the current date
         * @param savedInstanceState
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = getInstance();
            int year = c.get(YEAR);
            int month = c.get(MONTH);
            int day = c.get(DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        /**
         * On Time Date
         * Returns the date information back to the activity
         * @param view
         * @param month
         * @param day
         */
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            ((TextView) getActivity().findViewById(R.id.voteForm_tv_date)).setVisibility(View.VISIBLE);
            ((TextView) getActivity().findViewById(R.id.voteForm_tv_date)).setText(month + "/" + day + "/" + year);
        }
    }

    private void setupActionBar() {
        //Get the default actionbar instance
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

        //Initializes the custom action bar layout
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        //Set the actionbar title
        TextView actionTitle = (TextView) findViewById(R.id.title_text);
        actionTitle.setText("Create Group Event");

        final ImageButton popupButton = (ImageButton) findViewById(R.id.btn_menu);
        Button loginButton = (Button) findViewById(R.id.login_btn);
        if(loggedIn) {
            popupButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            popupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Creating the instance of PopupMenu
                    final PopupMenu popup = new PopupMenu(VoteForm.this, popupButton);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("My Profile")) {

                                Intent intent = new Intent(VoteForm.this, Profile.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to Profile");
                                startActivity(intent);
                            }
                            else if(item.getTitle().equals("View Groups")) {
                                Intent intent = new Intent(VoteForm.this, GroupsView.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to View Groups");
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(VoteForm.this, LoginPage.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to Login page");
                                startActivity(intent);
                            }
                            return true;
                        }
                    });

                    popup.show(); //showing popup menu
                }
            }); //closing the setOnClickListener method

        } else {
            popupButton.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(VoteForm.this, LoginPage.class);
                    intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                    Log.i("Intent", "Send User to Login page");
                    startActivity(intent);
                }
            });
        }

        //Detect the button click event of the home button in the actionbar
        ImageButton btnHome = (ImageButton) findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VoteForm.this, HomePage.class);
                // No real reason for sending UID with it, just because
                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                Log.i("Intent", "Send User to Home page");
                startActivity(intent);
            }
        });
    }
}
