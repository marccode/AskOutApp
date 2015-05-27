package com.example.marc.askout;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;


public class HomeActivity extends ActionBarActivity implements NavigationDrawerCallbacks {
//public class HomeActivity extends FragmentActivity

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;
    private EventsListFragment mEventsListFragment;
    private SearchView search;
    public static String myID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        myID = PreferenceManager.getDefaultSharedPreferences(this).getString("myID", "-1");

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);
        // populate the navigation drawer


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
        FragmentManager fragmentManager;
        switch (position) {
            case 0:
                // EVENTS LIST
                fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, new EventsListFragment()).commit();
                break;

            case 1:
                if (Profile.getCurrentProfile() != null) {
                    // MY EVENTS
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, new MyEventsListFragment()).commit();
                }
                else {
                    // INTERESTS
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, new InterestsFragment()).commit();
                }
                break;

            case 2:
                if (Profile.getCurrentProfile() != null) {
                    // INTERESTS
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, new InterestsFragment()).commit();
                }
                else {
                    // SETTINGS
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, new SettingsFragment()).commit();
                }
                break;

            case 3:
                if (Profile.getCurrentProfile() != null) {
                    // SETTINGS
                    fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, new SettingsFragment()).commit();

                    //Intent i = new Intent(this, NotificationService.class);
                    //startActivity(i);
                }
                else {
                    // LOG OUT
                    logOut();
                }
                break;

            case 4:
                // LOG OUT
                logOut();
                break;
    }
}
    private void logOut() {
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
                        myID = "-1";
                        Global.getInstance().interests = null;
                        Global.getInstance().mItems = null;
                        Global.getInstance().mItemsSaved = new ArrayList<ListViewItem>();
                        LoginManager.getInstance().logOut();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, new EventsListFragment()).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.home, menu);




            //search.QueryTextChange += (s, e) => _adapter.Filter.InvokeFilter(e.NewText);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            // action with ID action_refresh was selected
            case R.id.action_calendar:
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog d, int year, int month, int day) {
                                Global.getInstance().day = day;
                                Global.getInstance().month = month + 1;
                                Global.getInstance().year = year;
                                FragmentManager fragmentManager = getFragmentManager();
                                EventsListFragment ef = new EventsListFragment();
                                Bundle args = new Bundle();
                                args.putBoolean("refresh", true);
                                ef.setArguments(args);
                                fragmentManager.beginTransaction().replace(R.id.container, ef).commit();
                                mToolbar.setTitle("AskOut " + day + "/" + month + "/" + year);
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");

                break;
            case R.id.action_search:

                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                final EditText edittext= new EditText(this);
                alert.setTitle("Busca events del dia " + EventsListFragment.day + "/" + EventsListFragment.month + "/" + EventsListFragment.year);

                alert.setView(edittext);

                alert.setPositiveButton("Cerca", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        FragmentManager fragmentManager = getFragmentManager();
                        SearchFragment sf = new SearchFragment();
                        Bundle args = new Bundle();
                        args.putString("nom", edittext.getText().toString());
                        sf.setArguments(args);
                        fragmentManager.beginTransaction().replace(R.id.container, sf).commit();
                    }
                });

                alert.setNegativeButton("Enrere", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();

                //CANVI D'ACTIVITAT

                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
