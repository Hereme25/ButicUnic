package com.example.licenta2023.Adaptors;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.licenta2023.Entities.Anunt;
import com.example.licenta2023.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class AnuntAdaptor extends ArrayAdapter<Anunt> {

    public AnuntAdaptor(Context context, ArrayList<Anunt> anunturi){
        super(context, 0, anunturi);
    } //clasa ajutataoare pentru maparea unor date din listview
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Anunt anunt=getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.anunt_view,parent,false);
            //informatiile se adapteaza la interfata
        }
        TextView titlu= (TextView) convertView.findViewById(R.id.anunt_titlu);
        TextView locatie= (TextView) convertView.findViewById(R.id.anunt_locatie);
        TextView data= (TextView) convertView.findViewById(R.id.anunt_data);
        TextView pret= (TextView) convertView.findViewById(R.id.anunt_pret);
        ImageView imagineAnunt=(ImageView) convertView.findViewById(R.id.anunt_imagine);


        titlu.setText(anunt.getTitlu());
        locatie.setText(anunt.getLocalizare());
        data.setText(anunt.getDataAnunt());
        pret.setText(anunt.getPret().toString());
        String imagineAnuntUrl = anunt.getImagineUri();
        //Picasso.with(this.getContext()).load(anunt.getImagineUri()).fit().centerCrop().into(imagineAnunt);
        //Glide.with(this.getContext()).load(imagineAnuntUrl).into(imagineAnunt);
        Glide.with(getContext().getApplicationContext()).load(imagineAnuntUrl).into(imagineAnunt);


        return convertView;
    }
}
