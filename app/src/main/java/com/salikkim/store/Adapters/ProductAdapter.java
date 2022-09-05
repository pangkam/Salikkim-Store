package com.salikkim.store.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;

import com.salikkim.store.Activities.DetailsActivity;
import com.salikkim.store.Models.Products;
import com.salikkim.store.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<Products> itemsList;

    public ProductAdapter(Context context, List<Products> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(
                R.layout.card_product, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final int pos = position;
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        Shimmer shimmer = new Shimmer.ColorHighlightBuilder()
                .setBaseColor(Color.parseColor("#F3F3F3"))
                .setBaseAlpha(1)
                .setHighlightColor(Color.parseColor("#E7E7E7"))
                .setHighlightAlpha(1)
                .setDropoff(50)
                .build();
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        Glide.with(context).
                load(itemsList.get(pos).getThumbnail())
                .placeholder(shimmerDrawable)
                .into(itemViewHolder.thumbnail);
        itemViewHolder.title.setText(itemsList.get(pos).getName());
        itemViewHolder.price.setText(context.getString(R.string.Rs) + String.format("%.0f", itemsList.get(pos).getPrice()));
        itemViewHolder.price.setPaintFlags(itemViewHolder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        itemViewHolder.total.setText(context.getString(R.string.Rs) + String.format("%.0f", itemsList.get(pos).getSale_price()));
        itemViewHolder.discount.setText(String.format("%.0f", itemsList.get(pos).getDiscount()) + "% Offer");
        itemViewHolder.shipping_charge.setText(Html.fromHtml("<b>+ " + context.getString(R.string.Rs) + String.format("%.0f", itemsList.get(pos).getShipping_charge()) + "</b> (shipping charge)"));
        itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, DetailsActivity.class)
                        .putExtra("Product_id", itemsList.get(pos).getProduct_id())
                        .putExtra("Name", itemsList.get(pos).getName())
                        .putExtra("Seller_id", itemsList.get(pos).getSeller_id())
                        .putExtra("Seller_name", itemsList.get(pos).getSeller_name())
                        .putExtra("Price", itemsList.get(pos).getPrice())
                        .putExtra("Sale_price", itemsList.get(pos).getSale_price())
                        .putExtra("Shipping_charge", itemsList.get(pos).getShipping_charge())
                        .putExtra("Discount", itemsList.get(pos).getDiscount())
                        .putExtra("Color", itemsList.get(pos).getColor())
                        .putExtra("Product_desc", itemsList.get(pos).getProduct_descriptions())
                        .putExtra("Available_addresses", itemsList.get(pos).getAvailable_adresses())
                        .putExtra("Cod", itemsList.get(pos).getCod())
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail;
        private TextView title, price, total, discount, shipping_charge;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail_main);
            title = itemView.findViewById(R.id.main_title);
            price = itemView.findViewById(R.id.main_price);
            total = itemView.findViewById(R.id.main_total);
            discount = itemView.findViewById(R.id.main_discount);
            shipping_charge = itemView.findViewById(R.id.shipping_charge);
        }
    }
}
