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
    edu.byui.whatsupp.ThingToDoSelectFragment thingsFragment;

    public ThingToDoPresenter() {

    }

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
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                ThingToDo tempThing = new ThingToDo((String) document.get("url"),
                                        (String)document.get("title"),
                                        (String)document.get("address"),
                                        (String)document.get("city"),
                                        (long) document.get("zipCode"),
                                        (String)document.get("description"));
                                if(document.get("creator") != null) {
                                    tempThing.setCreator((String) document.get("creator"));
                                }
                                things.add(tempThing);

                            }
                            homePageActivity.setGridView(things);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });





    }

    public void getFragmentListThings(ThingToDoSelectFragment a){
        thingsFragment =  a;
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
                                ThingToDo tempThing = new ThingToDo((String) document.get("url"),
                                        (String)document.get("title"),
                                        (String)document.get("address"),
                                        (String)document.get("city"),
                                        (long) document.get("zipCode"),
                                        (String)document.get("description"));
                                if(document.get("creator") != null) {
                                    tempThing.setCreator((String) document.get("creator"));
                                }
                                things.add(tempThing);

                            }
                            thingsFragment.setGridView(things);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });





    }

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
                                    thingToDoForm.displayThingData(tempThing);
                                }


                            }


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });





    }

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


