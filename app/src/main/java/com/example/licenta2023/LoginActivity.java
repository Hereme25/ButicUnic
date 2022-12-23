package com.example.licenta2023;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button Inregistrare, Conectare;
    EditText email, password;
    TextView Butic, Unic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Inregistrare = (Button) findViewById(R.id.Inregistrare);
        Conectare = (Button) findViewById(R.id.Conectare);
        email = (EditText) findViewById(R.id.emailEditText);
        email.setVisibility(email.INVISIBLE);
        password = (EditText) findViewById(R.id.passwordEditText);
        password.setVisibility(password.INVISIBLE);
        Butic = (TextView) findViewById(R.id.textViewButic);
        Unic = (TextView) findViewById(R.id.textViewUnic);

        Inregistrare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conectare.setVisibility(Conectare.INVISIBLE);
                Butic.setVisibility(Butic.INVISIBLE);
                Unic.setVisibility(Unic.INVISIBLE);
                email.setVisibility(email.VISIBLE);
                password.setVisibility(password.VISIBLE);
                if (email.getText().toString().equals("admin") &&
                        password.getText().toString().equals("admin"))
                    Toast.makeText(getBaseContext(), "Login Successful !", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getBaseContext(), "Login Failed !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}