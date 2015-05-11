package com.example.marc.askout;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by marc on 5/4/15.
 */
public class EventsListFragment extends ListFragment {
    public List<ListViewItem> mItems;
    public JSONArray jArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize the items list
        mItems = new ArrayList<ListViewItem>();
        new RequestTask().execute("http://jediantic.upc.es/api/events");
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
            args.putString("data_inici", obj.getString("data_inici"));
            args.putString("data_final", obj.getString("data_final"));
            args.putString("nom", obj.getString("nom"));
            args.putString("nomLloc", obj.getString("nomLloc"));
            args.putString("data_inici", obj.getString("carrer"));
            args.putString("carrer", obj.getString("numero"));
            args.putString("districte", obj.getString("districte"));
            args.putString("municipi", obj.getString("municipi"));
            args.putString("latitude", obj.getString("latitude"));
            args.putString("longitude", obj.getString("longitude"));
            //args.putStringArray("categories", categories);
            //args.putStringArray("categories_generals", categories_generals);
            detailsEventFragment.setArguments(args);
            FragmentManager fm = getFragmentManager();
            //fm.beginTransaction().hide(this).commit();
            //detailsEventFragment.onCreate(args);
            //detailsEventFragment.onCreateView(LayoutInflater inflater,);

            fm.beginTransaction().replace(R.id.container, detailsEventFragment).commit();
            fm.beginTransaction().remove(this).commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
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
            Resources resources = getResources();
            mItems.add(new ListViewItem(resources.getDrawable(R.drawable.ic_pinzell), obj.getString("nom"), obj.getString("nomLloc")));

        }
        setListAdapter(new ListViewDemoAdapter(getActivity(), mItems));
        return s;
    }
}