package com.example.licenta2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    Button Conectare;
    TextView  Register, Butic_Unic;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Conectare = findViewById(R.id.Conectare);
            Register = findViewById(R.id.linkregister);
            Butic_Unic = findViewById(R.id.instagramlink);


            Conectare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i= new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
            });
            Register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i= new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(i);
                }
            });

            Butic_Unic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openInstagram();
                }
            });

        }
    public void openInstagram() {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/butic_unic/?igshid=ZDdkNTZiNTM%3D"));
        startActivity(webIntent);
    }
}