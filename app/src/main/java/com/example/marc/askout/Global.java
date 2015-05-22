package com.example.marc.askout;

import java.util.List;

/**
 * Created by marc on 5/22/15.
 */
public class Global {
    private static Global instance = null;

    public boolean[] interests;
    public List<ListViewItem> mItems = null;

    protected Global() {
    }

    public static synchronized Global getInstance() {
        if (null == instance) {
            instance = new Global();
        }
        return instance;
    }
}
