package com.example.marc.askout;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
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
import java.util.Date;
import java.util.List;

public class EventsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    SwipeRefreshLayout mSwipeRefreshLayout;
    //ListView mListView;
    JSONArray jArray;
    ExpandableListView mListView;
    int year;
    int month;
    int day;

    public static JSONArray jArray;
    private static int numEsdCat[];

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
            mListView =  (ExpandableListView) view.findViewById(R.id.activity_main_listview);

            groups = new SparseArray<Group>();

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
        setUpList();
        return s;
    }

    public void setUpList() {
        groups = new SparseArray<Group>();
        MyExpandableListAdapter adapter = new MyExpandableListAdapter(getActivity(), groups);
        numEsdCat = new int[10];
        mListView.setAdapter(adapter);

        Group espectacles = new Group("Espectacles");
        Group musica = new Group("Música");
        Group cinema = new Group("Cinema");
        Group museu = new Group("Museu");
        Group infantil = new Group("Infantil");
        Group esport = new Group("Esport");
        Group exposicio = new Group("Exposició");
        Group art = new Group("Art");
        Group ciencia = new Group("Ciència");
        Group ocicultura = new Group("Oci i Cultura");
        
        for (int i = 0; i < Global.getInstance().mItems.size(); ++i) {
            InfoSingleEvent info = new InfoSingleEvent(nom, nomLloc);
            switch (pony.get(0).toString()) {
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

                case "Oci&Cultura":
                    ocicultura.children.add(info);
                    numEsdCat[9]++;
                    break;

                default:
                    break;
            }
        }

        if(! espectacles.isEmpty()) groups.append(0, espectacles);
        if(! musica.isEmpty()) groups.append(0, musica);
        if(! cinema.isEmpty()) groups.append(0, cinema);
        if(! museu.isEmpty()) groups.append(0, museu);
        if(! infantil.isEmpty()) groups.append(0, infantil);
        if(! esport.isEmpty()) groups.append(0, esport);
        if(! exposicio.isEmpty()) groups.append(0, exposicio);
        if(! art.isEmpty()) groups.append(0, art);
        if(! ciencia.isEmpty()) groups.append(0, ciencia);
        if(! ocicultura.isEmpty()) groups.append(0, ocicultura);
        
        //mListView.setAdapter(new ListViewDemoAdapter(getActivity(), Global.getInstance().mItems));

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
                args.putString("di stricte",Global.getInstance().mItems.get(position).districte);
                args.putString("municipi", Global.getInstance().mItems.get(position).municipi);
                args.putString("categories", Global.getInstance().mItems.get(position).categories);

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