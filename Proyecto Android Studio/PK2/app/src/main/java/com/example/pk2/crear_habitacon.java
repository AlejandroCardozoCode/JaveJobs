package com.example.pk2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pk2.model.HabitacionElementoList;
import com.example.pk2.model.Motel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

//Para la creación de habitación.
public class crear_habitacon extends AppCompatActivity {
    private static final int RCODE_CAMERA = 1, RCODE_REXTERNAL = 2, RCODE_WEXTERNAL = 3;
    Bitmap imageArray[];
    EditText etxt_NombreHabitacion, etext_Precio, etext_Descripcion, horasI;
    String  dirImg1, dirImg2, dirImg3, idGobal;
    ImageButton img_1, img_2, img_3, img_4, img_5, img_6;
    Button btn_gallery, btn_camara, btn_save;
    int imgIndexClicked = -1;
    //Base de datos
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    static final String PATH_MOTEL = "motel/";
    Uri imagenUriFinal;
    FirebaseStorage storage;
    StorageReference storageReference;
    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean estaDisponible;


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        dirImg1 = "https://firebasestorage.googleapis.com/v0/b/pk2-machete.appspot.com/o/motel%2F273694-top-white-wallpaper-2560x1440.jpg?alt=media&token=fcce3e77-13b0-4ef6-a2a8-828094728ae4";
        dirImg2 = "https://firebasestorage.googleapis.com/v0/b/pk2-machete.appspot.com/o/motel%2F273694-top-white-wallpaper-2560x1440.jpg?alt=media&token=fcce3e77-13b0-4ef6-a2a8-828094728ae4";
        dirImg3 = "https://firebasestorage.googleapis.com/v0/b/pk2-machete.appspot.com/o/motel%2F273694-top-white-wallpaper-2560x1440.jpg?alt=media&token=fcce3e77-13b0-4ef6-a2a8-828094728ae4";
        idGobal = getIntent().getStringExtra("idMotel");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_habitacon);
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(0x00000008, 0x00000008);
        //Inflación de los editText:
        etext_Descripcion = findViewById(R.id.DescripcionHabitacion);
        etext_Precio = findViewById(R.id.inputMotelAdd);
        etxt_NombreHabitacion = findViewById(R.id.inputMotelNom);
        horasI = findViewById(R.id.horasInput);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //Inflación de los Botones de Imagen:
        img_1 = findViewById(R.id.img_1);
        img_2 = findViewById(R.id.img_2);
        img_3 = findViewById(R.id.img_3);
        //Inflación de Botones:
        btn_gallery = findViewById(R.id.botonAbrirGaleria);
        btn_camara = findViewById(R.id.botonTomarFoto);
        btn_save = findViewById(R.id.botonRegistrarse);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos(v);
            }
        });
        imageArray = new Bitmap[6];

    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    public void validarDatos(View v){
        String nom = etxt_NombreHabitacion.getText().toString();
        String des = etext_Descripcion.getText().toString();
        String precio = etext_Precio.getText().toString();
        String horas = horasI.getText().toString();
        if(nom.isEmpty() || des.isEmpty() || precio.isEmpty() || horas.isEmpty()){
            Toast.makeText(getApplicationContext(),"No se han ingresasdo todos los valores", Toast.LENGTH_LONG).show();
            return;
        }
        regist(v, nom,des,precio, dirImg1, dirImg2, dirImg3, idGobal, horas);
    }
    public void regist(View v, String nombre, String desv, String preicov, String imagen1v, String imagen2v, String imagen3v, String id, String horasv)
    {
        String nom = nombre;
        String des = desv;
        String precio = preicov;
        String imagen1 = imagen2v;
        String imagen2 = imagen2v;
        String imagen3 = imagen3v;
        String idMotel = id;
        String horas = horasv;
        if(!nom.isEmpty() ) {
            /*
            HabitacionElementoList habitacion = new HabitacionElementoList(nom,id,des,imagen1,imagen2,imagen3,horas,precio, temperatura);
            myRef = database.getReference(  PATH_HABITACION + idMotel  + "/habitacion/" + "1");
            myRef.setValue(habitacion);

             */

            myRef = database.getReference(PATH_MOTEL );
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot child : snapshot.getChildren()) {
                        Motel motel = child.getValue(Motel.class);
                        if(idMotel.equals(motel.getId())){
                            Integer numeroHabitacions = motel.getNumHab() +1;
                            motel.setNumHab(numeroHabitacions);
                            // aumentar nuemro de habitaciones del motel

                            HabitacionElementoList habitacion = new HabitacionElementoList(nom,idGobal,des,dirImg1,dirImg2,dirImg3,horas,precio);
                            motel.agregarHabitacion(habitacion);
                            myRef = database.getReference(PATH_MOTEL + idMotel);
                            myRef.setValue(motel);
                            //crear habitacion y asignarla
                            /*
                            HabitacionElementoList habitacion = new HabitacionElementoList(nom,idGobal,des,dirImg1,dirImg2,dirImg3,horas,precio, temperatura);
                            myRef = database.getReference(  PATH_HABITACION + idMotel  + "/habitacion/" + String.valueOf(numeroHabitacions) + "/");
                            myRef.setValue(habitacion);

                             */
                            finish();
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }
    public void clickedOnImg1(View v)
    {
        if (imgIndexClicked == 0)
        {
            applySelectColor(imgIndexClicked, -1);
            imgIndexClicked = -1;
        }
        else {
            applySelectColor(imgIndexClicked, 0);
            imgIndexClicked = 0;
        }
    }
    public void clickedOnImg2(View v)
    {
        if (imgIndexClicked == 1)
        {
            applySelectColor(imgIndexClicked, -1);
            imgIndexClicked = -1;
        }
        else {
            applySelectColor(imgIndexClicked, 1);
            imgIndexClicked = 1;
        }
    }
    public void clickedOnImg3(View v)
    {
        if (imgIndexClicked == 2)
        {
            applySelectColor(imgIndexClicked, -1);
            imgIndexClicked = -1;
        }
        else {
            applySelectColor(imgIndexClicked, 2);
            imgIndexClicked = 2;
        }
    }
    public void clickedOnImg4(View v)
    {
        if (imgIndexClicked == 3)
        {
            applySelectColor(imgIndexClicked, -1);
            imgIndexClicked = -1;
        }
        else {
            applySelectColor(imgIndexClicked, 3);
            imgIndexClicked = 3;
        }
    }
    public void clickedOnImg5(View v)
    {
        if (imgIndexClicked == 4)
        {
            applySelectColor(imgIndexClicked, -1);
            imgIndexClicked = -1;
        }
        else {
            applySelectColor(imgIndexClicked, 4);
            imgIndexClicked = 4;
        }
    }
    public void clickedOnImg6(View v)
    {
        if (imgIndexClicked == 5)
        {
            applySelectColor(imgIndexClicked, -1);
            imgIndexClicked = -1;
        }
        else {
            applySelectColor(imgIndexClicked, 5);
            imgIndexClicked = 5;
        }
    }
    public void onClickPhotoManager(View v)
    {
        if (imgIndexClicked != -1)
        {
            //Write external storage
            verificarPermiso(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,"Para usar la camara, es necesario este permiso!",
                    RCODE_WEXTERNAL);
        }
        else
        {
            Toast.makeText(crear_habitacon.this, "Por favor seleccionar una imagen...!",Toast.LENGTH_LONG).show();
        }


    }

    public void onClickGalleryManager(View v)
    {
        if (imgIndexClicked != -1)
        {
            //Write external storage
            verificarPermiso(this, Manifest.permission.READ_EXTERNAL_STORAGE,"Para usar la galería, es necesario aceptar el permiso!",
                    RCODE_REXTERNAL);
        }
        else
        {
            Toast.makeText(crear_habitacon.this, "Por favor seleccionar una imagen...!",Toast.LENGTH_LONG).show();
        }
    }


    private void verificarPermiso(Activity context, String permisos, String justificacion,
                                  int id_Code)
    {
        if ((ContextCompat.checkSelfPermission(context,
                permisos) != PackageManager.PERMISSION_GRANTED))
        {
            //En caso que no se haya aceptado el permiso
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permisos))
            {
                Toast.makeText(crear_habitacon.this, justificacion,Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permisos}, id_Code);
        }
        else
        {
            itHasPermission(id_Code);
        }

    }
    private void itHasPermission (int id_Code)
    {
        if (id_Code == RCODE_WEXTERNAL)
        {
            verificarPermiso(this, Manifest.permission.CAMERA,
                    "\"Para usar la camara, es necesario este permiso!\"",RCODE_CAMERA);
        }
        else if (id_Code == RCODE_CAMERA)
        {
            tomarFoto();
        }
        else if (id_Code == RCODE_REXTERNAL)
        {
            abrirGaleria();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],int[]grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case RCODE_CAMERA:
                if (grantResults.length>0
                        && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    tomarFoto();
                }
                else
                {
                    //TODO - Deny Permission
                }
                break;
            case RCODE_REXTERNAL:
                if (grantResults.length>0
                        && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    abrirGaleria();
                }
                else
                {
                    //TODO - Deny Permission
                }
                break;
            case RCODE_WEXTERNAL:
                if (grantResults.length>0
                        && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    verificarPermiso(this, Manifest.permission.CAMERA,
                            "\"Para usar la camara, es necesario este permiso!\"",RCODE_CAMERA); //Permisos de camara...!
                }
                else
                {
                    //TODO - Deny Permission
                }
                break;

        }

    }

    private void tomarFoto()
    {
        Intent tomarFotoIntent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        try
        {
            startActivityForResult(tomarFotoIntent, RCODE_CAMERA);
        }
        catch (ActivityNotFoundException e)
        {
            Log.e("PERMISSION_APP", e.getMessage());
        }
    }
    private void abrirGaleria()
    {
        Intent i_galeria = new Intent(Intent.ACTION_PICK);

        i_galeria.setType("image/*");
        try {
            startActivityForResult(i_galeria, RCODE_REXTERNAL);
        }
        catch (ActivityNotFoundException e)
        {
            Log.e("PERMISSION_APP", e.getMessage());
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case RCODE_CAMERA:
                if (resultCode == RESULT_OK )
                {
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageArray[imgIndexClicked] = imageBitmap;
                    changeImageOnScreen();
                }
                break;
            case RCODE_REXTERNAL:
                if (resultCode == RESULT_OK)
                {
                    try {
                        final Uri imageUri = data.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap myImage = BitmapFactory.decodeStream(imageStream);
                        imageArray[imgIndexClicked] = myImage;
                        imagenUriFinal = data.getData();
                        subirImagenFirebase(imgIndexClicked);
                        changeImageOnScreen();
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }
                }
        }
    }
    private void subirImagenFirebase( Integer imagenint) {

        StorageReference riversRef = storageReference.child("motel/" + idGobal + String.valueOf(imagenint));
        riversRef.putFile(imagenUriFinal)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();

                        uri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String photoLink = uri.toString();
                                if (imagenint == 0) {
                                    dirImg1 = photoLink;
                                }
                                else if (imagenint == 1) {
                                    dirImg2 = photoLink;
                                }
                                else if (imagenint == 2) {

                                    dirImg3 = photoLink;
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_LONG).show();
                    }
                });

    }

    private void applySelectColor (int prevIMG, int currIMG)
    {
        switch (currIMG)
        {
            case 0:
                img_1.getBackground().setColorFilter(Color.parseColor("#FFFFFFFF"),
                        PorterDuff.Mode.SRC_ATOP);
                break;
            case 1:
                img_2.getBackground().setColorFilter(Color.parseColor("#FFFFFFFF"),
                        PorterDuff.Mode.SRC_ATOP);
                break;
            case 2:
                img_3.getBackground().setColorFilter(Color.parseColor("#FFFFFFFF"),
                        PorterDuff.Mode.SRC_ATOP);
                break;
            default:
                break;

        }
        switch (prevIMG)
        {
            case 0:
                img_1.getBackground().setColorFilter(Color.parseColor("#FFDDDDDD"),
                        PorterDuff.Mode.SRC_ATOP);
                break;
            case 1:
                img_2.getBackground().setColorFilter(Color.parseColor("#FFDDDDDD"),
                        PorterDuff.Mode.SRC_ATOP);
                break;
            case 2:
                img_3.getBackground().setColorFilter(Color.parseColor("#FFDDDDDD"),
                        PorterDuff.Mode.SRC_ATOP);
                break;

            default:
                break;
        }

    }
    private void changeImageOnScreen()
    {
        switch (imgIndexClicked)
        {
            case 0:
                img_1.setImageBitmap(imageArray[imgIndexClicked]);

                break;
            case 1:
                img_2.setImageBitmap(imageArray[imgIndexClicked]);
                break;
            case 2:
                img_3.setImageBitmap(imageArray[imgIndexClicked]);
                break;
            case 3:
                img_4.setImageBitmap(imageArray[imgIndexClicked]);
                break;
            case 4:
                img_5.setImageBitmap(imageArray[imgIndexClicked]);
                break;
            case 5:
                img_6.setImageBitmap(imageArray[imgIndexClicked]);
                break;
        }
    }
}
