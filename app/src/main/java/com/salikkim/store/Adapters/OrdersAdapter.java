package com.salikkim.store.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.salikkim.store.Helper.VolleyEventListener;
import com.salikkim.store.Helper.VolleyRequest;
import com.salikkim.store.Models.Orders;
import com.salikkim.store.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    private final List<Orders> ordersList;

    public OrdersAdapter(Context context, List<Orders> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(
                R.layout.card_order, parent, false);
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
                load(ordersList.get(pos).getThumbnail())
                .placeholder(shimmerDrawable)
                .into(itemViewHolder.thumbnail);
        itemViewHolder.title.setText(ordersList.get(pos).getName());
        itemViewHolder.order_date.setText(ordersList.get(pos).getOrder_date());
        itemViewHolder.seller.setText("Seller: " + ordersList.get(pos).getSeller_name());
        itemViewHolder.price.setText(context.getString(R.string.Rs) + ordersList.get(pos).getPrice());
        itemViewHolder.price.setPaintFlags(itemViewHolder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        itemViewHolder.total.setText(context.getString(R.string.Rs) + ordersList.get(pos).getSale_price());
        itemViewHolder.color.setText("Color: " + ordersList.get(pos).getColor());
        itemViewHolder.size.setText("Size: " + ordersList.get(pos).getSize());
        itemViewHolder.qnty.setText("Qnty: " + ordersList.get(pos).getQnty());

        if (ordersList.get(pos).getStatus() == 3) {
            itemViewHolder.tv_deliver_info.setText("Delivered");
            itemViewHolder.tv_deliver_info.setTextColor((ContextCompat.getColor(context, R.color.green)));
        } else if (ordersList.get(pos).getStatus() == 2) {
            itemViewHolder.tv_deliver_info.setText("Pending Payment");
            itemViewHolder.tv_deliver_info.setTextColor((ContextCompat.getColor(context, R.color.red)));
            itemViewHolder.btn_pay.setVisibility(View.VISIBLE);
            itemViewHolder.btn_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        } else if (ordersList.get(pos).getStatus() == 1) {
            itemViewHolder.tv_deliver_info.setText("On the way");
            itemViewHolder.tv_deliver_info.setTextColor((ContextCompat.getColor(context, R.color.green)));
        } else {
            itemViewHolder.btn_info.setVisibility(View.VISIBLE);
            itemViewHolder.tv_deliver_info.setText("Not delivered");
            itemViewHolder.tv_deliver_info.setTextColor((ContextCompat.getColor(context, R.color.red)));
            itemViewHolder.tv_not_delivered_info.setText(ordersList.get(pos).getStatus_info());
            itemViewHolder.btn_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (itemViewHolder.tv_not_delivered_info.getVisibility() != View.VISIBLE) {
                        itemViewHolder.tv_not_delivered_info.setVisibility(View.VISIBLE);
                    } else {
                        itemViewHolder.tv_not_delivered_info.setVisibility(View.GONE);
                    }

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView thumbnail, btn_info;
        private TextView title, seller, price, total, color, size, qnty, tv_deliver_info, tv_not_delivered_info, btn_pay, order_date;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail_order);
            title = itemView.findViewById(R.id.order_title);
            seller = itemView.findViewById(R.id.order_seller_name);
            order_date = itemView.findViewById(R.id.order_date);
            price = itemView.findViewById(R.id.order_price);
            total = itemView.findViewById(R.id.order_sale_price);
            color = itemView.findViewById(R.id.order_color);
            size = itemView.findViewById(R.id.order_size);
            qnty = itemView.findViewById(R.id.tv_qnty);
            btn_info = itemView.findViewById(R.id.btn_deliver_info);
            tv_deliver_info = itemView.findViewById(R.id.tv_deliver_info);
            tv_not_delivered_info = itemView.findViewById(R.id.tv_not_deliver_info);
            btn_pay = itemView.findViewById(R.id.btn_order_pay_now);
        }
    }
}
