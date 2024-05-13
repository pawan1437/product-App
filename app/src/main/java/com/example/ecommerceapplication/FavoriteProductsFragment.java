package com.example.ecommerceapplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteProductsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> favoriteProducts = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private ProductService productService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite_products, container, false);

        recyclerView = view.findViewById(R.id.favoriteRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        productAdapter = new ProductAdapter(requireContext());
        recyclerView.setAdapter(productAdapter);

        favoriteProducts = new ArrayList<>();
        sharedPreferences = requireContext().getSharedPreferences("cart_preferences", Context.MODE_PRIVATE);

        // Load favorite products from SharedPreferences
        loadFavoriteProductsFromSharedPreferences();

        return view;
    }

    private void loadFavoriteProductsFromSharedPreferences() {
        favoriteProducts.clear();

        Map<String, ?> allEntries = sharedPreferences.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String productIdStr = entry.getKey();
            boolean isInCart = (boolean) entry.getValue();
            int productId = Integer.parseInt(productIdStr);

            if (isInCart) {
                getProductById(productId);
            }
        }
    }

    private void addToFavoriteProducts(Product product) {
        if (product != null) {
            if (!isProductInFavorites(product.getId())) {
                favoriteProducts.add(product);
                productAdapter.setProductList(favoriteProducts);
            }
        }
    }

    private boolean isProductInFavorites(int productId) {
        for (Product existingProduct : favoriteProducts) {
            if (existingProduct.getId() == productId) {
                return true;
            }
        }
        return false;
    }

    private void getProductById(int productId) {
        Log.d("FavoriteProductsFragment", "Fetching product with ID: " + productId);
        productService = RetrofitClient.getInstance().create(ProductService.class);
        Call<List<Product>> call = productService.getProductById(productId);

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    List<Product> productList = response.body();
                    if (productList != null && !productList.isEmpty()) {
                        for (Product product : productList) {
                            if (product.getId() == productId) {
                                Log.d("FavoriteProductsFragment", "Received product for ID " + productId + ": " + product.getTitle());
                                addToFavoriteProducts(product);
                                break;
                            }
                        }
                    } else {
                        Log.e("FavoriteProductsFragment", "Received null or empty product list for ID: " + productId);
                    }
                } else {
                    Log.e("FavoriteProductsFragment", "API call failed with code: " + response.code() + " for ID: " + productId);
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("FavoriteProductsFragment", "API call failed: " + t.getMessage() + " for ID: " + productId);
                t.printStackTrace();
            }
        });
    }


}
