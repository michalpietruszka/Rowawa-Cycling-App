package com.example.pietruszka.myapplicationmapactivi;

/**
 * Created by Pietruszka on 12.03.2018.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.joda.time.DateTime;
import org.json.JSONException;
import org.w3c.dom.*;

import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.xml.sax.InputSource;


import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.*;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.data.geojson.GeoJsonLayer;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

public class MapsActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapLongClickListener {

    private static Context mycontext;
    private GoogleMap mMap;
    private Marker koniec;
    private TextView DetailNav;
    private Button nawiguj;
    private Button rozpocznij;
    private zapisane currentSaved;
    private historia currentHistory;
    private PlaceAutocompleteFragment autocompleteFragmentStart;
    private PlaceAutocompleteFragment autocompleteFragmentCel;
    //pois
    private static Vector<stacje> vectorVeturilo = new Vector<>();
    private static Vector<Marker> vectorVeturiloMarker = new Vector<>();
    private static Vector<stacje> vectorNaprawa = new Vector<>();
    private static Vector<Marker> vectorNaprawaMarker = new Vector<>();
    private static InputStream inStream;
    private static Boolean flagaTrasyGeoJson=false;
    private static GeoJsonLayer layer;
    //navigation
    private Polyline currentPolyline;
    private Marker startMar;
    private Marker koniecMar;
    private LatLng startLocation=new LatLng(0, 0);;//z textBoxa
    private LatLng endLocation=new LatLng(0, 0);;//z textBoxa
    private LatLng currentLocation=new LatLng(52.5,21);//z aktualizowania wspolrzednych co 30s
    private FusedLocationProviderClient mFusedLocationClient;
    private Geocoder geocoder;
    private FloatingActionButton fabAddSaved;
    private FloatingActionButton fabDeleteNav;
    private static final int REQUEST_FINE_LOCATION = 11;
    //navigation menu, switche
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private String stateSwitch="00";

    //Firebase
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference1;

    //callback aktualizowanie lokalizacji
    private LocationCallback callback = new LocationCallback() {
        public void onLocationResult(LocationResult locationResult){
            if (locationResult.getLastLocation()!=null) {
                String lon = String.valueOf(locationResult.getLastLocation().getLongitude());
                String lat = String.valueOf(locationResult.getLastLocation().getLatitude());
                Toast toast = Toast.makeText(mycontext, lon + ", " + lat, Toast.LENGTH_LONG);
                currentLocation=new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
                toast.show();
            }
        }
    };

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //autocomplete
        autocompleteFragmentStart = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragmentStart);
        autocompleteFragmentCel = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragmentCel);
        EditText etPlace = (EditText)autocompleteFragmentStart.getView().findViewById(R.id.place_autocomplete_search_input);
        etPlace.setHint("Wpisz punkt początkowy");
        etPlace.setHintTextColor(Color.parseColor("#000000"));
        etPlace.setTextColor(Color.parseColor("#000000"));
        EditText etPlace1 = (EditText)autocompleteFragmentCel.getView().findViewById(R.id.place_autocomplete_search_input);
        etPlace1.setHint("Wpisz punkt końcowy");
        etPlace1.setHintTextColor(Color.parseColor("#000000"));
        etPlace1.setTextColor(Color.parseColor("#000000"));
        autocompleteFragmentStart.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                startLocation=place.getLatLng();
                startMar= mMap.addMarker(new MarkerOptions()
                        .position(startLocation)
                        .draggable(true)
                        .title("Cel")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startLocation.latitude, startLocation.longitude), 12.0f));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });


        autocompleteFragmentCel.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                endLocation=place.getLatLng();
                koniec= mMap.addMarker(new MarkerOptions()
                        .position(endLocation)
                        .draggable(true)
                        .title("Cel")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(endLocation.latitude, endLocation.longitude), 12.0f));
            }

            @Override
            public void onError(Status status) {
                //TO DO
            }
        });

        //
        nawiguj = findViewById(R.id.button2);
        rozpocznij = findViewById(R.id.button7);
        rozpocznij.setVisibility(View.INVISIBLE);
        DetailNav = findViewById(R.id.textView3);
        fabAddSaved = (FloatingActionButton) findViewById(R.id.fabDodajUlubione);
        fabDeleteNav = (FloatingActionButton) findViewById(R.id.fabDeleteNav);
        fabAddSaved.setOnClickListener(this);
        fabDeleteNav.setOnClickListener(this);
        mycontext=this;

        //Navigation menu----
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout.bringToFront();
        navigationView=findViewById(R.id.navID);
        navigationView.setNavigationItemSelectedListener(this);

        nawiguj.setOnClickListener(listener);

        //aktualizowanie lokalizacji
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mycontext);

        //pobieranie pois
        inStream=getResources().openRawResource(R.raw.stacje);
        new MyTask(this).execute();

        //Firebase
        databaseReference=FirebaseDatabase.getInstance().getReference("user1").child("history");
        databaseReference1=FirebaseDatabase.getInstance().getReference("user1").child("saved");
        //Geocoder
        geocoder = new Geocoder(this, Locale.getDefault());
    }

    //słuchacze buttonów pojawiających się po wyznaczeniu trasy(dodanie do ulubionych, koniec trasy itp.)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.fabDeleteNav:
                deleteNav();
                break;
            case R.id.fabDodajUlubione:
                String id1=databaseReference1.push().getKey();
                databaseReference1.child(id1).setValue(currentSaved);
                Toast.makeText(this, "Dodano do ulubionych",Toast.LENGTH_LONG).show();
                fabAddSaved.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }

    }

    //onBackPressed
    @Override
    public void onBackPressed() {
        if(currentPolyline != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Zamknij nawigację")
                    .setMessage("Czy skończyć nawigację?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            deleteNav();
                        }
                    }).create().show();

        }
        else{
            finish();
        }
    }

    public void deleteNav(){
        if(koniec != null){
            koniec.remove();
        }
        if(startMar != null){
            startMar.remove();
        }
        if(koniecMar != null){
            koniecMar.remove();
        }
        if(currentPolyline != null){
            currentPolyline.remove();
        }
        autocompleteFragmentCel.setText("");
        autocompleteFragmentStart.setText("");
        fabDeleteNav.setVisibility(View.INVISIBLE);
        fabAddSaved.setVisibility(View.INVISIBLE);
        DetailNav.setVisibility(View.INVISIBLE);
        nawiguj.setVisibility(View.VISIBLE);
        rozpocznij.setVisibility(View.INVISIBLE);
        autocompleteFragmentStart.getView().setVisibility(View.VISIBLE);
        autocompleteFragmentCel.getView().setVisibility(View.VISIBLE);

        endLocation=new LatLng(0, 0);
        startLocation=new LatLng(0, 0);
    }
    //otwieranie menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //listener navigation menu
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_nawiguj:
                break;
            case R.id.nav_Konto:
                Intent IntentKonto = new Intent(mycontext, logowanie.class);
                startActivity(IntentKonto);
                break;
            case R.id.nav_PokazPoi:
                Intent chooserInt = new Intent(mycontext, POIActivity.class);
                chooserInt.putExtra("state",stateSwitch);
                startActivityForResult(chooserInt,2);
                break;
            case R.id.nav_ulubione:
                Intent IntentPrzejazdy = new Intent(getBaseContext(), ulubione.class);
                startActivity(IntentPrzejazdy);
                break;
            case R.id.nav_info:
                Intent Intentinfo = new Intent(getBaseContext(), TabActivity.class);
                startActivity(Intentinfo);
                break;
            case R.id.nav_stat:
                Intent Intentstat = new Intent(getBaseContext(), statystykiActivity.class);
                startActivity(Intentstat);
                break;
            case R.id.nav_trasy:
                if(flagaTrasyGeoJson==false) {
                    try {
                        layer = new GeoJsonLayer(mMap, R.raw.trasy, getApplicationContext());
                        layer.addLayerToMap();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    layer.removeLayerFromMap();
                }
                flagaTrasyGeoJson=!flagaTrasyGeoJson;
                break;
            }
        //close navigation drawer
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //nawigacja
    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey(getString(R.string.google_maps_key))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            navigate(startLocation,endLocation);
        }
    };

    private void navigate(LatLng start, LatLng end)
    {
        String origin=start.latitude+","+start.longitude;
        String des=end.latitude+","+end.longitude;
        DateTime now = new DateTime();
        try {
            if (end.latitude!=0 && start.latitude!=0) {
                DirectionsResult result = DirectionsApi.newRequest(getGeoContext())
                        .mode(TravelMode.BICYCLING).origin(origin)
                        .destination(des).departureTime(now)
                        .await();
                if (result.routes.length == 0) {
                    Toast.makeText(mycontext, "Nie udało się znaleźć drogi", Toast.LENGTH_SHORT).show();
                    return;
                }

                addMarkersToMap(result, mMap);
                addPolyline(result, mMap);

                currentSaved = new zapisane(start, end, result.routes[0].legs[0].startAddress, result.routes[0].legs[0].endAddress, result.routes[0].legs[0].duration.humanReadable, result.routes[0].legs[0].distance.humanReadable);

                currentHistory = new historia(now.toString(), result.routes[0].legs[0].startAddress, result.routes[0].legs[0].endAddress, result.routes[0].legs[0].duration.humanReadable, result.routes[0].legs[0].distance.humanReadable);
                String id2 = databaseReference.push().getKey();
                databaseReference.child(id2).setValue(currentHistory);

                LatLng middle = new LatLng((start.latitude + end.latitude)/2, (start.longitude + end.longitude)/2);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(middle));
                float dist = (float) Math.sqrt(Math.pow((end.latitude - start.latitude),2) + Math.pow((end.longitude - start.longitude),2));
                float scale = setScale(dist);

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(middle.latitude, middle.longitude), scale));

                autocompleteFragmentStart.getView().setVisibility(View.GONE);
                autocompleteFragmentCel.getView().setVisibility(View.GONE);
                DetailNav.setVisibility(View.VISIBLE);
                DetailNav.setText(getEndLocationTitle(result));
                fabDeleteNav.setVisibility(View.VISIBLE);
                fabAddSaved.setVisibility(View.VISIBLE);
                nawiguj.setVisibility(View.INVISIBLE);
                rozpocznij.setVisibility(View.VISIBLE);
            }
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private float setScale(float dist){
        if (dist < 0.01)
            return 15;
        else if ((dist >= 0.01) && (dist < 0.5))
            return 13;
        else if ((dist >= 0.5) && (dist < 1))
            return 11;
        else if ((dist >= 1) && (dist < 1.5))
            return 9;
        else if ((dist >= 1.5) && (dist < 5))
            return 7;
        else if ((dist >= 5) && (dist < 10))
            return 5;
        else if ((dist >= 10) && (dist < 20))
            return 3;
        else
            return 1;
    }
    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
        //usunięcie poprzednich markerów
        if(koniec != null){
            koniec.remove();
        }
        if(startMar != null){
            startMar.remove();
        }
        if(koniecMar != null){
            koniecMar.remove();
        }
        //dodanie nowych markerów
        startMar=mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].startLocation.lat,results.routes[0].legs[0].startLocation.lng)).title(results.routes[0].legs[0].startAddress));
        koniecMar= mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].endLocation.lat,results.routes[0].legs[0].endLocation.lng)).title(results.routes[0].legs[0].endAddress).snippet(getEndLocationTitle(results)));
    }

    private String getEndLocationTitle(DirectionsResult results){
        return  "Czas :"+ results.routes[0].legs[0].duration.humanReadable + " Dystans :" + results.routes[0].legs[0].distance.humanReadable;
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        if(currentPolyline != null){
            currentPolyline.remove();
        }
        currentPolyline= mMap.addPolyline(new PolylineOptions().addAll(decodedPath));

    }

    @SuppressLint("MissingPermission")
    private void requestUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(50000);
        mFusedLocationClient.requestLocationUpdates(request, callback, null);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setPadding(0, 200, 0, 0);
        // Add a marker in Sydney and move the camera
        LatLng poland = new LatLng(52.138949, 19.515863);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(poland));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(poland.latitude, poland.longitude), 5.0f));

        mMap.setOnMapLongClickListener(this);
        if(checkPermissions()) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationButtonClickListener(this);
            mMap.setOnMyLocationClickListener(this);
        }
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            requestUpdates();
            return true;
        } else {
            requestPermissions();
            return false;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_FINE_LOCATION);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {
                }
            }
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        String lat=String.valueOf(location.getLatitude());
        String lon=String.valueOf(location.getLongitude());
        Log.d("reft", "Current location:\n" + lat+", "+lon);
        //start=location ale nie pobiera zmiennej Location location
    }

    @Override
    public boolean onMyLocationButtonClick() {
        startLocation=currentLocation;
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(startLocation.latitude, startLocation.longitude, 1);
            autocompleteFragmentStart.setText(addresses.get(0).getAddressLine(0));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(startLocation));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(startLocation.latitude, startLocation.longitude), 12.0f));
        } catch (IOException e) {
            autocompleteFragmentStart.setText(startLocation.latitude+","+startLocation.longitude);
        }
        return false;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        endLocation=latLng;
        if(koniec != null){
            koniec.remove();
        }

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(endLocation.latitude, endLocation.longitude, 1);
            autocompleteFragmentCel.setText(addresses.get(0).getAddressLine(0));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(endLocation));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(endLocation.latitude, endLocation.longitude), 14.0f));
        } catch (IOException e) {
            autocompleteFragmentCel.setText(endLocation.latitude+","+endLocation.longitude);
        }

        koniec= mMap.addMarker(new MarkerOptions()
                .position(endLocation)
                .draggable(true)
                .title("Cel")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    //asynchroniczne pobieranie pois z xml
    private static class MyTask extends AsyncTask<Void, Void, String> {

        private WeakReference<MapsActivity> activityReference;

        // only retain a weak reference to the activity
        MyTask(MapsActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            try
            {
                URL url=new URL("http://nextbike.net/maps/nextbike-official.xml");

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                //Document doc = builder.parse(url.openStream());
                Document doc = builder.parse(new InputSource(url.openStream()));
                XPathFactory xPathfactory = XPathFactory.newInstance();
                XPath xpath = xPathfactory.newXPath();
                XPathExpression expr = xpath.compile("/markers/country[@language='pl']/city[@uid='210']");
                Node nodeL = (Node) expr.evaluate(doc, XPathConstants.NODE);

                Element element = (Element)nodeL;

                NodeList nl = element.getChildNodes();

                for (int i = 0; i < nl.getLength(); i++) {
                    Node node = nl.item(i);
                    element = (Element)node;
                    String nazwa=element.getAttribute("name");
                    Double lat=Double.parseDouble(element.getAttribute("lat"));
                    Double lon=Double.parseDouble(element.getAttribute("lng"));
                    String dostRow=element.getAttribute("bikes");

                    vectorVeturilo.add(new stacje(nazwa,lat,lon,dostRow));

                }




               //   http://mapa.um.warszawa.pl/WebServices/SamoobslugoweStacjeNapraw/WGS84/findAll/
                Scanner sc=new Scanner(inStream);
                sc.useDelimiter("[,\\n]");
                String latN,lonN,nazwaN;
                while(sc.hasNext())
                {
                    lonN = sc.next();
                    latN = sc.next();
                    nazwaN = sc.next();
                    vectorNaprawa.add(new stacje(nazwaN,Double.parseDouble(latN),Double.parseDouble(lonN),""));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("blad..");
            }
            return "task finished";
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }

    //pois dodawanie do mapy
    private void readVeturilo() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.latitude, currentLocation.longitude), 15f));
        for (int i=0; i<vectorVeturilo.size();i++){
            vectorVeturiloMarker.add(mMap.addMarker(new MarkerOptions().position(new LatLng(vectorVeturilo.get(i).getLat(),vectorVeturilo.get(i).getLon())).title("Dostępne: "+vectorVeturilo.get(i).getDostRow()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))));
        }
    }

    private void readStationRepair() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.latitude, currentLocation.longitude), 15f));
        for (int i=0; i<vectorNaprawa.size();i++){
            vectorNaprawaMarker.add(mMap.addMarker(new MarkerOptions().position(new LatLng(vectorNaprawa.get(i).getLat(),vectorNaprawa.get(i).getLon())).title(vectorNaprawa.get(i).getNazwa()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
        }
    }

    private void deleteVeturilo(){
        for (int i=0; i<vectorVeturiloMarker.size();i++){
            vectorVeturiloMarker.get(i).remove();
        }
        vectorVeturiloMarker.clear();
    }

    private void deleteStationRepair(){
        for (int i=0; i<vectorNaprawaMarker.size();i++){
            vectorNaprawaMarker.get(i).remove();
        }
        vectorNaprawaMarker.clear();
    }

    //Pois switche
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            String num2 = data.getStringExtra("mail");
            stateSwitch=num2;

            Log.d("myTag", num2);
            if (num2.charAt(0)=='1'){
                readVeturilo();
            }
            else {
                deleteVeturilo();
            }
            if (num2.charAt(1)=='1'){
                readStationRepair();
            }
            else {
                deleteStationRepair();
            }
        }
    }

}
