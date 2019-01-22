package com.example.mdiya.projetlp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

public class Piscine implements Comparable {

    private String nom;
    private String nomComplet;
    private String ville;
    private int loisir;
    private int patauge;
    private int sport;
    private float note = -1;
    private String visiter;
    private String noter;
    private int id;
    private String adr;
    private double dist;

    public Piscine(String n, String nc, String v, int l,
                   int p, int s, String visiter, String noter,
                   int i, float lanote, String adr, double dist) {
        this.nom = n;
        this.nomComplet = nc;
        this.ville = v;
        this.loisir = l;
        this.patauge = p;
        this.sport = s;
        this.visiter = visiter;
        this.noter = noter;
        this.id = i;
        this.note = lanote;
        this.adr = adr;
        this.dist = dist;
    }

    public String getNom() {
        return nom;
    }

    public String getVille() {
        return ville;
    }

    public String getNomComplet() {
        return nomComplet;
    }

    public float getNote() {
        return note;
    }

    public int isLoisir() {
        return loisir;
    }

    public int isPatauge() {
        return patauge;
    }

    public int isSport() {
        return sport;
    }

    public String isVisiter() {
        return visiter;
    }

    public String isNoter() {
        return noter;
    }

    public int getId() {
        return id;
    }

    public String getAdr() {
        return adr;
    }

    @Override
    public String toString() {
        return ville + " " + nom;
    }

    @Override
    public int compareTo(Object o) {
        int compare = ((Piscine) o).isLoisir();
        return compare - this.isLoisir();
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setNomComplet(String nomComplet) {
        this.nomComplet = nomComplet;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public void setLoisir(int loisir) {
        this.loisir = loisir;
    }

    public void setPatauge(int patauge) {
        this.patauge = patauge;
    }

    public void setSport(int sport) {
        this.sport = sport;
    }

    public void setNote(float note) {
        this.note = note;
    }

    public void setVisiter(String visiter) {
        this.visiter = visiter;
    }

    public void setNoter(String noter) {
        this.noter = noter;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }
}
