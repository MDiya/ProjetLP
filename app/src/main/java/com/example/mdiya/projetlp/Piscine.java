package com.example.mdiya.projetlp;

class Piscine {
    private String nom,nomComplet,ville;
    private int loisir, patauge, sport;
    private float note = -1;
    private boolean  visiter, noter;
    private int id;

    public Piscine(String n, String nc,String v, int l, int p, int s, boolean visiter, boolean noter, int i, float lanote){
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
}
