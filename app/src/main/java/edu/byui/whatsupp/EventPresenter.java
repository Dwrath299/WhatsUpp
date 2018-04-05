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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static java.lang.StrictMath.toIntExact;

/**
 * EVENT PRESENTER
 * This is the database queries that gather the information for
 * events and votes.
 * @author Dallin Wrathall
 * @version 1.0.0
 */

public class EventPresenter {
    Event eventData;
    EventActivity eventActivity;
    edu.byui.whatsupp.ViewThingToDo viewThingToDoActivity;
    edu.byui.whatsupp.ViewEvent viewEventActivity;
    edu.byui.whatsupp.Profile profileActivity;
    edu.byui.whatsupp.EventForm eventForm;
    edu.byui.whatsupp.GroupView viewGroup;
    edu.byui.whatsupp.ViewVote viewVote;

    /**
     * Does nothing
     */
    public EventPresenter() {

    }


    // TODO: Input the user so if they have a group with a event display that.
    /**
     * Displays the public events for the corresponding thingToDo
     * @param a
     * @param string
     */
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
                                if(document.get("thingToDo").toString().equals(thingToDo) && document.get("group") == null) { // To get the events for the Thing
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


    //TODO: Should we display the private events here? Probably not.
    /**
     * Displays the events that a user is going to on their profile page.
     * @param a
     * @param string
     */
    public void getEventsForProfile(Activity a, String string){
        profileActivity = (edu.byui.whatsupp.Profile) a;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String uid = string;
        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> uids = new ArrayList<String>();
                            List<Event> events = new ArrayList<Event>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                uids = (List<String>) document.get("attendees");
                                for(int i = 0; i < uids.size(); i++) {
                                    if (uids.get(i) != null && uids.get(i).equals(uid)) { // To get the events for the Thing
                                        Event tempEvent = new Event((String) document.get("title"), (String) document.get("url"));
                                        tempEvent.setDate((String) document.get("date"));
                                        tempEvent.setTime((String) document.get("time"));
                                        tempEvent.setDescription((String) document.get("description"));
                                        tempEvent.setPublic((boolean) document.get("isPublic"));
                                        tempEvent.setThingToDo((String) document.get("thingToDo"));
                                        if (document.get("creator") != null) {
                                            tempEvent.setCreator((String) document.get("creator"));
                                        }
                                        events.add(tempEvent);
                                    }
                                }
                            }
                            profileActivity.displayEventsForProfile(events);


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });





    }



    /**
     * Gets the events that are happening for the associated group.
     * @param a The groupView Activity
     * @param string The group title
     */
    public void getEventsForGroup(Activity a, final String string){
        viewGroup = (edu.byui.whatsupp.GroupView) a;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String grouptitle = string;
        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Event> events = new ArrayList<Event>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.get("group") != null) {
                                    if (document.get("group").toString().equals(grouptitle)) { // To get the events for the Thing
                                        Event tempEvent = new Event((String) document.get("title"), (String) document.get("url"));
                                        tempEvent.setDate((String) document.get("date"));
                                        tempEvent.setTime((String) document.get("time"));
                                        tempEvent.setDescription((String) document.get("description"));
                                        tempEvent.setPublic((boolean) document.get("isPublic"));
                                        tempEvent.setThingToDo((String) document.get("thingToDo"));
                                        if (document.get("creator") != null) {
                                            tempEvent.setCreator((String) document.get("creator"));
                                        }
                                        events.add(tempEvent);
                                    }
                                }
                            }
                            viewGroup.displayEventsForGroup(events);
                            checkForVotes(string);


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });





    }




    /**
     * Checks to see if a group has any votes currently, if so send to the
     * group view to have button so users can go vote.
      * @param groupTitle
     */
    private void checkForVotes(final String groupTitle) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("votes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String documentRef;
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                if ( document.get("groupTitle").toString().equals(groupTitle)) { // To get the vote for the group

                                    viewGroup.displayVote(document.getId());
                                }

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });
    }

    /**

     * Gets the specific event details for displaying in
     * the View Event page. Including, pic,  creator, title,
     * then calls the get Attendees method.
     * @param a The view Event activity.
     * @param string The event title
     */
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
                                    tempEvent.setRefrence(document.getReference().getId());
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


    /**
     * Gets the user information for each of uids provided in the
     * String list. Called by  the Get Event method.
     * @param attendees list of UIDs
     */

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


    // TODO: Currently not working. Duplicates the members that stay.
    /**
     * Adds or subtracts attendees from the list.
     * @param docRef
     * @param attendees
     */
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


    /**
     *  Gets the information for the event to
     *  allow the user to edit the event they created.
     * @param a Event Form Activity
     * @param string Event Title
     */
    public void getEventToEdit(Activity a, String string){
        eventForm = (edu.byui.whatsupp.EventForm) a;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String title = string;
        db.collection("events")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ThingToDo> things = new ArrayList<ThingToDo>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.get("title").toString().equals(title)) {
                                    Event tempEvent = new Event((String)document.get("title"), (String) document.get("url"));
                                    tempEvent.setDate((String) document.get("date"));
                                    tempEvent.setTime((String) document.get("time"));
                                    tempEvent.setDescription((String) document.get("description"));
                                    tempEvent.setPublic((boolean) document.get("isPublic"));
                                    tempEvent.setThingToDo((String) document.get("thingToDo"));
                                    tempEvent.setRefrence(document.getReference().getId());

                                    if( document.get("creator") != null) {
                                        tempEvent.setCreator((String) document.get("creator"));
                                    }
                                    eventForm.displayEventData(tempEvent);
                                }


                            }


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });





    }

    /**
     * Deletes the event from the database.
     * This could mean the user changed their mind, or
     * the date was surpassed.
     * @param ref
     */
    public void deleteEventDocument(String ref) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("events").document(ref)
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

    // TODO: Create a vote activity and Vote Presenter.
    /**
     * Get the vote information from FireStore to pass to the Vote View
     * @param a View Vote page
     * @param voteid The id of the document in the database.
     */
    public void getVoteInfo(Activity a, String voteid) {
        viewVote = (edu.byui.whatsupp.ViewVote) a;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("votes").document(voteid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        Vote vote = new Vote((String) document.get("groupTitle"),new BigDecimal((long) document.get("numOfGroupMembers")).intValueExact(),
                                (String) document.get("option1"), (String) document.get("option1Desc"), (String) document.get("option2")
                                ,(String) document.get("option2Desc"),  (String) document.get("creator"),
                                (String) document.get("closeDate"), (String) document.get("closeTime"));
                        if(document.get("option3") != null) {
                            vote.setOption3(document.get("option3").toString());
                            vote.setOption3Desc(document.get("option3Desc").toString());
                        }
                        getVotePics(vote);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    /**
     * Get the URL of the pictures used for the things to do,
     * called by the getVoteInfo method. This calls back to the
     * display vote method in the View Vote.
     * @param vote Vote object from GetVoteInfo
     */
    public void getVotePics(final Vote vote) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("thingsToDo")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String option1Pic = "";
                            String option2Pic = "";
                            String option3Pic = "";
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.get("title").toString().equals(vote.getOption1())) {
                                    option1Pic = (String) document.get("url");
                                } else if (document.get("title").toString().equals(vote.getOption2())) {
                                    option2Pic = (String) document.get("url");
                                } else if (document.get("title").toString().equals(vote.getOption3())) {
                                    option3Pic = (String) document.get("url");
                                }


                            }

                        viewVote.displayVote(vote, option1Pic, option2Pic, option3Pic);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });
    }
}
