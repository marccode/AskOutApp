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
        if (Global.getInstance().interests == null) {
            Global.getInstance().interests = new boolean[]{false, false, false, false, false, false, false, false, false, false};
            new RequestTask().execute("http://jediantic.upc.es/api/userInterest/" + HomeActivity.myID);
        }
        else {
            setUpInterests();
        }
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
                aux(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String aux(String result) throws JSONException {
        if (result != null) {
            JSONArray jArray = new JSONArray(result);
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

                    case "Oci&Cultura":
                        Global.getInstance().interests[9] = interes;
                        break;

                    default:
                        break;
                }
            }
        }
        setUpInterests();
        return result;
    }

    public void setUpInterests() {
        gv = (GridView) view.findViewById(R.id.gridView1);
        InterestGridAdapter adapter = new InterestGridAdapter(getActivity(), Global.getInstance().interests);
        gv.setAdapter(adapter);
    }
}
