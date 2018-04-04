package edu.byui.whatsupp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
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



public class GroupPresenter {
    GroupActivity groupActivity;
    Group group;
    edu.byui.whatsupp.GroupsView groupsView;
    edu.byui.whatsupp.GroupView groupView;
    edu.byui.whatsupp.ViewVote voteView;

    public GroupPresenter() {

    }

    public void getGroup(Activity a, final String groupTitle) {
        groupView = (edu.byui.whatsupp.GroupView) a;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.get("title").equals(groupTitle)) {
                                    Group group = new Group(groupTitle, (ArrayList<String>) document.get("members"), (String) document.get("url"));
                                    group.setReference(document.getReference().toString());
                                    groupView.setCurrentGroup(group);
                                    if (document.get("creator") != null) {
                                        group.setCreator((String) document.get("creator"));
                                    }
                                }


                            }


                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });
    }

    public void getListThings(Activity a, final String uid){
        groupsView = (edu.byui.whatsupp.GroupsView) a;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("groups")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Group> groups = new ArrayList<Group>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                ArrayList<String> members = (ArrayList<String>) document.get("members");
                                for(int i = 0; i < members.size(); i ++) {
                                    String tempUser = members.get(i);
                                    if(uid.equals(tempUser)) {
                                        Group tempGroup = new Group((String) document.get("title"),
                                                                    (ArrayList<String>) document.get("members"),
                                                                    (String) document.get("url"));
                                        tempGroup.setMembers(members);
                                        tempGroup.setReference(document.getReference().toString());
                                        if (document.get("creator") != null) {
                                            tempGroup.setCreator((String) document.get("creator"));
                                        }
                                        groups.add(tempGroup);
                                    }
                                }

                            }
                           groupsView.displayGroups(groups);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });





    }

    public void getVote(Activity a, String voteRef) {
        voteView = (edu.byui.whatsupp.ViewVote) a;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("votes").document(voteRef);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}
