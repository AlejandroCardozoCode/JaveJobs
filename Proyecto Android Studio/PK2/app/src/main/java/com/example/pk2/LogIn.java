package com.example.pk2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.pk2.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {

    private boolean flag;
    Button bRegUsr;
    Button bRegDue;
    TextInputEditText usuario, contraseña;
    //autenticacion firebase
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    //ruta en la base
    static final String PATH_USERS = "users/";
    static final String PATH_DUENO = "dueno/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(R.color.moraitoMelo));
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //inflate
        bRegUsr = findViewById(R.id.usarioRegist);
        bRegDue = findViewById(R.id.dueñoRegist);
        usuario = findViewById(R.id.correoLogin);
        contraseña = findViewById(R.id.passwLogin);
        //inflate autentificacion
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        //llamado pantalla registro usuario
        bRegUsr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Registro.class);
                startActivity(intent);
            }
        });

        //llamado a el registro del duenio
        bRegDue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Registro_duenio.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = mAuth.getCurrentUser();
        actualizarPantalla(user);
    }

/*    @Override
    protected void onStart() {
        super.onStart();
        /*FirebaseUser user = mAuth.getCurrentUser();
        actualizarPantallaStart(user,contraseña.getText().toString());
    }*/
    //llamado funcion para ingresar
    public void login(View v)
    {
        String correo = usuario.getText().toString();
        String contra = contraseña.getText().toString();
        if(validar(correo,contra)) {
            //envio a firebase para confirmar
            mAuth.signInWithEmailAndPassword(correo, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        actualizarPantalla(mAuth.getCurrentUser());
                    }else {
                        //impresion de error y limpieza del formulario
                        usuario.setText(" ");
                        contraseña.setText(" ");
                    }

                }
            });
        }else {
            //el correo no es valido
            Toast.makeText(LogIn.this,"No se encontro el usuario", Toast.LENGTH_LONG).show();
        }
    }

    //funcion para validar que la contraseña y el correo ingresados son validos
    private boolean validar(String correo, String contr)
    {
        if(Patterns.EMAIL_ADDRESS.matcher(correo).matches() && contr.length()>=6) {
            return true;
        }else
            return false;
    }

    private void actualizarPantalla(FirebaseUser user)
    {
        flag = true;
        if(user != null)
        {
            myRef = database.getReference(PATH_USERS);
            myRef.orderByChild("correo").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Intent intent = new Intent(LogIn.this, ListaMoteles.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    for(DataSnapshot child : snapshot.getChildren()) {
                        Usuario usuario = child.getValue(Usuario.class);
                        if(usuario.getCorreo().equals(user.getEmail())) {
                            startActivity(intent);
                            flag = false;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LogIn.this,"ERROR:",Toast.LENGTH_LONG).show();
                }
            });
            if(flag)
            {
                myRef = database.getReference(PATH_DUENO);
                myRef.orderByChild("correo").equalTo(user.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot child : snapshot.getChildren()) {
                            Usuario dueno = child.getValue(Usuario.class);
                            if(dueno.getCorreo().equals(user.getEmail())) {
                                Intent intent = new Intent(LogIn.this, AdmLogActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LogIn.this,"ERROR:",Toast.LENGTH_LONG).show();
                    }
                });
            }
            /*//actualizacion de pantalla segun el rol de cada usuario para el administrador es uno y para usuarios es 0
            if(user.getDisplayName().equals("0")){
                Intent intent = new Intent(LogIn.this, ListaMoteles.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }else if(user.getDisplayName().equals("1")){
                Intent intent = new Intent(LogIn.this, AdmLogActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }*/
        }
    }

}