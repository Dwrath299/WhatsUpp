package edu.byui.whatsupp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class GroupsView extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "edu.byui.whatsapp.Message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_view);
    }

    public void viewGroup(View view) {
        Intent intent = new Intent(this, GroupView.class);
        intent.putExtra(EXTRA_MESSAGE, "View Group");
        Log.i("Intent", "Send User to Group View");
        startActivity(intent);
    }

    public void createGroup(View view) {
        Intent intent = new Intent(this, GroupCreation.class);
        intent.putExtra(EXTRA_MESSAGE, "Create Group");
        Log.i("Intent", "Send User to Group Creation");
        startActivity(intent);
    }

    public void searchGroup(String groupName) {
        //take from whats typed into the textbox, and search the list of groups
        

    }
}
