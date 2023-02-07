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

public class PrincipalPage extends AppCompatActivity {


    private DrawerLayout drawerLayout;
    private FirebaseDatabase firebaseDatabase;//decalara o referinta la baza de date
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private FirebaseUser user;
    TextView userName, userEmail;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_page);
        drawerLayout=findViewById(R.id.drawer_layout);
        mAuth=FirebaseAuth.getInstance();
        userName=findViewById(R.id.userFullName);
        userEmail=findViewById(R.id.userEmail);
        firebaseDatabase=FirebaseDatabase.getInstance();
        user=mAuth.getCurrentUser();
        reference=firebaseDatabase.getReference("Users");
        userId=user.getUid();
        reference.child(userId).addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(PrincipalPage.this, "Eroare la executie!", Toast.LENGTH_LONG).show();
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
        builder.setTitle("Sign out");
        builder.setMessage("Are you sure you want to logout ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                redirectActivity(PrincipalPage.this,LoginActivity.class);//redirectioneaza in LoginActivity
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
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

    public void ClickSignOut(View view){    logout(PrincipalPage.this);}
    public void ClickHome(View view){    closeDrawer(drawerLayout);}
}