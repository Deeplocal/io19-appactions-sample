package com.deeplocal.smores;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FinalFragment extends Fragment {

    public FinalFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_final, container, false);
        Typeface tf = Utils.getTypeface(getContext());
        ((TextView) rootView.findViewById(R.id.final_title_textview)).setTypeface(tf);
        ((TextView) rootView.findViewById(R.id.final_a_textview)).setTypeface(tf);
        ((TextView) rootView.findViewById(R.id.final_b_textview)).setTypeface(tf);
        ((TextView) rootView.findViewById(R.id.final_c_textview)).setTypeface(tf);
        ((TextView) rootView.findViewById(R.id.final_d_textview)).setTypeface(tf);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(Utils.TAG, "FinalFragment onCreate()");
    }
}
