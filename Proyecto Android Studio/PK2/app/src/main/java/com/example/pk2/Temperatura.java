package com.example.pk2;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

public class Temperatura extends AppCompatActivity implements SensorEventListener{

    private TextView temperatura, frase;
    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean estaDisponible;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperatura);

        temperatura = findViewById(R.id.tempP);
        frase = findViewById(R.id.fraseTemp);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            estaDisponible = true;
            frase.setText("");
            temperatura.setText("Calculando");
        }
        else{
            temperatura.setText("");
            frase.setText("La temperatura no se puede calcular con este dispositivo");
            estaDisponible = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(estaDisponible)
        {
            sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(estaDisponible)
        {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        temperatura.setText(event.values[0]+" °C");
        if((int)event.values[0] <= 15)
        {
            frase.setText(" \" Esta haciendo algo de frio, es hora de entrar en calor con tu pareja\"");
        }
        if((int)event.values[0] > 15 && event.values[0] <= 21)
        {
            frase.setText(" \" La temperatura es ideal para un momento tan especial como este \"");
        }
        if((int)event.values[0] > 21)
        {
            frase.setText(" \" Las cosas estan algo calientes, esto se pondra interesante\"");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}