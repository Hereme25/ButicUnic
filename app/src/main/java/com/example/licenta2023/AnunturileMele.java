package com.example.licenta2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
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

public class AnunturileMele extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;//decalara o referinta la baza de date
    private DatabaseReference anunturiReference;
    private DatabaseReference userReference;
    ListView anunturiLista;
    ArrayList<Anunt> anunturileMele = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    String userFullName;
    String userId;
    String fullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anunturile_mele);
        anunturiLista = findViewById(R.id.anunturileMeleListView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        anunturiReference = firebaseDatabase.getReference("Anunturi");
        user=mAuth.getCurrentUser();
        userReference=firebaseDatabase.getReference("Users");
        userId=user.getUid();
        userReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User profil=snapshot.getValue(User.class);
                if(profil != null){
                    fullName = profil.getNume() + " " + profil.getPrenume();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AnunturileMele.this, "Eroare la executie!", Toast.LENGTH_LONG).show();
            }
        });
        anunturiReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot anunt: snapshot.getChildren()){
                        Anunt currentAnunt = anunt.getValue(Anunt.class);
                        if(currentAnunt.getProprietar().equals(userFullName));
                            anunturileMele.add(currentAnunt);
                    }
                    if(anunturileMele.size()>0){
                        AnuntAdaptor anuntAdaptor = new AnuntAdaptor(AnunturileMele.this, anunturileMele);
                        anunturiLista.setAdapter(anuntAdaptor);
                        setListViewHeightBasedOnChildren(anunturiLista);
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
}