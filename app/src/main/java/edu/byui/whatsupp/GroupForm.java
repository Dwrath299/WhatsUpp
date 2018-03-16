package edu.byui.whatsupp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;

public class GroupForm extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_form);
        if (AccessToken.getCurrentAccessToken() != null)
            currentUser = new User(AccessToken.getCurrentAccessToken().getUserId());
        else
            currentUser = new User("123");
        setupActionBar();

    }

    public void addPicture(View view) {
        //Get incoming intent
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose Picture"), PICK_IMAGE_REQUEST);
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
        actionTitle.setText("WhatsUpp");

        final ImageButton popupButton = (ImageButton) findViewById(R.id.btn_menu);
        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                final PopupMenu popup = new PopupMenu(GroupForm.this, popupButton);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("My Profile")) {

                            Intent intent = new Intent(GroupForm.this, Profile.class);
                            intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                            Log.i("Intent", "Send User to Profile");
                            startActivity(intent);
                        }
                        else if(item.getTitle().equals("View Groups")) {
                            Intent intent = new Intent(GroupForm.this, GroupsView.class);
                            intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                            Log.i("Intent", "Send User to View Groups");
                            startActivity(intent);

                        }
                        else if(item.getTitle().equals("Logout")) {
                            Intent intent = new Intent(GroupForm.this, LoginPage.class);
                            // No real reason for sending UID with it, just because
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

        //Detect the button click event of the home button in the actionbar
        ImageButton btnHome = (ImageButton) findViewById(R.id.btn_home);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupForm.this, HomePage.class);
                // No real reason for sending UID with it, just because
                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                Log.i("Intent", "Send User to Home page");
                startActivity(intent);
            }
        });
    }
}
