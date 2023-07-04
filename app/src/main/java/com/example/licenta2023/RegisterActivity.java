package com.example.licenta2023;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta2023.Entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity {

    EditText numeinregistrare, prenumeinregistrare, emailinregistrare, parolainregistrare, telefoninregistrare, orasinregistrare, judetinregistrare;
    Button registerbutton;
    TextView Butic_Unic;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    ArrayList<String> orase = new ArrayList<>(Arrays.asList("Pitesti", "Bucuresti", "Iasi","Timisoara","Sulina"));
    ArrayList<String> judete = new ArrayList<>(Arrays.asList("Arges","Tulcea","Olt","Ilfov","Timis"));
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        numeinregistrare = findViewById(R.id.numeinregistrare);
        parolainregistrare = findViewById(R.id.parolainregistrare);
        prenumeinregistrare = findViewById(R.id.prenumeinregistrare);
        emailinregistrare = findViewById(R.id.emailinregistrare);
        telefoninregistrare = findViewById(R.id.telefoninregistrare);
        orasinregistrare = findViewById(R.id.orasinregistrare);
        judetinregistrare = findViewById(R.id.judetinregistrare);
        registerbutton = findViewById(R.id.registerbutton);
        Butic_Unic = findViewById(R.id.instagramlink);

        mAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();

        Butic_Unic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstagram();
            }
        });

        orasinregistrare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(RegisterActivity.this);
                dialog.setContentView(R.layout.dialog_search_spiner_city);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                EditText editText = dialog.findViewById(R.id.city_edit_text);
                ListView listView = dialog.findViewById(R.id.searchCityListView);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_list_item_1,orase);
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
                        orasinregistrare.setText(adapter.getItem(position));
                        dialog.dismiss();
                    }
                });
            }
        });

        judetinregistrare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(RegisterActivity.this);
                dialog.setContentView(R.layout.dialog_search_spiner_county);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                EditText editText = dialog.findViewById(R.id.county_edit_text);
                ListView listView = dialog.findViewById(R.id.searchCountyListView);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(RegisterActivity.this, android.R.layout.simple_list_item_1,judete);
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
                        judetinregistrare.setText(adapter.getItem(position));
                        dialog.dismiss();
                    }
                });
            }
        });

        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nume=numeinregistrare.getText().toString().trim();
                String prenume=prenumeinregistrare.getText().toString().trim();
                String email=emailinregistrare.getText().toString().trim();
                String parola=parolainregistrare.getText().toString().trim();
                String telefon=telefoninregistrare.getText().toString().trim();
                String oras=orasinregistrare.getText().toString().trim();
                String judet=judetinregistrare.getText().toString().trim();

                try {
                    if(validateRegister(nume, prenume, email, parola, telefon, oras, judet)){
                        mAuth.createUserWithEmailAndPassword(email, parola).addOnCompleteListener(task -> {
                            if(task.isSuccessful()){
                                User user= new User(nume, prenume, email, parola, telefon, oras, judet);
                                firebaseDatabase.getReference("Users").child(mAuth.getCurrentUser().getUid()).setValue(user);
                                FirebaseUser currentUser= FirebaseAuth.getInstance().getCurrentUser();
                                currentUser.sendEmailVerification();
                                Toast.makeText(RegisterActivity.this, "User inregistrat cu succes!", Toast.LENGTH_LONG).show();
                                Intent i= new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Eroare la inregistrare!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        parolainregistrare.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showMessage("Notificare", "Parola trebuie sa contina minim 8 caractere!");
                }
            }
        });
    }

    public void openInstagram() {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/butic_unic/?igshid=ZDdkNTZiNTM%3D"));
        startActivity(webIntent);
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder=new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public boolean validateRegister(String nume, String prenume, String email, String parola, String telefon, String oras, String judet) {
        if (nume.isEmpty()) {
            numeinregistrare.setError("Câmpul nume nu poate fi gol!");
            numeinregistrare.requestFocus();
            return false;
        } else {
            if (prenume.isEmpty()) {
                prenumeinregistrare.setError("Câmpul prenume nu poate fi gol!");
                prenumeinregistrare.requestFocus();
                return false;
            } else {
                if (email.isEmpty()) {
                    emailinregistrare.setError("Câmpul email nu poate fi gol!");
                    emailinregistrare.requestFocus();
                    return false;
                } else {
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        emailinregistrare.setError("Formatul adresa de email este incorect!");
                        emailinregistrare.requestFocus();
                        return false;
                    } else {
                        if (parola.isEmpty()) {
                            parolainregistrare.setError("Câmpul parolă nu poate fi gol!");
                            parolainregistrare.requestFocus();
                            return false;
                        } else {
                            if (parola.length() < 8) {
                                parolainregistrare.setError("Parola trebuie să conțină minim 8 caractere!");
                                parolainregistrare.requestFocus();
                                return false;
                            } else {
                                if (telefon.isEmpty()) {
                                    telefoninregistrare.setError("Câmpul telefon nu poate fi gol!");
                                    telefoninregistrare.requestFocus();
                                    return false;
                                } else {
                                    if (telefon.length() < 10) {
                                        telefoninregistrare.setError("Numărul de telefon trebuie să conțină minim 10 caractere!");
                                        telefoninregistrare.requestFocus();
                                        return false;
                                    } else {
                                        if (!Patterns.PHONE.matcher(telefon).matches()) {
                                            telefoninregistrare.setError("Formatul numărului de telfon este incorect!");
                                            telefoninregistrare.requestFocus();
                                            return false;
                                        } else {
                                            if (oras.isEmpty()) {
                                                orasinregistrare.setError("Câmpul oraș nu poate fi gol!");
                                                orasinregistrare.requestFocus();
                                                return false;
                                            } else {
                                                if (judet.isEmpty()) {
                                                    judetinregistrare.setError("Câmpul județ nu poate fi gol!");
                                                    judetinregistrare.requestFocus();
                                                    return false;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

            }
        }
        return true;
    }
}