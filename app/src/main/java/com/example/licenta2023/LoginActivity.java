package com.example.licenta2023;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button Conectare;
    EditText email, password;
    TextView Butic, Unic, Inregistrare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Inregistrare =findViewById(R.id.Inregistrare);
        Conectare =findViewById(R.id.Conectare);
        email =findViewById(R.id.emailEditText);
        email.setVisibility(email.INVISIBLE);
        password =findViewById(R.id.passwordEditText);
        password.setVisibility(password.INVISIBLE);
        Butic =findViewById(R.id.textViewButic);
        Unic =findViewById(R.id.textViewUnic);

        Inregistrare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }
}