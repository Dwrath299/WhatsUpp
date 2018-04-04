package edu.byui.whatsupp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static edu.byui.whatsupp.GroupView.EXTRA_MESSAGE;

public class ThingToDoSelect extends AppCompatActivity {

    ThingToDoActivity thingToDoActivity;
    List<ThingToDo> allThings;
    List<ThingToDo> selectedThings;
    private FirebaseAuth mAuth;
    User currentUser;
    Group currentGroup;
    String groupTitle;
    boolean loggedIn;
	
	/**
     * On Create
	 * Retrieves the information from the intent
	 * Gets current user info
	 * @param savedInstanceState
	 * 
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_to_do_select);
        Intent intent = getIntent();
        currentGroup = (Group) intent.getSerializableExtra("EXTRA_GROUP");
        groupTitle = currentGroup.getTitle();
        thingToDoActivity = new ThingToDoActivity();
        thingToDoActivity.getThingsToDo( this, groupTitle);
        selectedThings = new ArrayList<ThingToDo>();

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

    }

    public void setGridView(List<ThingToDo> things) {
        allThings = things;
        GridView gridview = findViewById(R.id.thingsToDo_grid);
        ThingToDoGridAdapter imageAdapter = new ThingToDoGridAdapter(this, things, this);
        gridview.setAdapter(imageAdapter);
    }

    public void thingClick(ThingToDo thing) {
        if(selectedThings.contains(thing)) {
            selectedThings.remove(thing);
        } else {
            if(selectedThings.size() < 3)
                selectedThings.add(thing);
            else
                Toast.makeText(getApplicationContext(), "A maximum of 3 to vote for. Sorry!", Toast.LENGTH_LONG).show();
        }


    }

    public void confirm(View view) {
        if(selectedThings.size() == 1) {
            Intent intent = new Intent(this, EventForm.class);
            Bundle extras = new Bundle();
            extras.putSerializable("EXTRA_GROUP", currentGroup);
            extras.putString("EXTRA_FORMTYPE","Group");
            extras.putString("EXTRA_FORMINFO", groupTitle);
            extras.putString("EXTRA_PICURL", selectedThings.get(0).getUrl());
            extras.putString("EXTRA_THINGTITLE", selectedThings.get(0).getTitle());
            Log.i("Intent", "Send User to Form");
            intent.putExtras(extras);

            startActivity(intent);
        } else if(selectedThings.size() > 1) {
            Intent intent = new Intent(this, VoteForm.class);
            Bundle extras = new Bundle();
            extras.putSerializable("EXTRA_GROUP", currentGroup);
            extras.putSerializable("EXTRA_OPTION1THING", selectedThings.get(0));
            extras.putSerializable("EXTRA_OPTION2THING", selectedThings.get(1));
            if (selectedThings.size() > 2 ) {
                extras.putSerializable("EXTRA_OPTION3THING", selectedThings.get(2));
            } else {
                extras.putSerializable("EXTRA_OPTION3THING", new ThingToDo("null"));
            }
            Log.i("Intent", "Send User to Form");
            intent.putExtras(extras);

            startActivity(intent);
        }
    }

    public void createGroupThingToDo(View view) {
        Intent intent = new Intent(this, ThingToDoForm.class);
        Bundle extras = new Bundle();
        extras.putString("EXTRA_FORMTYPE","Group");
        extras.putString("EXTRA_FORMINFO", groupTitle);
        Log.i("Intent", "Send User to Form");
        intent.putExtras(extras);

        startActivity(intent);
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
                    final PopupMenu popup = new PopupMenu(ThingToDoSelect.this, popupButton);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("My Profile")) {

                                Intent intent = new Intent(ThingToDoSelect.this, Profile.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to Profile");
                                startActivity(intent);
                            }
                            else if(item.getTitle().equals("View Groups")) {
                                Intent intent = new Intent(ThingToDoSelect.this, GroupsView.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to View Groups");
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(ThingToDoSelect.this, LoginPage.class);
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
                    Intent intent = new Intent(ThingToDoSelect.this, LoginPage.class);
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
                Intent intent = new Intent(ThingToDoSelect.this, HomePage.class);
                // No real reason for sending UID with it, just because
                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                Log.i("Intent", "Send User to Home page");
                startActivity(intent);
            }
        });
    }

}
