package com.example.pk2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pk2.model.Usuario;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.pk2.databinding.ActivityMapaBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapaBinding binding;

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef, myRefDueno;
    //Ruta en la que estan los usuarios y dueños
    static final String PATH_USERS = "users/";
    static final String PATH_DUENOS = "dueno/";

    private Marker posicionActual, motelMarker;
    String permission = Manifest.permission.ACCESS_FINE_LOCATION;
    int permission_id = 2;
    LatLng actual ;
    private LatLng mDestination;
    private Polyline mPolyline;
    String nombreDirMoterl = "";

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    String idDueno;

    //sensors
    SensorManager sensorManager;
    Sensor lightSensor;
    SensorEventListener lightSensorListener;
    static final int REQUTEST_CHECK_SETTINGS = 3;
    boolean is_gps_enabled = false;

    //map Search
    Geocoder mGeocoder;

    // variables de la pantalla
    Button botonChat;
    TextView txDireccion, txNombreMotel;

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        getWindow().getDecorView().getWindowInsetsController().setSystemBarsAppearance(0x00000008, 0x00000008);
        //inflate
        botonChat = findViewById(R.id.btChat);
        txDireccion = findViewById(R.id.textoMapaDireccion);
        txNombreMotel = findViewById(R.id.textMapaNombreMotel);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        botonChat.setOnClickListener(new botonListener());

        Intent intent = getIntent();
        nombreDirMoterl = intent.getStringExtra("Direccion");
        txNombreMotel.setText(intent.getStringExtra("NombreMotel").toString());
        txDireccion.setText(intent.getStringExtra("Direccion").toString());
        //mapa
        mGeocoder = new Geocoder(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        lightSensorListener = lightSensorCode();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = createLocationRequest();
        locationCallback = createLocationCallback();
        requestPermission(Mapa.this, permission, "permiso para acceder al gps", permission_id);
        mapFragment.getMapAsync(this);
    }

    private class botonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            Intent chatIntent = new Intent(v.getContext(), ChatActivity.class);
            myRefDueno = database.getReference(PATH_DUENOS);
            Intent currIntent = getIntent();
            Log.d("TEST_A", currIntent.getStringExtra("idMotel"));
            myRefDueno.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                        Usuario dueno = child.getValue(Usuario.class);
                        Log.d("TEST_A", currIntent.getStringExtra("idMotel"));
                        if (dueno.getId().equals(currIntent.getStringExtra("idMotel")))
                        {
                            idDueno = dueno.getId();
                        }
                    }
                    Log.d("TEST_A",idDueno);
                    chatIntent.putExtra("idReciever", idDueno);
                    chatIntent.putExtra("esDueno", false);
                    startActivity(chatIntent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                };

            });

        }
    }
    private SensorEventListener lightSensorCode() {
        SensorEventListener sel = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (mMap != null) {
                    if (event.values[0] < 100) {
                        Log.i("MAPS", "DARK_MAP" + event.values[0]);
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Mapa.this, R.raw.dark_map));
                    } else {
                        Log.i("MAPS", "WHITE_MAP" + event.values[0]);
                        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Mapa.this, R.raw.white_map));
                    }
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        return sel;
    }
    private void actualizarBD(Location loc){
        myRef = database.getReference(PATH_USERS);
        myRef.orderByChild("id").equalTo(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()){
                    Usuario act = child.getValue(Usuario.class);
                    act.setLat(loc.getLatitude());
                    act.setLon(loc.getLongitude());
                    myRef.child(act.getId()).setValue(act);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private LocationRequest createLocationRequest() {
        LocationRequest locationRequestV = LocationRequest.create()
                .setInterval(1000)
                .setFastestInterval(500)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequestV;
    }

    private LocationCallback createLocationCallback() {
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                if (location != null && nombreDirMoterl != "") {
                    if(posicionActual != null){
                        posicionActual.remove();
                    }
                    if(motelMarker != null)
                    {
                        motelMarker.remove();
                    }
                    Log.i("tag", "location: " + location.toString());
                    actual = new LatLng(location.getLatitude(), location.getLongitude());
                    actualizarBD(location);
                    posicionActual = mMap.addMarker(new MarkerOptions().position(actual).title("").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(actual));

                    if (mDestination == null){
                        mDestination = searchByName(nombreDirMoterl);
                    }
                    else {
                        motelMarker = mMap.addMarker(new MarkerOptions().position(mDestination).title(nombreDirMoterl).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                        drawRoute();
                    }

                }
            }
        };
        return locationCallback;
    }

    private void requestPermission(Activity context, String permission, String justification, int idCode) {

        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                Toast.makeText(context, justification, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{permission}, idCode);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkSettingsLocation();
        sensorManager.registerListener(this.lightSensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
        sensorManager.unregisterListener(this.lightSensorListener);
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            if (is_gps_enabled) {
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            }
        }
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void checkSettingsLocation() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                is_gps_enabled = true;
                startLocationUpdates();
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                is_gps_enabled = false;
                switch (statusCode) {
                    case CommonStatusCodes
                            .RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(Mapa.this, REQUTEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {

                        }
                        break;
                    case LocationSettingsStatusCodes
                            .SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUTEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                is_gps_enabled = true;
                startLocationUpdates();
            } else {
                Toast.makeText(Mapa.this, "El gps no esta activado", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(Mapa.this, R.raw.white_map));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(16));
    }


    private LatLng searchByName(String name) {
        LatLng position = null;
        if (!name.isEmpty()) {
            try {
                List<Address> addresses = mGeocoder.getFromLocationName(name, 2);
                if (addresses != null && !addresses.isEmpty()) {
                    Address addressResult = addresses.get(0);
                    position = new LatLng(addressResult.getLatitude(), addressResult.getLongitude());
                } else {
                    Toast.makeText(Mapa.this, "Direccion no encontrada", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(Mapa.this, "la direccion esta vacia", Toast.LENGTH_SHORT).show();
        }
        return position;
    }

    // implementacion de las rutas basados en el codigo enontrado en el siguiente link: https://www.wingsquare.com/blog/drawing-driving-route-directions-between-two-locations-using-google-directions-in-google-map-android/
    private void drawRoute(){

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(posicionActual.getPosition(), mDestination);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key=" + getString(R.string.google_maps_key);

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                Directions parser = new Directions();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(14);
                lineOptions.color(R.color.moraitoMelo);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            }
        }
    }
}
