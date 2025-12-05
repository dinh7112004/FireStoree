package com.example.md_08_ungdungfivestore.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.md_08_ungdungfivestore.R;
import com.example.md_08_ungdungfivestore.XemChiTiet;
import com.example.md_08_ungdungfivestore.SearchActivity; // ⭐ IMPORT MÀN HÌNH TÌM KIẾM ⭐
import com.example.md_08_ungdungfivestore.adapters.ProductAdapter;
import com.example.md_08_ungdungfivestore.models.Product;
import com.example.md_08_ungdungfivestore.services.ProductApiService;
import com.example.md_08_ungdungfivestore.services.ApiClientYeuThich;
import com.example.md_08_ungdungfivestore.services.YeuThichManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrangChuFragment extends Fragment {

    private static final String TAG = "TrangChuFragment";
    private EditText timKiemEditText;
    private RecyclerView rcvProducts;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private ProductApiService apiService;
    private YeuThichManager yeuThichManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yeuThichManager = new YeuThichManager(ApiClientYeuThich.getYeuThichService(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Đảm bảo R.layout.fragment_trangchu là file layout bạn đã gửi trước đó (trangchu_fragment.xml)
        View view = inflater.inflate(R.layout.fragment_trangchu, container, false);

        timKiemEditText = view.findViewById(R.id.timKiemEditText);
        rcvProducts = view.findViewById(R.id.rcvProducts);
        rcvProducts.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        // ⭐ LOGIC ĐỂ CHUYỂN SANG SEARCHACTIVITY KHI CLICK VÀO EDITTEXT ⭐
        timKiemEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sử dụng getActivity() để lấy context và khởi chạy Activity
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        // ⭐ Quan trọng: Ngăn không cho bàn phím xuất hiện trên Fragment
        // Đảm bảo trong XML (fragment_trangchu.xml) đã có android:focusable="false"
        // và android:clickable="true" cho timKiemEditText.
        // Hoặc bạn có thể setFocusable(false) ở đây nếu cần.
        timKiemEditText.setFocusable(false);
        // ----------------------------------------------------------------------

        adapter = new ProductAdapter(requireContext(), productList, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                if (product != null && product.getId() != null && !product.getId().isEmpty()) {
                    Log.d(TAG, "Item Clicked. Product ID: " + product.getId());

                    Intent intent = new Intent(requireContext(), XemChiTiet.class);
                    intent.putExtra("product", product);
                    startActivity(intent);
                } else {
                    Log.e(TAG, "Error: Product object or ID is null/empty on click.");
                    Toast.makeText(requireContext(), "Lỗi: Sản phẩm không có ID.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAddClick(Product product) {
                if (product != null && product.getId() != null && !product.getId().isEmpty()) {
                    Log.d(TAG, "Add Clicked. Product ID: " + product.getId());
                    addToWishlist(product);
                } else {
                    Log.e(TAG, "Error: Product object or ID is null/empty on add click.");
                    Toast.makeText(requireContext(), "Lỗi: Không thể thêm sản phẩm không có ID.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rcvProducts.setAdapter(adapter);
        setupApiService();
        fetchProducts();

        return view;
    }

    private void addToWishlist(Product product) {
        yeuThichManager.addToWishlist(product.getId(), new YeuThichManager.ToggleCallback() {
            @Override
            public void onSuccess(String message, boolean isAdded) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Lỗi thêm yêu thích: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupApiService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5001/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ProductApiService.class);
    }

    private void fetchProducts() {
        Call<List<Product>> call = apiService.getAllProducts();
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (!isAdded()) return;
                if (response.isSuccessful() && response.body() != null) {

                    // LOG KIỂM TRA ID NGAY SAU KHI NHẬN PHẢN HỒI API
                    if (!response.body().isEmpty()) {
                        Product firstProduct = response.body().get(0);
                        Log.d("ProductCheck_Fetch", "ID Sản phẩm đầu tiên nhận được: " + firstProduct.getId());
                    }
                    // ----------------------------------------------------

                    productList.clear();
                    productList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Lỗi tải sản phẩm: Response not successful or body is null.");
                    Toast.makeText(requireContext(), "Không tải được sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                if (!isAdded()) return;
                Log.e(TAG, "Lỗi mạng khi tải sản phẩm: " + t.getMessage(), t);
                Toast.makeText(requireContext(), "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}