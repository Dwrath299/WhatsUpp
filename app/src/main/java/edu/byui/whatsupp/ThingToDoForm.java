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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class ThingToDoForm extends AppCompatActivity {
    private StorageReference storageRef;
    public static final String EXTRA_MESSAGE = "edu.byui.whatsapp.Message";
    ThingToDo thing;
    private FirebaseAuth mAuth;
    User currentUser;
    String message;

    private int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_to_do_form);
        Intent intent = getIntent();
        message = intent.getStringExtra(HomePage.EXTRA_MESSAGE);
        mAuth = FirebaseAuth.getInstance();
        currentUser = new User(AccessToken.getCurrentAccessToken().getUserId());


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






}
