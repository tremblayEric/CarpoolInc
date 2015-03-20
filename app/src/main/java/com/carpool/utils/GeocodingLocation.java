package com.carpool.utils;

/*
Adapté et tiré de http://javapapers.com/android/android-geocoding-to-get-latitude-longitude-for-an-address/
 */

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

/**
 * Created by Gaëlle on 3/18/2015.
 */
public class GeocodingLocation {
    private static final String TAG = "GeocodingLocation";

    public static void getAddressFromLocation(final List<String> locationAd,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                double[] res;
                LinkedHashSet<double[]> result = new LinkedHashSet<>();
                try {
                    for(String locationAddress: locationAd){
                        res = new double[2];
                        List<Address> addressList = geocoder.getFromLocationName(locationAddress, 1);
                        if (addressList != null && addressList.size() > 0) {
                            Address address = addressList.get(0);
                            res[0] = address.getLatitude();
                            res[1] = address.getLongitude();
                            result.add(res);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (result != null) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("address", result);
                        message.setData(bundle);
                    } else {
                        message.what = 0;
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}
