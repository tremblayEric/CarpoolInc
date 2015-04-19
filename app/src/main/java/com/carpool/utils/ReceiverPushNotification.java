package com.carpool.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.carpool.ui.activities.ConsultationOffreActivity;
import com.carpool.ui.activities.NotificationsActivity;
import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Gaëlle on 4/18/2015.
 */
public class ReceiverPushNotification extends ParsePushBroadcastReceiver {
    @Override
    protected void onPushOpen(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        String jsonData = extras.getString("com.parse.Data");
        String pushStore = null;
        try {
            JSONObject json = new JSONObject(jsonData);
            pushStore = json.getString("offreId");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intentNotification = new Intent(context, ConsultationOffreActivity.class);
        intentNotification.putExtra("offreId", pushStore);
        intentNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentNotification);
    }
}
