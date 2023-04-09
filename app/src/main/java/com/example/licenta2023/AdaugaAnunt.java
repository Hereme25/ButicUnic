package com.example.licenta2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta2023.Entities.Anunt;
import com.example.licenta2023.Entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class AdaugaAnunt extends AppCompatActivity {

    EditText titlu, descriere, pret, categorie;
    ArrayList<String> categorii = new ArrayList<>(Arrays.asList("Blugi", "Geci", "Rochii","Bluze"));
    TextView localizare, email, nrtelefon;
    ImageView imagineProdus;
    Dialog dialog;
    private FirebaseDatabase firebaseDatabase;//decalara o referinta la baza de date
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private FirebaseUser user;
    String proprietarNume;
    String userId;
    Button adaugaAnunt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga_anunt);
        titlu=findViewById(R.id.TitlulBox);
        descriere=findViewById(R.id.DescriereBox);
        pret=findViewById(R.id.PretBox);
        categorie=findViewById(R.id.CategoriaBox);
        localizare=findViewById(R.id.LocalizareBox);
        email=findViewById(R.id.MailBox);
        nrtelefon=findViewById(R.id.TelefonBox);
        adaugaAnunt=findViewById(R.id.AdaugaAnuntButton);

        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        user=mAuth.getCurrentUser();
        reference=firebaseDatabase.getReference("Users");
        userId=user.getUid();
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User profil=snapshot.getValue(User.class);
                if(profil != null){
                    String localizareText = profil.getOras() + " / " + profil.getJudet();
                    String emailText = profil.getEmail();
                    String nrTelefonText = profil.getTelefon();
                    localizare.setText(localizareText);
                    email.setText(emailText);
                    nrtelefon.setText(nrTelefonText);
                    proprietarNume=profil.getNume()+" "+profil.getPrenume();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdaugaAnunt.this, "Eroare la executie!", Toast.LENGTH_LONG).show();
            }
        });

        adaugaAnunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titluText=titlu.getText().toString().trim();
                String categorieText=categorie.getText().toString().trim();
                String descriereText=descriere.getText().toString().trim();
                Double valoarePret=Double.parseDouble(pret.getText().toString().trim());
                String telefonText=nrtelefon.getText().toString().trim();
                String emailText=email.getText().toString().trim();
                String localizareText=localizare.getText().toString().trim();
                Anunt anunt = new Anunt(titluText, proprietarNume, categorieText, emailText, valoarePret, descriereText, localizareText, telefonText);
                firebaseDatabase.getReference("Anunturi").child(UUID.randomUUID().toString()).setValue(anunt);
                Toast.makeText(AdaugaAnunt.this, "Anunț adăugat cu succes!", Toast.LENGTH_LONG).show();
                Intent i= new Intent(AdaugaAnunt.this, PrincipalPage.class);
                startActivity(i);
                finish();
            }
        });

        categorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(AdaugaAnunt.this);
                dialog.setContentView(R.layout.dialog_search_spiner_category);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                EditText editText = dialog.findViewById(R.id.category_edit_text);
                ListView listView = dialog.findViewById(R.id.searchCategoryListView);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AdaugaAnunt.this, android.R.layout.simple_list_item_1,categorii);
                listView.setAdapter(adapter);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        adapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        categorie.setText(adapter.getItem(position));
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}