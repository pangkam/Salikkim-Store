package com.salikkim.store.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.salikkim.store.Models.Addresses;
import com.salikkim.store.R;

import java.util.ArrayList;

public class AddrAdapter extends ArrayAdapter<Addresses> {

    public AddrAdapter(Context context, ArrayList<Addresses> addressesLists) {
        super(context, 0, addressesLists);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.addr_item, parent, false
            );
        }

        TextView tv_address = convertView.findViewById(R.id.tv_addr);
        Addresses currentItem = getItem(position);
        if (currentItem != null) {
            tv_address.setText(currentItem.getAddress());
        }
        return convertView;
    }
}
