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

import java.util.List;

import static edu.byui.whatsupp.HomePage.EXTRA_MESSAGE;

/**
 * <h1>View Thing To Do</h1>
 * The viewing of a single thing to do.
 * Displays the title, pic, description,
 * address, and a list of current events
 * happening at the location
 * <p>
 *
 *
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */
public class ViewThingToDo extends AppCompatActivity {
    private String message;
    private ThingToDoActivity ttda;
    private EventActivity ea;
    User currentUser;
    boolean loggedIn;
    ListView listView;
    ThingToDo thing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiw_thing_to_do);
        ttda = new ThingToDoActivity();
        ea = new EventActivity(this);
        Intent intent = getIntent();
        // Get the logged in Status
        loggedIn = AccessToken.getCurrentAccessToken() == null;
        if(!loggedIn){ // For facebook, logged in = false
            loggedIn = true;
            currentUser = new User(AccessToken.getCurrentAccessToken().getUserId());
        } else {
            loggedIn = false;
            currentUser = new User("123");
        }
        message = intent.getStringExtra(EXTRA_MESSAGE);
        ttda.displayThingToDo(this, message);
        ea.displayEventsForThing((edu.byui.whatsupp.ViewThingToDo)this, message);

        setupActionBar();
    }

    /**
     * This method is used to display the components of
     * an individual ThingToDo object. It is called from the
     * ThingToDo presenter class when it is done getting it
     * from firebase
     * @param item a ThingToDo object
     */
    public void displayThingToDo(ThingToDo item) {
        thing = item;
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        ImageView imageView = (ImageView) findViewById(R.id.thingPicView);
        TextView address = (TextView) findViewById(R.id.addressView);
        TextView description = (TextView) findViewById(R.id.descriptionView);
        Picasso.with(this).load(thing.getUrl()).into(imageView);
        String text = thing.getAddress() + ", " + thing.getCity() + " " +  thing.getZipCode();
        address.setText(text);
        description.setText(thing.getDescription());
        imageView.setVisibility(View.VISIBLE);
        address.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);

        if(thing.getCreator() != null) { //If the creator he can edit, need to make a way of seeing if the user is admin
            String uid = currentUser.getUid();
            if(thing.getCreator().equals( uid)) {
                Button editButton = (Button) findViewById(R.id.editButton);
                editButton.setVisibility(View.VISIBLE);
            }
        }

    }


    /**
     * This method is send the user to the ThingToDoForm
     * page to edit the current selected ThingToDo
     * The user is only able to do this if they created
     * it.
     * @param view the view from the activity
     */
    public void updateThing(View view) {
        Intent intent = new Intent(this, ThingToDoForm.class);
        Bundle extras = new Bundle();
        extras.putString("EXTRA_FORMTYPE","Update");
        extras.putString("EXTRA_FORMINFO",thing.getTitle());
        Log.i("Intent", "Send User to Form");
        intent.putExtras(extras);

        startActivity(intent);
    }


    /**
     * This method is used to display the list of current events
     * for the current ThingToDo. It is called from the
     * ThingToDo presenter class when it is done getting the
     * data from firebase.
     * @param events a list of Events
     */
    public void displayEventsForThing(List<Event> events) {
        if (events.size() < 1) {
            // If there are no events, the image is a frowny face.
            Event event = new Event("No events currently for this place.", "http://moziru.com/images/emotions-clipart-frowny-face-12.jpg");
            events.add(event);
        }
        EventAdapter eventAdapter = new EventAdapter(this, events, this, 1);
        listView = (ListView) this.findViewById(R.id.listView1);
        listView.setAdapter(eventAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                Object o = listView.getItemAtPosition(position);
                Event event = (Event)o;
                Intent intent = new Intent(ViewThingToDo.this, ViewEvent.class);
                intent.putExtra(EXTRA_MESSAGE, event.getTitle());
                Log.i("Intent", "Send User to ViewEvent");
                startActivity(intent);

            }
        });

    }

    /**
     * Sends the user to the EventForm to create a
     * new current event for the currently choosen
     * ThingToDo
     * @param view the view of the activity
     */
    public void addEvent(View view) {
        Intent intent = new Intent(this, EventForm.class);
        Bundle extras = new Bundle();
        extras.putString("EXTRA_FORMTYPE","create");
        extras.putString("EXTRA_FORMINFO", thing.getTitle() );
        extras.putString("EXTRA_THINGTITLE",thing.getTitle());
        extras.putString("EXTRA_PICURL",thing.getUrl());
        intent.putExtras(extras);
        startActivity(intent);
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
                    final PopupMenu popup = new PopupMenu(ViewThingToDo.this, popupButton);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("My Profile")) {

                                Intent intent = new Intent(ViewThingToDo.this, Profile.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to Profile");
                                startActivity(intent);
                            }
                            else if(item.getTitle().equals("View Groups")) {
                                Intent intent = new Intent(ViewThingToDo.this, GroupsView.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to View Groups");
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(ViewThingToDo.this, LoginPage.class);
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
                    Intent intent = new Intent(ViewThingToDo.this, LoginPage.class);
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
                Intent intent = new Intent(ViewThingToDo.this, HomePage.class);
                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, "");
                Log.i("Intent", "Send User to Home Page");
                startActivity(intent);
            }
        });
    }
}
