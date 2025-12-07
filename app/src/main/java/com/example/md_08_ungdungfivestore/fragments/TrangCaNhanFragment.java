package com.example.md_08_ungdungfivestore.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout; // Sử dụng LinearLayout cho nút

import com.example.md_08_ungdungfivestore.ManDonHang;
import com.example.md_08_ungdungfivestore.R;

public class TrangCaNhanFragment extends Fragment {

    private LinearLayout btnDonHang;

    public TrangCaNhanFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout cho Fragment
        View view = inflater.inflate(R.layout.fragment_trang_ca_nhan, container, false);

        // Ánh xạ nút Đơn hàng
        btnDonHang = view.findViewById(R.id.btnDonHang);

        // Xử lý sự kiện click cho nút Đơn hàng
        btnDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi chạy Activity xem lịch sử đơn hàng
                Intent intent = new Intent(getActivity(), ManDonHang.class);
                startActivity(intent);
                // Có thể thêm Toast nếu cần: Toast.makeText(getActivity(), "Xem Đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện click cho nút Back (nếu Fragment này nằm trong Activity khác)
        view.findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
            }
        });

        // Bạn có thể thêm xử lý cho các nút khác (Thẻ ngân hàng, Thông tin cá nhân, Đăng xuất) tại đây

        return view;
    }
}