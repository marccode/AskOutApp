package com.example.marc.askout;

import android.graphics.drawable.Drawable;

/**
 * Created by marc on 5/7/15.
 */
public class ListViewItem {
    public final String id;
    public final String data_inici;
    public final String data_final;
    public final String nom;
    public final String nomLloc;
    public final String carrer;
    public final String numero;
    public final String districte;
    public final String municipi;
    public final String categories;
    public final Drawable icon;       // the drawable for the ListView item ImageView
    public int position;  // for the SearchFragment


    public ListViewItem(String id, String data_inici, String data_final, String nom, String nomLloc, String carrer, String numero, String districte, String municipi, String categories, Drawable icon) {
        this.id = id;
        this.data_inici = data_inici;
        this.data_final = data_final;
        this.nom = nom;
        this.nomLloc = nomLloc;
        this. carrer = carrer;
        this.numero = numero;
        this.districte = districte;
        this.municipi = municipi;
        this.categories = categories;
        this.icon = icon;
    }
}