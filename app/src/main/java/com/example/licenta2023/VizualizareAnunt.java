package com.example.licenta2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.licenta2023.Entities.Anunt;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VizualizareAnunt extends AppCompatActivity {

    TextView Titlu, Descriere, Categorie, Pret, Email, Telefon, Locatie, DataAnunt;
    Button StergereAnunt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vizualizare_anunt);
        Titlu=findViewById(R.id.TitluBazaDeDate);
        Descriere=findViewById(R.id.DescriereBazaDeDate);
        Categorie=findViewById(R.id.CategorieBazaDeDate);
        Pret=findViewById(R.id.PretBazaDeDate);
        Email=findViewById(R.id.EmailBazaDeDate);
        Telefon=findViewById(R.id.telefonBazaDeDate);
        Locatie=findViewById(R.id.LocatieBazaDeDate);
        DataAnunt=findViewById(R.id.DataAnuntBazaDeDate);
        StergereAnunt=findViewById(R.id.StergereAnunt);

        Intent intent = this.getIntent();
        if(intent!=null){
            String titlu = intent.getStringExtra("TitluAnunt");
            String descriere = intent.getStringExtra("DescriereAnunt");
            String categorie = intent.getStringExtra("CategorieAnunt");
            String pret = intent.getStringExtra("PretAnunt");
            String email = intent.getStringExtra("EmailAnunt");
            String telefon = intent.getStringExtra("TelefonAnunt");
            String locatie = intent.getStringExtra("LocatieAnunt");
            String dataAnunt = intent.getStringExtra("DataAnunt");
            Titlu.setText(titlu);
            Descriere.setText(descriere);
            Categorie.setText(categorie);
            Pret.setText(pret);
            Email.setText(email);
            Telefon.setText(telefon);
            Locatie.setText(locatie);
            DataAnunt.setText(dataAnunt);

            FirebaseDatabase.getInstance().getReference("Anunturi").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshotAnunturi) {
                    for(DataSnapshot anuntSnapshot: snapshotAnunturi.getChildren()){
                        Anunt anunt = anuntSnapshot.getValue(Anunt.class);
                        if(anunt.getEmail().equals(email)){
                            StergereAnunt.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}