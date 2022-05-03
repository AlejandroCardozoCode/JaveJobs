package com.example.pk2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.pk2.model.Motel;
import com.example.pk2.model.MotelElementoList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaMoteles extends AppCompatActivity {
    ImageButton bPerfil;
    CardView selecMotel;
    List<MotelElementoList> elementos;
    static final String PATH_MOTEL = "motel/";
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ofertas);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(0x00000008, 0x00000008);
        //inflate
        bPerfil = findViewById(R.id.botonPerifl);
        bPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Perfil.class);
                startActivity(intent);
            }
        });


        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        elementos = new ArrayList<>();
        cargar(mAuth.getCurrentUser());

    }
    private void cargar(FirebaseUser usuario)
    {
        myRef = database.getReference(PATH_MOTEL);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Motel motel = child.getValue(Motel.class);
                    MotelElementoList list = new MotelElementoList();
                    list.setNombre(motel.getNombre());
                    list.setDireccion(motel.getDireccion());
                    list.setImagen(motel.getDirImagen());
                    list.setId(motel.getId());

                    elementos.add(list);


                }

                adaptadorList listaAdaptador = new adaptadorList(elementos, ListaMoteles.this, new adaptadorList.OnItemClickListener() {
                    @Override
                    public void onItemClick(MotelElementoList elementos) {
                        Intent intent = new Intent(getApplicationContext(), listaOfertas.class);
                        intent.putExtra("idMotel",elementos.getId());
                        startActivity(intent);
                    }
                });
                RecyclerView recyclerView = findViewById(R.id.recicler);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(ListaMoteles.this));
                recyclerView.setAdapter(listaAdaptador);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}