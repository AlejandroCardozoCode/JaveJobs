package com.example.pk2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pk2.model.HabitacionElementoList;
import com.example.pk2.model.Motel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class listaOfertas extends AppCompatActivity {

    CardView selecHabitacion;
    TextView txDireccion, txNombreMotel;
    ImageView imagenMotel;
    static final String PATH_MOTEL = "motel/";
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    List<HabitacionElementoList> elementos;
    String idGlobal;
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_ofertas_cliente);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(0x00000008, 0x00000008);
        //inflate
        txDireccion = findViewById(R.id.textoDireccion);
        txNombreMotel = findViewById(R.id.textoHabitacionesNombreMotel);
        imagenMotel = findViewById(R.id.portadaMotelCliente);
        idGlobal = getIntent().getStringExtra("idMotel");
        elementos = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();


    }
    private void cargar(FirebaseUser usuario)
    {
        myRef = database.getReference(PATH_MOTEL);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()) {
                    Motel motel = child.getValue(Motel.class);
                    if ( motel.getId().equals(getIntent().getStringExtra("idMotel"))){
                        txNombreMotel.setText(motel.getNombre());
                        txDireccion.setText(motel.getDireccion());
                        String urlImage = motel.getDirImagen();
                        Log.e("url", urlImage);
                        Glide.with(getApplicationContext())
                                .load(urlImage)
                                .into(imagenMotel);
                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        elementos.clear();
        cargar(mAuth.getCurrentUser());
        cargarHab(idGlobal);

    }
    private void cargarHab(String idMotel)
    {
        myRef = database.getReference(PATH_MOTEL + idMotel + "/habitaciones/");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    HabitacionElementoList habitacion = child.getValue(HabitacionElementoList.class);
                    Log.e("habitacion", habitacion.toString());
                    elementos.add(habitacion);
                }
                adaptadorOferta listaAdaptador = new adaptadorOferta(elementos, listaOfertas.this, new adaptadorOferta.OnItemClickListener() {
                    @Override
                    public void onItemClick(HabitacionElementoList elementos) {
                        Intent intent = new Intent(getApplicationContext(), descripcionOferta.class);
                        intent.putExtra("nombre",elementos.getNombre());
                        intent.putExtra("precio",elementos.getPrecio());
                        intent.putExtra("des",elementos.getDescripcion());
                        intent.putExtra("temp",elementos.getTemperatura());
                        intent.putExtra("hora",elementos.getHoras());
                        intent.putExtra("img1",elementos.getImagen1());
                        intent.putExtra("img2",elementos.getImagen2());
                        intent.putExtra("img3",elementos.getImagen3());
                        startActivity(intent);
                    }
                });

                RecyclerView recyclerView = findViewById(R.id.recyclerHab);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(listaOfertas.this));
                recyclerView.setAdapter(listaAdaptador);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
