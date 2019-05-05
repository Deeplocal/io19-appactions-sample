package com.deeplocal.smores;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ReviewFragment extends Fragment {

    private TextView crackerTv;
    private TextView marshTv;
    private TextView chocTv;
    private TextView toastTv;
    private TextView quantityTv;

    public ReviewFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // set static instance so we can update the UI based on when this view is active
        ((MainActivity) getActivity()).reviewFragment = ReviewFragment.this;

        View rootView = inflater.inflate(R.layout.fragment_review, container, false);

        Typeface tf = Utils.getTypeface(getContext());

        crackerTv = rootView.findViewById(R.id.review_cracker_textview);
        marshTv = rootView.findViewById(R.id.review_marshmallow_textview);
        chocTv = rootView.findViewById(R.id.review_chocolate_textview);
        toastTv = rootView.findViewById(R.id.review_toasted_textview);
        quantityTv = rootView.findViewById(R.id.review_quantity_textview);

        crackerTv.setTypeface(tf);
        marshTv.setTypeface(tf);
        chocTv.setTypeface(tf);
        toastTv.setTypeface(tf);
        quantityTv.setTypeface(tf);

        ((Button) rootView.findViewById(R.id.confirmorder_button)).setTypeface(tf);
        ((Button) rootView.findViewById(R.id.review_back_button)).setTypeface(tf);

        ((TextView) rootView.findViewById(R.id.review_title_textview)).setTypeface(tf);

        SmoresOrder pendingOrder = ((MainActivity) getActivity()).getPendingOrder();
        updateText(pendingOrder);

        return rootView;
    }

    public void updateText(SmoresOrder pendingOrder) {

        Log.d(Utils.TAG, String.format("ReviewFragment: updateText()"));

        if (pendingOrder == null) {
            Log.e(Utils.TAG, "pendingOrder = null");
            return;
        }

        crackerTv.setText(pendingOrder.getCrackerString());
        marshTv.setText(pendingOrder.getMarshmallowString());
        chocTv.setText(pendingOrder.getChocolateString());
        toastTv.setText(String.format("Toast Level: %d", pendingOrder.toastLevel));
        quantityTv.setText(String.format("Quantity: %d", pendingOrder.quantity));
    }

}
