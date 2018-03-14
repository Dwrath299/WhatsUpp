package edu.byui.whatsupp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Dallin's PC on 2/26/2018.
 */

public class EventPresenter {
    Event eventData;
    EventActivity eventActivity;
    edu.byui.whatsupp.ViewThingToDo viewThingToDoActivity;
    edu.byui.whatsupp.ViewEvent viewEventActivity;

    public EventPresenter() {

    }

    public void getEventsForThing(Activity a, String string){
        viewThingToDoActivity = (edu.byui.whatsupp.ViewThingToDo) a;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String thingToDo = string;
        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Event> events = new ArrayList<Event>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.get("thingToDo").toString().equals(thingToDo)) { // To get the events for the Thing
                                    Event tempEvent = new Event((String)document.get("title"), (String) document.get("url"));
                                    tempEvent.setDate((String) document.get("date"));
                                    tempEvent.setTime((String) document.get("time"));
                                    tempEvent.setDescription((String) document.get("description"));
                                    tempEvent.setPublic((boolean) document.get("isPublic"));
                                    tempEvent.setThingToDo((String) document.get("thingToDo"));
                                    if(document.get("creator") != null) {
                                        tempEvent.setCreator((String) document.get("creator"));
                                    }
                                    events.add(tempEvent);
                                }
                            }
                            viewThingToDoActivity.displayEventsForThing(events);


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });





    }

    public void getEvent(Activity a, String string){
        viewEventActivity = (edu.byui.whatsupp.ViewEvent) a;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String event = string;
        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            //List<Event> events = new ArrayList<Event>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.get("title").toString().equals(event)) { // To get the events for the Thing
                                    ArrayList <String> attendees;
                                    Event tempEvent = new Event((String)document.get("title"), (String) document.get("url"));
                                    tempEvent.setDate((String) document.get("date"));
                                    tempEvent.setTime((String) document.get("time"));
                                    tempEvent.setDescription((String) document.get("description"));
                                    tempEvent.setPublic((boolean) document.get("isPublic"));
                                    tempEvent.setThingToDo((String) document.get("thingToDo"));
                                    attendees = (ArrayList<String>) document.get("attendees");
                                    tempEvent.setRefrence(document.getReference().toString());
                                    tempEvent.setAttendees(attendees);
                                    getEventAttendees(attendees);
                                    if( document.get("creator") != null) {
                                        tempEvent.setCreator((String) document.get("creator"));
                                    }
                                    viewEventActivity.displayEvent(tempEvent);
                                }
                            }



                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });

    }

    public void getEventAttendees (final ArrayList<String> attendees) {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<User> users = new ArrayList<User>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                for(int i = 0; i < attendees.size(); i ++) {
                                    if (document.get("uid").toString().equals(attendees.get(i))) {
                                        User tempUser = new User(attendees.get(i));

                                        tempUser.setLastName(document.get("lastName").toString());
                                        tempUser.setFirstName(document.get("firstName").toString());
                                        tempUser.setEmail(document.get("email").toString());
                                        tempUser.setGender(document.get("gender").toString());
                                        users.add(tempUser);



                                    }
                                }
                            }
                            viewEventActivity.displayAttendees(users);


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });

    }

    public void addAttendee(String docRef, List<String> attendees) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("events").document(docRef);

        // Set the "isCapital" field of the city 'DC'
        eventRef
                .update("attendees", attendees)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }
}
