package com.example.licenta2023;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta2023.Entities.Anunt;
import com.example.licenta2023.Entities.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class AdaugaAnunt extends AppCompatActivity {

    EditText titlu, descriere, pret, categorie;
    ArrayList<String> categorii = new ArrayList<>(Arrays.asList("Blugi", "Geci", "Rochii", "Bluze"));
    TextView localizare, email, nrtelefon;
    ImageView imagineProdus;
    Dialog dialog;
    Uri imagineUri;
    private FirebaseDatabase firebaseDatabase;//declara o referinta la baza de date
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private FirebaseUser user;
    String proprietarNume;
    String userId;
    String imagineUriString;
    Button adaugaAnunt;
    private static final int PICK_IMAGINE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga_anunt);
        titlu = findViewById(R.id.TitlulBox);
        imagineProdus = findViewById(R.id.ImagineProdus);
        descriere = findViewById(R.id.DescriereBox);
        pret = findViewById(R.id.PretBox);
        categorie = findViewById(R.id.CategoriaBox);
        localizare = findViewById(R.id.LocalizareBox);
        email = findViewById(R.id.MailBox);
        nrtelefon = findViewById(R.id.TelefonBox);
        adaugaAnunt = findViewById(R.id.AdaugaAnuntButton);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("imaginiAnunturi");
        user = mAuth.getCurrentUser();
        reference = firebaseDatabase.getReference("Users");
        userId = user.getUid();
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User profil = snapshot.getValue(User.class);
                if (profil != null) {
                    String localizareText = profil.getOras() + " / " + profil.getJudet();
                    String emailText = profil.getEmail();
                    String nrTelefonText = profil.getTelefon();
                    localizare.setText(localizareText);
                    email.setText(emailText);
                    nrtelefon.setText(nrTelefonText);
                    proprietarNume = profil.getNume() + " " + profil.getPrenume();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdaugaAnunt.this, "Eroare la executie!", Toast.LENGTH_LONG).show();
            }
        });

        imagineProdus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        adaugaAnunt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String titluText = titlu.getText().toString().trim();
                String categorieText = categorie.getText().toString().trim();
                String descriereText = descriere.getText().toString().trim();
                String telefonText = nrtelefon.getText().toString().trim();
                String emailText = email.getText().toString().trim();
                String localizareText = localizare.getText().toString().trim();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                String dataAnunt = LocalDateTime.now().format(format);
                if (validateAnunt(titluText, categorieText, descriereText, pret.getText().toString())) {
                    Double valoarePret = Double.parseDouble(pret.getText().toString().trim());
                    Anunt anunt = new Anunt(titluText, proprietarNume, categorieText, emailText, valoarePret, descriereText, localizareText, telefonText, imagineUriString, dataAnunt);
                    firebaseDatabase.getReference("Anunturi").child(UUID.randomUUID().toString()).setValue(anunt);
                    Toast.makeText(AdaugaAnunt.this, "Anunț adăugat cu succes!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(AdaugaAnunt.this, PrincipalPage.class);
                    startActivity(i);
                    finish();
                }
            }
        });
        categorie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(AdaugaAnunt.this);
                dialog.setContentView(R.layout.dialog_search_spiner_category);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                EditText editText = dialog.findViewById(R.id.category_edit_text);
                ListView listView = dialog.findViewById(R.id.searchCategoryListView);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AdaugaAnunt.this, android.R.layout.simple_list_item_1, categorii);
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
                        categorie.setText(adapter.getItem(position));
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGINE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mim = MimeTypeMap.getSingleton();
        return mim.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile() {
        if (imagineUri != null) {
            StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imagineUri));
            fileReference.putFile(imagineUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imagineUriString = taskSnapshot.getUploadSessionUri().toString();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGINE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagineUri = data.getData();
            uploadFile();
            Picasso.with(this).load(imagineUri).into(imagineProdus);
        }
    }

    public boolean validateAnunt(String titluText, String categoriaText, String descriereText, String valoarePret) {
        if (titluText.isEmpty()) {
            titlu.setError("Câmpul titlu nu poate fi gol!");
            titlu.requestFocus();
            return false;
        } else {
            if (descriereText.isEmpty()) {
                descriere.setError("Câmpul descriere nu poate fi gol!");
                descriere.requestFocus();
                return false;
            } else {
                if (valoarePret.isEmpty()) {
                    pret.setError("Câmpul preț nu poate fi gol!");
                    pret.requestFocus();
                    return false;
                } else {
                    if (categoriaText.isEmpty()) {
                        categorie.setError("Câmpul categorie nu poate fi gol!");
                        categorie.requestFocus();
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
