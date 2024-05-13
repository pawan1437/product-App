package com.example.ecommerceapplication;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductService {
    @GET("products")
    Call<List<Product>> getProducts();

    @GET("products")
    Call<List<Product>> searchProductsByTitle(@Query("title") String title);
    @GET("products")
    Call<List<Product>> getProductById(@Query("id") int id);
}
