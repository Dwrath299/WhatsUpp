package edu.byui.whatsupp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class GroupView extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "edu.byui.whatsapp.Message";

    private EditText yourEditText;

    Spinner spinner = (Spinner)findViewById(R.id.userSearchSpinner);
    edu.byui.whatsupp.Profile profileActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_view);

        yourEditText = (EditText) findViewById(R.id.yourEditTextId);

        yourEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                searchUser(GroupView.this, yourEditText.toString());


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        }


    public void createEvent(View view) {
        Intent intent = new Intent(this, EventForm.class);
        intent.putExtra(EXTRA_MESSAGE, "Create Group");
        Log.i("Intent", "Send User to Group Creation");
        startActivity(intent);

    }

    public void searchUser(final Activity activity, final String search) {

        profileActivity = (edu.byui.whatsupp.Profile) activity;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean notThere = true;

                            ArrayList<User> userList = new ArrayList<>();

                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if ((document.get("firstName").toString() + document.get("lastName")).contains(search)) {
                                    User user = new User(document.get("uid").toString());
                                    user.setLastName(document.get("lastName").toString());
                                    user.setFirstName(document.get("firstName").toString());
                                    user.setEmail(document.get("email").toString());
                                    user.setGender(document.get("gender").toString());
                                    userList.add(user);


                                    //profileActivity.displayUserInfo(user);

                                }
                            }

                            SpinnerAdapter dataAdapter = new SpinnerAdapter(GroupView.this, userList, GroupView.this);
                            //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(dataAdapter);



                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });

    }

}