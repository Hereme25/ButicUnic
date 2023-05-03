package com.example.licenta2023.Adaptors;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.licenta2023.Entities.Anunt;
import com.example.licenta2023.R;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class AnuntAdaptor extends ArrayAdapter<Anunt> {

    public AnuntAdaptor(Context context, ArrayList<Anunt> anunturi){
        super(context, 0, anunturi);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Anunt anunt=getItem(position);
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.anunt_view,parent,false);
        }
        TextView titlu= (TextView) convertView.findViewById(R.id.anunt_titlu);
        TextView locatie= (TextView) convertView.findViewById(R.id.anunt_locatie);
        TextView data= (TextView) convertView.findViewById(R.id.anunt_data);
        TextView pret= (TextView) convertView.findViewById(R.id.anunt_pret);
        ImageView imagineAnunt=(ImageView) convertView.findViewById(R.id.anunt_imagine);
        Picasso.with(getContext()).load(anunt.getImagineUri()).into(imagineAnunt);

        titlu.setText(anunt.getTitlu());
        locatie.setText(anunt.getLocalizare());
        data.setText(anunt.getDataAnunt());
        pret.setText(anunt.getPret().toString());

        return convertView;
    }


}
