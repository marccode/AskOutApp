package com.example.marc.askout;

import android.app.Fragment;
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
import android.widget.ExpandableListView;
import android.widget.Toast;

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

public class EventsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout mSwipeRefreshLayout;
    public static JSONArray jArray;
    ExpandableListView mExpendableListView;
    public static int year;
    public static int month;
    public static int day;
    String url;
    private static int numEsdCat[];
    ArrayList<Group> groups = new ArrayList<Group>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_expandablelist, container, false);

        if (savedInstanceState != null) {
            super.onCreate(savedInstanceState);
        }
        else {
            year = Global.getInstance().year;
            month = Global.getInstance().month;
            day = Global.getInstance().day;

            // Inflate the layout for this fragment
            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            mExpendableListView =  (ExpandableListView) view.findViewById(R.id.activity_main_expendablelistview);

            groups = new ArrayList<Group>();

            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorScheme(android.R.color.holo_green_dark,
                    android.R.color.holo_red_dark,
                    android.R.color.holo_blue_dark,
                    android.R.color.holo_orange_dark);
        }
        if (getArguments() != null) {
            if (getArguments().getBoolean("refresh")) {
                onRefresh();
            }
        }
        else if (Global.getInstance().mItems == null) {
            if (Global.getInstance().interests == null) {
                Global.getInstance().interests = new boolean[]{false, false, false, false, false, false, false, false, false, false, false};
                new RequestInterests().execute("http://jediantic.upc.es/api/userInterest/" + HomeActivity.myID);
            }
            else {
                Log.d("DEF", "interests != null");
                onRefresh();
            }
        }
        else {
            Log.d("DEF", "mItems != null");
            setUpExpandableList();
        }
        return view;
    }

    @Override
    public void onRefresh() {
        Log.d("DEF", "onRefresh");
        Global.getInstance().mItems = new ArrayList<ListViewItem>();
        groups = new ArrayList<Group>();
        url = "";

        for (int i = 0; i < Global.getInstance().interests.length; ++i) {
            if (Global.getInstance().interests[i])
                url = url.concat(Global.getInstance().categories[i] + "-");
        }
        if (url.length() > 0) url = url.substring(0, url.length() - 1);
        Log.d("DEF", "http://jediantic.upc.es/api/eventsDia/" + day + "-" + month + "-" + year + "/" + url);
        new RequestTask().execute("http://jediantic.upc.es/api/eventsDia/" + day + "-" + month + "-" + year + "/" + url);
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

    private String aux(String s) throws JSONException {
        if(isAdded()) {
            Resources resources = getResources();
            jArray = new JSONArray(s);

            //for (int i = 0; i < jArray.length(); i++) { // POT DONAR PROBLEMES SI EL jArray es més petit de 15!!!
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                JSONArray pony = obj.getJSONArray("categories_generals");


                Drawable icon = resources.getDrawable(R.drawable.icon);

                for (int j = 0; j < pony.length(); ++j) {
                    switch (pony.get(j).toString()) {
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

                    if (url.contains(pony.get(j).toString())) {
                        String carrer = obj.getString("carrer");
                        if (carrer == null) carrer = "";
                        Global.getInstance().mItems.add(new ListViewItem(obj.getString("_id"), obj.getString("data_inici"), obj.getString("data_final"), nom, nomLloc, carrer, obj.getString("numero"), obj.getString("districte"), obj.getString("municipi"), pony.get(j).toString(), icon));

                    }
                }
            }

            for (int i = 0; i < Global.getInstance().mItems.size(); ++i)
                Log.d("DEF", "ITEM (" + Integer.toString(i) + ") : " + Global.getInstance().mItems.get(i).nom + " - CATEGORIA: " + Global.getInstance().mItems.get(i).categories);

            setUpExpandableList();
        }
        return s;
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
        onRefresh();
        if (s != null) {
            JSONArray jArray = new JSONArray(s);
            boolean[] interests = new boolean[11];
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                boolean interes = obj.getBoolean("interes");
                String nom_interes = obj.getString("titol");
                switch (nom_interes) {
                    case "Espectacles":
                        interests[0] = interes;
                        break;

                    case "Música":
                        interests[1] = interes;
                        break;

                    case "Cinema":
                        interests[2] = interes;
                        break;

                    case "Museu":
                        interests[3] = interes;
                        break;

                    case "Infantil":
                        interests[4] = interes;
                        break;

                    case "Esport":
                        interests[5] = interes;
                        break;

                    case "Exposició":
                        interests[6] = interes;
                        break;

                    case "Art":
                        interests[7] = interes;
                        break;

                    case "Ciència":
                        interests[8] = interes;
                        break;

                    case "Oci":
                        interests[9] = interes;
                        break;

                    case "Cultura":
                        interests[10] = interes;
                        break;

                    default:
                        break;
                }
            }
            Global.getInstance().interests = interests;
        }
        return s;
    }

    public void setUpExpandableList() {

        Log.d("DEF", "setUpExpandableList");

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

            String nom_aux = item.nom, nomLloc_aux = item.nomLloc;

            InfoSingleEvent info = new InfoSingleEvent(nom_aux, nomLloc_aux, i);
            switch (item.categories) {
                case "Espectacles":
                    espectacles.children.add(info);
                    numEsdCat[0]++;
                    break;

                case "Música":
                    musica.children.add(info);
                    numEsdCat[1]++;
                    break;

                case "Cinema":
                    cinema.children.add(info);
                    numEsdCat[2]++;
                    break;

                case "Museu":
                    museu.children.add(info);
                    numEsdCat[3]++;
                    break;

                case "Infantil":
                    infantil.children.add(info);
                    numEsdCat[4]++;
                    break;

                case "Esport":
                    esport.children.add(info);
                    numEsdCat[5]++;
                    break;

                case "Exposició":
                    exposicio.children.add(info);
                    numEsdCat[6]++;
                    break;

                case "Art":
                    art.children.add(info);
                    numEsdCat[7]++;
                    break;

                case "Ciència":
                    ciencia.children.add(info);
                    numEsdCat[8]++;
                    break;

                case "Cultura":
                    cultura.children.add(info);
                    numEsdCat[9]++;
                    break;

                case "Oci":
                    oci.children.add(info);
                    numEsdCat[10]++;
                    break;

                default:
                    break;
            }
        }

        int i = 0;
        int j = 0;
        boolean[] interests = Global.getInstance().interests;
        if(interests[i]) {
            groups.add(j, espectacles);
            ++j;
        }
        ++i;
        if(interests[i]) {
            groups.add(j, musica);
            ++j;
        }
        ++i;
        if(interests[i]) {
            groups.add(j, cinema);
            ++j;
        }
        ++i;
        if(interests[i]) {
            groups.add(j, museu);
            ++j;
        }
        ++i;
        if(interests[i]) {
            groups.add(j, infantil);
            ++j;
        }
        ++i;
        if(interests[i]) {
            groups.add(j, esport);
            ++j;
        }
        ++i;
        if(interests[i]) {
            groups.add(j, exposicio);
            ++j;
        }
        ++i;
        if(interests[i]) {
            groups.add(j, art);
            ++j;
        }
        ++i;
        if(interests[i]) {
            groups.add(j, ciencia);
            ++j;
        }
        ++i;
        if(interests[i]) {
            groups.add(j, oci);
            ++j;
        }
        ++i;
        if(interests[i]) {
            groups.add(j, cultura);
            ++j;
        }

        MyExpandableListAdapter adapter = new MyExpandableListAdapter(getActivity(), groups);
        mExpendableListView.setAdapter(adapter);

        ColorDrawable myColor = new ColorDrawable(0xFFCFBEBE);
        mExpendableListView.setDivider(myColor);
        mExpendableListView.setDividerHeight(2);
    }

    public static int getEventPosition(int groupPosition) {
        int pos = 0;
        for (int i = 0; i < groupPosition - 1; i++) {
            pos += numEsdCat[i];
        }
        return pos;
    }
}