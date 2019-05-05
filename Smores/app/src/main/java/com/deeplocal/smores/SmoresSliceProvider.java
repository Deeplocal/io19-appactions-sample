package com.deeplocal.smores;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.util.Log;

import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceProvider;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

// https://developer.android.com/guide/slices/getting-started.html
public class SmoresSliceProvider extends SliceProvider {

    private ListBuilder mListBuilder;
    private Uri mSliceUri;
    private Context mContext;

    @Override
    public boolean onCreateSliceProvider() {
        mContext = getContext();
        return true;
    }

    public Slice onBindSlice(Uri sliceUri) {

        Log.d(Utils.TAG, String.format("sliceUri = %s", sliceUri));

        this.mSliceUri = sliceUri;
        if (mContext == null || this.mSliceUri.getPath() == null) {
            return null;
        }

        switch (this.mSliceUri.getPath()) {

            case "/last-order":

                mListBuilder = createLastOrderSlice();
                break;

            default:

                mListBuilder.addRow(
                        new ListBuilder.RowBuilder()
                                .setPrimaryAction(createActivityAction())
                                .setTitle("URI not found.")
                );
                break;
        }

        return mListBuilder.build();
    }

    private ListBuilder createLastOrderSlice() {

        Log.d(Utils.TAG, "createLastOrderSlice()");

        if (SliceBroadcastReceiver.sLastOrderStatus == SmoresOrder.ORDER_STATUS_UNKNOWN) {
            getLatestOrderStatus();
        }

        ListBuilder lb = new ListBuilder(mContext, this.mSliceUri, ListBuilder.INFINITY);
        lb.addRow(new ListBuilder.RowBuilder()
                .setTitleItem(IconCompat.createWithResource(getContext(), R.drawable.icon),
                        ListBuilder.ICON_IMAGE)
                .setTitle("Order Status: " + SliceBroadcastReceiver.sLastOrderStatus)
                .setPrimaryAction(createActivityAction())
        );

        return lb;
    }

    private void getLatestOrderStatus() {

        Log.d(Utils.TAG, "getLatestOrderStatus()");

        // need this or the slice errors out
        StrictMode.setThreadPolicy(
                new StrictMode.ThreadPolicy.Builder()
                        .permitNetwork()
                        .build()
        );

        final FirebaseFirestore mDb = FirebaseFirestore.getInstance();
        final String userId = Utils.loadUserId(mContext);

        CompletableFuture<SmoresOrder> future = Utils.getLatestOrderFuture(mDb, userId, "pending-orders");
        future.thenAccept(new Consumer<SmoresOrder>() {

            @Override
            public void accept(SmoresOrder smoresOrder) {

                if (smoresOrder == null) {

                    CompletableFuture<SmoresOrder> future = Utils.getLatestOrderFuture(mDb, userId, "completed-orders");
                    future.thenAccept(new Consumer<SmoresOrder>() {

                        @Override
                        public void accept(SmoresOrder smoresOrder) {

                            if (smoresOrder == null) {
                                Log.d(Utils.TAG,"completed-orders no orders");
                                SliceBroadcastReceiver.sLastOrderStatus = Constants.NO_PENDING_ORDERS;
                            } else {
                                Log.d(Utils.TAG, String.format("completed-orders smoresOrder.orderStatus = %s", smoresOrder.orderStatus));
                                SliceBroadcastReceiver.sLastOrderStatus = smoresOrder.orderStatus;
                            }
                            mContext.getContentResolver().notifyChange(Constants.SLICE_URI_LAST_ORDER, null);
                        }
                    });
                    return;
                }

                Log.d(Utils.TAG, String.format("pending-orders smoresOrder.orderStatus = %s", smoresOrder.orderStatus));
                SliceBroadcastReceiver.sLastOrderStatus = smoresOrder.orderStatus;
                mContext.getContentResolver().notifyChange(Constants.SLICE_URI_LAST_ORDER, null);
            }
        });
    }

    private SliceAction createActivityAction() {

        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(MainActivity.SLICE_EXTRA, "last-order");

        return SliceAction.create(PendingIntent.getActivity(mContext, 0, intent, 0),
                IconCompat.createWithResource(mContext, R.drawable.icon),
                ListBuilder.ICON_IMAGE, "Enter app");
    }

    @Override
    public void onSlicePinned(Uri sliceUri) {
        // register any observers that need to be notified of changes to the slice’s data
    }

    @Override
    public void onSliceUnpinned(Uri sliceUri) {
        // don’t forget to unregister any observers to avoid memory leaks
    }
}