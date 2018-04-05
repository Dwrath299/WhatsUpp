package edu.byui.whatsupp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.net.wifi.WifiConfiguration.Status.strings;
import static java.util.function.Predicate.isEqual;

/**
 * <h1>ThingToDo Presenter</h1>
 * The magic of database retrieval for ThingsToDo
 * Uses the firebase database
 *
 * @author  Dallin Wrathall
 * @version 1.0
 * @since   2018-03-21
 */
public class ThingToDoPresenter {
    ThingToDoActivity thingToDoActivity;


    static List<ThingToDo> things = new ArrayList<ThingToDo>();

    edu.byui.whatsupp.HomePage homePageActivity;
    edu.byui.whatsupp.ViewThingToDo viewThingToDoActivity;
    edu.byui.whatsupp.ThingToDoForm thingToDoForm;
    edu.byui.whatsupp.ThingToDoSelect thingsActivity;

    /**
     * Does nothing
     */
    public ThingToDoPresenter() {

    }

    //TODO Add category filtering.
    //TODO Add location filtering.
    //TODO Add sorting (Alphabetically, Recent, Closest, Popular, Cheapest).
    /**
     * Retrieves the public Things To Do from the database. Will call
     *  the set gridview in the HomePage activity.
     * @param a Homepage Activity
     */
    public void getListThings(Activity a){
        homePageActivity = (edu.byui.whatsupp.HomePage) a;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("thingsToDo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ThingToDo> things = new ArrayList<ThingToDo>();
                            for (DocumentSnapshot document : task.getResult()) {
                                if (document.get("group") == null) { // Don't get the private group things
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    ThingToDo tempThing = new ThingToDo((String) document.get("url"),
                                            (String) document.get("title"),
                                            (String) document.get("address"),
                                            (String) document.get("city"),
                                            (long) document.get("zipCode"),
                                            (String) document.get("description"));
                                    if (document.get("creator") != null) {
                                        tempThing.setCreator((String) document.get("creator"));
                                    }
                                    things.add(tempThing);
                                }

                            }
                            homePageActivity.setGridView(things);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });


    }

    /**
     * Retrieves the list of public + private Things To Do for a group
     * @param a THingToDo Select activity
     * @param groupTitle
     */
    public void getActivityListThings(Activity a, final String groupTitle){
        thingsActivity = (edu.byui.whatsupp.ThingToDoSelect) a;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("thingsToDo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ThingToDo> things = new ArrayList<ThingToDo>();
                            for (DocumentSnapshot document : task.getResult()) {
                                if ((document.get("group") == null) || document.get("group").toString().equals(groupTitle)) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    ThingToDo tempThing = new ThingToDo((String) document.get("url"),
                                            (String) document.get("title"),
                                            (String) document.get("address"),
                                            (String) document.get("city"),
                                            (long) document.get("zipCode"),
                                            (String) document.get("description"));
                                    if (document.get("creator") != null) {
                                        tempThing.setCreator((String) document.get("creator"));
                                    }
                                    things.add(tempThing);
                                }
                            }
                            thingsActivity.setGridView(things);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });





    }

    //TODO: Make all the database retrievals based on document ID not by title (Risk of duplicate titles).
    /**
     * Get a specific Thing To Do information from the database.
     * Calls the displayThingToDo Method in the View ThingToDo activity.
     * @param a ViewThingToDo Activity
     * @param string The thing to do title
     */
    public void getThing(Activity a, String string){
        viewThingToDoActivity = (edu.byui.whatsupp.ViewThingToDo) a;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String title = string;
        db.collection("thingsToDo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ThingToDo> things = new ArrayList<ThingToDo>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.get("title").toString().equals(title)) {
                                ThingToDo tempThing = new ThingToDo((String) document.get("url"),
                                        (String)document.get("title"),
                                        (String)document.get("address"),
                                        (String)document.get("city"),
                                        (long) document.get("zipCode"),
                                        (String)document.get("description"));
                                if(document.get("creator") != null) {
                                    tempThing.setCreator((String) document.get("creator"));
                                }
                                    viewThingToDoActivity.displayThingToDo(tempThing);
                                }


                            }


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });





    }

    /**
     * Gather the information for the Thing To Do, so it may be edited
     * on the ThingToDoForm page
     * @param a ThingToDoForm page
     * @param string ThingToDo title
     */
    public void getThingToEdit(Activity a, String string){
        thingToDoForm = (edu.byui.whatsupp.ThingToDoForm) a;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String title = string;
        db.collection("thingsToDo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ThingToDo> things = new ArrayList<ThingToDo>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.get("title").toString().equals(title)) {
                                    ThingToDo tempThing = new ThingToDo((String) document.get("url"),
                                            (String)document.get("title"),
                                            (String)document.get("address"),
                                            (String)document.get("city"),
                                            (long) document.get("zipCode"),
                                            (String)document.get("description"));
                                    tempThing.setReference(document.getReference().getId());
                                    if(document.get("creator") != null) {
                                        tempThing.setCreator((String) document.get("creator"));
                                    }
                                    if(document.get("group") != null) {
                                        tempThing.setGroup((String) document.get("group"));
                                    }
                                    thingToDoForm.displayThingData(tempThing);
                                }


                            }


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });





    }

    /**
     * Remove the thing to do from the database
     * @param ref Reference of the THing To Do
     */
    public void deleteThingDocument(String ref) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("thingsToDo").document(ref)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }







}


