package com.example.pocketdevs.pocketcloset.common;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.example.pocketdevs.pocketcloset.App;


/**
 * Location-related utils.
 *
 *
 */
public class LocationUtils {

    private static final int MAX_LOCATION_AGE_MINUTES = 2;

    /**
     *
     * @param context
     * @param listener
     */
    public static void getLastLocation(final Activity context, final LocationListener listener){
        try {
            final LocationManager locationManager = (LocationManager) App.get().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (location == null) {
                requestSingleLocation(context, listener, locationManager);
            } else {
                //double locationAgeMinutes = ((double) (SystemClock.elapsedRealtimeNanos() - location.getElapsedRealtimeNanos())) / 60000000000L;
                double locationAgeMinutes = 0.0;
                if (locationAgeMinutes > MAX_LOCATION_AGE_MINUTES) {
                    requestSingleLocation(context, listener, locationManager);
                } else {
                    listener.onLocationUpdate(location.getLatitude() + "," + location.getLongitude());
                }
            }
        }catch(Exception e){
            //Nothing to report
        }
    }

    /**
     *
     * @param context
     * @param listener
     * @param locationManager
     */
    private static void requestSingleLocation(final Activity context, final LocationListener listener, final LocationManager locationManager) {
        try {
            IntentFilter locIntentFilter = new IntentFilter("SINGLE_LOCATION_UPDATE_ACTION");
            Intent updateIntent = new Intent("SINGLE_LOCATION_UPDATE_ACTION");
            final PendingIntent singleUpdatePI = PendingIntent.getBroadcast(App.get(), 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            BroadcastReceiver singleUpdateReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    try {
                        context.unregisterReceiver(this);
                        String key = LocationManager.KEY_LOCATION_CHANGED;
                        if (!Utility.isNull(key) && intent.getExtras().get(key) != null) {
                            Location location = (Location) intent.getExtras().get(key);
                            if (location != null && listener != null) {
                                listener.onLocationUpdate(location.getLatitude() + "," + location.getLongitude());
                                locationManager.removeUpdates(singleUpdatePI);
                            }
                        }
                    }catch (Exception e){
                        //Nothing to report
                    }
                }
            };

            App.get().registerReceiver(singleUpdateReceiver, locIntentFilter);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_MEDIUM);

            locationManager.requestSingleUpdate(criteria, singleUpdatePI);
        }catch(Exception e){
            //Nothing to report
        }
    }

    public interface LocationListener {
        void onLocationUpdate(String location);
    }
}