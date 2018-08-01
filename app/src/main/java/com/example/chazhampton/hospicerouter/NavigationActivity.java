package com.example.chazhampton.hospicerouter;
//Working thanks to jeff carlson jc&boiys

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.location.Address;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;import java.util.Calendar;


// classes needed to initialize map
// classes needed to add location layer
// classes needed to add a marker
// classes to calculate a route
// classes needed to launch navigation UI


// classes needed to add location layer


// classes needed to add a marker


// classes to calculate a route


// classes needed to launch navigation UI


/*

public class NavigationActivity extends AppCompatActivity implements LocationEngineListener, PermissionsListener {


    private MapView mapView;

    // variables for adding location layer
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationPlugin;
    private LocationEngine locationEngine;
    private Location originLocation;


    // variables for adding a marker
    private Marker destinationMarker;
    private LatLng originCoord;
    private LatLng destinationCoord;


    // variables for calculating and drawing a route
    private Point originPosition;
    private Point destinationPosition;
    private DirectionsRoute currentRoute;
    private Long currentRoute_distance;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    private String myUserEmailVal;


    private Button button;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        String myVal = getIntent().getStringExtra("patientvalue");



        String[] split = myVal.split("Patient address:");
        String firstSubString = split[0];
        String secondSubString = split[1];
        String[] split_second = split[1].split("Patient Comments:");
        String patientAddress = split_second[0];//This will be out address string..
        String fourthSubString = split_second[1];

        Geocoder geocoder;
        List<Address> addresses = new List<Address>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Address> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] ts) {
                return null;
            }

            @Override
            public boolean add(Address address) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends Address> collection) {
                return false;
            }

            @Override
            public boolean addAll(int i, @NonNull Collection<? extends Address> collection) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Address get(int i) {
                return null;
            }

            @Override
            public Address set(int i, Address address) {
                return null;
            }

            @Override
            public void add(int i, Address address) {

            }

            @Override
            public Address remove(int i) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<Address> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<Address> listIterator(int i) {
                return null;
            }

            @NonNull
            @Override
            public List<Address> subList(int i, int i1) {
                return null;
            }
        };
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocationName(patientAddress,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        Double address_lat = addresses.get(0).getLatitude();
        Double address_long = addresses.get(0).getLongitude();

        System.out.println("*just before getMapAsync and then onMapReady called");


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {
                System.out.println("*inside onMapReady ");
                //startService(mapView);


                map = mapboxMap;
                System.out.println("Before enable location plugin");
                enableLocationPlugin();
                System.out.println("after enable location plugin");


                System.out.println("*inside onMapReady before originCoord");

                if (originLocation.getLatitude() == 0){
                    System.out.println("getLat is null!!!!");
                }

                System.out.println("*inside onMapReady Lat is bellow as double ");
                System.out.println("*inside onMapReady Lat:" + originLocation.getLatitude() + "Lng :" + originLocation.getLongitude());

                originCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());

                System.out.println("*inside onMapReady after origin Coord");

                ///////////////
                if (destinationMarker != null) {
                    System.out.println("*inside if loop if (destinationMarker != null)");
                    mapboxMap.removeMarker(destinationMarker);
                }
                System.out.println("*inside onMapReady not using if (dest... ) loop");

                //destinationCoord = point;
                System.out.println("*inside onMapReady before newLatLng");

                LatLng p_location = new LatLng(address_lat,address_long);
                System.out.println("*inside onMapReady aft LatLng");

                destinationCoord = p_location;
                System.out.println("*inside onMapReady before destinationMarker");

                destinationMarker = mapboxMap.addMarker(new MarkerOptions().position(destinationCoord) );
                System.out.println("*inside onMapReady after destination marker");


                //destinationPosition = Point.fromLngLat(destinationCoord.getLongitude(), destinationCoord.getLatitude());
                destinationPosition = Point.fromLngLat(address_long,address_lat);
                originPosition = Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude());
                getRoute(originPosition, destinationPosition);


                button = findViewById(R.id.startButton);
                System.out.println("*in Async onMapReady where making button blue");
                button.setEnabled(true);
                button.setBackgroundResource(R.color.mapboxBlue);
                Date currentTime = Calendar.getInstance().getTime();



                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("users");
                        String myUserEmailVal = getIntent().getStringExtra("userEmailKey");
                        System.out.println(myUserEmailVal + "***** EmailVal in coute calc add");
                        System.out.println(currentRoute_distance + "** route distance");
                        mRef.child(myUserEmailVal).child("userMiles").child("trips").child(currentTime.toString()).push().setValue(currentRoute_distance);
                        mRef.child(myUserEmailVal).child("userMiles").addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                System.out.println("*in Async onMapReady in addListinerForSingleValue in onDataChange before database");
                                Long Miles = (Long) dataSnapshot.getValue();
                                Long MilesAfter = (Miles + currentRoute_distance);
                                dataSnapshot.getRef().setValue(MilesAfter);
                                System.out.println("*in Async onMapReady in addListinerForSingleValue in onDataChange after database");

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}});

                        System.out.println("*in Async onMapReady in addListinerForSingleValue before simulateRoute true");

                        boolean simulateRoute = true;
                        System.out.println("*in Async onMapReady in addListinerForSingleValue after simulateRoute true");

                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(currentRoute)

                                .shouldSimulateRoute(simulateRoute)
                                .build();
                        System.out.println("*in Async onMapReady in addListinerForSingleValue after .builder() & before .startNavigation");


                        // Call this method with Context from within an Activity
                        NavigationLauncher.startNavigation(NavigationActivity.this, options);
                        System.out.println("*in Async onMapReady in addListinerForSingleValue after .builder() & after .startNavigation");

                    }
                });


            }


            ;
        });


    }


    public void startService(View v) {
        String input = "HelloWorld";

        Intent serviceIntent = new Intent(this, Serviceroute.class);
        serviceIntent.putExtra("inputExtra", input);

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, Serviceroute.class);
        stopService(serviceIntent);
    }


    private void getRoute(Point origin, Point destination) {
        System.out.println("* inside getRoute");
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);
                        currentRoute_distance = (Long)  Math.round(((response.body().routes().get(0).distance())/1609.344));//Convert meters to miles..
                        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@-->");
                        System.out.println(currentRoute_distance);

/*
                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("users");
                        String myUserEmailVal = getIntent().getStringExtra("userEmailKey");
                        System.out.println(myUserEmailVal + "***** EmailVal in coute calc add");
                        mRef.child(myUserEmailVal).child("userMiles").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Long Miles = (Long) dataSnapshot.getValue();
                                Long MilesAfter = (Miles + currentRoute_distance);
                                dataSnapshot.getRef().setValue(MilesAfter);}

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}});


                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, map, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);

                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationPlugin() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Create an instance of LOST location engine
            initializeLocationEngine();

            locationPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
            locationPlugin.setRenderMode(RenderMode.COMPASS);
            System.out.println("** Creating instance of LOST location engine");
        } else {
            System.out.println("** NOT IN LOST location loop");
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @TargetApi(23)
    private void initializeLocationEngine() {
        System.out.println("Inside initiize location");

        LocationEngineProvider locationEngineProvider = new LocationEngineProvider(this);
        locationEngine = locationEngineProvider.obtainBestLocationEngineAvailable();
       // locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY); **REPLACED WITH BALANCED to try AND FIX LOCAtiON
        locationEngine.setPriority(LocationEnginePriority.BALANCED_POWER_ACCURACY);
        locationEngine.activate();

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            System.out.println("Permission not enables for coarse location");
        }
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Inside initiize location >= SDK_INT");

            return  ;
        }
        System.out.println("Inside initiize location before lastlocation = get last");

        Location lastLocation = locationEngine.getLastLocation();
        System.out.println("after initiize location before lastlocation = get last");

        if (lastLocation != null) {
            System.out.println("Inside initiize location lastlocation !=null");

            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        } else {
            System.out.println("Inside initiize location lastlocation == null");

            locationEngine.addLocationEngineListener(this);
        }
    }

    private void setCameraPosition(Location location) {
        System.out.println("Inside Set camera position");

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 13));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationPlugin();
        } else {
            finish();
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            originLocation = location;
            setCameraPosition(location);
            locationEngine.removeLocationEngineListener(this);
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

}
*/

public class NavigationActivity extends AppCompatActivity implements LocationEngineListener, PermissionsListener {


    private MapView mapView;

    // variables for adding location layer
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationLayerPlugin locationPlugin;
    private LocationEngine locationEngine;
    private Location originLocation;


    // variables for adding a marker
    private Marker destinationMarker;
    private LatLng originCoord;
    private LatLng destinationCoord;


    // variables for calculating and drawing a route
    private Point originPosition;
    private Point destinationPosition;
    private DirectionsRoute currentRoute;
    private static final String TAG = "DirectionsActivity";
    private NavigationMapRoute navigationMapRoute;
    private Long currentRoute_distance;

    //String myVal = getIntent().getStringExtra("patientvalue"); //****MAYEB THIS SHOULD BE HERE


    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_map);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        String myVal = getIntent().getStringExtra("patientvalue");


        String[] split = myVal.split("Patient address:");
        String firstSubString = split[0];
        String secondSubString = split[1];
        String[] split_second = split[1].split("Patient Comments:");
        String patientAddress = split_second[0];//This will be out address string..
        String fourthSubString = split_second[1];


        Geocoder geocoder;
        List<Address> addresses = new List<Address>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Address> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] ts) {
                return null;
            }

            @Override
            public boolean add(Address address) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends Address> collection) {
                return false;
            }

            @Override
            public boolean addAll(int i, @NonNull Collection<? extends Address> collection) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public Address get(int i) {
                return null;
            }

            @Override
            public Address set(int i, Address address) {
                return null;
            }

            @Override
            public void add(int i, Address address) {

            }

            @Override
            public Address remove(int i) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<Address> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<Address> listIterator(int i) {
                return null;
            }

            @NonNull
            @Override
            public List<Address> subList(int i, int i1) {
                return null;
            }
        };
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocationName(patientAddress,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);// If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        Double address_lat = addresses.get(0).getLatitude();
        Double address_long = addresses.get(0).getLongitude();

        System.out.println("*just before getMapAsync and then onMapReady called");


        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {


                map = mapboxMap;
                enableLocationPlugin();


                originCoord = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());
                System.out.println("*inside onMapReady after origin Coord");

                ///////////////
                if (destinationMarker != null) {
                    System.out.println("*inside if loop if (destinationMarker != null)");
                    mapboxMap.removeMarker(destinationMarker);
                }
                System.out.println("*inside onMapReady not using if (dest... ) loop");

                //destinationCoord = point;
                System.out.println("*inside onMapReady before newLatLng");

                LatLng p_location = new LatLng(address_lat,address_long);
                System.out.println("*inside onMapReady aft LatLng");

                destinationCoord = p_location;
                System.out.println("*inside onMapReady before destinationMarker");

                destinationMarker = mapboxMap.addMarker(new MarkerOptions().position(destinationCoord) );
                System.out.println("*inside onMapReady after destination marker");


                //destinationPosition = Point.fromLngLat(destinationCoord.getLongitude(), destinationCoord.getLatitude());
                destinationPosition = Point.fromLngLat(address_long,address_lat);
                originPosition = Point.fromLngLat(originCoord.getLongitude(), originCoord.getLatitude());
                getRoute(originPosition, destinationPosition);


                button = findViewById(R.id.startButton);
                System.out.println("*in Async onMapReady where making button blue");
                button.setEnabled(true);
                button.setBackgroundResource(R.color.mapboxBlue);
                Date currentTime = Calendar.getInstance().getTime();



                button = findViewById(R.id.startButton);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("users");
                        String myUserEmailVal = getIntent().getStringExtra("userEmailKey");
                        System.out.println(myUserEmailVal + "***** EmailVal in coute calc add");
                        System.out.println(currentRoute_distance + "** route distance");
                        //mRef.child(myUserEmailVal).child("userMiles").setValue(currentRoute_distance);
                        //mRef.child(myUserEmailVal).child("userMiles").child("trips").child(currentTime.toString()).push().setValue(currentRoute_distance);
                        mRef.child(myUserEmailVal).child("userMiles").addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                System.out.println("*in Async onMapReady in addListinerForSingleValue in onDataChange before database");

                                //mRef.child(myUserEmailVal).child("userMiles").setValue(currentRoute_distance);

                                Long Miles = (Long) dataSnapshot.getValue();
                                Long MilesAfter = (Miles + currentRoute_distance);
                                dataSnapshot.getRef().setValue(MilesAfter);
                                System.out.println("*in Async onMapReady in addListinerForSingleValue in onDataChange after database");
                                mRef.child(myUserEmailVal).child("trips").child(currentTime.toString()).push().setValue(currentRoute_distance);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {}});

                        System.out.println("*in Async onMapReady in addListinerForSingleValue before simulateRoute true");

                        boolean simulateRoute = true;
                        System.out.println("*in Async onMapReady in addListinerForSingleValue after simulateRoute true");

                        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                .directionsRoute(currentRoute)

                                .shouldSimulateRoute(simulateRoute)
                                .build();
                        System.out.println("*in Async onMapReady in addListinerForSingleValue after .builder() & before .startNavigation");


                        // Call this method with Context from within an Activity
                        NavigationLauncher.startNavigation(NavigationActivity.this, options);
                        System.out.println("*in Async onMapReady in addListinerForSingleValue after .builder() & after .startNavigation");

                    }
                });


            }


            ;
        });





}

    public void startService(View v) {
        String input = "HelloWorld";

        Intent serviceIntent = new Intent(this, Serviceroute.class);
        serviceIntent.putExtra("inputExtra", input);

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, Serviceroute.class);
        stopService(serviceIntent);
    }


    private void getRoute(Point origin, Point destination) {
        NavigationRoute.builder(this)
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        // You can get the generic HTTP info about the response
                        Log.d(TAG, "Response code: " + response.code());
                        if (response.body() == null) {
                            Log.e(TAG, "No routes found, make sure you set the right user and access token.");
                            return;
                        } else if (response.body().routes().size() < 1) {
                            Log.e(TAG, "No routes found");
                            return;
                        }

                        currentRoute = response.body().routes().get(0);
                        currentRoute_distance = (Long)  Math.round(((response.body().routes().get(0).distance())/1609.344));//Convert meters to miles..
                        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@-->");
                        System.out.println(currentRoute_distance);

                        // Draw the route on the map
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, map, R.style.NavigationMapRoute);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                        Log.e(TAG, "Error: " + throwable.getMessage());
                    }
                });
    }


    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationPlugin() {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Create an instance of LOST location engine
            initializeLocationEngine();

            locationPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
            locationPlugin.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings( {"MissingPermission"})
    private void initializeLocationEngine() {
        LocationEngineProvider locationEngineProvider = new LocationEngineProvider(this);
        locationEngine = locationEngineProvider.obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            originLocation = lastLocation;
            setCameraPosition(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }

    private void setCameraPosition(Location location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 13));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationPlugin();
        } else {
            finish();
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            originLocation = location;
            setCameraPosition(location);
            locationEngine.removeLocationEngineListener(this);
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


}
