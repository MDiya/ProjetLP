package com.example.mdiya.projetlp;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


public class Accueil extends AppCompatActivity {

    public static ArrayList<Piscine> maListe;
    public static CustomAdapter customAdapter;
    SharedPreferences sharedPreferences;
    private final int MYFLAG = 1234;
    private PrefManager prefManager;
    //private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
//        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
//                AppDatabase.class, "database-name").build();
        maListe = new ArrayList<Piscine>();
        final ListView myList = (ListView) findViewById(R.id.mylist);

        customAdapter = new CustomAdapter(this);
        if(ConnexionInternet.isConnectedInternet(Accueil.this)){
            Ion.with(this)
                    .load( "https://data.nantesmetropole.fr/api/records/1.0/search/?dataset=244400404_piscines-nantes-metropole&rows=15")
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>(){
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            JsonArray records = result.getAsJsonArray("records");
                            Iterator<JsonElement> ite = records.iterator();
                            while(ite.hasNext()){
                                JsonObject item = ite.next().getAsJsonObject();
                                JsonObject fields = item.getAsJsonObject("fields");
                                String nom = fields.getAsJsonPrimitive("nom_usuel").getAsString();
                                String nomComplet = fields.getAsJsonPrimitive("nom_complet").getAsString();
                                String ville = fields.getAsJsonPrimitive("commune").getAsString();
                                int loisir =0, patauge=0,sport=0;
                                float lanote = (float) 0.0;
                                boolean visit, note;
                                String adr = "";
                                if (fields.has("adresse")){
                                    adr = adr +fields.getAsJsonPrimitive("adresse").getAsString() +", ";
                                }
                                else {
                                    adr+=("");
                                }
                                adr = adr + fields.getAsJsonPrimitive("commune").getAsString()+ ", ";
                                if (fields.has("cp")){
                                    adr = adr +fields.getAsJsonPrimitive("cp").getAsString();
                                }
                                if(fields.has("bassin_loisir") ){
                                    if (fields.getAsJsonPrimitive("bassin_loisir").getAsString().equals("OUI")){
                                        loisir = 1;
                                    }
                                    else {
                                        loisir = 0;
                                    }
                                }
                                else{
                                    loisir = -1;
                                }
                                if(fields.has("bassin_sportif")) {
                                    if (fields.getAsJsonPrimitive("bassin_sportif").getAsString().equals("OUI")) {
                                        sport = 1;
                                    } else {
                                        sport = 0;
                                    }
                                }
                                else{
                                    sport = -1;
                                }
                                if(fields.has("pataugeoire")){
                                    if(fields.getAsJsonPrimitive("pataugeoire").getAsString().equals("OUI")) {
                                        patauge = 1;
                                    }
                                    else {
                                        patauge = 0;
                                    }
                                }
                                else{
                                    patauge = -1;
                                }
                                int id = fields.getAsJsonPrimitive("idobj").getAsInt();
                                String myId = Integer.toString(id);
                                sharedPreferences = Accueil.this.getSharedPreferences(myId, Context.MODE_PRIVATE);
                                if(sharedPreferences.contains("sauv") && sharedPreferences.getBoolean("sauv", false)){
                                    visit = true;
                                }
                                else {
                                    visit = false;
                                }
                                if(sharedPreferences.contains("rate")){
                                    note = true;
                                    lanote = sharedPreferences.getFloat("rate",0);
                                }
                                else{
                                    note = false;
                                }
                                Piscine piscine = new Piscine(nom,nomComplet,ville,loisir,patauge,sport,visit,note, id, lanote, adr);
                                maListe.add(piscine);
                            }
                            customAdapter.notifyDataSetChanged();
                            myList.setAdapter(customAdapter);

                        }

                    });
        }

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Accueil.this, Details.class);
                Piscine piscine = (Piscine) customAdapter.getItem(position);
                int i = piscine.getId();
                String extraId = Integer.toString(i);
                intent.putExtra("id", extraId);
                startActivityForResult(intent, MYFLAG);
            }
        });

        ImageButton loisir = (ImageButton) findViewById(R.id.loisir);
        loisir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(maListe);
                customAdapter.notifyDataSetChanged();
            }
        });

        ImageButton star = (ImageButton) findViewById(R.id.sortstar);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(maListe, new Comparator<Piscine>() {
                    @Override
                    public int compare(Piscine o1, Piscine o2) {
                        return Float.floatToIntBits(o2.getNote() - o1.getNote());
                    }
                });
                customAdapter.notifyDataSetChanged();
            }
        });

        TextView sortvisiter = (TextView) findViewById(R.id.sortvisiter);
        sortvisiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(maListe, new Comparator<Piscine>() {
                    @Override
                    public int compare(Piscine o1, Piscine o2) {
                        int res =0;
                        if (o1.isVisiter()){
                            res--;
                        }
                        if (o2.isVisiter()){
                            res++;
                        }
                        return res;
                    }
                });
                customAdapter.notifyDataSetChanged();
            }
        });
        ImageButton baby = (ImageButton) findViewById(R.id.bebe);
        baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(maListe, new Comparator<Piscine>() {
                    @Override
                    public int compare(Piscine o1, Piscine o2) {
                        return o2.isPatauge()-o1.isPatauge();
                    }
                });
                customAdapter.notifyDataSetChanged();
            }
        });
        ImageButton sortsport = (ImageButton) findViewById(R.id.imgsport);
        sortsport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(maListe, new Comparator<Piscine>() {
                    @Override
                    public int compare(Piscine o1, Piscine o2) {
                        return o2.isSport()-o1.isSport();
                    }
                });
                customAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MYFLAG) {
            if (resultCode == RESULT_OK) {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent introduction = new Intent(Accueil.this, WelcomeActivity.class);
        prefManager = new PrefManager(getApplicationContext());
        prefManager.setFirstTimeLaunch(true);
        startActivity(introduction);
        return true;
    }
}
