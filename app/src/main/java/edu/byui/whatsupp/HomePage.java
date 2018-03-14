package edu.byui.whatsupp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class HomePage extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "edu.byui.whatsapp.Message";
    public ThingToDoActivity thingToDoActivity;
    private FirebaseAuth mAuth;
    List<ThingToDo> things;
    FirebaseUser currentUser;
    ProgressBar spinner;

    boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        setupActionBar();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        thingToDoActivity = new ThingToDoActivity(this);
        thingToDoActivity.displayThingsToDo(this);
        Button loginButton = (Button) findViewById(R.id.button3);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        loggedIn = AccessToken.getCurrentAccessToken() == null;
        //loggedIn = false;
        //loggedIn = sharedPref.getBoolean("LoggedIn", loggedIn);
        if(!loggedIn){
            loginButton.setText("Logout");
            loggedIn = true;
        } else {
            loginButton.setText("Login");
            loggedIn = false;
        }
    }



    public void setGridView(List<ThingToDo> t) {
        things = t;
        spinner.setVisibility(View.GONE);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        ImageAdapter imageAdapter = new ImageAdapter(this, things, this);
        gridview.setAdapter(imageAdapter);

    }


    public void goToLogin (View view) {
        Intent intent = new Intent(this, LoginPage.class);
        intent.putExtra(EXTRA_MESSAGE, "HomePage");
        Log.i("Intent", "Send User to Login");
        startActivity(intent);

    }
    public void addThingToDo (View view) {
        if (!loggedIn) //Make sure they are logged in.
        {
            Toast.makeText(HomePage.this, "You must log in to add",
                    Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(this, ThingToDoForm.class);
            intent.putExtra(EXTRA_MESSAGE, "HomePage");
            Log.i("Intent", "Send User to Form");
            startActivity(intent);
        }
    }
    public void thingClick(String title) {


        Intent intent = new Intent(this, ViewThingToDo.class);
        intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, title);
        Log.i("Intent", "Send User to ThingToDoView");
        startActivity(intent);
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
        actionTitle.setText("WhatsUpp");

        final ImageButton popupButton = (ImageButton) findViewById(R.id.btn_menu);
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
                        if(item.getTitle() == "My Profile") {

                            Intent intent = new Intent(HomePage.this, Profile.class);
                            intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                            Log.i("Intent", "Send User to Profile");
                            startActivity(intent);
                        }
                        else if(item.getTitle() =="View Groups") {
                            Intent intent = new Intent(HomePage.this, Groups.class);
                            intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                            Log.i("Intent", "Send User to View Groups");
                            startActivity(intent);

                        }
                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        }); //closing the setOnClickListener method

        //Detect the button click event of the home button in the actionbar
        ImageButton btnHome = (ImageButton) findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Home Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
