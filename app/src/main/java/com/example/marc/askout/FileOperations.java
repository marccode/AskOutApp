package com.example.marc.askout;

/**
 * Created by albertvinespujadas on 4/26/15.
 */

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileOperations {                                                                       //classe que ens permet llegir i escriure puntuacions a un arxiu a la memoria SD

    Context context;
    String FILENAME;

    public FileOperations(Context context) {
        this.context = context;
        FILENAME = "token.txt";
    }

    public void writeToken(String s) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_APPEND);
            fos.write(s.getBytes());
            fos.close();
        }
        catch (IOException e)  {
            e.printStackTrace();
        }
    }

    public String readToken() {
        String s;
        FileInputStream fis;
        StringBuilder fileContent = new StringBuilder("");
        int n = 0;
        try {
            fis = context.openFileInput(FILENAME);
            byte[] buffer = new byte[1024];
            while ((n = fis.read(buffer)) != -1) {
                fileContent.append(new String(buffer, 0, n));
            }
        }
        catch (IOException e)  {
            e.printStackTrace();
        }
        s = fileContent.toString();
        if (!s.isEmpty()) {
            return s;
        }
        else return "0";
    }

    public ArrayList<String> getScores() {
        ArrayList<String> list = new ArrayList<String>(0);
        FileInputStream fis;
        int n;
        try {
            fis = context.openFileInput(FILENAME);
            byte[] buffer = new byte[1024];
            while ((n = fis.read(buffer)) != -1) {
                String s = new String(buffer, 0, n);
                list.add(s);
            }
        }
        catch (IOException e)  {
            e.printStackTrace();
        }
        return list;
    }

    public List<Integer> splitScores(ArrayList<String> scores) {                                    //dividim les puntuacions, separades per X
        List<Integer> l = new ArrayList<Integer>();
        String s = new String();
        if (scores.size() > 0) s = scores.get(0);
        String p = new String();
        for (int i = 0; i < s.length(); i++) {
            while (s.charAt(i) != 'X') {
                p = p + s.charAt(i);
                i++;
            }
            if (s.charAt(i) == 'X') {
                l.add( Integer.parseInt(p));
                p = "";
            }
        }
        return l;
    }
}
