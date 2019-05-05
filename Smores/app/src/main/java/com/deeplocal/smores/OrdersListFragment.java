package com.deeplocal.smores;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class OrdersListFragment extends Fragment {

    private BaseAdapter prevOrdersAdapter;

    public OrdersListFragment() {
        super();
        prevOrdersAdapter = ((MainActivity) getActivity()).prevOrdersAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate layout
        View rootView = inflater.inflate(R.layout.fragment_orderslist, container, false);

        // set font
        Typeface tf = Utils.getTypeface(getContext());
        TextView emptyView = rootView.findViewById(R.id.orderlist_empty_textview);
        emptyView.setTypeface(tf);

        // setup listview
        ListView ordersList = rootView.findViewById(R.id.orderlist_listview);
        ordersList.setAdapter(prevOrdersAdapter);
        ordersList.setEmptyView(emptyView);

        return rootView;
    }
}
