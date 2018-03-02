package edu.byui.whatsupp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

public class HomePage extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "edu.byui.whatsapp.Message";
    public ThingToDoActivity thingToDoActivity;
    ProgressBar spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);
        thingToDoActivity = new ThingToDoActivity(this);
        thingToDoActivity.displayThingsToDo(this);
    }

//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_home_page);
//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.mainmenu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            // action with ID action_refresh was selected
//            case R.id.action_refresh:
//                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
//                        .show();
//                break;
//            // action with ID action_settings was selected
//            case R.id.action_settings:
//                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
//                        .show();
//                break;
//            default:
//                break;
//        }
//
//        return true;
//    }

    public void setGridView(List<ThingToDo> things) {
        spinner.setVisibility(View.GONE);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        ImageAdapter imageAdapter = new ImageAdapter(this, things, this);
        gridview.setAdapter(imageAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(HomePage.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void goToLogin (View view) {
        Intent intent = new Intent(this, LoginPage.class);
        intent.putExtra(EXTRA_MESSAGE, "HomePage");
        Log.i("Intent", "Send User to Login");
        startActivity(intent);

    }
    public void addThingToDo (View view) {
        Intent intent = new Intent(this, ThingToDoForm.class);
        intent.putExtra(EXTRA_MESSAGE, "HomePage");
        Log.i("Intent", "Send User to Form");
        startActivity(intent);
    }
    public void thingClick() {
        Intent intent = new Intent(this, LoginPage.class);
        intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, "HomePage");
        Log.i("Intent", "Send User to ThingToDoView");
        startActivity(intent);
    }
}
