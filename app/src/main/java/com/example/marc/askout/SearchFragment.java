package com.example.marc.askout;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marc on 5/4/15.
 */
public class SearchFragment extends Fragment {
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView mListView;
    List<ListViewItem> mItemsFound;
    String nom;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);

        mListView =  (ListView) view.findViewById(R.id.activity_main_listview);
        nom = getArguments().getString("nom");
        mItemsFound = new ArrayList<ListViewItem>();
        for (int i= 0; i < Global.getInstance().mItems.size(); ++i) {
            if (Global.getInstance().mItems.get(i).nom.toLowerCase().contains(nom.toLowerCase())) {
                ListViewItem item = Global.getInstance().mItems.get(i);
                item.position = i;
                mItemsFound.add(item);
            }
        }
        setUpList();

        return view;
    }

    public void setUpList() {
        mListView.setAdapter(new ListViewDemoAdapter(getActivity(), mItemsFound));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailsEventFragment detailsEventFragment = new DetailsEventFragment();
                //JSONObject obj = jArray.getJSONObject(position);
                Bundle args = new Bundle();
                args.putInt("position", mItemsFound.get(position).position);
                args.putString("from", "eventsList");

                detailsEventFragment.setArguments(args);
                FragmentManager fm = getFragmentManager();

                //fm.beginTransaction().hide(getCurrentFragment()).commit();
                fm.beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.container, detailsEventFragment).commit();
            }
        });

        ColorDrawable myColor = new ColorDrawable(0xFFCFBEBE);
        mListView.setDivider(myColor);
        mListView.setDividerHeight(2);
    }

}