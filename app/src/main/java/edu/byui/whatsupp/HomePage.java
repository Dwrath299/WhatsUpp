package edu.byui.whatsupp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;
import static android.provider.AlarmClock.EXTRA_MESSAGE;


/**
 * <h1>Home Page</h1>
 * The Home Page is the beginning screen in the app,
 * displays the Things To Do in the area. The Users
 * are also able to create new things on this page.
 * <p>
 *
 *
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */
public class HomePage extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "edu.byui.whatsapp.Message";
    public ThingToDoActivity thingToDoActivity;
    private FirebaseAuth mAuth;
    List<ThingToDo> things;
    User currentUser;
    ProgressBar spinner;
    private String CHANNEL_ID = "";
    public int notificationId = 0;

    boolean loggedIn;

    /**
     * Creates the activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        thingToDoActivity = new ThingToDoActivity();
        thingToDoActivity.displayThingsToDo(this);

        // Get the logged in Status
        loggedIn = AccessToken.getCurrentAccessToken() == null;
        if (!loggedIn) { // For facebook, logged in = false
            loggedIn = true;
            currentUser = new User(AccessToken.getCurrentAccessToken().getUserId());
        } else {
            loggedIn = false;
            currentUser = new User("123");
        }
        //This needs to be done AFTER log in.
        setupActionBar();
        NotificationManager notificationManager = NotificationManager.from(this);
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
        Intent intent = new Intent(this, GroupView.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

//        Intent snoozeIntent = new Intent(this, MyBroadcastReceiver.class);
//        snoozeIntent.setAction(ACTION_SNOOZE);
//        snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
//        PendingIntent snoozePendingIntent =
//                PendingIntent.getBroadcast(this, 0, snoozeIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        // Build the notification and add the action.
//        Notification newMessageNotification = new Notification.Builder(mContext, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_message)
//                .setContentTitle(getString(R.string.title))
//                .setContentText(getString(R.string.content))
//                .addAction(action)
//                .build();
//
//        // Issue the notification.


        notificationManager.notify(notificationId,mBuilder.build());
    }

    /**
     * Sets up the grid to display the list of things  to do.
     * @param t
     */
    public void setGridView(List<ThingToDo> t) {
        things = t;
        spinner.setVisibility(View.GONE);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        ImageAdapter imageAdapter = new ImageAdapter(this, things, this);
        gridview.setAdapter(imageAdapter);

    }

    /**
     * Sends the user to the login page.
     * @param view
     */
    public void goToLogin (View view) {
        Intent intent = new Intent(this, LoginPage.class);
        intent.putExtra(EXTRA_MESSAGE, "HomePage");
        Log.i("Intent", "Send User to Login");
        startActivity(intent);
    }

    /**
     * If the user is logged in, the user is sent to the ThingToDoForm to add a new thing to do.
     * @param view
     */
    public void addThingToDo (View view) {
        if (!loggedIn) //Make sure they are logged in.
        {
            Toast.makeText(HomePage.this, "You must log in to add",
                    Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, ThingToDoForm.class);
            intent.putExtra(EXTRA_MESSAGE, "Create");
            Log.i("Intent", "Send User to Form");
            startActivity(intent);
        }
    }

    /**
     * Allows users to click on a thing to do in the grid and view it in more detail.
     * @param title
     */
    public void thingClick(String title) {
        Intent intent = new Intent(this, ViewThingToDo.class);
        intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, title);
        Log.i("Intent", "Send User to ThingToDoView");
        startActivity(intent);
    }

    /**
     * Sets up the custom action bar for the home page.
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
                    final PopupMenu popup = new PopupMenu(HomePage.this, popupButton);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("My Profile")) {

                                Intent intent = new Intent(HomePage.this, Profile.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to Profile");
                                startActivity(intent);
                            }
                            else if(item.getTitle().equals("View Groups")) {
                                Intent intent = new Intent(HomePage.this, GroupsView.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to View Groups");
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(HomePage.this, LoginPage.class);
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
                    Intent intent = new Intent(HomePage.this, LoginPage.class);
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
                Toast.makeText(getApplicationContext(), "Sorry to disappoint, you are on the home page!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
