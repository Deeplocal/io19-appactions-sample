package com.deeplocal.smores;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class IngredientOptionFragment extends Fragment {

    public static final String ARG_IMAGE_RESID = "image_resource_id";
    public static final String ARG_LABEL_STRING = "label_string";

    // required empty public constructor
    public IngredientOptionFragment() {}

    public static IngredientOptionFragment newInstance(String label, int resId) {
        IngredientOptionFragment fragment = new IngredientOptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LABEL_STRING, label);
        args.putInt(ARG_IMAGE_RESID, resId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(Utils.TAG, "creating ingredient option view");

        View view = inflater.inflate(R.layout.fragment_ingredient_option, container, false);

        Bundle b = getArguments();
        if (b != null) {

            ImageView imageView = view.findViewById(R.id.ingredient_option_imageview);
            imageView.setImageDrawable(getResources().getDrawable(b.getInt(ARG_IMAGE_RESID), getActivity().getTheme()));

            TextView textView = view.findViewById(R.id.ingredient_option_textview);
            String label = b.getString(ARG_LABEL_STRING);
            if (label == null) {
                textView.setVisibility(View.INVISIBLE);
            } else {
                textView.setText(label);
                textView.setTypeface(Utils.getTypeface(getContext()));
            }
        }

        return view;
    }
}
