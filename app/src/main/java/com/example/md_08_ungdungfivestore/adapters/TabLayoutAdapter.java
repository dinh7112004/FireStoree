package com.example.md_08_ungdungfivestore.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.md_08_ungdungfivestore.fragments.ChoGiaoHangFragment;
import com.example.md_08_ungdungfivestore.fragments.ChoXacNhanFragment;
import com.example.md_08_ungdungfivestore.fragments.DaGiaoFragment; // ⭐ Import Fragment đã giao
import com.example.md_08_ungdungfivestore.fragments.DaHuyFragment; // ⭐ Import Fragment đã hủy

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
        // Tương ứng với 4 tab đã định nghĩa trong ManDonHang.java:
        // 0: Chờ xác nhận
        // 1: Đang giao
        // 2: Đã giao
        // 3: Đã hủy

        switch (position) {
            case 0:
                // Tab "Chờ xác nhận"
                return new ChoXacNhanFragment();
            case 1:
                // Tab "Đang giao"
                return new ChoGiaoHangFragment();
            case 2:
                // Tab "Đã giao" (Sử dụng DaGiaoFragment để xử lý Đánh giá)
                return new DaGiaoFragment();
            case 3:
                // Tab "Đã hủy" (Sử dụng DaHuyFragment)
                return new DaHuyFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        // Đảm bảo trả về 4 nếu ManDonHang.java đã thiết lập 4 tab
        return tabs;
    }
}