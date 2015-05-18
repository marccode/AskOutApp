package com.example.marc.askout;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_interests, container, false);
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        mListView =  (ListView) view.findViewById(R.id.activity_main_listview);

        mItems = new ArrayList<ListViewItem>();

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorScheme(android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);

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
            Log.d("REFRESH", "1");
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
                mSwipeRefreshLayout.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Do anything with response...
        }
    }

    private String aux(String s) throws JSONException {
        Log.d("REFRESH", "2");
        //Handle
        Log.d("REFRESH", "3");
        Log.d("REFRESH", s);
        JSONArray jArray = new JSONArray(s);
        Log.d("REFRESH", "4");
        for (int i=0; i < 15; i++) {
            Log.d("REFRESH", Integer.toString(i));
            JSONObject obj = jArray.getJSONObject(i);
            Log.d("REFRESH", Integer.toString(i));
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

        mListView.setAdapter(new ListViewDemoAdapter(getActivity(), mItems));
        ColorDrawable myColor = new ColorDrawable(0xFFCFBEBE);
        mListView.setDivider(myColor);
        mListView.setDividerHeight(2);



        //Toast.makeText(this.getActivity(), titles.get(0), Toast.LENGTH_SHORT).show();
        //String[] titles2 = new String[] {"title1", "title2", "title3"};
        //MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(getConte, titles, dates_init);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
        //        android.R.layout.simple_list_item_1, titles);
        //setListAdapter(adapter);
        return s;
    }

}