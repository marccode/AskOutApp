package com.example.marc.askout.dummy;

/**
 * Created by albertvinespujadas on 5/21/15.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marc.askout.DetailsEventFragment;
import com.example.marc.askout.EventsListFragment;
import com.example.marc.askout.Global;
import com.example.marc.askout.Group;
import com.example.marc.askout.InfoSingleEvent;
import com.example.marc.askout.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private final SparseArray<Group> groups;
    public LayoutInflater inflater;
    public Activity activity;

    public MyExpandableListAdapter(Activity act, SparseArray<Group> groups) {
        Log.d("PFF", "BUILDER");
        activity = act;
        this.groups = groups;
        inflater = act.getLayoutInflater();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Log.d("PFF", "getChild");
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

        TextView nom = (TextView) convertView.findViewById(R.id.tvTitle);
        nom.setText(children.nom);
        TextView nomLloc = (TextView) convertView.findViewById(R.id.tvDescription);
        nomLloc.setText(children.nomLloc);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsEventFragment detailsEventFragment = new DetailsEventFragment();
                int position = EventsListFragment.getEventPosition(groupPosition) + childPosition;

                Bundle args = new Bundle();
                /*
                args.putString("id", Global.getInstance().mItems.get(position).id);
                args.putString("data_inici", Global.getInstance().mItems.get(position).data_inici);
                args.putString("data_final", Global.getInstance().mItems.get(position).data_final);
                args.putString("nom", Global.getInstance().mItems.get(position).nom);
                args.putString("nomLloc", Global.getInstance().mItems.get(position).nomLloc);
                args.putString("carrer", Global.getInstance().mItems.get(position).carrer);
                args.putString("numero", Global.getInstance().mItems.get(position).numero);
                args.putString("districte",Global.getInstance().mItems.get(position).districte);
                args.putString("municipi", Global.getInstance().mItems.get(position).municipi);
                args.putString("categories", Global.getInstance().mItems.get(position).categories);
                */
                args.putInt("position", position);
                args.putString("from", "eventsList");

                Toast.makeText(activity, "Has fet clic a l'element " + position, Toast.LENGTH_SHORT).show();
                Context context = parent.getContext();
                ((Activity) context).getFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                        .replace(R.id.container, detailsEventFragment).commit();
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Log.d("PFF", "getGroupView - " + Integer.toString(groupPosition));
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
