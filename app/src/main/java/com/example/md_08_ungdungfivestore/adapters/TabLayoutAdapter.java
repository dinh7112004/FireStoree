package com.example.md_08_ungdungfivestore.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.md_08_ungdungfivestore.fragments.ChoGiaoHangFragment;
import com.example.md_08_ungdungfivestore.fragments.ChoXacNhanFragment;
import com.example.md_08_ungdungfivestore.fragments.DanhGiaFragment;

public class TabLayoutAdapter extends FragmentPagerAdapter {
    private Context context;
    int tabs;

    public TabLayoutAdapter(Context context, FragmentManager fm, int tabs) {
        super(fm);
        this.context = context;
        this.tabs = tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ChoXacNhanFragment choXacNhanFragment = new ChoXacNhanFragment();
                return choXacNhanFragment;
            case 1:
                ChoGiaoHangFragment choGiaoHangFragment = new ChoGiaoHangFragment();
                return choGiaoHangFragment;
            case 2:
                DanhGiaFragment danhGiaFragment = new DanhGiaFragment();
                return danhGiaFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabs;
    }
}