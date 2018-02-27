package edu.byui.whatsupp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dallin's PC on 2/15/2018.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<ThingToDo> things;

    public ImageAdapter() {

        things = new ArrayList<ThingToDo>();
    }

    public int getCount() {
        return things.size();
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
            imageButton.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageButton.setScaleType(ImageButton.ScaleType.CENTER_CROP);
            imageButton.setPadding(8, 8, 8, 8);
        } else {
            imageButton = (ImageButton) convertView;
        }


        //imageButton.setImageResource(mThumbIds[position]);
        return imageButton;
    }

    public void addThing(ThingToDo thing) {
        things.add(thing);
    }
}