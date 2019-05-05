package com.deeplocal.smores;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Log.d(Utils.TAG, String.format("get section fragment (position = %d)", position));

        if (position == 0)
            return new OrdersListFragment();

        if (position == 1)
            return new TitleFragment();

        if ((position >= 2) && (position <= 6)) {
            return IngredientFragment.newInstance(position - 2);
        }

        if (position == 7)
            return new ReviewFragment();

        if (position == 8)
            return new FinalFragment();

        return null;
    }

    @Override
    public int getCount() {
        return 9; // prev orders, title screen, 5 ingredients, confirmation, final
    }
}