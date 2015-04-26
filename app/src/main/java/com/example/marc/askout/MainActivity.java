package com.example.marc.askout;

import android.app.AlertDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;


public class MainActivity extends FragmentActivity {

    private CallbackManager callbackManager;
    public static FileOperations fop;
    private String TOKEN;

    private boolean checklogin() {
        return false;
    }

    public void writeToken() {
        fop.writeToken(TOKEN);
    }

    public String getToken() {
        return fop.readToken();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("MainActivity", "TOKEN" + loginResult.getAccessToken().getToken());
                        Toast.makeText(getApplicationContext(),"TOKEN" + loginResult.getAccessToken().getToken(), Toast.LENGTH_LONG).show();
                        TOKEN = loginResult.getAccessToken().getToken();
                        loginResult.getAccessToken().isExpired();
                        loginResult.getAccessToken();
                        writeToken();
                    }

                    @Override
                    public void onCancel() {
                        Log.d("MainActivity", "cancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("MainActivity", "error");
                    }

                    private void showAlert() {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(R.string.cancelled)
                                .setMessage(R.string.permission_not_granted)
                                .setPositiveButton(R.string.ok, null)
                                .show();
                    }
                });
        /*
        if (checklogin()) { //no login
            Log.d("MainActivity", "L'usuari ja ha fet login");
        }
        else {
            Log.d("MainActivity", "L'usuari no ha fet login");
        }*/
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
