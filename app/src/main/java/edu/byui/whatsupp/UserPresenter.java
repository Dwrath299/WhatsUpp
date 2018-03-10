package edu.byui.whatsupp;

import android.support.annotation.NonNull;
import android.util.Log;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Dallin's PC on 2/26/2018.
 */

public class UserPresenter {
    User userData;
    UserActivity userActivity;
    AccessToken userToken;
    boolean newUser;



    public void UserPresenter() {

    }

    public void isNewUser(AccessToken token) {
        final String userID= token.getUserId();
        userToken = token;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("thingsToDo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ThingToDo> things = new ArrayList<ThingToDo>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.get("uid").toString().equals(userID)) { // IF it got here that means it is a new user.
                                    addNewUser(userToken);

                                    }
                                }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });

    }
    public void addNewUser(AccessToken token) {

    }

    public void requestUserData() {

    }

    public void changeGroupData() {

    }
}
