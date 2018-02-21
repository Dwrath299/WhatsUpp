package edu.byui.whatsupp;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        new Load(this).execute();
    }

    private static class Load extends AsyncTask<String, Integer, String> {

        private WeakReference<Activity> activityRef;

        Load(Activity activity) {
            activityRef = new WeakReference<Activity>(activity);
            activity.findViewById(R.id.progressBar).setVisibility(TextView.VISIBLE);
            activity.findViewById(R.id.gridView).setVisibility(TextView.INVISIBLE);
        }
        @Override
        protected void onPostExecute(String result) {
            Activity activity = activityRef.get();
            if (activity != null) {
                TextView tv = (TextView) activity.findViewById(R.id.textView);
                tv.setText(result);
            }
            //activity.findViewById(R.id.progressBar).setVisibility(TextView.INVISIBLE);
            activity.findViewById(R.id.gridView).setVisibility(TextView.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Activity activity = activityRef.get();
            if (activity != null) {
                ProgressBar pb = (ProgressBar) activity.findViewById(R.id.progressBar);
                pb.setProgress(values[0]);
            }
        }
        }

        @Override
        protected String doInBackground(String... strings) {
            //String result = "";
            int progress = 0;
            for (String string : strings) {
               //result += string + "\n";
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
