package com.example.pk2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pk2.model.HabitacionElementoList;
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

public class AdmLogActivity extends AppCompatActivity {
    Button btnAgHab, botonCerrarSesion, chat;
    TextView nombreMotel, direccionMotel;
    ImageView portada;
    static final String PATH_USER = "dueno/";
    static final String PATH_MOTEL = "motel/";
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    Integer numeroHab = 0;

    List<HabitacionElementoList> elementos;
    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adm_log);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(0x00000008, 0x00000008);
        //inflate
        btnAgHab = findViewById(R.id.btnAgHab);
        nombreMotel = findViewById(R.id.textoHabitacionesNombreMotel);
        portada = findViewById(R.id.portadaMotelDueno);
        direccionMotel = findViewById(R.id.textoDireccion);
        botonCerrarSesion = findViewById(R.id.logOutDuenio);
        chat = findViewById(R.id.chats);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        elementos = new ArrayList<>();
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //aca tiene que llamar al historial de chats
                Intent intent = new Intent(v.getContext(),activity_admin_chats.class);
                intent.putExtra("valor", direccionMotel.getText());
                startActivity(intent);
            }
        });
        btnAgHab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intCrearHab = new Intent(v.getContext(),crear_habitacon.class);
                intCrearHab.putExtra("idMotel", mAuth.getCurrentUser().getUid());
                intCrearHab.putExtra("numero", numeroHab);
                startActivity(intCrearHab);
            }
        });
        botonCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(AdmLogActivity.this,LogIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                AdmLogActivity.this.finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        elementos.clear();
        cargar(mAuth.getCurrentUser());
        cargarHab(mAuth.getCurrentUser());

    }

    private void cargar(FirebaseUser usuario)
    {
        myRef = database.getReference(PATH_MOTEL);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child : snapshot.getChildren()) {
                    Motel motel = child.getValue(Motel.class);
                    if(usuario.getUid().equals(motel.getId())){
                        String urlImage = motel.getDirImagen();
                        Glide.with(getApplicationContext())
                                .load(urlImage)
                                .into(portada);
                        nombreMotel.setText(motel.getNombre());
                        direccionMotel.setText(motel.getDireccion());
                        numeroHab = motel.getNumHab();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void cargarHab(FirebaseUser usuario)
    {
        myRef = database.getReference(PATH_MOTEL + mAuth.getCurrentUser().getUid() + "/habitaciones/");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    HabitacionElementoList habitacion = child.getValue(HabitacionElementoList.class);
                    Log.e("habitacion", habitacion.toString());
                    elementos.add(habitacion);
                }
                adaptadorHabi listaAdaptador = new adaptadorHabi(elementos, AdmLogActivity.this, new adaptadorHabi.OnItemClickListener() {
                    @Override
                    public void onItemClick(HabitacionElementoList elementos) {
                        Intent intent = new Intent(getApplicationContext(),Descripcion_habitacion.class);
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

                RecyclerView recyclerView = findViewById(R.id.recyclerHabDuen);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdmLogActivity.this));
                recyclerView.setAdapter(listaAdaptador);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
