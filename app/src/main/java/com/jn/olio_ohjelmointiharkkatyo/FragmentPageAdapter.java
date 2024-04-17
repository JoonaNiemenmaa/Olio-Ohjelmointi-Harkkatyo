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
                return new DataFragment();
            case 1:
                return new CompareFragment();
        }
        return new DataFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

