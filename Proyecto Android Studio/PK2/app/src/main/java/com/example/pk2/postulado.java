package com.example.pk2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;





public class postulado extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postulado);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(0x00000008, 0x00000008);
    }
}