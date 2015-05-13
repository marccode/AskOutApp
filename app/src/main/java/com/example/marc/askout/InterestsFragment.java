package com.example.marc.askout;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
    public static String [] prgmNameList={"Espectacles","Música","Cinema","Museu","Infantil","Esport","Exposició","Art","Ciència", "Oci i Cultura"};
    public static int [] prgmImages={R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon, R.drawable.icon,R.drawable.icon,R.drawable.icon};


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //setContentView(R.layout.activity_main);

        //String [] prgmNameList={"Espectacles","Música","Cinema","Museu","Infantil","Esport","Exposició","Art","Ciència", "Oci i Cultura"};
        //int [] prgmImages={R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon,R.drawable.icon, R.drawable.icon,R.drawable.icon,R.drawable.icon};

        View view =  inflater.inflate(R.layout.fragment_interests, container, false);

        //prgmNameList,prgmImages
        InterestGridAdapter adapter = new InterestGridAdapter(getActivity());

        gv = (GridView) view.findViewById(R.id.gridView1);
        gv.setAdapter(adapter);

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

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
    {
        ImageView imageView = ((Holder) v.getTag()).img;
        imageView.setImageResource(InterestGridAdapter.imageSelId[position]);
    }
}
