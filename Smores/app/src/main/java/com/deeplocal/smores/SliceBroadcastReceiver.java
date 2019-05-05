package com.deeplocal.smores;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class SliceBroadcastReceiver extends BroadcastReceiver {

    public static String sLastOrderStatus = SmoresOrder.ORDER_STATUS_UNKNOWN;
    public static String EXTRA_ORDER_STATUS = "order_status";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("SliceBroadcastReceiver", "SliceBroadcastReceiver onReceive()");

        if (intent.hasExtra(EXTRA_ORDER_STATUS)) {
            sLastOrderStatus = intent.getStringExtra(EXTRA_ORDER_STATUS);
        }

        context.getContentResolver().notifyChange(Constants.SLICE_URI_LAST_ORDER, null);
    }
}
