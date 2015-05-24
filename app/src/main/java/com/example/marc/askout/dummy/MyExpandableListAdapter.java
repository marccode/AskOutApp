package com.example.marc.askout.dummy;

/**
 * Created by albertvinespujadas on 5/21/15.
 */
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marc.askout.DetailsEventFragment;
import com.example.marc.askout.EventsListFragment;
import com.example.marc.askout.Group;
import com.example.marc.askout.InfoSingleEvent;
import com.example.marc.askout.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private final ArrayList<Group> groups;
    public LayoutInflater inflater;
    public Activity activity;

    public MyExpandableListAdapter(Activity act, ArrayList<Group> groups) {
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {
        final InfoSingleEvent children = (InfoSingleEvent) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_details, null);
        }
        TextView nom = null;
        TextView nomLloc = null;
        nom = (TextView) convertView.findViewById(R.id.tvTitle);
        nom.setText(children.nom);
        nomLloc = (TextView) convertView.findViewById(R.id.tvDescription);
        nomLloc.setText(children.nomLloc);

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DetailsEventFragment detailsEventFragment = new DetailsEventFragment();
                    int position = EventsListFragment.getEventPosition(groupPosition) + childPosition;
                    JSONObject obj = EventsListFragment.jArray.getJSONObject(position);
                    ArrayList<String> categories = new ArrayList<String>();
                    ArrayList<String> categories_generals = new ArrayList<String>();
                    Bundle args = new Bundle();
                    args.putString("id", obj.getString("_id"));
                    args.putString("data_inici", obj.getString("data_inici"));
                    args.putString("data_final", obj.getString("data_final"));
                    args.putString("nom", obj.getString("nom"));
                    args.putString("nomLloc", obj.getString("nomLloc"));
                    args.putString("carrer", obj.getString("carrer"));
                    args.putString("numero", obj.getString("numero"));
                    args.putString("districte", obj.getString("districte"));
                    args.putString("municipi", obj.getString("municipi"));
                    args.putString("categories", obj.getString("categories_generals"));
                    //args.putStringArray("categories", categories);
                    //args.putStringArray("categories_generals", categories_generals);
                    detailsEventFragment.setArguments(args);

                    Toast.makeText(activity, "Has fet clic a l'element " + position, Toast.LENGTH_SHORT).show();
                    Context context = parent.getContext();
                    ((Activity) context).getFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.container, detailsEventFragment).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }
        Group group = (Group) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(group.string);
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
