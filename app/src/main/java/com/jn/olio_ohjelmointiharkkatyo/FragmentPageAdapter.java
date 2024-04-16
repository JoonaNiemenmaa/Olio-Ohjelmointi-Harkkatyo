package com.jn.olio_ohjelmointiharkkatyo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentPageAdapter extends FragmentStateAdapter {
    public FragmentPageAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DisplayMunicipalityDataFragment();
            case 1:
                return new CompareFragment();
            case 2:
                return new QuizFragment();
        }
        return new DisplayMunicipalityDataFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

