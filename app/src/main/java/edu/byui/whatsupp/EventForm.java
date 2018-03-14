package edu.byui.whatsupp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class EventForm extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String message;
    EventActivity ea;
    TimePicker timePicker;
    DatePicker datePicker;
    private int PICK_IMAGE_REQUEST = 1;
    String thingUrl;
    String thingTitle;
    boolean needToStoreImage;
    private StorageReference storageRef;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);

        // Bring in the thing to do info
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        thingTitle = extras.getString("EXTRA_THINGTITLE");
        thingUrl = extras.getString("EXTRA_THINGURL");
        ImageView eventImageView = (ImageView) findViewById(R.id.eventImageView);
        Picasso.with(this).load(thingUrl).into(eventImageView);
        EditText eventTitle = (EditText) findViewById(R.id.editEventTitle);
        eventTitle.setHint(thingTitle + " event");

        //Get user info
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //Initialize the time and date pickers
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        datePicker = (DatePicker) findViewById(R.id.datePicker);

        ea = new EventActivity(this);

        // If the user doesn't select a pic, just use the url of the already exsisting
        needToStoreImage = false;
    }

    public void addPicture(View view) {
        //Get incoming intent
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose Picture"), PICK_IMAGE_REQUEST);
        needToStoreImage = true;
    }

    public void pickTime(View view) {
        hideTimeOrDate();
        timePicker.setVisibility(View.VISIBLE);
    }

    private void hideTimeOrDate(){
        if(timePicker.getVisibility() == View.VISIBLE) {
            if (getCurrentFocus() != null && getCurrentFocus() instanceof TimePicker) {

                timePicker.setVisibility(View.INVISIBLE);
            }
        }
        if(datePicker.getVisibility() == View.VISIBLE) {
            if (getCurrentFocus() != null && getCurrentFocus() instanceof DatePicker) {

                datePicker.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void pickDate(View view) {
        hideTimeOrDate();
        datePicker.setVisibility(View.VISIBLE);
    }

    public void submit (View view) {

        // NEED TO MAKE SURE THERE ALREADY ISN'T ONE

        EditText editText = findViewById(R.id.editEventTitle);
        String title = editText.getText().toString();
        editText = findViewById(R.id.editEventDescription);
        String description= editText.getText().toString();
        editText = findViewById(R.id.editCity);
        String time = editText.getText().toString();
        editText = findViewById(R.id.editDescription);
        String date = editText.getText().toString();
        String url;
        if(needToStoreImage) {
            //Create ref
            storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference thingToDoRef = storageRef.child("EventImages/" +title + ".jpg");
            // Get the data from an ImageView as bytes
            ImageView imageView = (ImageView) findViewById(R.id.eventpic1);
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
            url = thingUrl;
        }

        event = new Event( title, description, date, time, thingTitle, url);

        // Returns back to the previous page
        finish();

    }
    //This will get run when the past process is completed
    public void addToDB(String url) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        event.setCreator(currentUser.getUid());
        if(needToStoreImage) {
            event.setUrl(url);
        }
        db.collection("events")
                .add(event)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(EventForm.this, "Successfully Created " + event.getTitle(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                        Toast.makeText(EventForm.this, "Failure",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
