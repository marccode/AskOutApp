package com.example.marc.askout;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView mListView;
    private List<ListViewItem> mItems;
    JSONArray jArray;
    PENENENENNE

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);

        if (savedInstanceState != null) {
            super.onCreate(savedInstanceState);
        }

        else {
            // Inflate the layout for this fragment
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            mListView =  (ListView) view.findViewById(R.id.activity_main_listview);

            mItems = new ArrayList<ListViewItem>();

            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorScheme(android.R.color.holo_green_dark,
                    android.R.color.holo_red_dark,
                    android.R.color.holo_blue_dark,
                    android.R.color.holo_orange_dark);
        }

        return view;
    }

    @Override
    public void onRefresh() {
        //new myTask().execute();
        Toast.makeText(getActivity(), "refresh", Toast.LENGTH_LONG).show();
        new RequestTask().execute("http://jediantic.upc.es/api/events");
    }

    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            String responseString = null;
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response;
                response = httpclient.execute(new HttpGet(uri[0]));
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    responseString = out.toString();
                    out.close();
                } else{
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    throw new IOException(statusLine.getReasonPhrase());
                }
            } catch (ClientProtocolException e) {
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                aux(result);
                mSwipeRefreshLayout.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Do anything with response...
        }
    }

    private String aux(String s) throws JSONException {

        if (s != null) {
            jArray = new JSONArray(s);
            for (int i = 0; i < 15; i++) {
                JSONObject obj = jArray.getJSONObject(i);
                JSONArray pony = obj.getJSONArray("categories_generals");
                Toast.makeText(getActivity(), pony.get(0).toString(), Toast.LENGTH_SHORT).show();

                Resources resources = getResources();
                Drawable icon = resources.getDrawable(R.drawable.icon);


                switch (pony.get(0).toString()) {
                    case "Espectacles":
                        icon = resources.getDrawable(R.drawable.icon);
                        break;

                    case "Música":
                        icon = resources.getDrawable(R.drawable.ic_headphones_black_24dp);
                        break;

                    case "Cinema":
                        icon = resources.getDrawable(R.drawable.ic_theaters_black_24dp);
                        break;

                    case "Museu":
                        icon = resources.getDrawable(R.drawable.ic_account_balance_black_24dp);
                        break;

                    case "Infantil":
                        icon = resources.getDrawable(R.drawable.ic_duck_black_24dp);
                        break;

                    case "Esport":
                        icon = resources.getDrawable(R.drawable.ic_dribbble_black_24dp);
                        break;

                    case "Exposició":
                        icon = resources.getDrawable(R.drawable.icon);
                        break;

                    case "Art":
                        icon = resources.getDrawable(R.drawable.ic_palette_black_24dp);
                        break;

                    case "Ciència":
                        icon = resources.getDrawable(R.drawable.ic_beaker_outline_black_24dp);
                        break;

                    case "Oci&Cultura":
                        icon = resources.getDrawable(R.drawable.icon);
                        break;

                    default:
                        break;
                }
                mItems.add(new ListViewItem(icon, obj.getString("nom"), obj.getString("nomLloc")));
            }

            mListView.setAdapter(new ListViewDemoAdapter(getActivity(), mItems));

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // When clicked, show a toast with the TextView text
                    Toast.makeText(getActivity(), "You clicked " + Integer.toString(position), Toast.LENGTH_SHORT).show();
                    try {
                        DetailsEventFragment detailsEventFragment = new DetailsEventFragment();
                        JSONObject obj = jArray.getJSONObject(position);
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
                        FragmentManager fm = getFragmentManager();

                        //fm.beginTransaction().hide(getCurrentFragment()).commit();
                        fm.beginTransaction().replace(R.id.container, detailsEventFragment).commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            ColorDrawable myColor = new ColorDrawable(0xFFCFBEBE);
            mListView.setDivider(myColor);
            mListView.setDividerHeight(2);
        }
        else {
            Toast.makeText(getActivity(), "Connecta't a internet per obtenir els esdeveniments!", Toast.LENGTH_SHORT).show();
        }
        return s;
    }

}