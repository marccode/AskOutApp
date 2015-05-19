package com.example.marc.askout;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InterestsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InterestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InterestsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    GridView gv;
    Context context;
    ArrayList prgmName;
    View view;



    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InterestsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InterestsFragment newInstance(String param1, String param2) {
        InterestsFragment fragment = new InterestsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public InterestsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_interests, container, false);
        new RequestTask().execute("http://jediantic.upc.es/api/userInterests/" + HomeActivity.myID);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    class RequestTask extends AsyncTask<String, String, String> {

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
                setInterests(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String setInterests(String result) throws JSONException {
        boolean[] selected = {false, false, false, false, false, false, false, false, false, false};
        if (result != null) {
            JSONArray ja = new JSONArray(result);
            JSONObject o = ja.getJSONObject(0);
            JSONArray jArray = o.getJSONArray("interessos");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                boolean interes = obj.getBoolean("interes");
                String nom_interes = obj.getString("titol");
                switch (nom_interes) {
                    case "Espectacles":
                        selected[0] = interes;
                        Log.d("sad", "espectacles: " + interes);
                        break;

                    case "Música":
                        Log.d("sad", "musica: " + interes);
                        selected[1] = interes;
                        break;

                    case "Cinema":
                        selected[2] = interes;
                        break;

                    case "Museu":
                        selected[3] = interes;
                        break;

                    case "Infantil":
                        selected[4] = interes;
                        break;

                    case "Esport":
                        selected[5] = interes;
                        break;

                    case "Exposició":
                        selected[6] = interes;
                        break;

                    case "Art":
                        selected[7] = interes;
                        break;

                    case "Ciència":
                        selected[8] = interes;
                        break;

                    case "Oci&Cultura":
                        selected[9] = interes;
                        break;

                    default:
                        Log.d("sad", "default");
                        break;
                }
            }
        }
        else {
            Log.d("sad", "else");
        }
        Log.d("sad", "1");
        InterestGridAdapter adapter = new InterestGridAdapter(getActivity(), selected);

        gv = (GridView) view.findViewById(R.id.gridView1);
        gv.setAdapter(adapter);
        return result;
    }


}
