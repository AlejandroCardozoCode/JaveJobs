package com.example.pk2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.pk2.model.Chat;
import com.example.pk2.model.ChatElementoList;
import com.example.pk2.model.MotelElementoList;
import com.example.pk2.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class activity_admin_chats extends AppCompatActivity {

    static final String USER_PATH = "users/";
    static final String DUENO_PATH = "dueno/";
    static final String CHAT_PATH = "chat/";
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    List<ChatElementoList> elementos;
    List<String> userIDList;
    RecyclerView myRecycler;


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chats);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(0x00000008, 0x00000008);
        //inflate
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        elementos = new ArrayList<>();
        userIDList = new ArrayList<>();

    }
    private void cargar()
    {
        myRef = database.getReference(CHAT_PATH);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot child : snapshot.getChildren())
                {
                    Chat actual = child.getValue(Chat.class);
                    if (actual.getId_Dueno().equals( mAuth.getCurrentUser().getUid()))
                    {
                        userIDList.add(actual.getId_Usuario());
                    }
                }
                elementos.clear();
                createElements();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userIDList.clear();
        cargar();
    }

    private void createElements()
    {
        myRef = database.getReference(USER_PATH);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot child : snapshot.getChildren())
                {
                    Usuario actual = child.getValue(Usuario.class);
                    for (String e : userIDList)
                    {
                        if (e.equals(actual.getId()))
                        {
                            elementos.add(new ChatElementoList(actual.getId(),
                                    actual.getNombre(), actual.getApellido()));
                        }
                    }

                }
                AdaptadorListaChatsDueno adapter = new AdaptadorListaChatsDueno(elementos,
                        activity_admin_chats.this, new AdaptadorListaChatsDueno.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(ChatElementoList elementos)
                    {
                        Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
                        intent.putExtra("idReciever", elementos.getId_Reciever());
                        intent.putExtra("esDueno", true);
                        intent.putExtra("valor", getIntent().getStringExtra("valor"));
                        startActivity(intent);
                    }
                });
                myRecycler = findViewById(R.id.listChat_rView);
                myRecycler.setLayoutManager(new LinearLayoutManager(activity_admin_chats.this));
                myRecycler.setHasFixedSize(true);
                myRecycler.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });
    }

}