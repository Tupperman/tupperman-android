package ch.tupperman.tupperman.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class TupperReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String json = intent.getStringExtra("json");
        Toast.makeText(context, json, Toast.LENGTH_LONG).show();
    }
}
