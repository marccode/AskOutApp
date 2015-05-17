package com.example.marc.askout;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

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

/**
 * Created by marc on 5/4/15.
 */
public class EventsListFragment extends ListFragment {
    private List<ListViewItem> mItems;
    JSONArray jArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the items list
        mItems = new ArrayList<ListViewItem>();
        //Resources resources = getResources();
        new RequestTask().execute("http://jediantic.upc.es/api/events");


        //mItems.add(new ListViewItem(resources.getDrawable(R.drawable.aim), getString(R.string.aim), getString(R.string.aim_description)));
        //mItems.add(new ListViewItem(resources.getDrawable(R.drawable.bebo), getString(R.string.bebo), getString(R.string.bebo_description)));
        //mItems.add(new ListViewItem(resources.getDrawable(R.drawable.youtube), getString(R.string.youtube), getString(R.string.youtube_description)));

        // initialize and set the list adapter
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // remove the dividers from the ListView of the ListFragment
        getListView().setDivider(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // retrieve theListView item
        ListViewItem item = mItems.get(position);

        // do something
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
            args.putString("latitude", obj.getString("latitude"));
            args.putString("longitude", obj.getString("longitude"));
            args.putString("categories", obj.getString("categories_generals"));
            //args.putStringArray("categories", categories);
            //args.putStringArray("categories_generals", categories_generals);
            detailsEventFragment.setArguments(args);
            FragmentManager fm = getFragmentManager();

            fm.beginTransaction().hide(this).commit();
            fm.beginTransaction().replace(R.id.container, detailsEventFragment).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("RESULT", "qwer");
        Toast.makeText(this.getActivity(), "asdasd", Toast.LENGTH_SHORT).show();
        new RequestTask().execute("http://jediantic.upc.es/api/events");
        Toast.makeText(this.getActivity(), "qweqw", Toast.LENGTH_SHORT).show();
    }
    */

    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            Log.d("RESULT", "asasd");
            //Toast.makeText(getActivity(), "asdasd", Toast.LENGTH_SHORT).show();
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response;
            String responseString = null;
            try {
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Do anything with response...
        }
    }

    private String aux(String s) throws JSONException {
        //Toast.makeText(getActivity(), "aux", Toast.LENGTH_LONG).show();
        List<String> titles = new ArrayList<String>();
        //Handle
        jArray = new JSONArray(s);
        for (int i=0; i < 10; i++) {
            JSONObject obj = jArray.getJSONObject(i);
            //Toast.makeText(this.getActivity(), obj.getString("titol"), Toast.LENGTH_SHORT).show();
            //titles.add(obj.getString("titol"));

            /*
            switch(obj.getString("category")) {
                case ("esports"):
                    //mItems.add();
                    break;
                default:
                    break;
            }
            */
            Resources resources = getResources();
            mItems.add(new ListViewItem(resources.getDrawable(R.drawable.ic_art_sel), obj.getString("nom"), obj.getString("nomLloc")));

        }

        setListAdapter(new ListViewDemoAdapter(getActivity(), mItems));
        ListView listView = getListView();
        ColorDrawable myColor = new ColorDrawable(0xFFCFBEBE);
        listView.setDivider(myColor);
        listView.setDividerHeight(2);

        //Toast.makeText(this.getActivity(), titles.get(0), Toast.LENGTH_SHORT).show();
        //String[] titles2 = new String[] {"title1", "title2", "title3"};
        //MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(getConte, titles, dates_init);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
        //        android.R.layout.simple_list_item_1, titles);
        //setListAdapter(adapter);
        return s;
    }
}