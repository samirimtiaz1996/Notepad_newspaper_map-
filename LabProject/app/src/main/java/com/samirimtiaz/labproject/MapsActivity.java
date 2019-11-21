package com.samirimtiaz.labproject;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    Intent intent;

    protected boolean hasAllPermissionGranted(int[] grantResults){
        for(int check:grantResults){
            if(check==PackageManager.PERMISSION_DENIED){
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==1){
            if(hasAllPermissionGranted(grantResults)){
                for(String currentPermission:permissions){
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {

                        try{
                            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 1000, locationListener);}
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title("Got this location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                Geocoder geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());
                try {
                    String address="";
                    List<Address> addresses=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    if(addresses.size()>0 && addresses!=null){
                        if(addresses.get(0).getCountryName()!=null && addresses.get(0).getSubThoroughfare()!=null && addresses.get(0).getThoroughfare()!=null && addresses.get(0).getLocality()!=null){
                            address=addresses.get(0).getCountryName()+" "+ addresses.get(0).getSubThoroughfare()+" "+ addresses.get(0).getThoroughfare()+" "+addresses.get(0).getLocality();
                            FavPlaceActivity.arrayList.add(address);
                            FavPlaceActivity.latitude.add(latLng.latitude);
                            FavPlaceActivity.longitude.add(latLng.longitude);
                            FavPlaceActivity.sharedPreferences.edit().putString("Name",ObjectSerializer.serialize(FavPlaceActivity.arrayList)).apply();
                            FavPlaceActivity.sharedPreferences.edit().putString("Latitude",ObjectSerializer.serialize(FavPlaceActivity.latitude)).apply();
                            FavPlaceActivity.sharedPreferences.edit().putString("Longitude",ObjectSerializer.serialize(FavPlaceActivity.longitude)).apply();
                            Toast.makeText(MapsActivity.this,"SAVED",Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(MapsActivity.this,"Try again",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(MapsActivity.this,"Try again",Toast.LENGTH_LONG).show();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(getApplicationContext(),FavPlaceActivity.class);
                startActivity(intent);
                finish();
            }
        });

        intent=getIntent();
        if(intent.getBooleanExtra("addData",false)){
            locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            locationListener=new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("Your position"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),10));
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            }
            else {
                try {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1000, locationListener);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        else {
            Location location=new Location(LocationManager.GPS_PROVIDER);
            location.setLatitude(FavPlaceActivity.latitude.get(intent.getIntExtra("dataNumber",-1)));
            location.setLongitude(FavPlaceActivity.longitude.get(intent.getIntExtra("dataNumber",-1)));
            mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title(FavPlaceActivity.arrayList.get(intent.getIntExtra("dataNumber",100))));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),10));

        }
    }

}
