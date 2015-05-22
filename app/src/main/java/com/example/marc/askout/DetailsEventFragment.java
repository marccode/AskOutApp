package com.example.marc.askout;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;


public class DetailsEventFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private OnFragmentInteractionListener mListener;

    private String id;
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
    private TextView categoriesText;

    FloatingActionButton botoDesplegable;
    FloatingActionButton botoGuardar;
    FloatingActionButton botoCompartir;
    FloatingActionButton botoMapa;
    FloatingActionButton botoRecordatori;


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
        final View rootView = inflater.inflate(R.layout.fragment_details_event, container, false);

        if (getArguments() != null) {
            //obtenim les dades a traves del Bundle
            id = getArguments().getString("id");
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
            nomText.setText(nom);
            nomLlocText = (TextView) rootView.findViewById(R.id.nomLlocEsd);
            nomLlocText.setText(nomLloc + "\n" + carrer + " " + numero + "\n" + districte + " " + municipi);
            dataText = (TextView) rootView.findViewById(R.id.dataEsd);

            String any =  data_inici.substring(0,4);
            String mes = data_inici.substring(5,7);
            String dia = data_inici.substring(8,10);
            String hora = data_inici.substring(11, 16);
            dataText.setText("\n" + dia + "/" + mes + "/" + any + " " + hora);
            categoriesText = (TextView) rootView.findViewById(R.id.categoriesEsd);
            String categoria = getArguments().getString("categories");
            categoria = categoria.replace("[", "");
            categoria = categoria.replace("]", "");
            categoria = categoria.replace("&", " i ");
            categoria = categoria.substring(1,categoria.length()-1);
            categoriesText.setText("\n" + categoria);
            categoriesText.post(new Runnable() {
                @Override
            public void run() {                                                              //una vegada els textview estan llestos, comptem les linees per donar tamany al layout amb les linees
                    int numeroLinies = nomText.getLineCount() + nomLlocText.getLineCount() + dataText.getLineCount() + categoriesText.getLineCount();
                    if (numeroLinies == 10) {
                        dataText.setText(data_inici);
                        dataText.setLines(2);
                    } else if (numeroLinies == 11) {
                        dataText.setText(data_inici);
                        dataText.setLines(1);
                    }
                    Toast.makeText(getActivity(), "numero linees" + numeroLinies, Toast.LENGTH_LONG).show();
                }
            });
            //CODI PER ELS FLOATING BUTTON
            botoCompartir = (FloatingActionButton) rootView.findViewById(R.id.floatingButtonShare);
            botoGuardar = (FloatingActionButton) rootView.findViewById(R.id.floatingButtonSave);
            botoMapa = (FloatingActionButton) rootView.findViewById(R.id.floatingButtonMaps);
            botoRecordatori = (FloatingActionButton) rootView.findViewById(R.id.floatingButtonReminder);
            botoGuardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(getActivity())
                            .title("Atenció")
                            .content("Vols guardar l'esdeveniment a la llista dels teus esdeveniments?")
                            .positiveText("GUARDAR")
                            .negativeText("CANCEL·LAR")
                            .positiveColorRes(R.color.material_blue_grey_900)
                            .neutralColorRes(R.color.material_blue_grey_900)
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {

                                }
                            }).show();
                    String userId = Profile.getCurrentProfile().getId(); // NO ES AQUEST ID
                    guardarEsdeveniment(userId, id);
                }
            });

            botoRecordatori.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "niet", Toast.LENGTH_LONG).show();
                    if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("notification_preference", true)) {
                        boolean wrapInScrollView = true;
                        new MaterialDialog.Builder(getActivity())
                                .title("Tria l'hora de l'avís")
                                .customView(R.layout.timepicker_layout, wrapInScrollView)
                                .positiveText("Guardar")
                                .negativeText("Cancelar")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        // GET TIME AND CALL SETALARM WITH THE TIME
                                        TimePicker timePicker = (TimePicker) rootView.findViewById(R.id.timePicker);
                                        //int hora =  timePicker.getCurrentHour();
                                        //String hora = timePicker.getCurrentHour().toString();
                                        Toast.makeText(getActivity(), "HORA I MINUT ", Toast.LENGTH_LONG).show();
                                        //setAlarm(10, 10, 10);
                                    }
                                }).show();
                    } else {
                        new MaterialDialog.Builder(getActivity())
                                .title("Atenció")
                                .content("Activa les notificacions per poder programara un avís!")
                                .positiveText("Activa")
                                .negativeText("Cancela")
                                .positiveColorRes(R.color.material_blue_grey_900)
                                .neutralColorRes(R.color.material_blue_grey_900)
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("notification_preference", true).commit();
                                        //showTimePicker(data_inici);
                                    }
                                }).show();
                    }
                }
            });

            botoMapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //OBRIR MAPS - MATERIAL DIALOG per confirmar
                    new MaterialDialog.Builder(getActivity())
                            .title("Atenció")
                            .content("Vols obrir Google Maps a la localització de l'esdeveniment?")
                            .positiveText("OBRIR")
                            .negativeText("CANCEL·LAR")
                            .positiveColorRes(R.color.material_blue_grey_900)
                            .neutralColorRes(R.color.material_blue_grey_900)
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    openGoogleMaps();
                                }
                            }).show();
                }
            });

            botoCompartir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //COMPARTIR
                    performPublish();
                }
            });

        }
        if (Profile.getCurrentProfile() == null) {
            botoCompartir.setVisibility(View.INVISIBLE);
            botoGuardar.setVisibility(View.INVISIBLE);
            botoRecordatori.setVisibility(View.INVISIBLE);
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

    // SHARE WITH FACEBOOK

    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("HelloFacebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("HelloFacebook", "Success!");
            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getString(R.string.successfully_posted_post, id);
                showResult(title, alertMessage);
            }
        }

        private void showResult(String title, String alertMessage) {
            /*
            new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setMessage(alertMessage)
                    .setPositiveButton(R.string.ok, null)
                    .show();
            */
        }
    };



    private void postStatusUpdate() {
        Profile profile = Profile.getCurrentProfile();
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle("AskOut!")
                .setContentDescription(
                        "Blah Blah Blah, have a look at this shitty event")
                .setContentUrl(Uri.parse("http://jediantic.upc.es/"))
                .build();

        Boolean canPresentShareDialog = ShareDialog.canShow(
                ShareLinkContent.class);
        if (canPresentShareDialog) {
            ShareDialog shareDialog = new ShareDialog(getActivity());
            CallbackManager callbackManager = CallbackManager.Factory.create();
            shareDialog.registerCallback(callbackManager, shareCallback);
            shareDialog.show(linkContent);
        } else if (profile != null && hasPublishPermission()) {
            ShareApi.share(linkContent, shareCallback);
        }
    }

    private boolean hasPublishPermission() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
    }

    private void performPublish() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            if (hasPublishPermission()) {
                // We can do the action right away.
                postStatusUpdate();
                return;
            } else {
                // We need to get new permissions, then complete the action when we get called back.
                LoginManager.getInstance().logInWithPublishPermissions(getActivity(), Arrays.asList("publish_actions"));
                return;
            }
        }
    }

    class RequestTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... uri) {
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
            //Do anything with response...
        }
    }

    private void guardarEsdeveniment(String userId, String eventId) {
        new RequestTask().execute("http://jediantic.upc.es/api/anarEvent/" + HomeActivity.myID + "/" + eventId);
    }

    // NOTIFICACIONS
    private void showTimePicker(String hora) {
        // SELECT TIME
        boolean wrapInScrollView = true;
        new MaterialDialog.Builder(getActivity())
                .title("Tria l'hora de l'avís")
                .customView(R.layout.timepicker_layout, wrapInScrollView)
                .positiveText("Guardar")
                .negativeText("Cancelar")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        View rootView = getView();
                        // GET TIME AND CALL SETALARM WITH THE TIME
                        TimePicker timePicker = ((TimePicker) rootView.findViewById(R.id.timePicker));
                        int hora =  timePicker.getCurrentHour();
                        //String hora = timePicker.getCurrentHour().toString();
                        //Toast.makeText(getActivity(), "HORA I MINUT ", Toast.LENGTH_LONG).show();
                        //setAlarm(10, 10, 10);
                    }
                }).show();
    }

    private void setAlarm(int hour, int minute, int second) {
        // SET ALARM

        Intent myIntent = new Intent(getActivity(), NotificationService.class);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getActivity().ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0, myIntent, 0);

        /*
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 50);
        calendar.set(Calendar.SECOND, 00);
        */

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 30);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);  //set repeating every 24 hours
    }


    // OPEN GOOGLE MAPS
    private void openGoogleMaps() {
        String uri = String.format(Locale.ENGLISH, "http://www.google.es/maps/place/" + carrer + ",+" + numero + ",+" + "+" + municipi);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        try
        {
            startActivity(intent);
        }
        catch(ActivityNotFoundException ex)
        {
            try
            {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            }
            catch(ActivityNotFoundException innerEx)
            {
                Toast.makeText(getActivity(), "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }
}
