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
        despreNoi.setText("text gai shdisgasyfd jhvvjav aaghcvaghc g ghc asghc saghc saghc ah egd aghc achv chgc hvc cv chdcg cacjc cvhjev we kevhejw e kuvdayhvayhvciy gda" +
                "dw ghcb cahkcvakhcvhvkhe" +
                "bhd ajgc jahcvhavcjhavckchva" +
                "bahabjchavhcyyueyfvcajhcacfaycv ahc " +
                "kjcvjhvcjhvcjhvccbhdcvewvfyhwf hb cdsh" +
                " cnbdhjvjhvbh ehjrvjehyw" +
                "nsdhjbjfyvewfbew" +
                "jdfhbjhr" +
                "jbfbjhrbfr" +
                "jbfjhrbfjvrbjhfbrjgbrhjgbkjrgbkg" +
                "mfh bhjrbvjhrbvhjfbfjhfbe" +
                "nj bjvhjrfhvrjhfbrfjbe" +
                "bhdsbbjhv" );
    }
}