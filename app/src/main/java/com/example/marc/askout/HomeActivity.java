package com.example.marc.askout;

import android.app.FragmentManager;
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
    private EventsListFragment mEventsListFragment;

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


        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, new EventsListFragment()).commit();

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
        Toast.makeText(this, "menu option " + Integer.toString(position) , Toast.LENGTH_LONG).show();
        FragmentManager fragmentManager;
        switch (position) {
            case 0:
                // EVENTS LIST
                fragmentManager = getFragmentManager();
                //fragmentManager.beginTransaction().replace(R.id.container, new EventsListFragment()).commit();
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

                    /*
                    if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notification_preference", true)) {
                        Toast.makeText(this, "IF", Toast.LENGTH_LONG).show();

                        Intent myIntent = new Intent(this , SettingsFragment.class);
                        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, 13);
                        calendar.set(Calendar.MINUTE, 40);
                        calendar.set(Calendar.SECOND, 00);

                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , pendingIntent);  //set repeating every 24 hours



                        /*
                        Intent intent = new Intent(this, SettingsFragment.class);
                        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

                        // build notification
                        // the addAction re-use the same intent to keep the example short
                        Notification n  = new Notification.Builder(this)
                                .setContentTitle("New mail from " + "test@gmail.com")
                                .setContentText("Subject")
                                .setSmallIcon(R.drawable.icon)
                                .setContentIntent(pIntent)
                                .setAutoCancel(true)
                                .addAction(R.drawable.icon, "Call", pIntent)
                                .addAction(R.drawable.icon, "More", pIntent)
                                .addAction(R.drawable.icon, "And more", pIntent).build();


                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                        notificationManager.notify(0, n);
                        */
                    /*
                    }
                    else {
                        Toast.makeText(this, "ELSE", Toast.LENGTH_LONG).show();
                    }
                    */

                }
                break;
            case 2:
                if (Profile.getCurrentProfile() != null) {
                    // INTERESTS-

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
                    //fragmentManager = getFragmentManager();
                    //fragmentManager.beginTransaction().replace(R.id.container, new SettingsFragment()).commit();
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
