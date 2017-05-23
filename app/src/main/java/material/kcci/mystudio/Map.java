package material.kcci.mystudio;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by db2 on 2017-05-23.
 */
//MAP 추가 왜 안되지

public class Map
{
    Context mapContext;
    GoogleMap _map;

    public Map(Context context, GoogleMap map)
    {
        mapContext = context;
        _map = map;
    }

    protected void requestMyLocation() {
        LocationManager manager =
                (LocationManager) mapContext.getSystemService(Context.LOCATION_SERVICE);

        try {
            long minTime = 10000;
            float minDistance = 0;
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime,minDistance,new LocationListener()
                    {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
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
                    }
            );

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                showCurrentLocation(lastLocation);
            }

            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,minTime,minDistance,new LocationListener()
                    {
                        @Override
                        public void onLocationChanged(Location location) {
                            showCurrentLocation(location);
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
                    }
            );
        } catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    private void showCurrentLocation(Location location)
    {
        LatLng curPoint = new LatLng(location.getLatitude(), location.getLongitude());
        _map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));
    }

    protected void showMyLocationMarker(double lat, double lon)
    {
        Log.d("showMyLocation","showMyLocation");
        _map.clear();
        LatLng findDest = new LatLng(lat,lon);
        _map.addMarker(new MarkerOptions().position(findDest).snippet("Lat:"
                + findDest.latitude
                + "Lng:" + findDest.longitude).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("목적지"));
        _map.moveCamera(CameraUpdateFactory.newLatLngZoom(findDest,15));
        _map.animateCamera(CameraUpdateFactory.zoomTo(15), 10000, null);
    }
}