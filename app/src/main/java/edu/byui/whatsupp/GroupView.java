package edu.byui.whatsupp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class GroupView extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "edu.byui.whatsapp.Message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);
    }

    public void createEvent(View view) {
        Intent intent = new Intent(this, EventForm.class);
        intent.putExtra(EXTRA_MESSAGE, "Create Group");
        Log.i("Intent", "Send User to Group Creation");
        startActivity(intent);

    }
}
