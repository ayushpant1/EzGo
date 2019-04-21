package e.mamtanegi.vehicledetection;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import e.mamtanegi.vehicledetection.Activities.DriverSettingsActivity;
import e.mamtanegi.vehicledetection.Activities.UserTypeActivity;
import e.mamtanegi.vehicledetection.Contants.Constants;

public class DriverMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, RoutingListener {

    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};
    GoogleApiClient googleApiClient;
    Location lastLocation;
    LocationRequest locationRequest;
    DatabaseReference assignedCustomerLocationRef;
    private GoogleMap mMap;
    private Button btnLogout;
    private String customerId = "";
    private Marker pickupMarker;
    private DatabaseReference assignedCustomerRef;
    private ValueEventListener assignedCustomerLocationRefListener;
    private boolean isLoggingOut = false;
    private TextView tvCustomerName;
    private TextView tvCustomerPhoneNo;
    private ImageView imgCustomerProfilePic;
    private LinearLayout llcustomerInfo;
    private DatabaseReference customerDatabase;
    private Button btnSettings;
    private List<Polyline> polylines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        tvCustomerName = (TextView) findViewById(R.id.tv_customer_name);
        tvCustomerPhoneNo = (TextView) findViewById(R.id.tv_customer_phone_no);
        polylines = new ArrayList<>();
        imgCustomerProfilePic = (ImageView) findViewById(R.id.customer_profile_image);

        llcustomerInfo = (LinearLayout) findViewById(R.id.customer_info);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnSettings = (Button) findViewById(R.id.btn_settings);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLoggingOut = true;
                disconnectDriver();
                Intent intent = new Intent(DriverMapActivity.this, UserTypeActivity.class);
                startActivity(intent);
                SharedPrefUtils.setStringPreference(DriverMapActivity.this, Constants.USER_ID, null);
                finish();
            }
        });
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverMapActivity.this, DriverSettingsActivity.class);
                startActivity(intent);
            }
        });
        getAssignedCustomer();
    }

    private void getAssignedCustomer() {
        String driverIdId = SharedPrefUtils.getStringPreference(DriverMapActivity.this, Constants.USER_ID);
        DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance().getReference().child("SignupOwners").child(driverIdId).child(Constants.CUSTOMER_RIDE_ID);
        assignedCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    customerId = dataSnapshot.getValue().toString();
                    getAssignedCustomerPickupLocation();
                    getAssignedCustomerInfo();
                } else {
                    erasePoltlines();
                    customerId = "";
                    if (pickupMarker != null) {
                        pickupMarker.remove();
                    }
                    if (assignedCustomerLocationRefListener != null) {
                        assignedCustomerLocationRef.removeEventListener(assignedCustomerLocationRefListener);
                    }

                    llcustomerInfo.setVisibility(View.GONE);
                    tvCustomerName.setText("");
                    tvCustomerPhoneNo.setText("");
                    imgCustomerProfilePic.setImageDrawable(getResources().getDrawable(R.drawable.user));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getAssignedCustomerInfo() {
        llcustomerInfo.setVisibility(View.VISIBLE);
        customerDatabase = FirebaseDatabase.getInstance().getReference().child("SignupUsers").child(customerId);
        Utils.showProgressDialog(this, true);
        customerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    Utils.dismissProgressDialog();
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if (map.get("email") != null) {
                        String email = map.get("email").toString();
                        tvCustomerName.setText(email);
                    }
                    if (map.get("phoneno") != null) {
                        String phoneNo = map.get("phoneno").toString();
                        tvCustomerPhoneNo.setText(phoneNo);

                    }
                    if (map.get("profileImageUri") != null) {
                        String profileUrl = map.get("profileImageUri").toString();
                        Glide.with(getApplication()).load(profileUrl).into(imgCustomerProfilePic);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAssignedCustomerPickupLocation() {
        assignedCustomerLocationRef = FirebaseDatabase.getInstance().getReference().child("customerRequest").child(customerId).child("l");
        assignedCustomerLocationRefListener = assignedCustomerLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && customerId.equals("")) {
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationlat = 0;
                    double locationlong = 0;
                    if (map.get(0) != null) {
                        locationlat = Double.parseDouble(map.get(0).toString());
                    }
                    if (map.get(1) != null) {
                        locationlat = Double.parseDouble(map.get(1).toString());
                    }
                    LatLng pickupLatlong = new LatLng(locationlat, locationlong);

                    pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLatlong).title("Pickup Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.pickup_icon)));
                    getRouteToMarker(pickupLatlong);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getRouteToMarker(LatLng pickupLatlong) {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), pickupLatlong)
                .build();
        routing.execute();
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
        mMap.animateCamera(CameraUpdateFactory.zoomTo(25));
        String userId = SharedPrefUtils.getStringPreference(this, Constants.USER_ID);
        GeoFire geoFireAvailable = null;
        GeoFire geoFireWorking = null;
        DatabaseReference refAvailable;
        DatabaseReference refWorking;
        if (userId != null) {

            refAvailable = FirebaseDatabase.getInstance().getReference("DriverAvailable");
            refWorking = FirebaseDatabase.getInstance().getReference("driverWorking");
            geoFireAvailable = new GeoFire(refAvailable);
            geoFireWorking = new GeoFire(refWorking);

        }

        switch (customerId) {
            case "":
                if (geoFireWorking != null) {
                    geoFireWorking.removeLocation(userId);
                }
                geoFireAvailable.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {

                    }
                });
                break;
            default:
                geoFireAvailable.removeLocation(userId);
                geoFireWorking.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {

                    }
                });
                break;
        }


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

    private void disconnectDriver() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        String userId = SharedPrefUtils.getStringPreference(this, Constants.USER_ID);
        if (userId != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("DriverAvailable");
            GeoFire geoFire = new GeoFire(databaseReference);
            geoFire.removeLocation(userId);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isLoggingOut) {

            disconnectDriver();
        }

    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRoutingCancelled() {

    }

    private void erasePoltlines() {
        for (Polyline polyline : polylines) {
            polyline.remove();
        }
        polylines.clear();
    }
}
