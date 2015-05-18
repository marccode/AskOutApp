package com.example.marc.askout;

/**
 * Created by marc on 5/5/15.
 */

// ARA NO ES FA SERVIR

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] titles;

    public MySimpleArrayAdapter(Context context, String[] titles) {
        super(context, R.layout.rowlayout, titles);
        this.context = context;
        this.titles = titles;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.title);
        //ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(titles[position]);
        // change the icon for Windows and iPhone
        return rowView;
    }
}