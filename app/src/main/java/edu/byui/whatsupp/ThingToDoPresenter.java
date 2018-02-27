package edu.byui.whatsupp;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.net.wifi.WifiConfiguration.Status.strings;

/**
 * Created by Dallin's PC on 2/26/2018.
 */

public class ThingToDoPresenter {
    ThingToDoActivity thingToDoActivity;

    public ThingToDoPresenter() {

    }

    private static class LoadThings extends AsyncTask<String, Integer, String> {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        private WeakReference<Activity> activityRef;


        LoadThings(Activity activity) {
            activityRef = new WeakReference<Activity>(activity);
            activity.findViewById(R.id.progressBar).setVisibility(TextView.VISIBLE);
            activity.findViewById(R.id.gridview).setVisibility(TextView.INVISIBLE);

        }
        @Override
        protected void onPostExecute(String result) {
            Activity activity = activityRef.get();
            if (activity != null) {

            }
            activity.findViewById(R.id.progressBar).setVisibility(TextView.INVISIBLE);
            activity.findViewById(R.id.gridview).setVisibility(TextView.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Activity activity = activityRef.get();
            if (activity != null) {
                ProgressBar pb = (ProgressBar) activity.findViewById(R.id.progressBar);
                pb.setProgress(values[0]);
            }
        }


        @Override
        protected String doInBackground(String... strings) {
            List<ThingToDo> things = new ArrayList<ThingToDo>();

            String result = "";
            int progress = 0;
            for (String string : strings) {


                db.collection("thingsToDo")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        things.add(document.toObject(ThingToDo.class));
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });
                for (int i = 0; i < 
                result += string + "\n";
                progress++;
                publishProgress(progress);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }
}


