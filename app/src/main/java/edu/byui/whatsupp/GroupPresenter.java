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



public class GroupPresenter {
    GroupActivity groupActivity;
    Group group;
    edu.byui.whatsupp.GroupsView groupsView;
    edu.byui.whatsupp.GroupView groupView;

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
                                    groupView.setCurrentGroup(group);
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
}
