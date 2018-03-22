
package edu.byui.whatsupp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.AccessToken;
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
import java.util.Calendar;

import static java.util.Calendar.*;

/**
 * <h1>Event Form</h1>
 * The Event Form is the activity class where Users
 * will create a new Event. Request input for a
 * picture, title, description, time and date
 * <p>
 *
 *
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */

public class EventForm extends AppCompatActivity {
    private FirebaseAuth mAuth;
    User currentUser;
    String message;
    EventActivity ea;
    private int PICK_IMAGE_REQUEST = 1;
    String thingUrl;
    String thingTitle;
    boolean needToStoreImage;
    private StorageReference storageRef;
    Event event;
    boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);

        // Bring in the thing to do info
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        thingTitle = extras.getString("EXTRA_THINGTITLE");
        thingUrl = extras.getString("EXTRA_THINGURL");
        ea = new EventActivity(this);
        //If thing url is "update" not creating a new one.
        if(thingUrl.equals("update")) {
            ea.getEventForUpdate(this, thingTitle);
        } else {
            ImageView eventImageView = (ImageView) findViewById(R.id.eventImageView);
            Picasso.with(this).load(thingUrl).into(eventImageView);

            EditText eventTitle = (EditText) findViewById(R.id.editEventTitle);
            eventTitle.setHint(thingTitle + " event");
        }

        // If the user doesn't select a pic, just use the url of the already exsisting
        needToStoreImage = false;
        // Get the logged in Status
        loggedIn = AccessToken.getCurrentAccessToken() == null;
        if(!loggedIn){ // For facebook, logged in = false
            loggedIn = true;
            currentUser = new User(AccessToken.getCurrentAccessToken().getUserId());
        } else {
            loggedIn = false;
            currentUser = new User("123");
        }

        setupActionBar();
    }

    public void displayEventData(Event event) {
        this.event = event;
        ((TextView) this.findViewById(R.id.tv_time)).setText(event.getTime());
        ((TextView) this.findViewById(R.id.tv_date)).setText(event.getDate());
        ((EditText) this.findViewById(R.id.editEventTitle)).setText(event.getTitle());
        ((EditText) this.findViewById(R.id.editEventDescription)).setText(event.getDescription());
        ImageView eventImageView = (ImageView) findViewById(R.id.eventImageView);
        Picasso.with(this).load(event.getUrl()).into(eventImageView);
        ((Button) this.findViewById(R.id.delete_event)).setVisibility(View.VISIBLE);
    }

    public void deleteEvent(View view) {
        ea.deleteEvent(event.getRefrence());
        Intent intent = new Intent(EventForm.this, HomePage.class);
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
        needToStoreImage = true;
    }


    public void submit (View view) {

        // NEED TO MAKE SURE THERE ALREADY ISN'T ONE
        if(thingUrl == "update") {
            ea.deleteEvent(event.getRefrence());
        }
        EditText editText = findViewById(R.id.editEventTitle);
        String title = editText.getText().toString();
        editText = findViewById(R.id.editEventDescription);
        String description= editText.getText().toString();
        TextView textView = (TextView) findViewById(R.id.tv_time);
        String time = textView.getText().toString();
        textView = (TextView) findViewById(R.id.tv_date);
        String date = textView.getText().toString();
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

        if(!needToStoreImage) {
            addToDB(url);
        }
        // Returns back to the previous page
        finish();

    }
    //This will get run when the past process is completed
    public void addToDB(String url) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        event.setCreator(currentUser.getUid());
        event.addAttendee(currentUser.getUid());
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


    // For getting the time
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(newFragment)
                .commit();
        newFragment.show(fm, "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = getInstance();
            int hour = c.get(HOUR_OF_DAY);
            int minute = c.get(MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            ((TextView) getActivity().findViewById(R.id.tv_time)).setVisibility(View.VISIBLE);
            ((TextView) getActivity().findViewById(R.id.tv_time)).setText(hourOfDay + ":" + minute);

        }
    }

    // For getting the date
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(newFragment)
                .commit();
        newFragment.show(fm, "datePicker");

    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = getInstance();
            int year = c.get(YEAR);
            int month = c.get(MONTH);
            int day = c.get(DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            ((TextView) getActivity().findViewById(R.id.tv_date)).setVisibility(View.VISIBLE);
            ((TextView) getActivity().findViewById(R.id.tv_date)).setText(month + "/" + day + "/" + year);
        }
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
        actionTitle.setText("Create an Event");

        final ImageButton popupButton = (ImageButton) findViewById(R.id.btn_menu);
        Button loginButton = (Button) findViewById(R.id.login_btn);
        if(loggedIn) {
            popupButton.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
            popupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Creating the instance of PopupMenu
                    final PopupMenu popup = new PopupMenu(EventForm.this, popupButton);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.popup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("My Profile")) {

                                Intent intent = new Intent(EventForm.this, Profile.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to Profile");
                                startActivity(intent);
                            }
                            else if(item.getTitle().equals("View Groups")) {
                                Intent intent = new Intent(EventForm.this, GroupsView.class);
                                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                                Log.i("Intent", "Send User to View Groups");
                                startActivity(intent);

                            } else {
                                Intent intent = new Intent(EventForm.this, LoginPage.class);
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
                    Intent intent = new Intent(EventForm.this, LoginPage.class);
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
                Intent intent = new Intent(EventForm.this, HomePage.class);
                // No real reason for sending UID with it, just because
                intent.putExtra(ThingToDoForm.EXTRA_MESSAGE, currentUser.getUid());
                Log.i("Intent", "Send User to Home page");
                startActivity(intent);
            }
        });
    }
}
