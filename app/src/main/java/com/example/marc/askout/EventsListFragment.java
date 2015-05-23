package com.example.marc.askout;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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

public class EventsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView mListView;
    JSONArray jArray;
    int year;
    int month;
    int day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);
        if (savedInstanceState != null) {
            super.onCreate(savedInstanceState);
        }
        else {
            if (getArguments() != null) {
                year = getArguments().getInt("year");
                month = getArguments().getInt("month");
                day = getArguments().getInt("day");
            }

            /*
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            calendar.get(Calendar.YEAR)
            */

            // Inflate the layout for this fragment
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            mListView =  (ListView) view.findViewById(R.id.activity_main_listview);

            //Global.getInstance().mItems = new ArrayList<ListViewItem>();

            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorScheme(android.R.color.holo_green_dark,
                    android.R.color.holo_red_dark,
                    android.R.color.holo_blue_dark,
                    android.R.color.holo_orange_dark);
        }

        if (Global.getInstance().mItems == null) {
            onRefresh();
        }
        else {
            setUpList();
        }
        return view;
    }

    @Override
    public void onRefresh() {
        Global.getInstance().mItems = new ArrayList<ListViewItem>();
        new RequestTask().execute("http://jediantic.upc.es/api/events/");// + date.toString());
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
                if (result != null) aux(result);
                else
                    Toast.makeText(getActivity(), "Connecta't a internet per obtenir els esdeveniments!", Toast.LENGTH_SHORT).show();

                mSwipeRefreshLayout.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Do anything with response...
        }
    }

    private String aux(String s) throws JSONException {
        jArray = new JSONArray(s);
        Toast.makeText(getActivity(), Integer.toString(jArray.length()), Toast.LENGTH_LONG).show();

        for (int i = 0; i < 8; i++) { // POT DONAR PROBLEMES SI EL jArray es més petit de 15!!!
            Log.d("FOR", Integer.toString(i));
            Toast.makeText(getActivity(), Integer.toString(i), Toast.LENGTH_LONG).show();
            JSONObject obj = jArray.getJSONObject(i);
            JSONArray pony = obj.getJSONArray("categories_generals");

            Resources resources = getResources();
            Drawable icon = resources.getDrawable(R.drawable.icon);


            switch (pony.get(0).toString()) {
                case "Espectacles":
                    icon = resources.getDrawable(R.drawable.ic_theater_black_24dp);
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
                    icon = resources.getDrawable(R.drawable.ic_crop_original_black_24dp);
                    break;

                case "Art":
                    icon = resources.getDrawable(R.drawable.ic_palette_black_24dp);
                    break;

                case "Ciència":
                    icon = resources.getDrawable(R.drawable.ic_beaker_outline_black_24dp);
                    break;

                case "Oci&Cultura":
                    icon = resources.getDrawable(R.drawable.ic_book_open_black_24dp);
                    break;

                default:
                    break;
            }
            String nom = obj.getString("nom");
            String nomLloc = obj.getString("nomLloc");
            if (nom.length() > 40) {
                nom = nom.substring(0,40);
                nom = nom + "...";
            }
            if (nomLloc.length() > 45) {
                nomLloc = nomLloc.substring(0,45);
                nomLloc = nomLloc + "...";
            }
            Global.getInstance().mItems.add(new ListViewItem(obj.getString("_id"), obj.getString("data_inici"), obj.getString("data_final"), nom, nomLloc, obj.getString("carrer"), obj.getString("numero"), obj.getString("districte"), obj.getString("municipi"), obj.getString("categories_generals"), icon ));
        }
        Toast.makeText(getActivity(), "aBOUT TOsetUpList", Toast.LENGTH_LONG).show();
        setUpList();
        return s;
    }

    public void setUpList() {
        mListView.setAdapter(new ListViewDemoAdapter(getActivity(), Global.getInstance().mItems));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                Toast.makeText(getActivity(), "You clicked " + Integer.toString(position), Toast.LENGTH_SHORT).show();
                DetailsEventFragment detailsEventFragment = new DetailsEventFragment();
                //JSONObject obj = jArray.getJSONObject(position);

                Bundle args = new Bundle();
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
                args.putInt("position", position);
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