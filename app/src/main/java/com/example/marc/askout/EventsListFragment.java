package com.example.marc.askout;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by marc on 5/4/15.
 */
public class EventsListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events_list_layout, container, false);
        //Toast.makeText(this, "BLAH", Toast.LENGTH_LONG).show();
        Log.d("FRAGMENT", "onCreateView - EventsListFragment");
        return view;
    }
}