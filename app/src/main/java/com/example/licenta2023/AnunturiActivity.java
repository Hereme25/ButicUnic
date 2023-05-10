package com.example.licenta2023;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.licenta2023.Adaptors.AnuntAdaptor;
import com.example.licenta2023.Entities.Anunt;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnunturiActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;//decalara o referinta la baza de date
    private DatabaseReference reference;
    ListView anunturiLista;
    ArrayList<Anunt> anunturi = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anunturi);
        anunturiLista = findViewById(R.id.anunturiListView);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Anunturi");
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
                        anunturiLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(AnunturiActivity.this, VizualizareAnunt.class);
                                intent.putExtra("TitluAnunt",anunturi.get(position).getTitlu());
                                intent.putExtra("DescriereAnunt",anunturi.get(position).getDescriere());
                                intent.putExtra("PretAnunt",anunturi.get(position).getPret().toString());
                                intent.putExtra("CategorieAnunt",anunturi.get(position).getCategorie());
                                intent.putExtra("LocatieAnunt",anunturi.get(position).getLocalizare());
                                intent.putExtra("DataAnunt",anunturi.get(position).getDataAnunt());
                                intent.putExtra("TelefonAnunt",anunturi.get(position).getTelefon());
                                intent.putExtra("EmailAnunt",anunturi.get(position).getEmail());
                                startActivity(intent);
                            }
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
}