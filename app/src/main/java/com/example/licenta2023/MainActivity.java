package com.example.licenta2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    Button Conectare;
    TextView  Register;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Conectare = findViewById(R.id.Conectare);
            Register = findViewById(R.id.linkregister);

            Conectare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent j= new Intent(MainActivity.this, LoginActivity.class);
                }
            });
            Register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i= new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(i);
                }
            });
        }
    }