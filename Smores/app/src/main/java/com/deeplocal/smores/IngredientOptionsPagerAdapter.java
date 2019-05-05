package com.deeplocal.smores;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class IngredientOptionsPagerAdapter extends FragmentPagerAdapter {

    private static final String[][] SMORES_INGR_LABELS = {
            { "Honey Graham", "Chocolate", "Gluten Free" }, // cracker
            { "Vanilla", "Chocolate" }, // marshmallow
            { "Milk", "Dark", "Oreo" }, // chocolate
            { "Low", "Medium", "High" }, // toast level
            { null, null, null } // quantity 1-3
    };

    private static final int[][] SMORES_IMAGE_RES = {
            { R.drawable.cracker_honey, R.drawable.cracker_chocolate, R.drawable.cracker_gf },
            { R.drawable.marshmallow_vanilla, R.drawable.marshmallow_chocolate },
            { R.drawable.chocolate_milk, R.drawable.chocolate_dark, R.drawable.chocolate_oreo },
            { R.drawable.toast_low, R.drawable.toast_med, R.drawable.toast_high },
            { R.drawable.nums_1, R.drawable.nums_2, R.drawable.nums_3 }
    };

    private int which;

    public IngredientOptionsPagerAdapter(FragmentManager fm, int which) {
        super(fm);
        this.which = which;
    }

    @Override
    public Fragment getItem(int position) {
        return IngredientOptionFragment.newInstance(SMORES_INGR_LABELS[this.which][position], SMORES_IMAGE_RES[this.which][position]);
    }

    @Override
    public int getCount() {
        return SMORES_INGR_LABELS[this.which].length;
    }
}
