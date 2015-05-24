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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marc.askout.InfoSingleEvent;
import com.example.marc.askout.dummy.MyExpandableListAdapter;

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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class EventsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout mSwipeRefreshLayout;
    public static JSONArray jArray;
    ExpandableListView mExpendableListView;
    ListView mListView;
    int year;
    int month;
    int day;
    private static int numEsdCat[];
    ArrayList<Group> groups = new ArrayList<Group>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.fragment_events_list, container, false);
        View view = inflater.inflate(R.layout.fragment_events_expandablelist, container, false);

        if (savedInstanceState != null) {
            super.onCreate(savedInstanceState);
        }
        else {
            if (getArguments() != null) {
                year = getArguments().getInt("year");
                month = getArguments().getInt("month");
                day = getArguments().getInt("day");
            }
            else {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
            }

            // Inflate the layout for this fragment
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            mExpendableListView =  (ExpandableListView) view.findViewById(R.id.activity_main_expendablelistview);
            mListView =  (ListView) view.findViewById(R.id.activity_main_listview);

            groups = new ArrayList<Group>();

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
            //setUpList();
            setUpExpandableList();
        }
        return view;
    }

    @Override
    public void onRefresh() {
        Global.getInstance().mItems = new ArrayList<ListViewItem>();
        String url = "";

        if (Global.getInstance().interests == null) {
            Global.getInstance().interests = new boolean[]{false, false, false, false, false, false, false, false, false, false, false};
            new RequestInterests().execute("http://jediantic.upc.es/api/userInterest/" + HomeActivity.myID);
        }


        for (int i = 1; i < Global.getInstance().interests.length; ++i) {
            if (Global.getInstance().interests[i])
                url = url.concat(Global.getInstance().categories[i] + "-");
        }
        if (url.length() > 0) url = url.substring(0, url.length() - 1);
        new RequestTask().execute("http://jediantic.upc.es/api/eventsDia/" + day + "-" + month + "-" + year + "/" + url);// + date.toString());
    }

    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            Log.d("UFF", uri[0]);
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

    class RequestInterests extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
                Log.d("URL", uri[0]);
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
                auxInterests(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String auxInterests(String s) throws JSONException {
        if (s != null) {
            JSONArray jArray = new JSONArray(s);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                boolean interes = obj.getBoolean("interes");
                String nom_interes = obj.getString("titol");
                switch (nom_interes) {
                    case "Espectacles":
                        Global.getInstance().interests[0] = interes;
                        break;

                    case "Música":
                        Global.getInstance().interests[1] = interes;
                        break;

                    case "Cinema":
                        Global.getInstance().interests[2] = interes;
                        break;

                    case "Museu":
                        Global.getInstance().interests[3] = interes;
                        break;

                    case "Infantil":
                        Global.getInstance().interests[4] = interes;
                        break;

                    case "Esport":
                        Global.getInstance().interests[5] = interes;
                        break;

                    case "Exposició":
                        Global.getInstance().interests[6] = interes;
                        break;

                    case "Art":
                        Global.getInstance().interests[7] = interes;
                        break;

                    case "Ciència":
                        Global.getInstance().interests[8] = interes;
                        break;

                    case "Oci":
                        Global.getInstance().interests[9] = interes;
                        break;

                    case "Cultura":
                        Global.getInstance().interests[10] = interes;
                        break;

                    default:
                        break;
                }
            }
        }
        return s;
    }

    private String aux(String s) throws JSONException {
        jArray = new JSONArray(s);

        //for (int i = 0; i < jArray.length(); i++) { // POT DONAR PROBLEMES SI EL jArray es més petit de 15!!!
        for (int i = 0; i < 15; i++) {
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

                case "Cultura":
                    icon = resources.getDrawable(R.drawable.ic_school_black_24dp);
                    break;
                case "Oci":
                    icon = resources.getDrawable(R.drawable.ic_gamepad_variant_black_24dp);
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
            Global.getInstance().mItems.add(new ListViewItem(obj.getString("_id"), obj.getString("data_inici"), obj.getString("data_final"), nom, nomLloc, obj.getString("carrer"), obj.getString("numero"), obj.getString("districte"), obj.getString("municipi"), pony.get(0).toString(), icon ));
        }

        setUpExpandableList();
        //setUpList();
        return s;
    }


    public void setUpList() {
        mListView.setAdapter(new ListViewDemoAdapter(getActivity(), Global.getInstance().mItems));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, show a toast with the TextView text
                Toast.makeText(getActivity(), "You clicked " + Integer.toString(position), Toast.LENGTH_SHORT).show();
                DetailsEventFragment detailsEventFragment = new DetailsEventFragment();

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

    public void setUpExpandableList() {
        Log.d("PFF", "setUpExpandableList()");
        //SparseArray<Group> groups = new SparseArray<Group>();
        numEsdCat = new int[11];

        Group espectacles = new Group("Espectacles");
        Group musica = new Group("Música");
        Group cinema = new Group("Cinema");
        Group museu = new Group("Museu");
        Group infantil = new Group("Infantil");
        Group esport = new Group("Esport");
        Group exposicio = new Group("Exposició");
        Group art = new Group("Art");
        Group ciencia = new Group("Ciència");
        Group oci = new Group("Oci");
        Group cultura = new Group("Cultura");
        
        for (int i = 0; i < Global.getInstance().mItems.size(); ++i) {
            ListViewItem item = Global.getInstance().mItems.get(i);
            InfoSingleEvent info = new InfoSingleEvent(item.nom, item.nomLloc);
            //Log.d("PFF", "nom: " + item.nom + " - nomLloc: " + item.nomLloc + " - categories: " + item.categories);
            switch (item.categories) {
                case "Espectacles":
                    Log.d("PFF", "espectacles");
                    espectacles.children.add(info);
                    numEsdCat[0]++;
                    break;

                case "Música":
                    Log.d("PFF", "musica");
                    musica.children.add(info);
                    numEsdCat[1]++;
                    break;

                case "Cinema":
                    Log.d("PFF", "cinema");
                    cinema.children.add(info);
                    numEsdCat[2]++;
                    break;

                case "Museu":
                    Log.d("PFF", "musica");
                    museu.children.add(info);
                    numEsdCat[3]++;
                    break;

                case "Infantil":
                    Log.d("PFF", "infantil");
                    infantil.children.add(info);
                    numEsdCat[4]++;
                    break;

                case "Esport":
                    Log.d("PFF", "esport");
                    esport.children.add(info);
                    numEsdCat[5]++;
                    break;

                case "Exposició":
                    Log.d("PFF", "exposicio");
                    exposicio.children.add(info);
                    numEsdCat[6]++;
                    break;

                case "Art":
                    Log.d("PFF", "art");
                    art.children.add(info);
                    numEsdCat[7]++;
                    break;

                case "Ciència":
                    Log.d("PFF", "ciencia");
                    ciencia.children.add(info);
                    numEsdCat[8]++;
                    break;

                case "Cultura":
                    Log.d("PFF", "cultura");
                    cultura.children.add(info);
                    numEsdCat[9]++;
                    break;

                case "Oci":
                    Log.d("PFF", "oci");
                    oci.children.add(info);
                    numEsdCat[10]++;
                    break;

                default:
                    break;
            }
        }

        int i = 0;
        if(! espectacles.isEmpty()) groups.add(i++, espectacles);
        if(! musica.isEmpty()) groups.add(i++, musica);
        if(! cinema.isEmpty()) groups.add(i++, cinema);
        if(! museu.isEmpty()) groups.add(i++, museu);
        if(! infantil.isEmpty()) groups.add(i++, infantil);
        if(! esport.isEmpty()) groups.add(i++, esport);
        if(! exposicio.isEmpty()) groups.add(i++, exposicio);
        if(! art.isEmpty()) groups.add(i++, art);
        if(! ciencia.isEmpty()) groups.add(i++, ciencia);
        if(! oci.isEmpty()) groups.add(i++, oci);
        if(! cultura.isEmpty()) groups.add(i++, cultura);

        //mListView.setAdapter(new ListViewDemoAdapter(getActivity(), Global.getInstance().mItems));

        /*
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
        */
        MyExpandableListAdapter adapter = new MyExpandableListAdapter(getActivity(), groups);
        mExpendableListView.setAdapter(adapter);

        ColorDrawable myColor = new ColorDrawable(0xFFCFBEBE);
        mExpendableListView.setDivider(myColor);
        mExpendableListView.setDividerHeight(2);
    }

    public static int getEventPosition(int groupPosition) {
        int pos = 0;
        for (int i = 0; i < groupPosition; i++) {
            pos += numEsdCat[i];
            if (numEsdCat[i] == 0 ) i = i - 1;
        }
        return pos;
    }
}