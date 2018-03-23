package edu.byui.whatsupp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * <h1>ThingToDo Form</h1>
 * The activity class for creating a new ThingToDo
 * Asks for input of picture, title, description,
 * and address information.
 *
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */
public class ThingToDoForm extends AppCompatActivity {
    private StorageReference storageRef;
    public static final String EXTRA_MESSAGE = "edu.byui.whatsapp.Message";
    ThingToDo thing;
    ThingToDoActivity ttda;
    private FirebaseAuth mAuth;
    User currentUser;
    String message;
    boolean loggedIn;

    private int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_to_do_form);
        Intent intent = getIntent();
        message = intent.getStringExtra(HomePage.EXTRA_MESSAGE);
        setupActionBar();
        // If not creating then updating.
        if(message != "Create")
        {
            ttda = new ThingToDoActivity(this);
            ttda.getThingToEdit(this, message);
        }
        mAuth = FirebaseAuth.getInstance();
        // Get the logged in Status
        loggedIn = AccessToken.getCurrentAccessToken() == null;
        if(!loggedIn){ // For facebook, logged in = false
            loggedIn = true;
            currentUser = new User(AccessToken.getCurrentAccessToken().getUserId());
        } else {
            loggedIn = false;
            currentUser = new User("123");
        }


    }

    public void displayThingData(ThingToDo thing) {
        this.thing = thing;
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        EditText title = (EditText) findViewById(R.id.editTitle);
        EditText address = (EditText) findViewById(R.id.editAddress);
        EditText description = (EditText) findViewById(R.id.editDescription);
        EditText city = (EditText) findViewById(R.id.editCity);
        EditText zip = (EditText) findViewById(R.id.editZip);
        Picasso.with(this).load(thing.getUrl()).into(imageView);
        title.setText(thing.getTitle());
        address.setText(thing.getAddress());
        description.setText(thing.getDescription());
        city.setText(thing.getCity());
        zip.setText(Long.toString(thing.getZipCode()));
        Button delete = (Button) findViewById(R.id.thing_delete_btn);
        delete.setVisibility(View.VISIBLE);
        Button update = (Button) findViewById(R.id.button2);
        update.setText("Update");
    }

    public void deleteThingToDo(View view) {
        ttda.deleteThing(thing.getReference());
        Intent intent = new Intent(ThingToDoForm.this, HomePage.class);
        // No real reason for sending UID with it, just because
        intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
        Log.i("Intent", "Send User to Home page");
        startActivity(intent);
    }

    public void addPicture(View view) {
        //Get incoming intent
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose Picture"), PICK_IMAGE_REQUEST);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void submit (View view) {
        Button update = (Button) findViewById(R.id.button2);
        if(update.getText() == "Update")
        {
            ttda.deleteThing(thing.getReference());
        }
        // NEED TO MAKE SURE THERE ALREADY ISN'T ONE
        storageRef = FirebaseStorage.getInstance().getReference();
        EditText editText = findViewById(R.id.editTitle);
        String title = editText.getText().toString();
        editText = findViewById(R.id.editAddress);
        String address= editText.getText().toString();
        editText = findViewById(R.id.editCity);
        String city = editText.getText().toString();
        editText = findViewById(R.id.editZip);
        long zip = Integer.parseInt(editText.getText().toString());
        editText = findViewById(R.id.editDescription);
        String description = editText.getText().toString();
        String url;
        //Create ref NEED TO MAKE SURE THERE
        StorageReference thingToDoRef = storageRef.child("ThingsToDoImages/" +title + ".jpg");

        // Get the data from an ImageView as bytes
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
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
        thing = new ThingToDo(url, title, address, city, zip, description);

        // Returns back to the previous page
        finish();

    }
    //This will get run when the past process is completed
    public void addToDB(String url) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        thing.setCreator(currentUser.getUid());
        thing.setUrl(url);
        db.collection("thingsToDo")
                .add(thing)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(ThingToDoForm.this, "Successfully Added " + thing.getTitle(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                        Toast.makeText(ThingToDoForm.this, "Failure",
                                Toast.LENGTH_SHORT).show();
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
        actionTitle.setText("Create a Place");

        final ImageButton popupButton = (ImageButton) findViewById(R.id.btn_menu);
        Button loginButton = (Button) findViewById(R.id.login_btn);
        if(loggedIn) {
            popupButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            popupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Creating the instance of PopupMenu
                    final PopupMenu popup = new PopupMenu(ThingToDoForm.this, popupButton);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("My Profile")) {

                                Intent intent = new Intent(ThingToDoForm.this, Profile.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to Profile");
                                startActivity(intent);
                            }
                            else if(item.getTitle().equals("View Groups")) {
                                Intent intent = new Intent(ThingToDoForm.this, GroupsView.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to View Groups");
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(ThingToDoForm.this, LoginPage.class);
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
                    Intent intent = new Intent(ThingToDoForm.this, LoginPage.class);
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
                Intent intent = new Intent(ThingToDoForm.this, HomePage.class);
                // No real reason for sending UID with it, just because
                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                Log.i("Intent", "Send User to Home page");
                startActivity(intent);
            }
        });
    }
}
