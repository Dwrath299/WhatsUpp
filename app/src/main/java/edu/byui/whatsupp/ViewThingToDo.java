package edu.byui.whatsupp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ViewThingToDo extends AppCompatActivity {
    private String message;
    private ThingToDoActivity ttda;
    private EventActivity ea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiw_thing_to_do);
        ttda = new ThingToDoActivity(this);
        ea = new EventActivity(this);
        Intent intent = getIntent();
        message = intent.getStringExtra(HomePage.EXTRA_MESSAGE);
    }

    //public void setListView(List<Event> events)
}
