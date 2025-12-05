package com.example.md_08_ungdungfivestore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.md_08_ungdungfivestore.adapters.SearchSuggestionAdapter;
import com.example.md_08_ungdungfivestore.adapters.ProductAdapter;
import com.example.md_08_ungdungfivestore.fragments.SelectOptionsBottomSheetFragment;
import com.example.md_08_ungdungfivestore.models.Product;
import com.example.md_08_ungdungfivestore.services.SearchApiClient;
import com.example.md_08_ungdungfivestore.services.SearchApi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity
        implements SearchSuggestionAdapter.OnSuggestionClickListener, ProductAdapter.OnItemClickListener {

    private EditText edtSearchQuery;
    private ImageView btnBack;
    private RecyclerView rvCategoryChips;
    private RecyclerView rvSearchSuggestions;
    private RecyclerView rvSearchResults;

    private SearchSuggestionAdapter suggestionAdapter;
    private ProductAdapter productAdapter;

    private SearchApi searchApi;

    private static final String PREFS_NAME = "SearchHistory";
    private static final String HISTORY_KEY = "queries";
    private static final int MAX_HISTORY = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Khởi tạo API Service
        searchApi = SearchApiClient.getInstance().getSearchApi();

        // 1. Khởi tạo Views
        edtSearchQuery = findViewById(R.id.edt_search_query);
        btnBack = findViewById(R.id.btn_back);
        rvCategoryChips = findViewById(R.id.rv_category_chips);
        rvSearchSuggestions = findViewById(R.id.rv_search_suggestions);
        rvSearchResults = findViewById(R.id.rv_search_results);

        // 2. Thiết lập Nút Back
        btnBack.setOnClickListener(v -> finish());

        // 3. Thiết lập Adapters và RecyclerViews
        setupRecyclerViews();

        // 4. Xử lý logic Tìm kiếm
        setupSearchLogic();

        // 5. ⭐ GỌI HÀM MỚI: Hiển thị cả Lịch sử và Sản phẩm ban đầu khi mới mở màn hình ⭐
        displayCombinedInitialView();
    }

    private void setupRecyclerViews() {
        // Lịch sử dùng LinearLayoutManager
        rvSearchSuggestions.setLayoutManager(new LinearLayoutManager(this));
        suggestionAdapter = new SearchSuggestionAdapter(getSearchHistory(), this);
        rvSearchSuggestions.setAdapter(suggestionAdapter);

        // Sản phẩm dùng GridLayoutManager
        rvSearchResults.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(this, Collections.emptyList(), this);
        rvSearchResults.setAdapter(productAdapter);

        if (rvCategoryChips != null) {
            rvCategoryChips.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        }
    }

    private void setupSearchLogic() {
        // Xử lý khi nhấn nút SEARCH trên bàn phím (Vẫn giữ)
        edtSearchQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = v.getText().toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query); // Gọi hàm tìm kiếm và lưu lịch sử
                    return true;
                }
            }
            return false;
        });

        // Xử lý khi người dùng gõ chữ (Live Search)
        edtSearchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.length() == 0) {
                    // Khi ô tìm kiếm trống, hiển thị CẢ LỊCH SỬ VÀ SẢN PHẨM
                    displayCombinedInitialView();
                } else {
                    // Khi gõ chữ, chỉ hiển thị Live Search Results
                    performLiveSearch(query);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onSuggestionClick(String query) {
        edtSearchQuery.setText(query);
        edtSearchQuery.setSelection(query.length());
        performSearch(query);
    }

    // TRONG SearchActivity.java

    @Override
    public void onItemClick(Product product) {
        // 1. Mở màn hình XemChiTiet và truyền đối tượng Product
        Intent intent = new Intent(SearchActivity.this, XemChiTiet.class);
        // Đảm bảo đối tượng Product đã implements Serializable hoặc Parcelable
        intent.putExtra("product", product);
        startActivity(intent);
    }

    @Override
    public void onAddClick(Product product) {
        Intent intent = new Intent(SearchActivity.this, XemChiTiet.class);
        // Đảm bảo đối tượng Product đã implements Serializable hoặc Parcelable
        intent.putExtra("product", product);
        startActivity(intent);
    }

    private void saveSearchQuery(String query) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> history = prefs.getStringSet(HISTORY_KEY, new LinkedHashSet<>());

        history.remove(query);
        Set<String> newHistory = new LinkedHashSet<>();
        newHistory.add(query);
        newHistory.addAll(history);

        if (newHistory.size() > MAX_HISTORY) {
            newHistory = new LinkedHashSet<>(new ArrayList<>(newHistory).subList(0, MAX_HISTORY));
        }

        prefs.edit().putStringSet(HISTORY_KEY, newHistory).apply();
    }

    private List<String> getSearchHistory() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> history = prefs.getStringSet(HISTORY_KEY, new LinkedHashSet<>());
        return new ArrayList<>(history);
    }

    /**
     * HÀM CHÍNH: Hiển thị đồng thời Lịch sử Tìm kiếm và Sản phẩm gợi ý (All Products).
     * Được gọi khi ô tìm kiếm trống.
     */
    private void displayCombinedInitialView() {
        // 1. Hiển thị cả hai RecyclerView
        rvSearchSuggestions.setVisibility(View.VISIBLE);
        rvSearchResults.setVisibility(View.VISIBLE);

        // 2. Cập nhật lịch sử tìm kiếm
        suggestionAdapter.updateSuggestions(getSearchHistory());

        // 3. Tải tất cả sản phẩm vào rvSearchResults
        searchApi.searchProducts("").enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productAdapter.setProducts(response.body());
                } else {
                    productAdapter.setProducts(Collections.emptyList());
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Lỗi khi tải sản phẩm: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Hàm này thực hiện tìm kiếm trực tiếp (live search) mỗi khi người dùng gõ chữ.
     * Chỉ hiển thị kết quả sản phẩm.
     */
    public void performLiveSearch(String query) {
        // 1. ẨN LỊCH SỬ TÌM KIẾM KHI ĐANG TÌM KIẾM
        rvSearchSuggestions.setVisibility(View.GONE);
        rvSearchResults.setVisibility(View.VISIBLE);

        searchApi.searchProducts(query).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> foundProducts = response.body();
                    productAdapter.setProducts(foundProducts);
                } else {
                    productAdapter.setProducts(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                productAdapter.setProducts(Collections.emptyList());
            }
        });
    }

    // Hàm được gọi khi người dùng nhấn Enter/Search (Lưu lịch sử)
    public void performSearch(String query) {
        // 1. Thực hiện tìm kiếm (sử dụng hàm live search)
        performLiveSearch(query);

        // 2. Lưu vào lịch sử (Chỉ khi nhấn Enter/Search)
        saveSearchQuery(query);

        // 3. Hiển thị Toast thông báo
        Toast.makeText(this, "Đang tìm kiếm sản phẩm cho: " + query, Toast.LENGTH_SHORT).show();
    }
}