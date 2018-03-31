package edu.byui.whatsupp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;

import java.util.ArrayList;
import java.util.List;

import static edu.byui.whatsupp.HomePage.EXTRA_MESSAGE;

public class GroupsView extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "edu.byui.whatsapp.Message";
    ListView listView;
    User currentUser;
    GroupActivity ga;
    boolean loggedIn;
    ArrayList<User> members;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_view);
        // Get the logged in Status
        loggedIn = AccessToken.getCurrentAccessToken() == null;
        if(!loggedIn){ // For facebook, logged in = false
            loggedIn = true;
            currentUser = new User(AccessToken.getCurrentAccessToken().getUserId());
        } else {
            loggedIn = false;
            currentUser = new User("123");
        }
        ga = new GroupActivity();
        ga.getUsersGroups(this, currentUser.getUid());
        setupActionBar();
    }

    public void viewGroup(View view) {
        Intent intent = new Intent(this, GroupView.class);
        intent.putExtra(EXTRA_MESSAGE, "View Group");
        Log.i("Intent", "Send User to Group View");
        startActivity(intent);
    }

    public void createGroup(View view) {
        Intent intent = new Intent(this, GroupForm.class);
        intent.putExtra(EXTRA_MESSAGE, "Create Group");
        Log.i("Intent", "Send User to Group Creation");
        startActivity(intent);
    }


    public void displayGroups(List<Group> groups) {
        if (groups.size() < 1) {
            // If there are no groups, the image is a very large frowny face.
            Group group = new Group("Not currently part of any group. Please make some friends.", members, "http://moziru.com/images/emotions-clipart-frowny-face-12.jpg");
            groups.add(group);
        }
        GroupAdapter groupAdapter = new GroupAdapter(this, groups, this);
        listView = (ListView) this.findViewById(R.id.groups_list);
        listView.setAdapter(groupAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {

                Object o = listView.getItemAtPosition(position);
                Group group = (Group)o;
                Intent intent = new Intent(GroupsView.this, GroupView.class);
                intent.putExtra(EXTRA_MESSAGE, group.getTitle());
                Log.i("Intent", "Send User to GroupView");
                startActivity(intent);

            }
        });
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
        actionTitle.setText("Your Groups");

        final ImageButton popupButton = (ImageButton) findViewById(R.id.btn_menu);
        Button loginButton = (Button) findViewById(R.id.login_btn);
        if(loggedIn) {
            popupButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            popupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Creating the instance of PopupMenu
                    final PopupMenu popup = new PopupMenu(GroupsView.this, popupButton);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("My Profile")) {

                                Intent intent = new Intent(GroupsView.this, Profile.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to Profile");
                                startActivity(intent);
                            }
                            else if(item.getTitle().equals("View Groups")) {
                                Intent intent = new Intent(GroupsView.this, GroupsView.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to View Groups");
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(GroupsView.this, LoginPage.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to Login page");
                                startActivity(intent);
                            }
                            return true;
                        }
                    });

                    popup.show(); //showing popup menu
                }
            }); //closing the setOnClickListener method

        } else {
            popupButton.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GroupsView.this, LoginPage.class);
                    intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                    Log.i("Intent", "Send User to Login page");
                    startActivity(intent);
                }
            });
        }

        //Detect the button click event of the home button in the actionbar
        ImageButton btnHome = (ImageButton) findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupsView.this, HomePage.class);
                // No real reason for sending UID with it, just because
                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                Log.i("Intent", "Send User to Home page");
                startActivity(intent);
            }
        });
    }
}
