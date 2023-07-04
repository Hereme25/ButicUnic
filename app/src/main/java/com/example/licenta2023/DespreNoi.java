package com.example.licenta2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta2023.Entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DespreNoi extends AppCompatActivity {

    TextView despreNoi;
    private DrawerLayout drawerLayout;
    TextView userName, userEmail;
    String userId;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;//declara o referinta la baza de date
    private DatabaseReference userReference;//referinta la tabelul de user din baza de date
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despre_noi);
        despreNoi=findViewById(R.id.desprenoitext);
        despreNoi.setText("Tu ce faci cu hainele pe care nu le mai porti?\n" +
                "Da o noua viata articolelor vestimentare pe care nu le mai porti si descopera articole pre-loved din comunitate. Reinnoieste-ti garderoba simplu si economic - descarca Butic Unic!\n" +
                "Achizitioneaza articole premium la preturi avantajoase intr-un mod simplu. Descopera cele mai tari piese de la alti iubitori de moda vintage si pre-loved. Reinventeaza-ti stilul — on a budget!\n" +
                "Ne vedem si pe Instagram");
        drawerLayout=findViewById(R.id.drawer_layout);
        userName=findViewById(R.id.userFullName);
        userEmail=findViewById(R.id.userEmail);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        user=mAuth.getCurrentUser();
        userReference=firebaseDatabase.getReference("Users");
        userId=user.getUid();

        userReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User profil=snapshot.getValue(User.class);
                if(profil != null){
                    String fullName = profil.getNume() + " " + profil.getPrenume();
                    String email = profil.getEmail();
                    userName.setText(fullName);
                    userEmail.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DespreNoi.this, "Eroare la executie!", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void closeDrawer(DrawerLayout drawerLayout)
    {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void logout(Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Delogare");
        builder.setMessage("Ești sigur ca vrei să te deloghezi?");
        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                redirectActivity(DespreNoi.this,MainActivity.class);//redirectioneaza in MainActivity
            }
        });

        builder.setNegativeButton("Nu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity,aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
    public void ClickMenu(View view)
    {
        openDrawer(drawerLayout);
    }

    public void openDrawer(DrawerLayout drawerLayout)
    {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickSignOut(View view)
    {
        logout(DespreNoi.this);
    }
    public void ClickAdd(View view)
    {

        redirectActivity(DespreNoi.this,AdaugaAnunt.class);
    }
    public void ClickHome(View view)
    {
        redirectActivity(DespreNoi.this,PrincipalPage.class);
    }
    public void ClickDespreNoi(View view)
    {
        closeDrawer(drawerLayout);
    }
    public void ClickAnunturi(View view)
    {
        redirectActivity(DespreNoi.this,AnunturiActivity.class);
    }
    public void ClickAnunturileMele(View view)
    {
        redirectActivity(DespreNoi.this, AnunturileMele.class);
    }
    public void ClickLogo(View view)
    {
        closeDrawer(drawerLayout);
    }
}