package com.example.mdiya.projetlp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
//@Entity(tableName = "piscine_table")
class Piscine implements Comparable{
  //  @NonNull
    //@ColumnInfo(name = "nom")
    private String nom;
    //@NonNull
    //@ColumnInfo(name = "nomComplet")
    private  String nomComplet;
    //@NonNull
    //@ColumnInfo(name = "ville")
    private  String ville;
    //@NonNull
    //@ColumnInfo(name = "loisir")
    private int loisir;
    //@NonNull
    //@ColumnInfo(name = "pautage")
    private int patauge;
    //@NonNull
    //@ColumnInfo(name = "sport")
    private int sport;
    //@NonNull
    //@ColumnInfo(name = "note")
    private float note = -1;
    //@NonNull
    //@ColumnInfo(name = "visiter")
    private boolean visiter;
    //@NonNull
    //@ColumnInfo(name = "noter")
    private boolean noter;
    //@PrimaryKey
    //@NonNull
    //@ColumnInfo(name = "id")
    private int id;

    public Piscine( String n,  String nc,  String v,  int l,
                    int p, int s, boolean visiter, boolean noter,
                    int i, float lanote){
        this.nom =n;
        this.nomComplet = nc;
        this.ville = v;
        this.loisir = l;
        this.patauge = p;
        this.sport =s;
        this.visiter = visiter;
        this.noter = noter;
        this.id =i;
        this.note = lanote;
    }

    public String getNom(){return nom;}

    public String getVille(){return ville;}

    public String getNomComplet(){return nomComplet;}

    public float getNote(){return note;}

    public int isLoisir() {
        return loisir;
    }

    public int isPatauge() {
        return patauge;
    }

    public int isSport() {
        return sport;
    }

    public boolean isVisiter() {
        return visiter;
    }

    public boolean isNoter() {
        return noter;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return ville+" "+nom;
    }

    @Override
    public int compareTo(Object o) {
        int compare = ((Piscine)o).isLoisir();
        return compare- this.isLoisir();
    }
}
