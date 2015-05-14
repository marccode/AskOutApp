package com.example.marc.askout;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    String[] result = {"Espectacles", "Música", "Cinema", "Museu", "Infantil", "Esport", "Exposició", "Art", "Ciència", "Oci i Cultura"};
    public int[] imageId = {R.drawable.icon, R.drawable.ic_musica_no_sel, R.drawable.ic_cinema_no_sel, R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon};
    public int[] imageSelId = {R.drawable.ic_action, R.drawable.ic_musica_sel, R.drawable.ic_cinema_sel, R.drawable.ic_action, R.drawable.ic_action, R.drawable.ic_action, R.drawable.ic_action, R.drawable.ic_action, R.drawable.ic_action, R.drawable.ic_action};
    public boolean[] selected = {false, false, false, false, false, false, false, false, false, false};

    private static LayoutInflater inflater = null;

    //public InterestGridAdapter(MainActivity mainActivity, String[] prgmNameList, int[] prgmImages) {
    public InterestGridAdapter(Activity mainActivity) {

        // TODO Auto-generated constructor stub
        //result=prgmNameList;
        context = mainActivity;
        //imageId=prgmImages;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
        holder.img.setImageResource(imageId[position]);


        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();

                // DIRLI A LA API QUE ARA M'INTERESSA TAL


                // CANVIAR LA ICONA
                ImageView img = (ImageView) v.findViewById(R.id.imageView1);

                if (selected[position]) {
                    new RequestTask().execute("http://jediantic.upc.es/api/users/55536e7e0e1bbbb5b3b85bec/" + result[position] + "/false");
                    selected[position] = false;
                    img.setImageResource(imageId[position]);
                }
                else {
                    new RequestTask().execute("http://jediantic.upc.es/api/users/55536e7e0e1bbbb5b3b85bec/" + result[position] + "/true");
                    selected[position] = true;
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