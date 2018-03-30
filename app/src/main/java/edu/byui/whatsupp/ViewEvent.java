package edu.byui.whatsupp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static edu.byui.whatsupp.HomePage.EXTRA_MESSAGE;


/**
 * <h1>View Event</h1>
 * Where users can view an individual
 * event for a ThingToDo. Displays the picture,
 * title, description and a list of Users that
 * are attending
 * <p>
 *
 *
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */
public class ViewEvent extends AppCompatActivity {

    private String message;
    private EventActivity ea;
    private FirebaseAuth mAuth;
    User currentUser;
    boolean loggedIn;
    ListView listView;
    Event event;
    List<String> attendees;
    Button joinButton;
	
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
        setContentView(R.layout.activity_view_event);
        ea = new EventActivity(this);
        Intent intent = getIntent();
        mAuth = FirebaseAuth.getInstance();
        joinButton = (Button) this.findViewById(R.id.attendee_btn);
        // Get the logged in Status
        loggedIn = AccessToken.getCurrentAccessToken() == null;
        if(!loggedIn){ // For facebook, logged in = false
            loggedIn = true;
            currentUser = new User(AccessToken.getCurrentAccessToken().getUserId());
        } else {
            loggedIn = false;
            currentUser = new User("123");
        }
        // Message is the event title
        message = intent.getStringExtra(HomePage.EXTRA_MESSAGE);
        ea.displayEvent((edu.byui.whatsupp.ViewEvent)this, message);
        attendees = new ArrayList<String>();
        setupActionBar();

    }

    /**
     * This method is used to display the components of
     * an individual Event object. It is called from the
     * Event presenter class when it is done getting it
     * from firebase
     * @param item An Event object
     */
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
                Button editButton = (Button) findViewById(R.id.eventEditButton);
                editButton.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * This method is the recieving side of
     * EventPresenter to display the list of
     * users that are going to the event.
     * @param users A list of User Objects
     */
    public void displayAttendees(List<User> users) {
        //It is possible for the event to have no attendees if the creator leaves the event.
        if(users.size() < 1) {
            User filler = new User("No one going yet...", null, null, null, null);
            users.add(filler);
        }
        //Check to see if the current user has already said they are coming
        // If so, make the join button say leave event
        boolean joined = false;
        for(int i = 0; i < users.size(); i++){
            if (currentUser.getUid().equals(users.get(i).getUid())){
                joined = true;
            }
            //Give the list to the class so it can update it if needs be.
            attendees.add(users.get(i).getUid());
        }

        if (joined == false) {
            joinButton.setText("Join Event");
        } else {
            joinButton.setText("Leave Event");
        }
        joinButton.setVisibility(View.VISIBLE);



        UserAdapter userAdapter = new UserAdapter(this, users, this, 1);
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

    /**
     * This method updates  the list of Attendees
     * for the current event. Once it either removes
     * or adds to the event. It will send that updated
     * list to Event Presenter to  change it on the
     * server side.
     * @param view the View object from the activity
     */
    public void updateAttendees(View view) {
        if(joinButton.getText().equals("Join Event")) {
            attendees.add(currentUser.getUid());
            joinButton.setText("Leave Event");
        } else {
            for(int i = 0; i < attendees.size(); i++){
                if (currentUser.getUid().equals(attendees.get(i))){
                    attendees.remove(i);
                }
            }
            joinButton.setText("Join Event");
        }
        ea.addAttendee(event.getRefrence(), attendees);
        ea.displayEvent((edu.byui.whatsupp.ViewEvent)this, message);
    }

    /**
     * This method is used to set up
     * the action bar to have a home button and
     * a login button if the user is not logged in
     * or a drop down menu if the user is logged in.
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
        actionTitle.setText(message);

        final ImageButton popupButton = (ImageButton) findViewById(R.id.btn_menu);
        Button loginButton = (Button) findViewById(R.id.login_btn);
        if(loggedIn) {
            popupButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            popupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Creating the instance of PopupMenu
                    final PopupMenu popup = new PopupMenu(ViewEvent.this, popupButton);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("My Profile")) {

                                Intent intent = new Intent(ViewEvent.this, Profile.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to Profile");
                                startActivity(intent);
                            }
                            else if(item.getTitle().equals("View Groups")) {
                                Intent intent = new Intent(ViewEvent.this, GroupsView.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to View Groups");
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(ViewEvent.this, LoginPage.class);
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
                    Intent intent = new Intent(ViewEvent.this, LoginPage.class);
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
                Intent intent = new Intent(ViewEvent.this, HomePage.class);
                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, "");
                Log.i("Intent", "Send User to Home Page");
                startActivity(intent);
            }
        });
    }


    /**
     * This method sends the user to the EventForm
     * activity to update the currently viewing event.
     * @param view the view object from the activity
     */
    public void updateEvent(View view) {
        Intent intent = new Intent(this, EventForm.class);
        Bundle extras = new Bundle();
        extras.putString("EXTRA_FORMTYPE","create");
        extras.putString("EXTRA_FORMINFO", event.getTitle() );
        extras.putString("EXTRA_THINGTITLE",event.getThingToDo());
        extras.putString("EXTRA_PICURL",event.getUrl());
        intent.putExtras(extras);
        startActivity(intent);
    }
}
