package edu.byui.whatsupp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import static edu.byui.whatsupp.ThingToDoForm.EXTRA_MESSAGE;

/**
 * Created by Dallin's PC on 2/15/2018.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<ThingToDo> things;
    edu.byui.whatsupp.HomePage activity;

    public ImageAdapter(Context c, List<ThingToDo> t, Activity a) {
        mContext = c;
        things = t;
        activity = (edu.byui.whatsupp.HomePage) a;
    }

    public int getCount() {
        return things.size();
    }

    public void setList(List<ThingToDo> t) {
        things = t;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageButton imageButton;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageButton = new ImageButton(mContext);
            imageButton.setLayoutParams(new GridView.LayoutParams(230, 230));
            imageButton.setScaleType(ImageButton.ScaleType.CENTER_CROP);
            imageButton.setPadding(8, 8, 8, 0);
        } else {
            imageButton = (ImageButton) convertView;
        }
        ThingToDo tempThing = things.get(position);


        imageButton.setOnClickListener(new View.OnClickListener()   {
            public void onClick(View v)  {
                activity.thingClick();
            }

        });
        String imageUrl = tempThing.getUrl();
        Picasso.with(mContext).load(imageUrl).into(imageButton);

        return imageButton;
    }

    public void displayThingToDo() {

    }


}