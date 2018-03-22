package edu.byui.whatsupp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * <h1>User Presenter</h1>
 * Where the database magic happens to
 * retrieve user information.
 * Uses the firebase API
 * <p>
 *
 *
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */
public class UserPresenter {
    User userData;
    UserActivity userActivity;
    AccessToken userToken;
    edu.byui.whatsupp.Profile profileActivity;
    boolean newUser;



    public void UserPresenter() {

    }

    /**
     * When a user logs on, this finds out if this is the user's first time
     * ever logging on. If so, call the SetFacebookData method. Otherwise
     * do nothing.
     * @param token an AccessToken that carries the user's UID
     * @param loginResult the LoginResult object created from facebook API
     */
    public void isNewUser(AccessToken token, final LoginResult loginResult) {
        final String userID= token.getUserId();
        userToken = token;
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
                                if(document.get("uid").toString().equals(userID)) {
                                    // IF it got here that means it isn't a new user.
                                    notThere = false;

                                    }
                                }
                            if(notThere) {
                                setFacebookData(loginResult);
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });

    }

    /**
     * This method extracts the user information from Facebook.
     * This is only called if a new user that has never logged on before
     * logs on.
     * @param loginResult the LoginResult object created from facebook API
     */
    private void setFacebookData(final LoginResult loginResult)
    {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        // Application code
                        try {

                            Log.i("Response",response.toString());

                            String email = response.getJSONObject().getString("email");
                            String firstName = response.getJSONObject().getString("first_name");
                            String lastName = response.getJSONObject().getString("last_name");
                            String gender = response.getJSONObject().getString("gender");
                            String uid = response.getJSONObject().getString("id");

                            Log.i("Login" + "Email", email);
                            Log.i("Login"+ "FirstName", firstName);
                            Log.i("Login" + "LastName", lastName);
                            Log.i("Login" + "Gender", gender);
                            User tempUser = new User(firstName, lastName, email, gender, uid);
                            addNewUser(tempUser);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,email,first_name,last_name,gender");
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * This method writes the user information to the database.
     * This is only called if a new user that has never logged on before
     * logs on, and is called from the SetFaceBookData method.
     * @param user a User Object that contains all the new user information.
     */
    public void addNewUser(User user) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("User Writing","User added");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("User Writing","Failed to add user");

                    }
                });

    }



    /**
     * Retrieves the user data from the database so that it can send
     * it to the profile activity. This can be the current user, or
     * another user.
     * @param activity Activty Object so we can call the display
     *                 method in the activity class.
     * @param uid A string that is the UID of the user we want to
     *            retrieve
     */
    public void requestUserData(final Activity activity, final String uid) {

        profileActivity = (edu.byui.whatsupp.Profile) activity;

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
                                if(document.get("uid").toString().equals(uid)) {
                                    User user = new User(uid);
                                    user.setLastName(document.get("lastName").toString());
                                    user.setFirstName(document.get("firstName").toString());
                                    user.setEmail(document.get("email").toString());
                                    user.setGender(document.get("gender").toString());
                                    profileActivity.displayUserInfo(user);

                                }
                            }


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });

    }


}
