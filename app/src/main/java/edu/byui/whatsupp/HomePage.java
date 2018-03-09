package edu.byui.whatsupp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;

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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item)
    {
        return false;
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
}
