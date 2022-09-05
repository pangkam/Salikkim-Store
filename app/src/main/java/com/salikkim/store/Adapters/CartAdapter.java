package com.salikkim.store.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.salikkim.store.Models.Cart;
import com.salikkim.store.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private String addr_id;
    private final List<Cart> cartList;
    private DelClickListener delClickListener;
    private OnQuantitySetListener quantitySetListener;

    public CartAdapter(Context context, List<Cart> cartList, String addr_id) {
        this.context = context;
        this.cartList = cartList;
        this.addr_id = addr_id;
    }

    public interface DelClickListener {
        void mClick(int position);
    }

    public interface OnQuantitySetListener {
        void qClick(View v, int position);
    }

    public void setQntyListener(OnQuantitySetListener qlistener) {
        quantitySetListener = qlistener;
    }

    public void setOnDelClickListener(DelClickListener listener) {
        delClickListener = listener;
    }

    public void setAvailableAddress(String add) {
        this.addr_id = add;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(
                R.layout.card_cart, parent, false);
        return new ItemViewHolder(v, delClickListener, quantitySetListener);
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
                load(cartList.get(pos).getThumbnail())
                .placeholder(shimmerDrawable)
                .into(itemViewHolder.thumbnail);
        itemViewHolder.title.setText(cartList.get(pos).getName());
        itemViewHolder.seller.setText("Seller: " + cartList.get(pos).getSeller_name());
        itemViewHolder.price.setText(context.getString(R.string.Rs) + String.format("%.0f", cartList.get(pos).getPrice()));
        itemViewHolder.price.setPaintFlags(itemViewHolder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        itemViewHolder.total.setText(context.getString(R.string.Rs) + String.format("%.0f", cartList.get(pos).getSale_price()));
        itemViewHolder.discount.setText(String.format("%.0f",cartList.get(pos).getDiscount()) + "% Offer");
        itemViewHolder.color.setText("Color: " + cartList.get(pos).getColor());
        itemViewHolder.size.setText("Size: " + cartList.get(pos).getSize());
        itemViewHolder.qnty.setText("Qnty: " + cartList.get(pos).getQnty());
        if (cartList.get(pos).getCod() != 0) {
            itemViewHolder.cod.setVisibility(View.VISIBLE);
        }
        String all_vals = cartList.get(pos).getAvailable_adresses();
        List<String> list = Arrays.asList(all_vals.split(","));

        if (!list.contains(addr_id)) {
            itemViewHolder.tv_not_deliver.setText("This item is not deliver to selected address");
            itemViewHolder.tv_not_deliver.setVisibility(View.VISIBLE);
        } else if (cartList.get(pos).getQnty() > cartList.get(pos).getTotal_qnty()) {
            itemViewHolder.tv_not_deliver.setText("Only " + cartList.get(pos).getTotal_qnty() + " left, You cannot proceed this item at the time. Please reduce the quantity");
            itemViewHolder.tv_not_deliver.setVisibility(View.VISIBLE);
        } else {
            itemViewHolder.tv_not_deliver.setText("");
            itemViewHolder.tv_not_deliver.setVisibility(View.GONE);
        }

        if (!list.contains(addr_id) && cartList.get(pos).getQnty() > cartList.get(pos).getTotal_qnty()) {
            itemViewHolder.tv_not_deliver.setText("Only " + cartList.get(pos).getTotal_qnty() + " left, and this item is not deliver to selected address");
            itemViewHolder.tv_not_deliver.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout pop_up;
        private ImageView thumbnail, btn_del;
        private TextView title, seller, price, total, color, size, qnty, discount, tv_not_deliver, cod;

        public ItemViewHolder(@NonNull View itemView, DelClickListener listener, OnQuantitySetListener qListener) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail_cart);
            btn_del = itemView.findViewById(R.id.btn_delete_cart);
            title = itemView.findViewById(R.id.cart_title);
            seller = itemView.findViewById(R.id.cart_seller_name);
            price = itemView.findViewById(R.id.cart_price);
            total = itemView.findViewById(R.id.cart_sale_price);
            color = itemView.findViewById(R.id.cart_color);
            size = itemView.findViewById(R.id.cart_size);
            qnty = itemView.findViewById(R.id.tv_qnty);
            discount = itemView.findViewById(R.id.cart_discount);
            tv_not_deliver = itemView.findViewById(R.id.tv_not_deliver_info);
            pop_up = itemView.findViewById(R.id.select_qnty);
            cod = itemView.findViewById(R.id.cart_cod);

            btn_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            listener.mClick(pos);
                        }
                    }
                }
            });
            pop_up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (qListener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            qListener.qClick(view, pos);
                        }
                    }
                }
            });

        }
    }
}
