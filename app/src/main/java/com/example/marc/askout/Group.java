package com.example.marc.askout;

/**
 * Created by albertvinespujadas on 5/21/15.
*/
import java.util.ArrayList;
import java.util.List;

public class Group {

    public String string;
    public final List<InfoSingleEvent> children = new ArrayList<InfoSingleEvent>();

    public Group(String string) {
        this.string = string;
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

}