package edu.byui.whatsupp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ViewVote extends AppCompatActivity {
    private User currentUser;
    private FirebaseAuth mAuth;
    private boolean loggedIn;
    private String selectedThing;
    private ArrayList<String> usersVoted;
    private String voteRef;
    EventActivity ea;
    Vote vote;

    /**
     * Gets the current user info, and log in status.
     * Call the Event Activity to get the vote info
     * Call to set up action bar.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vote);
        Intent intent = getIntent();
        voteRef = intent.getStringExtra(HomePage.EXTRA_MESSAGE);
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
        ea = new EventActivity(this);
        selectedThing = "";
        ea.getVoteInfo(this, voteRef);
    }

    /**
     * Once the user has selected an option,
     * they click the submit button, this updates
     * the vote in firebase. Refreshes, then displays
     * percents affiliated with choices.
     * @param view
     */
    public void submitVote(View view) {
        if(selectedThing == "") {
            Toast.makeText(this, "Please select an option", Toast.LENGTH_LONG);
        } else {
            String optionChanged = "";
            long  newNum = 0;
            if(selectedThing == vote.getOption1()) {
                vote.addVoteFor1();
                optionChanged = "votesFor1";
                newNum = vote.getVotesFor1();
            } else if (selectedThing == vote.getOption2()) {
                vote.addVoteFor2();
                optionChanged = "votesFor2";
                newNum = vote.getVotesFor2();
            } else if (selectedThing == vote.getOption3()) {
                vote.addVoteFor3();
                optionChanged = "votesFor3";
                newNum = vote.getVotesFor3();

            }
            if(usersVoted == null) {
                usersVoted = new ArrayList<String>();
            }
            usersVoted.add(currentUser.getUid());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String ref = vote.getVoteID();
            db.collection("votes").document(ref)
                    .update(optionChanged, newNum)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });
            db.collection("votes").document(ref)
                    .update("usersVoted", usersVoted)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });
        }
        // This will refresh it.
        ea.getVoteInfo(this, voteRef);
    }

    /**
     * This is called from the Event Presenter to set up the displaying the vote.
     * Sets up pictures, headers, descritpions, and handles pic clicking.
     * @param v The vote, initialized with all the info
     * @param option1Pic String of the URL
     * @param option2Pic
     * @param option3Pic
     */
    public void displayVote(Vote v, String option1Pic, String option2Pic, String option3Pic) {
        vote = v;
        usersVoted = vote.getUsersVoted();

        // Don't need to display pics if user already voted.
        if((usersVoted == null)  || !(usersVoted.contains(currentUser.getUid()))) {
            // Set up the pictures
            ImageView optionPic = findViewById(R.id.vote_option1_pic);
            Picasso.with(this).load(option1Pic).into(optionPic);
            optionPic.getLayoutParams().height = 225;
            optionPic.getLayoutParams().width = 225;
            optionPic.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int backgroundColor = ContextCompat.getColor(ViewVote.this, R.color.colorAccent50);
                    if (selectedThing.equals(vote.getOption1())) {
                        selectedThing = "";
                        ((ImageView) ViewVote.this.findViewById(R.id.vote_option1_pic)).getDrawable().setColorFilter(0x00000000, PorterDuff.Mode.LIGHTEN);
                    } else {
                        if (selectedThing.equals(vote.getOption2())) {
                            ((ImageView) ViewVote.this.findViewById(R.id.vote_option2_pic)).getDrawable().setColorFilter(0x00000000, PorterDuff.Mode.LIGHTEN);

                        } else if (selectedThing.equals(vote.getOption3())) {
                            ((ImageView) ViewVote.this.findViewById(R.id.vote_option3_pic)).getDrawable().setColorFilter(0x00000000, PorterDuff.Mode.LIGHTEN);

                        }
                        selectedThing = vote.getOption1();
                        ((ImageView) ViewVote.this.findViewById(R.id.vote_option1_pic)).getDrawable().setColorFilter(backgroundColor, PorterDuff.Mode.LIGHTEN);


                    }

                }
            });
            optionPic = findViewById(R.id.vote_option2_pic);
            Picasso.with(this).load(option2Pic).into(optionPic);
            optionPic.getLayoutParams().height = 225;
            optionPic.getLayoutParams().width = 225;
            optionPic.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int backgroundColor = ContextCompat.getColor(ViewVote.this, R.color.colorAccent50);
                    if (selectedThing.equals(vote.getOption2())) {
                        selectedThing = "";
                        ((ImageView) ViewVote.this.findViewById(R.id.vote_option2_pic)).getDrawable().setColorFilter(0x00000000, PorterDuff.Mode.LIGHTEN);
                    } else {
                        if (selectedThing.equals(vote.getOption1())) {
                            ((ImageView) ViewVote.this.findViewById(R.id.vote_option1_pic)).getDrawable().setColorFilter(0x00000000, PorterDuff.Mode.LIGHTEN);

                        } else if (selectedThing.equals(vote.getOption3())) {
                            ((ImageView) ViewVote.this.findViewById(R.id.vote_option3_pic)).getDrawable().setColorFilter(0x00000000, PorterDuff.Mode.LIGHTEN);

                        }
                        selectedThing = vote.getOption2();
                        ((ImageView) ViewVote.this.findViewById(R.id.vote_option2_pic)).getDrawable().setColorFilter(backgroundColor, PorterDuff.Mode.LIGHTEN);


                    }

                }
            });
            optionPic = findViewById(R.id.vote_option3_pic);
            Picasso.with(this).load(option3Pic).into(optionPic);
            optionPic.getLayoutParams().height = 225;
            optionPic.getLayoutParams().width = 225;
            optionPic.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    int backgroundColor = ContextCompat.getColor(ViewVote.this, R.color.colorAccent50);
                    if (selectedThing.equals(vote.getOption3())) {
                        selectedThing = "";
                        ((ImageView) ViewVote.this.findViewById(R.id.vote_option3_pic)).getDrawable().setColorFilter(0x00000000, PorterDuff.Mode.LIGHTEN);
                    } else {
                        if (selectedThing.equals(vote.getOption1())) {
                            ((ImageView) ViewVote.this.findViewById(R.id.vote_option1_pic)).getDrawable().setColorFilter(0x00000000, PorterDuff.Mode.LIGHTEN);

                        } else if (selectedThing.equals(vote.getOption3())) {
                            ((ImageView) ViewVote.this.findViewById(R.id.vote_option3_pic)).getDrawable().setColorFilter(0x00000000, PorterDuff.Mode.LIGHTEN);

                        }
                        selectedThing = vote.getOption3();
                        ((ImageView) ViewVote.this.findViewById(R.id.vote_option3_pic)).getDrawable().setColorFilter(backgroundColor, PorterDuff.Mode.LIGHTEN);


                    }

                }
            });
        } else { //If users already voted, then display percents.
            int totalVotes = vote.getVotesFor1() + vote.getVotesFor2() + vote.getVotesFor3();
            TextView percent = findViewById(R.id.vote_option1_percent);
            int optionPercent = vote.getVotesFor1() / totalVotes * 100;
            percent.setText(optionPercent + "%");
            percent.setVisibility(View.VISIBLE);
            percent = findViewById(R.id.vote_option2_percent);
            optionPercent = vote.getVotesFor2() / totalVotes * 100;
            percent.setText(optionPercent + "%");
            percent.setVisibility(View.VISIBLE);
            //TODO: make it so it checks if there are 3 options
            percent = findViewById(R.id.vote_option3_percent);
            optionPercent = vote.getVotesFor3() / totalVotes * 100;
            percent.setText(optionPercent + "%");
            percent.setVisibility(View.VISIBLE);
            Button voteSubmit = findViewById(R.id.vote_submit);
            voteSubmit.setVisibility(View.INVISIBLE);

        }
        // Set headers
        TextView tv = findViewById(R.id.vote_option1_head);
        tv.setText(vote.getOption1());
        tv = findViewById(R.id.vote_option2_head);
        tv.setText(vote.getOption2());
        tv = findViewById(R.id.vote_option3_head);
        tv.setText(vote.getOption3());

        // Set descriptions
        tv = findViewById(R.id.vote_option1_desc);
        tv.setText(vote.getOption1Desc());
        tv = findViewById(R.id.vote_option2_desc);
        tv.setText(vote.getOption2Desc());
        tv = findViewById(R.id.vote_option3_desc);
        tv.setText(vote.getOption3Desc());
    }


    /**
     * Setup ActionBar
     * Intializes the action bar to have the functionality of
     * the home button and drop down list if the user is
     * logged in, otherwise, a log in button.
     * Called by the On Create method
     */
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
        actionTitle.setText("Vote");

        final ImageButton popupButton = (ImageButton) findViewById(R.id.btn_menu);
        Button loginButton = (Button) findViewById(R.id.login_btn);
        if(loggedIn) {
            popupButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            popupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Creating the instance of PopupMenu
                    final PopupMenu popup = new PopupMenu(ViewVote.this, popupButton);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("My Profile")) {

                                Intent intent = new Intent(ViewVote.this, Profile.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to Profile");
                                startActivity(intent);
                            }
                            else if(item.getTitle().equals("View Groups")) {
                                Intent intent = new Intent(ViewVote.this, GroupsView.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to View Groups");
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(ViewVote.this, LoginPage.class);
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
                    Intent intent = new Intent(ViewVote.this, LoginPage.class);
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
                Intent intent = new Intent(ViewVote.this, HomePage.class);
                // No real reason for sending UID with it, just because
                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                Log.i("Intent", "Send User to Home page");
                startActivity(intent);
            }
        });
    }
}
