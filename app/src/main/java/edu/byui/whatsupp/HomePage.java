package edu.byui.whatsupp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.lang.ref.WeakReference;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class HomePage extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "edu.byui.whatsapp.Message";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);



        //new Load(this).execute();
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(HomePage.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static class Load extends AsyncTask<String, Integer, String> {

        private WeakReference<Activity> activityRef;


        Load(Activity activity) {
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
            String result = "";
            int progress = 0;
            for (String string : strings) {

                /** Download
                File localFile = File.createTempFile("images", "jpg");
                riversRef.getFile(localFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                // Successfully downloaded data to local file
                                // ...
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle failed download
                        // ...
                    }
                }); */
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

    public void addThingToDo (View view) {
        Intent intent = new Intent(this, ThingToDoForm.class);
        intent.putExtra(EXTRA_MESSAGE, "work");
        Log.i("Intent", "Send User to Form");
        startActivity(intent);
    }
}
