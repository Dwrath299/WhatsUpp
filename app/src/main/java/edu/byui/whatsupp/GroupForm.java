package edu.byui.whatsupp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CustomTabMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * <h1>Group Form</h1>
 * The Group Form is the activity class where Users
 * will create a new Group. Request input for a
 * picture, title, and users.
 * 
 *
 *
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */

public class GroupForm extends AppCompatActivity {

    //copied over from EventForm to get submit() to work
    String formType;
    EventActivity ea;
    GroupActivity ga;
    Event event;
    Group group;
    boolean needToStoreImage;
    private StorageReference storageRef;
    String picURL;
    String thingTitle;
    String formInfo;


    private int PICK_IMAGE_REQUEST = 1;
    User currentUser;
    boolean loggedIn;
    Spinner spinner2;
    EditText search;

    ArrayList<User> userList = new ArrayList<User>();
    int numMembers = 0;
    //ArrayList<User> userList = new ArrayList<User>();
    ArrayList<String> userNameList = new ArrayList<String>();
    final ArrayList<User> list = new ArrayList<>();
    List<User> selectedUsers = new ArrayList<User>();



	/**
     * On Create
	 * Retrieves the information from the intent
	 * Gets current user info
	 * @param savedInstanceState
	 * 
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_form);
        // Get the logged in Status
        loggedIn = AccessToken.getCurrentAccessToken() == null;
        if (!loggedIn) { // For facebook, logged in = false
            loggedIn = true;
            currentUser = new User(AccessToken.getCurrentAccessToken().getUserId());
        } else {
            loggedIn = false;
            currentUser = new User("123");
        }

        setupActionBar();
        populateUserList();

        //spinner2 = (Spinner) findViewById(R.id.spinner2);
        //search =(EditText)findViewById(R.id.SearchBar);
        /*
        search.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                searchUser(GroupForm.this, search.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
        });
        */
    }

    /*
        Adds user id's to an arraylist group of users that is stored in firebase
     */

    public void createGroup (View view) {

            //.... we don't need this, right?
        /*if(formType.equals("update")) {
            ea.deleteEvent(event.getRefrence());
        }*/
            EditText editText = findViewById(R.id.groupName);
            String title = editText.getText().toString();

            String url;
            if (needToStoreImage) {
                //Create ref
                storageRef = FirebaseStorage.getInstance().getReference();
                //StorageReference thingToDoRef = storageRef.child("EventImages/" +title + ".jpg");
                StorageReference thingToDoRef = storageRef.child("notsurewhatfolderthiswillbein/" + title + ".jpg");

                // Get the data from an ImageView as bytes
                ImageView imageView = (ImageView) findViewById(R.id.groupImage);
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                Bitmap bitmap = imageView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                // Upload
                UploadTask uploadTask = thingToDoRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        addToDB(downloadUrl.toString());

                    }
                });
                // Get URL
                url = "Will get replaced";
            } else {
                url = picURL;
            }
            ArrayList<String> uidList = new ArrayList<String>();
            for(int i = 0; i < selectedUsers.size(); i ++) {
                uidList.add(selectedUsers.get(i).getUid());
            }

            group = new Group(title, uidList, url);
            group.setNumMembers(numMembers);

            if (!needToStoreImage) {
                addToDB(url);
            }
            // Returns back to the previous page
            finish();

        }
        //This will get run when the past process is completed

    public void addToDB(String url) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        group.setCreator(currentUser.getUid());
        //if(formType.equals("group")) {
        //    event.setGroup(formInfo);
        //}
        //event.addAttendee(currentUser.getUid());
        if (needToStoreImage) {
            group.setUrl(url);
        }
        db.collection("groups")
                .add(group)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(GroupForm.this, "Successfully Created " + group.getTitle(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                        Toast.makeText(GroupForm.this, "Failure",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /*
        Updates the real-time listview of users who are currently selected
     */

    public void updateList() {
        ListView LV = (ListView) findViewById(R.id.selUsers);
        UserAdapter dataAdapter = new UserAdapter(this, selectedUsers, GroupForm.this, 2);
        LV.setAdapter(dataAdapter);
    }

    /*
        Fills a list with potential users that is used as the pool of users in the
        autocompletetextview
     */

    public void populateUserList() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean notThere = true;


                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                User user = new User(document.get("uid").toString());
                                user.setLastName(document.get("lastName").toString());
                                user.setFirstName(document.get("firstName").toString());
                                user.setEmail(document.get("email").toString());
                                user.setGender(document.get("gender").toString());
                                //userList.add(user);
                                list.add(user);
                            }

                            //Creating the instance of ArrayAdapter containing list of fruit names

                            //Getting the instance of AutoCompleteTextView
                            AutoCompleteTextView auto = (AutoCompleteTextView) findViewById(R.id.actv);
                            ArrayAdapter<User> dataAdapter = new ArrayAdapter<User>(GroupForm.this, android.R.layout.simple_dropdown_item_1line, list);


                            auto.setThreshold(1);//will start working from first character
                            auto.setAdapter(dataAdapter);//setting the adapter data into the AutoCompleteTextView
                            auto.setTextColor(Color.BLACK);

                            auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                        long arg3) {
                                    User selected = (User) arg0.getAdapter().getItem(arg2);
                                    selectedUsers.add(selected);
                                    numMembers++;
                                    Toast.makeText(GroupForm.this,
                                            "Clicked " + arg2 + " name: " + selected.firstName,
                                            Toast.LENGTH_SHORT).show();
                                    updateList();
                                }
                            });


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });

    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /*

     */

    public void addPicture(View view) {
        //Get incoming intent
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose Picture"), PICK_IMAGE_REQUEST);
    }

    /*
    public void searchUser(final Activity activity, final String search) {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean notThere = true;


                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                    User user = new User(document.get("uid").toString());
                                    user.setLastName(document.get("lastName").toString());
                                    user.setFirstName(document.get("firstName").toString());
                                    user.setEmail(document.get("email").toString());
                                    user.setGender(document.get("gender").toString());
                                    userList.add(user);
                            }

                            SpinnerAdapter dataAdapter = new SpinnerAdapter(GroupForm.this, userList, GroupForm.this);
                            //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner2.setAdapter(dataAdapter);
                            spinner2.setVisibility(View.VISIBLE);



                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });

    }
    */

	/**
     * Setup ActionBar
	 * Intializes the action bar to have the functionality of
	 * the home button and drop down list if the user is
	 * logged in, otherwise, a log in button.
	 * Called by the On Create method
     */
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
        actionTitle.setText("Create a Group");

        final ImageButton popupButton = (ImageButton) findViewById(R.id.btn_menu);
        Button loginButton = (Button) findViewById(R.id.login_btn);
        if(loggedIn) {
            popupButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
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

                            } else {
                                Intent intent = new Intent(GroupForm.this, LoginPage.class);
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
                    Intent intent = new Intent(GroupForm.this, LoginPage.class);
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
                Intent intent = new Intent(GroupForm.this, HomePage.class);
                // No real reason for sending UID with it, just because
                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                Log.i("Intent", "Send User to Home page");
                startActivity(intent);
            }
        });
    }
}
