package com.deeplocal.smores;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class TitleFragment extends Fragment {

    public TitleFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_title, container, false);
        TextView tv = rootView.findViewById(R.id.title_textview);
        tv.setTypeface(Utils.getTypeface(getContext()));
        return rootView;
    }
}
