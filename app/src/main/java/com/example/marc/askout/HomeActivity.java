package com.example.marc.askout;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.Profile;
import com.facebook.login.LoginManager;


public class HomeActivity extends ActionBarActivity implements NavigationDrawerCallbacks {
//public class HomeActivity extends FragmentActivity

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private FragmentManager fm;
    private FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        // populate the navigation drawer

        fm = getFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.container, new EventsListFragment());
        ft.commit();


        if (Profile.getCurrentProfile() != null) {
            mNavigationDrawerFragment.setUserData(Profile.getCurrentProfile().getName());
        }
        else {
            //Toast.makeText(this, "IS NUL ON HOME ACTIVITY", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        Toast.makeText(this, "asdasd" + Integer.toString(position) , Toast.LENGTH_LONG).show();
        switch (position) {
            case 0:
                // EVENTS LIST:
                //new RequestTask().execute("http://jediantic.upc.es/api/events");
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                new MaterialDialog.Builder(this)
                        .title("Warning")
                        .content("Do you want to go the first screen and log in again?")
                        .positiveText("LOG OUT")
                        .negativeText("CANCEL")
                        .positiveColorRes(R.color.material_blue_grey_900)
                        .neutralColorRes(R.color.material_blue_grey_900)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                //LOG OUT FROM FACEBOOK
                                LoginManager.getInstance().logOut();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(i);
                            }
                        }).show();
        break;

    }
}


    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else {
            super.onBackPressed();
            mNavigationDrawerFragment.openDrawer();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
