package com.example.mdiya.projetlp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private DecimalFormat df = new DecimalFormat(".#");

    public CustomAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return Accueil.maListe.size();
    }

    @Override
    public Object getItem(int position) {
        return Accueil.maListe.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lv_item, null, true);


            holder.ville = (TextView) convertView.findViewById(R.id.ville);
            holder.nom = (TextView) convertView.findViewById(R.id.nom);
            holder.note = (TextView) convertView.findViewById(R.id.note);
            holder.loisir = (ImageView) convertView.findViewById(R.id.loisir);
            holder.patauge = (ImageView) convertView.findViewById(R.id.patauge);
            holder.sport = (ImageView) convertView.findViewById(R.id.sport);
            holder.visite = (ImageView) convertView.findViewById(R.id.visite);
            holder.star = (ImageView) convertView.findViewById(R.id.star);

            holder.distance = (TextView) convertView.findViewById(R.id.distance);

            convertView.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.ville.setText(Accueil.maListe.get(position).getVille());
        holder.nom.setText(Accueil.maListe.get(position).getNom());

        df.setRoundingMode(RoundingMode.DOWN);

        double distancePiscine = Accueil.maListe.get(position).getDist();
        Log.e("dist conv->", String.valueOf(distancePiscine * 10));
        holder.distance.setText(distancePiscine > 1.0 ? (df.format(distancePiscine) + " KM.") : df.format(distancePiscine * 1000) + " M.");

        float tmpNote = Accueil.maListe.get(position).getNote();
        if (tmpNote >= 0) {
            String tmpString = String.valueOf(Accueil.maListe.get(position).getNote());
            holder.note.setText(tmpString);
        } else {
            holder.note.setText("");
        }
        if (Accueil.maListe.get(position).isLoisir() == 1) {
            holder.loisir.setImageResource(R.drawable.ic_checked);
        } else if (Accueil.maListe.get(position).isLoisir() == 0) {
            holder.loisir.setImageResource(R.drawable.ic_cancel);
        } else {
            holder.loisir.setImageResource(R.drawable.ic_question);
        }
        if (Accueil.maListe.get(position).isPatauge() == 1) {
            holder.patauge.setImageResource(R.drawable.ic_checked);
        } else if (Accueil.maListe.get(position).isPatauge() == 0) {
            holder.patauge.setImageResource(R.drawable.ic_cancel);
        } else {
            holder.patauge.setImageResource(R.drawable.ic_question);
        }
        if (Accueil.maListe.get(position).isSport() == 1) {
            holder.sport.setImageResource(R.drawable.ic_checked);
        } else if (Accueil.maListe.get(position).isSport() == 0) {
            holder.sport.setImageResource(R.drawable.ic_cancel);
        } else {
            holder.sport.setImageResource(R.drawable.ic_question);
        }
        if (Accueil.maListe.get(position).isVisiter().equals("OUI")) {
            holder.visite.setImageResource(R.drawable.ic_checked);
        } else {
            holder.visite.setImageResource(R.drawable.ic_cancel);
        }
        if (Accueil.maListe.get(position).isNoter().equals("OUI")) {
            String tmpString = String.valueOf(Accueil.maListe.get(position).getNote());
            holder.note.setText(tmpString);
            holder.star.setImageResource(R.drawable.ic_starfull);
        }

        return convertView;
    }

    private class ViewHolder {
        private ImageView loisir, patauge, sport, visite, star;
        private TextView ville, nom, note, distance;
    }
}

