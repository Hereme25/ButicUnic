package com.example.licenta2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailconectare,parolaconectare;
    Button login;
    TextView Butic_Unic;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop(){
        super.onStop();
        if(mAuthListener!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailconectare=findViewById(R.id.emailconectare);
        parolaconectare=findViewById(R.id.parolaconectare);
        login=findViewById(R.id.Login);
        Butic_Unic=findViewById(R.id.instagramlink);

        mAuth=FirebaseAuth.getInstance();
        mAuth.signOut();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user!=null){
                    if(user.isEmailVerified()){//verificare email
                        conectare(user);
                    } else{
                        Toast.makeText(LoginActivity.this,"Asteptam confirmarea adresei de email!", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(getIntent());//refresh
                    }
                } else {
                    conectare(null);
                }
            }
        };

        Butic_Unic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInstagram();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email= emailconectare.getText().toString().trim();
                String parola= parolaconectare.getText().toString().trim();
                if(validateLogin(email, parola)){
                    signIn(email, parola);
                }
            }
        });

    }

    public void signIn( String email, String parola){
        mAuth.signInWithEmailAndPassword(email, parola).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        emailconectare.setError("Email incorect!");
                        emailconectare.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        parolaconectare.setError("Parola incorecta!");
                        parolaconectare.requestFocus();
                    }catch (FirebaseNetworkException e){
                        Toast.makeText(LoginActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    } conectare(null);
                }
            }
        });
    }

    public void conectare(FirebaseUser user){
        if(user != null){
            Toast.makeText(LoginActivity.this,"Te-ai conecatat cu succes!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, PrincipalPage.class));
        }
    }
    public void openInstagram() {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vaccinare-covid.gov.ro/precizari-privind-inscrierea-pe-platforma-de-vaccinare/"));
        startActivity(webIntent);
    }

    public boolean validateLogin(String email, String parola){
        if(email.isEmpty()){
            emailconectare.setError("Campul email nu poate fi gol!");
            emailconectare.requestFocus();
            return false;
        } else{
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                emailconectare.setError("Emailul introdus nu este valid!");
                emailconectare.requestFocus();
                return false;
            } else {
                if(parola.isEmpty()){
                    parolaconectare.setError("Campul parola nu poate fi gol!");
                    parolaconectare.requestFocus();
                    return false;
                } else {
                    if(parola.length()<8){
                        parolaconectare.setError("Parola introdusa trebuie sa contina cel putin 8 caractere!");
                        parolaconectare.requestFocus();
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        }
    }
}