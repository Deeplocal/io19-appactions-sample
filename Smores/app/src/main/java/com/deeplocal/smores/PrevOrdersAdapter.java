package com.deeplocal.smores;

import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PrevOrdersAdapter extends BaseAdapter {

    private static final int MAX_PREV_ORDERS = 15; // max number of previous orders to store locally

    MainActivity activity;
    ArrayList<SmoresOrder> prevOrders;

    public PrevOrdersAdapter(MainActivity mainActivity) {
        super();
        this.activity = mainActivity;
        this.prevOrders = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return prevOrders.size();
    }

    @Override
    public Object getItem(int position) {

        if ((position + 1) > prevOrders.size()) {
            return null;
        }

        return prevOrders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();
        View row = inflater.inflate(R.layout.listitem_order, parent, false);

        TextView descLeft = row.findViewById(R.id.orderlist_desc_left_textview);
        TextView descRight = row.findViewById(R.id.orderlist_desc_right_textview);
        TextView datetime = row.findViewById(R.id.orderlist_datetime_textview);
        TextView status = row.findViewById(R.id.orderlist_status_textview);

        SmoresOrder order = prevOrders.get(position);
        descLeft.setText(String.format("%s\n%s\n%s", order.getCrackerString(), order.getMarshmallowString(), order.getChocolateString()));
        descRight.setText(String.format("Toast Level %d\nQuantity %d", order.toastLevel, order.quantity));
        datetime.setText(Utils.getDate(order.lastUpdate));
        status.setText(order.orderStatus);

        Typeface tf = Utils.getTypeface(activity);
        descLeft.setTypeface(tf);
        descRight.setTypeface(tf);
        datetime.setTypeface(tf);
        status.setTypeface(tf);

        return row;
    }

    public void addItem(SmoresOrder order) {
        prevOrders.add(order);
        limitLocalOrders();
        notifyDataSetInvalidated();
    }

    public void addItem(int index, SmoresOrder order) {
        prevOrders.add(index, order);
        limitLocalOrders();
        notifyDataSetInvalidated();
    }

    public void checkUpdate(SmoresOrder order) {
        for (SmoresOrder prevOrder : prevOrders) {
            if (prevOrder.equals(order)) {
                prevOrder.update(order);
                notifyDataSetInvalidated();
                Log.d(Utils.TAG, String.format("prevOrder %s updated", prevOrder._firebaseId));
                break;
            }
        }
    }

    private void limitLocalOrders() {

        // remove old previous order if necessary
        while (this.getCount() > MAX_PREV_ORDERS) {
            prevOrders.remove(MAX_PREV_ORDERS - 1);
        }
    }
}
