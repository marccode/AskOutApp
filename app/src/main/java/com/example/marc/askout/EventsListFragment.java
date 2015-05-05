package com.example.marc.askout;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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

/**
 * Created by marc on 5/4/15.
 */
public class EventsListFragment extends ListFragment {

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events_list_layout, container, false);
        //Toast.makeText(this, "BLAH", Toast.LENGTH_LONG).show();
        Log.d("FRAGMENT", "onCreateView - EventsListFragment");
        //ListView lv = (ListView) findViewById(R.id.listview);
        return view;
    }
    */

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("RESULT", "qwer");
        Toast.makeText(this.getActivity(), "asdasd", Toast.LENGTH_SHORT).show();
        new RequestTask().execute("http://jediantic.upc.es/api/events");
        Toast.makeText(this.getActivity(), "qweqw", Toast.LENGTH_SHORT).show();
    }

    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
            Log.d("RESULT", "asasd");
            //Toast.makeText(this.getActivity(), "asdasd", Toast.LENGTH_SHORT).show();
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

        //String[] titles = new String[] {"title1", "title2", "title3"};
        List<String> titles = new ArrayList<String>();
        //Handle
        JSONArray jArray = new JSONArray(s);
        for (int i=0; i < jArray.length(); i++) {
            JSONObject obj = jArray.getJSONObject(i);
            Toast.makeText(this.getActivity(), obj.getString("titol"), Toast.LENGTH_SHORT).show();
            titles.add(obj.getString("titol"));
        }

        Toast.makeText(this.getActivity(), titles.get(0), Toast.LENGTH_SHORT).show();
        //String[] titles2 = new String[] {"title1", "title2", "title3"};
        //MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(getConte, titles, dates_init);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, titles);
        setListAdapter(adapter);
        return s;
    }
}