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

import com.getbase.floatingactionbutton.FloatingActionButton;

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

    private TextView nomText;
    private TextView nomLlocText;

    private TextView dataText;
    private TextView carrerText;
    private TextView districteText;
    private TextView coordsText;

    FloatingActionButton botoDesplegable;
    FloatingActionButton botoGuardar;
    FloatingActionButton botoCompartir;
    FloatingActionButton botoMapa;


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
            nomText = (TextView) rootView.findViewById(R.id.nomEsd);
            nomText.setText("nom: " + nom);
            nomLlocText = (TextView) rootView.findViewById(R.id.nomLlocEsd);
            nomLlocText.setText("nomLloc: " + nomLloc);
            carrerText = (TextView) rootView.findViewById(R.id.carrerEsd);
            carrerText.setText("carrer i numero: " + carrer + " " + numero);
            districteText = (TextView) rootView.findViewById(R.id.districteEsd);
            districteText.setText("districte i municipi: " + districte + " " + municipi);
            dataText = (TextView) rootView.findViewById(R.id.dataEsd);
            dataText.setText("dataI i dataF: " + data_inici + " " + data_final);
            coordsText = (TextView) rootView.findViewById(R.id.coordsEsd);
            coordsText.setText("latitude i longitude: " + latitude + " " + longitude);

            //CODI PER ELS FLOATING BUTTON
            botoCompartir = (FloatingActionButton) rootView.findViewById(R.id.floatingButtonShare);
            botoGuardar = (FloatingActionButton) rootView.findViewById(R.id.floatingButtonSave);
            botoMapa = (FloatingActionButton) rootView.findViewById(R.id.floatingButtonMaps);

            botoGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //GUARDAR ELEMENT - MATERIAL DIALOG COM LOG OUT a homeactivity
                }
            });

            botoMapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //OBRIR MAPS - MATERIAL DIALOG COM LOG OUT a homeactivity
                }
            });

            botoMapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //OBRIR MAPS - MATERIAL DIALOG COM LOG OUT a homeactivity
                }
            });
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
