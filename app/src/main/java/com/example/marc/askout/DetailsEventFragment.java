package com.example.marc.askout;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;


public class DetailsEventFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private OnFragmentInteractionListener mListener;

    private String data_inici;
    private String data_final;
    private String nom;
    private String nomLloc;
    private String carrer;
    private String numero;
    private String districte;
    private String municipi;
    private String latitude;
    private String longitude;

    private TextView titolEsd;
    private TextView descripcioEsd;

    public DetailsEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_details_event, container, false);
        if (getArguments() != null) {
            //obtenim les dades a traves del Bundle
            data_inici = getArguments().getString("data_inici");
            data_final = getArguments().getString("data_inici");
            nom = getArguments().getString("nom");
            nomLloc = getArguments().getString("nomLloc");
            carrer = getArguments().getString("carrer");
            numero = getArguments().getString("numero");
            districte = getArguments().getString("districte");
            municipi = getArguments().getString("municipi");
            latitude = getArguments().getString("latitude");
            longitude  = getArguments().getString("longitude");
            //assignem les dades al contingut XML
            titolEsd = (TextView) rootView.findViewById(R.id.titolEsd);
            titolEsd.setText(nom);
        }
        rootView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rootView;
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

}
