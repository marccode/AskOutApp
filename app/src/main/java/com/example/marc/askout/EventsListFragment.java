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
    private List<ListViewItem> mItems;
    Date date;
    ExpandableListView mListView;
    SparseArray<Group> groups = new SparseArray<Group>();

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
                int year = getArguments().getInt("year");
                int month = getArguments().getInt("month");
                int day = getArguments().getInt("day");
                date = new Date(year, month, day);
            }
            else {
                date = new Date();
            }


            /*
            Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
            calendar.get(Calendar.YEAR)
            */



            //Toast.makeText(getActivity(), date.toString(), Toast.LENGTH_SHORT).show();

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
        onRefresh();
        MyExpandableListAdapter adapter = new MyExpandableListAdapter(getActivity(), groups);
        numEsdCat = new int[10];
        mListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onRefresh() {
        //new myTask().execute();
        Toast.makeText(getActivity(), "refresh", Toast.LENGTH_LONG).show();
        new RequestTask().execute("http://jediantic.upc.es/api/events/");// + date.toString());
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
            for (int i = 0; i < 50; i++) {
                JSONObject obj = jArray.getJSONObject(i);
                JSONArray pony = obj.getJSONArray("categories_generals");
                Resources resources = getResources();
                Drawable icon = resources.getDrawable(R.drawable.icon);

                String nom = obj.getString("nom");
                String nomLloc = obj.getString("nomLloc");
                /*
                if (nom.length() > 40) {
                    nom = nom.substring(0,40);
                    nom = nom + "...";
                }
                if (nomLloc.length() > 45) {
                    nomLloc = nomLloc.substring(0,45);
                    nomLloc = nomLloc + "...";
                }
                */
                InfoSingleEvent info;
                switch (pony.get(0).toString()) {
                    case "Espectacles":
                        info = new InfoSingleEvent(nom, nomLloc);
                        espectacles.children.add(info);
                        numEsdCat[0]++;
                        icon = resources.getDrawable(R.drawable.ic_headphones_black_24dp);
                        break;

                    case "Música":
                        info = new InfoSingleEvent(nom, nomLloc);
                        musica.children.add(info);
                        numEsdCat[1]++;
                        icon = resources.getDrawable(R.drawable.ic_headphones_black_24dp);
                        break;

                    case "Cinema":
                        info = new InfoSingleEvent(nom, nomLloc);
                        cinema.children.add(info);
                        numEsdCat[2]++;
                        icon = resources.getDrawable(R.drawable.ic_theaters_black_24dp);
                        break;

                    case "Museu":
                        info = new InfoSingleEvent(nom, nomLloc);
                        museu.children.add(info);
                        numEsdCat[3]++;
                        icon = resources.getDrawable(R.drawable.ic_account_balance_black_24dp);
                        break;

                    case "Infantil":
                        info = new InfoSingleEvent(nom, nomLloc);
                        infantil.children.add(info);
                        numEsdCat[4]++;
                        icon = resources.getDrawable(R.drawable.ic_duck_black_24dp);
                        break;

                    case "Esport":
                        info = new InfoSingleEvent(nom, nomLloc);
                        esport.children.add(info);
                        numEsdCat[5]++;
                        icon = resources.getDrawable(R.drawable.ic_dribbble_black_24dp);
                        break;

                    case "Exposició":
                        info = new InfoSingleEvent(nom, nomLloc);
                        exposicio.children.add(info);
                        numEsdCat[6]++;
                        icon = resources.getDrawable(R.drawable.ic_headphones_black_24dp);
                        break;

                    case "Art":
                        info = new InfoSingleEvent(nom, nomLloc);
                        art.children.add(info);
                        numEsdCat[7]++;
                        icon = resources.getDrawable(R.drawable.ic_palette_black_24dp);
                        break;

                    case "Ciència":
                        info = new InfoSingleEvent(nom, nomLloc);
                        ciencia.children.add(info);
                        numEsdCat[8]++;
                        icon = resources.getDrawable(R.drawable.ic_beaker_outline_black_24dp);
                        break;

                    case "Oci&Cultura":
                        info = new InfoSingleEvent(nom, nomLloc);
                        ocicultura.children.add(info);
                        numEsdCat[9]++;
                        icon = resources.getDrawable(R.drawable.ic_headphones_black_24dp);
                        break;

                    default:
                        break;
                }
                //mItems.add(new ListViewItem(icon, nom, nomLloc));
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


            ColorDrawable myColor = new ColorDrawable(0xFFCFBEBE);
            mListView.setDivider(myColor);
            mListView.setDividerHeight(2);
        }
        else {
            Toast.makeText(getActivity(), "Connecta't a internet per obtenir els esdeveniments!", Toast.LENGTH_SHORT).show();
        }
        return s;
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