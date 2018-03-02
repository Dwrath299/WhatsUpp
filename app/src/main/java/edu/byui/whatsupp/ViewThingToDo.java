package edu.byui.whatsupp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        ttda.displayThingToDo(this, message);
    }

    public void displayThingToDo(ThingToDo thing) {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        ImageView imageView = (ImageView) findViewById(R.id.thingPicView);
        TextView address = (TextView) findViewById(R.id.addressView);
        TextView description = (TextView) findViewById(R.id.descriptionView);
        Picasso.with(this).load(thing.getUrl()).into(imageView);
        address.setText(thing.getAddress() + ", " + thing.getCity() + " " +  thing.getZipCode());
        description.setText(thing.getDescription());
        imageView.setVisibility(View.VISIBLE);
        address.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);

    }

    //public void setListView(List<Event> events)
}
