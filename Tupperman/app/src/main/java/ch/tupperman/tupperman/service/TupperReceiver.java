package ch.tupperman.tupperman.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class TupperReceiver extends BroadcastReceiver {
    private Boolean isOnline = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        isOnline = intent.getBooleanExtra("isOnline", false);
    }

    public Boolean getIsOnline() {
        return isOnline;
    }
}
