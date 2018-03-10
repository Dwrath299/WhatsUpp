package edu.byui.whatsupp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class GroupsView extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "edu.byui.whatsapp.Message";
    ListView listView;

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

    public void displayGroups(List<Group> groups) {
        if (groups.size() < 1) {
            // If there are no groups, the image is a very large frowny face.
            Group group = new Group("Not currently part of any group. Please make some friends.", "http://moziru.com/images/emotions-clipart-frowny-face-12.jpg");
            groups.add(group);
        }
        GroupAdapter groupAdapter = new GroupAdapter(this, groups, this);
        listView = (ListView) this.findViewById(R.id.listView1);
        listView.setAdapter(groupAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                Object o = listView.getItemAtPosition(position);
                Group group = (Group)o;
                Toast.makeText(GroupsView.this, "You selected " + group.getTitle() ,
                        Toast.LENGTH_LONG).show();

            }
        });
}
