package com.example.namayesh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class NamayeshReciever extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) != null) {
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                if (MoviePlayActivity.ringingState != null) {
                    MoviePlayActivity.ringingState.setValue(true);
                }
                /*Toast.makeText(context, "ring", Toast.LENGTH_SHORT).show();*/
            } else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                if (MoviePlayActivity.ringingState != null) {
                    MoviePlayActivity.ringingState.setValue(false);
                }
                /*Toast.makeText(context, "Idle", Toast.LENGTH_SHORT).show();*/
            }
        }


        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            if (networkInfo.getType() == connectivityManager.TYPE_WIFI) {
                SplashActivity.networkStatus.setValue(1);
                /*Toast.makeText(context, "wifi", Toast.LENGTH_LONG).show();*/
            } else if (networkInfo.getType() == connectivityManager.TYPE_MOBILE) {
                SplashActivity.networkStatus.setValue(2);
                /*Toast.makeText(context, "data", Toast.LENGTH_LONG).show();*/
            }
        } else {
            SplashActivity.networkStatus.setValue(0);
            /*Toast.makeText(context, "disConnect", Toast.LENGTH_LONG).show();*/
        }


    }

}
