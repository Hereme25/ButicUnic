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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenta2023.Adaptors.AnuntAdaptor;
import com.example.licenta2023.Entities.Anunt;
import com.example.licenta2023.Entities.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnunturiActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private FirebaseDatabase firebaseDatabase;//decalara o referinta la baza de date
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private FirebaseUser user;
    TextView userName, userEmail;
    public static String userId;
    ListView anunturiLista;
    private DatabaseReference userReference;
    ArrayList<Anunt> anunturi = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anunturi);
        drawerLayout=findViewById(R.id.drawer_layout);
        mAuth=FirebaseAuth.getInstance();
        anunturiLista = findViewById(R.id.anunturiListView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Anunturi");
        userName=findViewById(R.id.userFullName);
        userEmail=findViewById(R.id.userEmail);
        firebaseDatabase=FirebaseDatabase.getInstance();
        user=mAuth.getCurrentUser();
        userReference=firebaseDatabase.getReference("Users");
        userId=user.getUid();

        userReference.child(userId).addValueEventListener(new ValueEventListener() {
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
                Toast.makeText(AnunturiActivity.this, "Eroare la executie!", Toast.LENGTH_LONG).show();
            }
        });
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot anunt: snapshot.getChildren()){
                        Anunt currentAnunt = anunt.getValue(Anunt.class);
                        anunturi.add(currentAnunt);
                    }
                    if(anunturi.size()>0){
                        AnuntAdaptor anuntAdaptor = new AnuntAdaptor(AnunturiActivity.this, anunturi);
                        anunturiLista.setAdapter(anuntAdaptor);
                        setListViewHeightBasedOnChildren(anunturiLista);
                        anunturiLista.setOnItemClickListener((parent, view, position, id) -> {
                            Intent intent = new Intent(AnunturiActivity.this, VizualizareAnunt.class);
                            intent.putExtra("TitluAnunt",anunturi.get(position).getTitlu());
                            intent.putExtra("DescriereAnunt",anunturi.get(position).getDescriere());
                            intent.putExtra("PretAnunt",anunturi.get(position).getPret().toString());
                            intent.putExtra("CategorieAnunt",anunturi.get(position).getCategorie());
                            intent.putExtra("LocatieAnunt",anunturi.get(position).getLocalizare());
                            intent.putExtra("DataAnunt",anunturi.get(position).getDataAnunt());
                            intent.putExtra("TelefonAnunt",anunturi.get(position).getTelefon());
                            intent.putExtra("EmailAnunt",anunturi.get(position).getEmail());
                            intent.putExtra("LinkPoza", anunturi.get(position).getImagineUri());
                            startActivity(intent);
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    public void closeDrawer(DrawerLayout drawerLayout)
    {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void logout(Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Delogare");
        builder.setMessage("Ești sigur ca vrei să te deloghezi?");
        builder.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                redirectActivity(AnunturiActivity.this,MainActivity.class);//redirectioneaza in AnunturiActivity
            }
        });

        builder.setNegativeButton("Nu", new DialogInterface.OnClickListener() {
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
    public void ClickMenu(View view)
    {
        openDrawer(drawerLayout);
    }

    public void openDrawer(DrawerLayout drawerLayout)
    {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void ClickSignOut(View view)
    {
        logout(AnunturiActivity.this);
    }
    public void ClickAdd(View view)
    {

        redirectActivity(AnunturiActivity.this,AdaugaAnunt.class);
    }
    public void ClickHome(View view)
    {

        redirectActivity(AnunturiActivity.this, PrincipalPage.class);
    }
    public void ClickDespreNoi(View view)
    {

        redirectActivity(AnunturiActivity.this,DespreNoi.class);
    }
    public void ClickAnunturi(View view)
    {
        closeDrawer(drawerLayout);
    }
    public void ClickAnunturileMele(View view)
    {
        redirectActivity(AnunturiActivity.this,AnunturileMele.class);
    }
    public void ClickLogo(View view)
    {
        closeDrawer(drawerLayout);
    }
}