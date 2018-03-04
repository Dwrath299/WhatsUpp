package edu.byui.whatsupp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewThingToDo extends AppCompatActivity {
    private String message;
    private ThingToDoActivity ttda;
    private EventActivity ea;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veiw_thing_to_do);
        ttda = new ThingToDoActivity(this);
        ea = new EventActivity(this);
        Intent intent = getIntent();
        message = intent.getStringExtra(HomePage.EXTRA_MESSAGE);
        ttda.displayThingToDo(this, message);
        ea.displayEventsForThing((edu.byui.whatsupp.ViewThingToDo)this, message);
    }

    public void displayThingToDo(ThingToDo thing) {
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

    }

    public void displayEventsForThing(List<Event> events) {
        if (events.size() < 1) {
            // If there are no events, the image is a frowny face.
            Event event = new Event("No events currently for this place.", "http://moziru.com/images/emotions-clipart-frowny-face-12.jpg");
            events.add(event);
        }
        EventAdapter eventAdapter = new EventAdapter(this, events, this);
        listView = (ListView) this.findViewById(R.id.listView1);
        listView.setAdapter(eventAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                Object o = listView.getItemAtPosition(position);
                Event event = (Event)o;
                Toast.makeText(ViewThingToDo.this, "You selected " + event.getTitle() ,
                        Toast.LENGTH_LONG).show();

            }
        });

    }
}
