package com.example.mdiya.projetlp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;


public class Accueil extends AppCompatActivity {

    public static ArrayList<Piscine> maListe;
    public static CustomAdapter customAdapter;
    SharedPreferences sharedPreferences;
    private final int MYFLAG = 1234;
    private PrefManager prefManager;
    ListView myList;
    TextView indispo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        maListe = new ArrayList<Piscine>();
        myList = (ListView) findViewById(R.id.mylist);
        indispo = (TextView) findViewById(R.id.indisponible);
        customAdapter = new CustomAdapter(this);

        this.getLocationService();

        if (ConnexionInternet.isConnectedInternet(Accueil.this)) {
            Ion.with(Accueil.this)
                    .load("https://data.nantesmetropole.fr/api/records/1.0/search/?dataset=244400404_piscines-nantes-metropole&rows=15")
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            JsonArray records = result.getAsJsonArray("records");
                            Iterator<JsonElement> ite = records.iterator();
                            while (ite.hasNext()) {

                                JsonObject item = ite.next().getAsJsonObject();
                                JsonObject fields = item.getAsJsonObject("fields");
                                String nom = fields.getAsJsonPrimitive("nom_usuel").getAsString();
                                String nomComplet = fields.getAsJsonPrimitive("nom_complet").getAsString();
                                String ville = fields.getAsJsonPrimitive("commune").getAsString();
                                int loisir = 0, patauge = 0, sport = 0;
                                float lanote = (float) 0.0;
                                String visit, note;
                                String adr = "";
                                if (fields.has("adresse")) {
                                    adr = adr + fields.getAsJsonPrimitive("adresse").getAsString() + ", ";
                                } else {
                                    adr += ("");
                                }
                                adr = adr + fields.getAsJsonPrimitive("commune").getAsString() + ", ";
                                if (fields.has("cp")) {
                                    adr = adr + fields.getAsJsonPrimitive("cp").getAsString();
                                }
                                if (fields.has("bassin_loisir")) {
                                    if (fields.getAsJsonPrimitive("bassin_loisir").getAsString().equals("OUI")) {
                                        loisir = 1;
                                    } else {
                                        loisir = 0;
                                    }
                                } else {
                                    loisir = -1;
                                }
                                if (fields.has("bassin_sportif")) {
                                    if (fields.getAsJsonPrimitive("bassin_sportif").getAsString().equals("OUI")) {
                                        sport = 1;
                                    } else {
                                        sport = 0;
                                    }
                                } else {
                                    sport = -1;
                                }
                                if (fields.has("pataugeoire")) {
                                    if (fields.getAsJsonPrimitive("pataugeoire").getAsString().equals("OUI")) {
                                        patauge = 1;
                                    } else {
                                        patauge = 0;
                                    }
                                } else {
                                    patauge = -1;
                                }
                                int id = fields.getAsJsonPrimitive("idobj").getAsInt();
                                String myId = Integer.toString(id);
                                sharedPreferences = Accueil.this.getSharedPreferences(myId, Context.MODE_PRIVATE);
                                if (sharedPreferences.contains("sauv") && sharedPreferences.getBoolean("sauv", false)) {
                                    visit = "OUI";
                                } else {
                                    visit = "NON";
                                }
                                if (sharedPreferences.contains("rate")) {
                                    note = "OUI";
                                    lanote = sharedPreferences.getFloat("rate", 0);
                                } else {
                                    note = "NON";
                                }

                                JsonArray location = null;
                                if(fields.has("location")){
                                    location = fields.getAsJsonArray("location");
                                }
                                double distance = distFrom(location.get(0).getAsDouble(), location.get(1).getAsDouble(), uLat, uLon);

                                Piscine piscine = new Piscine(nom, nomComplet, ville, loisir, patauge, sport, visit, note, id, lanote, adr, distance);
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
                        int res = 0;
                        if (o1.isVisiter().equals("OUI")) {
                            res--;
                        }
                        if (o2.isVisiter().equals("OUI")) {
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
                        return o2.isPatauge() - o1.isPatauge();
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
                        return o2.isSport() - o1.isSport();
                    }
                });
                customAdapter.notifyDataSetChanged();
            }
        });
        TextView distance = (TextView) findViewById(R.id.nomDelaPiscine);
        distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(maListe, new Comparator<Piscine>() {
                    @Override
                    public int compare(Piscine o1, Piscine o2) {
                        if (o1.getDist() < o2.getDist()) return -1;
                        if (o1.getDist() > o2.getDist()) return 1;
                        return 0;
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

    // Localitation
    private double uLat = 0.0;
    private double uLon = 0.0;

    public void getLocationService() {
        FusedLocationProviderClient providerClient = getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            // Gerer ca

            checkPermissions();
            return;
        }
        providerClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    uLat = location.getLatitude();
                    uLon = location.getLongitude();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("U/L -> ", e.getMessage());
            }
        });
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1234;

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
    }

    private double distFrom(double latP, double lngP, double latU, double lngU) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(latU - latP);
        double dLng = Math.toRadians(lngU - lngP);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(latP)) * Math.cos(Math.toRadians(latU)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
