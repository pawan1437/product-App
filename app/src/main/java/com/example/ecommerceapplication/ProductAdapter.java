package com.example.ecommerceapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private SharedPreferences sharedPreferences;

    public ProductAdapter(Context context) {
        this.sharedPreferences = context.getSharedPreferences("cart_preferences", Context.MODE_PRIVATE);
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.titleTextView.setText(product.getTitle());
        holder.priceTextView.setText(String.valueOf(product.getPrice()));
        holder.descriptionTextView.setText(product.getDescription());
        if (!product.getImages().isEmpty()) {
            Picasso.get().load(product.getImages().get(0)).into(holder.imageView);
        }

        boolean isInCart = getCartState(product.getId());
        product.setCart(isInCart);

        if (product.inCart()) {
            holder.favoriteButton.setText("Remove from Cart");
        } else {
            holder.favoriteButton.setText("Add to Cart");
        }

        holder.favoriteButton.setOnClickListener(v -> {
            product.setCart(!product.inCart());

            if (product.inCart()) {
                holder.favoriteButton.setText("Remove from Cart");
                addToCart(product.getId());
            } else {
                holder.favoriteButton.setText("Add to Cart");
                removeFromCart(product.getId());
            }

            notifyDataSetChanged();
        });
    }

    private void addToCart(int productId) {
        sharedPreferences.edit().putBoolean(String.valueOf(productId), true).apply();
    }

    private void removeFromCart(int productId) {
        sharedPreferences.edit().remove(String.valueOf(productId)).apply();
    }

    private void saveCartState(int productId, boolean isInCart) {
        sharedPreferences.edit().putBoolean(String.valueOf(productId), isInCart).apply();
    }

    private boolean getCartState(int productId) {
        return sharedPreferences.getBoolean(String.valueOf(productId), false);
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, priceTextView, descriptionTextView;
        ImageView imageView;
        boolean isExpanded = false;
        TextView favoriteButton;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            imageView = itemView.findViewById(R.id.imageView);
            favoriteButton = itemView.findViewById(R.id.favoriteButton);

            descriptionTextView.setOnClickListener(v -> {
                isExpanded = !isExpanded;
                setDescriptionText(descriptionTextView.getText().toString(), this);
            });
        }

        void setDescriptionText(String description, ProductViewHolder holder) {
            if (isExpanded) {
                descriptionTextView.setText(description);
                descriptionTextView.setMaxLines(Integer.MAX_VALUE);
            } else {
                descriptionTextView.setMaxLines(4);
                descriptionTextView.setEllipsize(TextUtils.TruncateAt.END);
            }
        }
    }
}

