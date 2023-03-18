package com.example.licenta2023;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DespreNoi extends AppCompatActivity {

    TextView despreNoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despre_noi);
        despreNoi=findViewById(R.id.desprenoitext);
        despreNoi.setText("Tu ce faci cu hainele pe care nu le mai porti?\n" +
                "Da o noua viata articolelor vestimentare pe care nu le mai porti si descopera articole pre-loved din comunitate. Reinnoieste-ti garderoba simplu si economic - descarca Butic Unic!" +
                "Achizitioneaza articole premium la preturi avantajoase intr-un mod simplu. Descopera cele mai tari piese de la alti iubitori de moda vintage si pre-loved. Reinventeaza-ti stilul — on a budget!" +
                "Ne vedem si pe Instagram");
    }
}