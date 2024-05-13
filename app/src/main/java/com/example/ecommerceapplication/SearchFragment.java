package com.example.ecommerceapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private ProductService productService;
    private EditText searchEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.new_recycleView);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        searchAdapter = new SearchAdapter();
        recyclerView.setAdapter(searchAdapter);

        searchEditText = view.findViewById(R.id.search);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                } else {
                    searchAdapter.setProductList(new ArrayList<>());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        productService = RetrofitClient.getInstance().create(ProductService.class);

        return view;
    }

    private void performSearch(String query) {
        Call<List<Product>> call = productService.searchProductsByTitle(query);
        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> searchResults = response.body();
                    searchAdapter.setProductList(searchResults);
                } else {
                    Log.e("SearchFragment", "Failed to fetch search results");
                    searchAdapter.setProductList(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("SearchFragment", "Error searching products", t);
                searchAdapter.setProductList(new ArrayList<>());
            }
        });
    }
}
