package com.example.mdiya.projetlp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.sip.SipSession;
import android.nfc.Tag;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class Details extends AppCompatActivity {

    private PrefManager prefManager;
    private String Tag ="123123123";
    private  FloatingActionButton fab1,fab2,fab3,fab;
    boolean isFABOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
//        LocationManager locationManager = (LocationManager) getSystemService(Details.this.LOCATION_SERVICE);
//        LocationListener locationListener = new MyLocationListener();
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        final TextView nom = (TextView) findViewById(R.id.nomEntier);
        final TextView nom_usuel = (TextView) findViewById(R.id.nomUsuel);
        final TextView addr = (TextView) findViewById(R.id.addr);
        final TextView commune = (TextView) findViewById(R.id.commune);
        final TextView cp = (TextView) findViewById(R.id.codepostal);
        final TextView tel = (TextView) findViewById(R.id.tel);
        final TextView info = (TextView) findViewById(R.id.complements);
        final TextView site = (TextView) findViewById(R.id.site);
        final ImageView isLibreService = (ImageView) findViewById(R.id.isLibreService);
        final ImageView isSportif = (ImageView) findViewById(R.id.isSportif);
        final ImageView isLoisir = (ImageView) findViewById(R.id.isBassinLoisir);
        final ImageView isApprenti = (ImageView) findViewById(R.id.isApprentissage);
        final ImageView isPlongeoir = (ImageView) findViewById(R.id.isPlongeoir);
        final ImageView isToboggan = (ImageView) findViewById(R.id.isToboggan);
        final ImageView isPatauge = (ImageView) findViewById(R.id.isPataugeoire);
        final ImageView isHandicap = (ImageView) findViewById(R.id.isHandicap);
        final TextView payement = (TextView) findViewById(R.id.payement);

        final TextView score = (TextView) findViewById(R.id.score);
        final RatingBar maSeekBar = (RatingBar) findViewById(R.id.mySeekBar);
        Button valider = (Button) findViewById(R.id.valider);
        final RadioButton oui = (RadioButton) findViewById(R.id.oui);
        final RadioButton non = (RadioButton) findViewById(R.id.non);

        final String[] location = {""};
        final String[] tmpString = {""};
        final String[] lasemaine = new String[]{"lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi", "dimanche"};
        final HashMap<String,ArrayList<String>> lesJours = new HashMap<>();
        lesJours.put("lundi", new ArrayList<String>());
        lesJours.put("mardi", new ArrayList<String>());
        lesJours.put("mercredi", new ArrayList<String>());
        lesJours.put("jeudi", new ArrayList<String>());
        lesJours.put("vendredi", new ArrayList<String>());
        lesJours.put("samedi", new ArrayList<String>());
        lesJours.put("dimanche", new ArrayList<String>());



        maSeekBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tmpString[0] = Float.toString(rating);
                score.setText(tmpString[0] + " étoiles");
            }
        });
        Intent intent = getIntent();
        String pref = intent.getStringExtra("id");
        final SharedPreferences sharedPreferences = getBaseContext().getSharedPreferences(pref, MODE_PRIVATE);

        String url = "https://data.nantesmetropole.fr/api/records/1.0/search/?dataset=244400404_piscines-nantes-metropole&q=idobj%3D";
        url = url + intent.getStringExtra("id");
        if (ConnexionInternet.isConnectedInternet(Details.this)){
            Ion.with(this)
                    .load( url)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>(){
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            JsonArray records = result.getAsJsonArray("records");
                            Iterator<JsonElement> ite = records.iterator();
                            while(ite.hasNext()){
                                JsonObject item = ite.next().getAsJsonObject();
                                JsonObject fields = item.getAsJsonObject("fields");
                                nom_usuel.setText(fields.getAsJsonPrimitive("nom_usuel").getAsString());
                                nom.setText(fields.getAsJsonPrimitive("nom_complet").getAsString());
                                commune.setText(fields.getAsJsonPrimitive("commune").getAsString());
                                if (fields.has("adresse")){
                                    addr.setText(fields.getAsJsonPrimitive("adresse").getAsString());
                                }
                                else {
                                    addr.setHeight(0);
                                    addr.setText("");
                                }
                                if (fields.has("cp")){
                                    cp.setText(fields.getAsJsonPrimitive("cp").getAsString());
                                }
                                else {
                                    cp.setHeight(0);
                                    cp.setText("");
                                }
                                if (fields.has("tel")){
                                    tel.setText(fields.getAsJsonPrimitive("tel").getAsString());
                                }
                                else {
                                    tel.setHeight(0);
                                    tel.setText("");
                                }
                                if (fields.has("infos_complementaires")){
                                    info.setText(fields.getAsJsonPrimitive("infos_complementaires").getAsString());
                                }
                                else {
                                    info.setHeight(0);
                                }
                                if (fields.has("web")){
                                    site.setText(fields.getAsJsonPrimitive("web").getAsString());
                                }
                                else {
                                    site.setHeight(0);
                                    site.setText("");
                                }
                                if(fields.has("location")){
                                    location[0] = fields.getAsJsonArray("location").toString();
                                }
                                if(fields.has("libre_service")){
                                    if(fields.getAsJsonPrimitive("libre_service").getAsString().equals("OUI")){
                                        isLibreService.setImageResource(R.drawable.ic_checked);
                                    }
                                    else {
                                        isLibreService.setImageResource(R.drawable.ic_cancel);
                                    }
                                }
                                else {
                                    isLibreService.setImageResource(R.drawable.ic_question);
                                }

                                if(fields.has("bassin_sportif")){
                                    if(fields.getAsJsonPrimitive("bassin_sportif").getAsString().equals("OUI")){
                                        isSportif.setImageResource(R.drawable.ic_checked);
                                    }
                                    else {
                                        isSportif.setImageResource(R.drawable.ic_cancel);
                                    }
                                }
                                else {
                                    isSportif.setImageResource(R.drawable.ic_question);
                                }

                                if(fields.has("bassin_loisir")){
                                    if(fields.getAsJsonPrimitive("bassin_loisir").getAsString().equals("OUI")){
                                        isLoisir.setImageResource(R.drawable.ic_checked);
                                    }
                                    else {
                                        isLoisir.setImageResource(R.drawable.ic_cancel);
                                    }
                                }
                                else {
                                    isLoisir.setImageResource(R.drawable.ic_question);
                                }

                                if(fields.has("bassin_apprentissage")){
                                    if(fields.getAsJsonPrimitive("bassin_apprentissage").getAsString().equals("OUI")){
                                        isApprenti.setImageResource(R.drawable.ic_checked);
                                    }
                                    else {
                                        isApprenti.setImageResource(R.drawable.ic_cancel);
                                    }
                                }
                                else {
                                    isApprenti.setImageResource(R.drawable.ic_question);
                                }

                                if(fields.has("plongeoir")){
                                    if(fields.getAsJsonPrimitive("plongeoir").getAsString().equals("OUI")){
                                        isPlongeoir.setImageResource(R.drawable.ic_checked);
                                    }
                                    else {
                                        isPlongeoir.setImageResource(R.drawable.ic_cancel);
                                    }
                                }
                                else {
                                    isPlongeoir.setImageResource(R.drawable.ic_question);
                                }

                                if(fields.has("toboggan")){
                                    if(fields.getAsJsonPrimitive("toboggan").getAsString().equals("OUI")){
                                        isToboggan.setImageResource(R.drawable.ic_checked);
                                    }
                                    else {
                                        isToboggan.setImageResource(R.drawable.ic_cancel);
                                    }
                                }
                                else {
                                    isToboggan.setImageResource(R.drawable.ic_question);
                                }

                                if(fields.has("pataugeoire")){
                                    if(fields.getAsJsonPrimitive("pataugeoire").getAsString().equals("OUI")){
                                        isPatauge.setImageResource(R.drawable.ic_checked);
                                    }
                                    else {
                                        isPatauge.setImageResource(R.drawable.ic_cancel);
                                    }
                                }
                                else {
                                    isPatauge.setImageResource(R.drawable.ic_question);
                                }

                                if(fields.has("accessibilite_handicap")){
                                    if(fields.getAsJsonPrimitive("accessibilite_handicap").getAsString().equals("OUI")){
                                        isHandicap.setImageResource(R.drawable.ic_checked);
                                    }
                                    else {
                                        isHandicap.setImageResource(R.drawable.ic_cancel);
                                    }
                                }
                                else {
                                    isHandicap.setImageResource(R.drawable.ic_question);
                                }
                                if(fields.has("moyen_paiement")){
                                    payement.setText(fields.getAsJsonPrimitive("moyen_paiement").getAsString());
                                }
                                else {
                                    payement.setHeight(0);
                                }

                            }
                            if (sharedPreferences.contains("sauv")){
                                oui.setChecked(sharedPreferences.getBoolean("sauv",false));
                                non.setChecked(!sharedPreferences.getBoolean("sauv",false));
                            }
                            else {
                                oui.setChecked(false);
                                non.setChecked(true);
                            }
                            if(sharedPreferences.contains("rate")){
                                maSeekBar.setRating(sharedPreferences.getFloat("rate",0));
                            }
                        }

                    });
        }
        View.OnClickListener mytel = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tel.getText().equals("") ){
                    String tmp = "tel:"+tel.getText().toString().replace(" ", "");
                    Uri number = Uri.parse(tmp);
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    if (ActivityCompat.checkSelfPermission(Details.this,
                            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(Details.this,
                                "Vous ne pouvez pas appeler avec cet appareil",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startActivity(callIntent);
                }
            }
        };
        View.OnClickListener myweb = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!site.getText().equals("")){
                    Uri webpage = Uri.parse(site.getText().toString());
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    startActivity(webIntent);
                }
            }
        };

        View.OnClickListener gps = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!location.toString().equals("")){
                    location[0] = location[0].replace("[", "");
                    location[0] = location[0].replace("]","");
                    String locationtmp = "geo:"+location[0]+"?z=18";
                    Uri location = Uri.parse(locationtmp);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                    startActivity(mapIntent);
                }
            }
        };
        site.setOnClickListener(myweb);
        tel.setOnClickListener(mytel);

        addr.setOnClickListener(gps);
        commune.setOnClickListener(gps);
        cp.setOnClickListener(gps);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oui.isChecked()){
                    sharedPreferences.edit().putBoolean("sauv",true).apply();
                }
                else{
                    sharedPreferences.edit().putBoolean("sauv",false).apply();
                }
                sharedPreferences.edit().putFloat("rate",maSeekBar.getRating()).apply();
                startActivity(new Intent(Details.this,Accueil.class));
                overridePendingTransition(0, 0);
                setResult(1234);
                finish();
                overridePendingTransition(0, 0);
            }
        });

        final TextView horaires = (TextView) findViewById(R.id.horaire);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar_debut = new GregorianCalendar();
        String debut = sdf.format(calendar_debut.getTime());

        final Calendar calendar_fin = new GregorianCalendar();
        calendar_fin.add(Calendar.DATE, 6);
        String fin = sdf.format(calendar_fin.getTime());

        String urlHoraires = "https://data.nantesmetropole.fr/api/records/1.0/search/?dataset=244400404_piscines-nantes-metropole-horaires&q=date_fin+%3E+"
                +debut+"+idobj%3D"+intent.getStringExtra("id")+"+date_debut%3C"+fin+"&rows=30";
        final ArrayList<String> lesHoraires = new ArrayList<String>();
        if (ConnexionInternet.isConnectedInternet(Details.this)){
            Ion.with(this)
                    .load( urlHoraires)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            JsonArray records = result.getAsJsonArray("records");
                            Iterator<JsonElement> ite = records.iterator();
                            while (ite.hasNext()) {
                                String ajout = "";
                                JsonObject item = ite.next().getAsJsonObject();
                                JsonObject fields = item.getAsJsonObject("fields");
                                if (fields.has("jour") && lesJours.containsKey(fields.getAsJsonPrimitive("jour").getAsString())){
                                    if(fields.has("heure_debut")){
                                        ajout = ajout + fields.getAsJsonPrimitive("heure_debut").getAsString() +" à ";
                                    }
                                    if(fields.has("heure_fin")){
                                        ajout = ajout + fields.getAsJsonPrimitive("heure_fin").getAsString() +" ";
                                    }
                                    if(!ajout.equals("")){
                                        lesJours.get(fields.getAsJsonPrimitive("jour").getAsString()).add(ajout);
                                    }
                                }
                                lesHoraires.add(ajout);
                            }
                            StringBuilder finalHoraire = new StringBuilder();
                            boolean vide = true;
                            for (int i = 0; i<7;i++){
                                finalHoraire.append(lasemaine[i]).append(" ");
                                if(lesJours.containsKey(lasemaine[i]) && !Objects.requireNonNull(lesJours.get(lasemaine[i])).isEmpty()){
                                    vide = false;
                                    Collections.sort(lesJours.get(lasemaine[i]), new Comparator<String>() {
                                        public int compare(String o1, String o2) {
                                            return extractInt(o1) -extractInt(o2);
                                        }

                                        int extractInt(String s) {
                                            String num = s.replaceAll("\\D", "");
                                            // return 0 if no digits found
                                            return num.isEmpty() ? 0 : Integer.parseInt(num);
                                        }
                                    });
                                    for (String s : Objects.requireNonNull(lesJours.get(lasemaine[i]))) {
                                        finalHoraire.append(s).append(" ");
                                    }

                                }
                                else finalHoraire.append("fermé");
                                finalHoraire.append("\n");
                            }

                            if (vide){
                                horaires.setText("Horaires indisponibles");
                            }
                            else horaires.setText(finalHoraire.toString());
                        }
                    });
        }

         fab = (FloatingActionButton) findViewById(R.id.fab);
         fab1 = (FloatingActionButton) findViewById(R.id.fab1);
         fab2 = (FloatingActionButton) findViewById(R.id.fab2);
         fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
        fab1.setOnClickListener(myweb);
        fab2.setOnClickListener(gps);
        fab3.setOnClickListener(mytel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent introduction = new Intent(Details.this, WelcomeActivity.class);
        prefManager = new PrefManager(getApplicationContext());
        prefManager.setFirstTimeLaunch(true);
        startActivity(introduction);
        return true;
    }
    private void showFABMenu(){
        isFABOpen=true;
        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
        fab.animate().rotation(90);
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);
        fab.animate().rotation(0);
    }

    @Override
    public void onBackPressed() {
        if(isFABOpen){
            closeFABMenu();
        }
        else {
            finish();
        }
    }

    private double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return   earthRadius * c;
    }
}
