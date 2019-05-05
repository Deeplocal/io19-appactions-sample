package com.deeplocal.smores;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class IngredientFragment extends Fragment {

    private static final String[] OPTS = { "Cracker", "Marshmallow", "Chocolate", "Toast Level", "Quantity" };
    private static final String ARG_WHICH = "WHICH_INGREDIENT";

    public static MainActivity mainActivity;

    private int which = -1;

    public IngredientFragment() {
        super();
    }

    public static IngredientFragment newInstance(int which) {

        IngredientFragment f = new IngredientFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_WHICH, which);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ingredient, container, false);

        which = getArguments().getInt(ARG_WHICH);

        TextView tv = rootView.findViewById(R.id.ingredient_title_textview);
        tv.setText(String.format("Choose Your %s", OPTS[which]));
        tv.setTypeface(Utils.getTypeface(getContext()));

        final ViewPager optionsViewPager = rootView.findViewById(R.id.ingredient_viewpager);
        final PagerAdapter optionsPagerAdapter = new IngredientOptionsPagerAdapter(getChildFragmentManager(), which);
        optionsViewPager.setAdapter(optionsPagerAdapter);
        Log.d(Utils.TAG, "set ingredient viewpager adpater");

        IngredientFragment.mainActivity.currentIngredientSelection[which] = 0;

        optionsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                IngredientFragment.mainActivity.currentIngredientSelection[which] = position;
                Log.d(Utils.TAG, String.format("mainActivity.currentIngredientSelection[%d] = %d", which, position));
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });

        // swipe back with left arrow
        rootView.findViewById(R.id.ingredient_leftarrow).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int i = optionsViewPager.getCurrentItem();
                if (i > 0) {
                    optionsViewPager.setCurrentItem(i - 1);
                }
            }
        });

        // swipe forward with right arrow
        rootView.findViewById(R.id.ingredient_rightarrow).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int i = optionsViewPager.getCurrentItem();
                if (i < (optionsPagerAdapter.getCount() - 1)) {
                    optionsViewPager.setCurrentItem(i + 1);
                }
            }
        });

        return rootView;
    }
}
