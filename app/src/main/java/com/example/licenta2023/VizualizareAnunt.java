package com.example.licenta2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.licenta2023.Entities.Anunt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VizualizareAnunt extends AppCompatActivity {

    TextView Titlu, Descriere, Categorie, Pret, Email, Telefon, Locatie, DataAnunt;
    Button StergereAnunt;
    FirebaseUser user;
    String emailUser;
    FirebaseAuth auth;
    static int Permission = 100;

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


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        emailUser = user.getEmail();

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
            String pozaUrl = intent.getStringExtra("LinkPoza");
            Titlu.setText(titlu);
            Descriere.setText(descriere);
            Categorie.setText(categorie);
            Pret.setText(pret);
            Email.setText(email);
            Telefon.setText(telefon);
            Locatie.setText(locatie);
            DataAnunt.setText(dataAnunt);



            Telefon.setOnClickListener(v -> {
                if(ContextCompat.checkSelfPermission(VizualizareAnunt.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

                    ActivityCompat.requestPermissions(VizualizareAnunt.this, new String[]{Manifest.permission.CALL_PHONE},Permission);

                }
                Intent i = new Intent(Intent.ACTION_DIAL,Uri.fromParts("tel",telefon,null));
                startActivity(i);
            });
            FirebaseDatabase.getInstance().getReference("Anunturi").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshotAnunturi) {
                    for(DataSnapshot anuntSnapshot: snapshotAnunturi.getChildren()){
                        Anunt anunt = anuntSnapshot.getValue(Anunt.class);
                        if (anunt.getUid().equals(user.getUid())) {
                            StergereAnunt.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            StergereAnunt.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(VizualizareAnunt.this);
                builder.setTitle("Ștergere anunț!");
                builder.setMessage("Ești sigur că vrei să ștergi anunțul?");
                builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference("Anunturi").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot anuntSnapshot:snapshot.getChildren()){
                                    Anunt anuntCurent = anuntSnapshot.getValue(Anunt.class);
                                    if(anuntCurent.getTitlu().equals(titlu)){
                                        anuntSnapshot.getRef().removeValue();
                                        Intent i = new Intent(VizualizareAnunt.this, AnunturileMele.class);
                                        startActivity(i);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                builder.setNegativeButton("Nu", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            });
        }
    }
}