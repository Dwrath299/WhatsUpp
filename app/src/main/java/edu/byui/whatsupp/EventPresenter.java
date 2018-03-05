package edu.byui.whatsupp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

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

public class EventPresenter {
    Event eventData;
    EventActivity eventActivity;
    edu.byui.whatsupp.ViewThingToDo viewThingToDoActivity;

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
}
