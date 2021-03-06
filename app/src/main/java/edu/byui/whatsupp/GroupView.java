package edu.byui.whatsupp;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.*;
import android.widget.Spinner;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static edu.byui.whatsupp.HomePage.EXTRA_MESSAGE;

import com.facebook.AccessToken;
/**
 * <h1>Group View</h1>
 * The Group View will display the group that the user chose.
 * Displays the events happening in the group and a message board.
 * Users can create events for the group by pressing the create 
 * button.
 * 
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */

public class GroupView extends AppCompatActivity  {
    public static final String EXTRA_MESSAGE = "edu.byui.whatsapp.Message";
    User currentUser;
    boolean loggedIn;
    Group currentGroup;
    private FirebaseListAdapter<ChatMessage> adapter;
    private EditText yourEditText;
    private String voteRef;
    EventActivity ea;
    GroupActivity ga;
    String message;
    ListView listView;
    //Spinner spinner = (Spinner) findViewById(R.id.userSearchSpinner);
    edu.byui.whatsupp.Profile profileActivity;


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
        setContentView(R.layout.activity_group_view);
        Intent intent = getIntent();
        message = intent.getStringExtra(EXTRA_MESSAGE);
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
        ga = new GroupActivity();
        ea.getEventsForGroup((edu.byui.whatsupp.GroupView)this, message);
        ga.getGroup(this, message);
        //Show the chat messages from the group
        displayChatMessages();




        //Setting up onclick listener so user can send a message to the group
        FloatingActionButton fab =
                (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of ChatMessage to the Firebase database
                FirebaseDatabase.getInstance()
                        .getReference()
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser()
                                        .getDisplayName(), message)
                        );

                // Clear the input
                input.setText("");
            }
        });


    }

    public void setCurrentGroup(Group group) {
        currentGroup = group;
    }

    /**
	 * Display Events For Group
     * This method is used to display the list of current events
     * for the current ThingToDo. It is called from the
     * ThingToDo presenter class when it is done getting the
     * data from firebase.
     * @param events a list of Events
     */
    public void displayEventsForGroup(List<Event> events) {
        if (events.size() < 1) {
            // If there are no events, the image is a frowny face.
            Event event = new Event("No events currently for this group.", "http://moziru.com/images/emotions-clipart-frowny-face-12.jpg");
            events.add(event);
        }
        EventAdapter eventAdapter = new EventAdapter(this, events, this, 3);
        listView = (ListView) this.findViewById(R.id.group_event_list);
        listView.setAdapter(eventAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                Object o = listView.getItemAtPosition(position);
                Event event = (Event)o;
                Intent intent = new Intent(GroupView.this, ViewEvent.class);
                intent.putExtra(EXTRA_MESSAGE, event.getTitle());
                Log.i("Intent", "Send User to ViewEvent");
                startActivity(intent);

            }
        });

    }

    /**
     * Called from the event presenter, it means there is a vote happening
     * in the group. Shows the button that will take them to the vote.
     * @param voteRef
     */
    public void displayVote(String voteRef) {
        this.voteRef = voteRef;
        Button btn = findViewById(R.id.go_to_vote_btn);
        btn.setText("Vote Currently in Process");
        btn.setVisibility(View.VISIBLE);
    }

    /**
     * Only available if there is a vote happening in the group.
     *
     * @param view from the button
     */
    public void goToVote(View view) {
        Intent intent = new Intent(this, ViewVote.class);
        intent.putExtra(EXTRA_MESSAGE, voteRef);
        Log.i("Intent", "Send User to Vote");
        startActivity(intent);
    }

	/**
	 * Display Chat Messages
     * Will display the messages specific to this group.
	 * Displays the message, the user who sent it, and the time
	 * they sent it.
     * 
     */
    public void displayChatMessages() {
        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();


        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                if(model.getGroup().equals(message)) {
                    // Get references to the views of message.xml
                    TextView messageText = (TextView) v.findViewById(R.id.message_text);
                    TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                    TextView messageTime = (TextView) v.findViewById(R.id.message_time);

                    // Set their text
                    messageText.setText(model.getMessageText());
                    messageUser.setText(model.getMessageUser());

                    // Format the date before showing it
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                            model.getMessageTime()));
                    String CHANNEL_ID = "";
                    int notificationId = 0;
                    NotificationManager notificationManager =  getSystemService(NotificationManager.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // Create the NotificationChannel, but only on API 26+ because
                        // the NotificationChannel class is new and not in the support library
                        CharSequence name = getString(R.string.channel_name);
                        String description = getString(R.string.channel_description);
                        int importance = NotificationManager.IMPORTANCE_DEFAULT;
                        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                        channel.setDescription(description);
                        // Register the channel with the system
                        notificationManager.createNotificationChannel(channel);
                    }

                    // Create an explicit intent for an Activity in your app
                    Intent intent = new Intent(GroupView.this, GroupView.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(GroupView.this, 0, intent, 0);

                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(GroupView.this, CHANNEL_ID)
                            .setSmallIcon(R.drawable.messenger_bubble_small_blue)
                            .setContentTitle("My notification")
                            .setContentText("Hello World!")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            // Set the intent that will fire when the user taps the notification
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);

                    notificationManager.notify(notificationId,mBuilder.build());
                }
            }
        };

        listOfMessages.setAdapter(adapter);
    }


	/**
	 * Create Event
	 * The user will be directed to the Thing To Do Select
	 * page to either create an event or vote for the group.
     * @param view
     */
    public void createEvent(View view) {
        Intent intent = new Intent(GroupView.this, ThingToDoSelect.class);
        // Send the group title
        Bundle extras = new Bundle();
        extras.putSerializable("EXTRA_GROUP",currentGroup);

        intent.putExtras(extras);

        Log.i("Intent", "Send User to ThingToDoSelect");
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
        actionTitle.setText("WhatsUpp");

        final ImageButton popupButton = (ImageButton) findViewById(R.id.btn_menu);
        Button loginButton = (Button) findViewById(R.id.login_btn);
        if(loggedIn) {
            popupButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            popupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Creating the instance of PopupMenu
                    final PopupMenu popup = new PopupMenu(GroupView.this, popupButton);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("My Profile")) {

                                Intent intent = new Intent(GroupView.this, Profile.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to Profile");
                                startActivity(intent);
                            }
                            else if(item.getTitle().equals("View Groups")) {
                                Intent intent = new Intent(GroupView.this, GroupsView.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to View Groups");
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(GroupView.this, LoginPage.class);
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
                    Intent intent = new Intent(GroupView.this, LoginPage.class);
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
                Intent intent = new Intent(GroupView.this, HomePage.class);
                // No real reason for sending UID with it, just because
                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                Log.i("Intent", "Send User to Login page");
                startActivity(intent);
            }
        });
    }




}
