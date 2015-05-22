package com.example.marc.askout;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by marc on 5/12/15.
 */
public class InterestGridAdapter extends BaseAdapter {

    Context context;

    //String[] result = {"Espectacles", "Música", "Cinema", "Museu", "Infantil", "Esport", "Exposició", "Art", "Ciència", "Oci i Cultura"};
    public boolean[] selected;// = {false, false, false, false, false, false, false, false, false, false};
    public static String [] result={"Espectacles","Música","Cinema","Museu","Infantil","Esport","Exposició","Art","Ciència", "Oci i Cultura"};
    public static int [] imageSelId={R.drawable.ic_theater_black_24dp,R.drawable.ic_headphones_black_24dp,R.drawable.ic_theaters_black_24dp,R.drawable.ic_account_balance_black_24dp,R.drawable.ic_duck_black_24dp,R.drawable.ic_dribbble_black_24dp,R.drawable.ic_crop_original_black_24dp, R.drawable.ic_palette_black_24dp,R.drawable.ic_beaker_outline_black_24dp,R.drawable.ic_book_open_black_24dp};
    public int[] imageId = {R.drawable.ic_theater_grey600_24dp, R.drawable.ic_headphones_grey600_24dp, R.drawable.ic_theaters_grey600_24dp, R.drawable.ic_account_balance_grey600_24dp, R.drawable.ic_duck_grey600_24dp, R.drawable.ic_dribbble_grey600_24dp, R.drawable.ic_crop_original_grey600_24dp, R.drawable.ic_palette_grey600_24dp, R.drawable.ic_beaker_outline_grey600_24dp, R.drawable.ic_book_open_grey600_24dp};


    private static LayoutInflater inflater = null;

    //public InterestGridAdapter(MainActivity mainActivity, String[] prgmNameList, int[] prgmImages) {
    public InterestGridAdapter(Activity mainActivity, boolean[] interests) {

        // TODO Auto-generated constructor stub
        context = mainActivity;
        selected = interests;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {
        TextView tv;
        ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        Holder holder = new Holder();
        final View rowView;

        rowView = inflater.inflate(R.layout.interest_grid_item, null);
        holder.tv = (TextView) rowView.findViewById(R.id.textView1);
        holder.img = (ImageView) rowView.findViewById(R.id.imageView1);

        holder.tv.setText(result[position]);
        if (selected[position])
            holder.img.setImageResource(imageSelId[position]);
        else
            holder.img.setImageResource(imageId[position]);


        rowView.setOnClickListener(new View.OnClickListener() {
            // SI L'USUARI NO HA INICIAT SESSIÓ NO S'ENVIA RES A LA API.

            @Override
            public void onClick(View v) {
                // CANVIAR LA ICONA
                ImageView img = (ImageView) v.findViewById(R.id.imageView1);

                if (Global.getInstance().interests[position]) {
                    if (result[position].equals("Oci i Cultura")) {
                        new RequestTask().execute("http://jediantic.upc.es/api/users/" + HomeActivity.myID + "/Oci&Cultura/false");
                    }
                    else {
                        new RequestTask().execute("http://jediantic.upc.es/api/users/" + HomeActivity.myID + "/" + result[position] + "/false");
                    }
                    Global.getInstance().interests[position] = false;
                    img.setImageResource(imageId[position]);
                }
                else {
                    if (result[position].equals("Oci i Cultura")) {
                        new RequestTask().execute("http://jediantic.upc.es/api/users/" + HomeActivity.myID + "/Oci&Cultura/true");
                    }
                    else {
                        new RequestTask().execute("http://jediantic.upc.es/api/users/" + HomeActivity.myID + "/" + result[position] + "/true");
                    }
                    Global.getInstance().interests[position] = true;
                    img.setImageResource(imageSelId[position]);
                }
            }
        });
        return rowView;
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
        }
    }
}