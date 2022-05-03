package com.example.pk2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class descripcionOferta extends AppCompatActivity {

    TextView nombre, descripcion, precio;
    ImageView img1, img2, img3;

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_oferta);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(0x00000008, 0x00000008);
        //inflate
        nombre = findViewById(R.id.nombreHabitacion);
        descripcion = findViewById(R.id.descripcionHabitacion);
        precio = findViewById(R.id.precioHabitacion);
        img1 = findViewById(R.id.imagen1Habitacion);
        img2 = findViewById(R.id.imagen2Habitacion);
        img3 = findViewById(R.id.imagen3Habitacion);

        nombre.setText(getIntent().getStringExtra("nombre"));
        descripcion.setText(getIntent().getStringExtra("des"));

        precio.setText("Salario: $"+ getIntent().getStringExtra("precio")+ " / " + getIntent().getStringExtra("hora") + " horas");
        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("img1"))
                .into(img1);
        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("img2"))
                .into(img2);
        Glide.with(getApplicationContext())
                .load(getIntent().getStringExtra("img3"))
                .into(img3);
    }
}