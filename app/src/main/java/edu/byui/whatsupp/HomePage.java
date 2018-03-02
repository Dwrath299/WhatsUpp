package edu.byui.whatsupp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class HomePage extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "edu.byui.whatsapp.Message";
    public ThingToDoActivity thingToDoActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        thingToDoActivity = new ThingToDoActivity(this);



        //new Load(this).execute();
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        thingToDoActivity.displayThingsToDo( (ImageAdapter) gridview.getAdapter());
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
        intent.putExtra(EXTRA_MESSAGE, "work");
        Log.i("Intent", "Send User to Login");
        startActivity(intent);

    }
    public void addThingToDo (View view) {
        Intent intent = new Intent(this, ThingToDoForm.class);
        intent.putExtra(EXTRA_MESSAGE, "work");
        Log.i("Intent", "Send User to Form");
        startActivity(intent);
    }
}
