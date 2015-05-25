package com.example.marc.askout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by marc on 5/22/15.
 */
public class Global {
    private static Global instance = null;

    public boolean[] interests = null;
    public List<ListViewItem> mItems = null;
    public List<ListViewItem> mItemsSaved = null;//  = new ArrayList<ListViewItem>();
    public static String [] categories={"Espectacles","Música","Cinema","Museu","Infantil","Esport","Exposició","Art","Ciència", "Oci", "Cultura"};
    public int day;
    public int month;
    public int year;

    protected Global() {
        Calendar now = Calendar.getInstance();
        year = now.get(Calendar.YEAR);
        month = now.get(Calendar.MONTH);
        day = now.get(Calendar.DAY_OF_MONTH);
    }

    public static synchronized Global getInstance() {
        if (null == instance) {
            instance = new Global();
        }
        return instance;
    }
}
