package e.mamtanegi.vehicledetection;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import e.mamtanegi.vehicledetection.Activities.UserTypeActivity;
import e.mamtanegi.vehicledetection.Contants.Constants;

public class CustomerMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;
    GeoQuery geoQuery;
    private GoogleMap mMap;
    private Button btnLogout;
    private Button btnLocationRequest;
    private LatLng pickupLocation;
    private int radius = 1;
    private boolean driverFound = false;
    private String driverFoundId;
    private boolean requestBol = false;
    private Marker driverMarker;
    private DatabaseReference driverLocationRef;
    private ValueEventListener driverLocationListener;
    private Marker pickUpMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerMapActivity.this, UserTypeActivity.class);
                startActivity(intent);
                SharedPrefUtils.setStringPreference(CustomerMapActivity.this, Constants.USER_ID, null);
                finish();
            }
        });
        btnLocationRequest = (Button) findViewById(R.id.request);
        btnLocationRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId;
                if (requestBol) {
                    requestBol = false;
                    geoQuery.removeAllListeners();
                    driverLocationRef.removeEventListener(driverLocationListener);

                    if (driverFoundId != null) {
                        DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("SignupOwners").child(driverFoundId);
                        driverRef.setValue(null);
                        driverFoundId = null;
                    }
                    driverFound = false;
                    radius = 1;
                    userId = SharedPrefUtils.getStringPreference(CustomerMapActivity.this, Constants.USER_ID);
                    if (userId != null) {
                        DatabaseReference customerDatabaseRefrence = FirebaseDatabase.getInstance().getReference("customerRequest");
                        GeoFire geoFire = new GeoFire(customerDatabaseRefrence);
                        geoFire.removeLocation(userId);
                        if (pickUpMarker != null) {
                            pickUpMarker.remove();
                        }
                        btnLocationRequest.setText("Call Uber");
                    }
                } else {
                    requestBol = true;
                    userId = SharedPrefUtils.getStringPreference(CustomerMapActivity.this, Constants.USER_ID);

                    if (userId != null) {
                        DatabaseReference customerDatabaseRefrence = FirebaseDatabase.getInstance().getReference("customerRequest");
                        GeoFire geoFire = new GeoFire(customerDatabaseRefrence);
                        geoFire.setLocation(userId, new GeoLocation(lastLocation.getLatitude(), lastLocation.getLongitude()), new GeoFire.CompletionListener() {
                            @Override
                            public void onComplete(String key, DatabaseError error) {

                            }
                        });
                        pickupLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                        pickUpMarker = mMap.addMarker(new MarkerOptions().position(pickupLocation).title("Pickup Hedd  re"));

                        btnLocationRequest.setText("Getting your Driver...");
                        getClosestDriver();

                    }
                }
            }

        });
    }

    private void getClosestDriver() {
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("DriverAvailable");
        GeoFire geoFire = new GeoFire(driverLocation);

        geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {


            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!driverFound && requestBol) {
                    driverFound = true;
                    driverFoundId = key;
                    DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference().child("SignupOwners").child(driverFoundId);
                    String customerId = SharedPrefUtils.getStringPreference(CustomerMapActivity.this, Constants.USER_ID);
                    HashMap map = new HashMap();
                    map.put(Constants.CUSTOMER_RIDE_ID, customerId);
                    driverRef.updateChildren(map);

                    getDriverLocation();
                    btnLocationRequest.setText("Looking for Driver Location");


                }
            }


            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!driverFound) {
                    radius += 1;
                    getClosestDriver();
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

    }

    private void getDriverLocation() {
        driverLocationRef = FirebaseDatabase.getInstance().getReference().child("driverWorking").child(driverFoundId).child("l");
        driverLocationListener = driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && requestBol) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationlat = 0;
                    double locationlong = 0;
                    btnLocationRequest.setText("Driver Found");
                    if (map.get(0) != null) {
                        locationlat = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null) {
                        locationlat = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng driverLatLong = new LatLng(locationlat, locationlong);
                    if (driverMarker != null) {
                        driverMarker.remove();
                    }
                    Location local = new Location("");
                    local.setLatitude(pickupLocation.latitude);
                    local.setLongitude(pickupLocation.longitude);

                    Location local2 = new Location("");
                    local2.setLatitude(driverLatLong.latitude);
                    local2.setLongitude(driverLatLong.longitude);

                    float distance = local.distanceTo(local2);
                    if (distance < 100) {
                        btnLocationRequest.setText("Driver's Arrived");
                    } else {
                        btnLocationRequest.setText("Driver Found:" + String.valueOf(distance));
                    }
                    btnLocationRequest.setText("Driver Found:" + String.valueOf(distance));

                    driverMarker = mMap.addMarker(new MarkerOptions().position(driverLatLong).title("Your Driver"));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).
                        addOnConnectionFailedListener(this).
                        addApi(LocationServices.API).build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, (com.google.android.gms.location.LocationListener) this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
