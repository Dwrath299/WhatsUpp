package edu.byui.whatsupp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class ThingToDoForm extends AppCompatActivity {
    private StorageReference storageRef;
    private DatabaseReference mDatabase;

    private int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thing_to_do_form);
        Intent intent = getIntent();
        String message = intent.getStringExtra(HomePage.EXTRA_MESSAGE);

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
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("thingsToDo").push();
        String key = mDatabase.child("thingsToDo").push().getKey();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        storageRef = FirebaseStorage.getInstance().getReference();
        EditText editText = findViewById(R.id.editTitle);
        String title = editText.getText().toString();
        editText = findViewById(R.id.editAddress);
        String address= editText.getText().toString();
        editText = findViewById(R.id.editCity);
        String city = editText.getText().toString();
        editText = findViewById(R.id.editZip);
        int zip = Integer.parseInt(editText.getText().toString());
        editText = findViewById(R.id.editDescription);
        String description = editText.getText().toString();
        ThingToDo thing = new ThingToDo(storageRef, title, address, city, zip, description);
        Uri file = Uri.fromFile(new File(storageRef.getBucket()));
        StorageReference riversRef = storageRef.child("images/rivers.jpg");
        // Upload
        storageRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

        Map<String, Object> thingMap = thing.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/thingsToDo/" + key, thingMap);
        childUpdates.put(key, thingMap);

        mDatabase.updateChildren(childUpdates);
        Toast.makeText(ThingToDoForm.this, "Success",
                Toast.LENGTH_SHORT).show();


    }




}
