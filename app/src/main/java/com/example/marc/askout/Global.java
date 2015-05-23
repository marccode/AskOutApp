package com.example.marc.askout;

import java.util.List;

/**
 * Created by marc on 5/22/15.
 */
public class Global {
    private static Global instance = null;

    public boolean[] interests = null;
    public List<ListViewItem> mItems = null;
    public List<ListViewItem> mItemsSaved = null;

    protected Global() {
    }

    public static synchronized Global getInstance() {
        if (null == instance) {
            instance = new Global();
        }
        return instance;
    }
}