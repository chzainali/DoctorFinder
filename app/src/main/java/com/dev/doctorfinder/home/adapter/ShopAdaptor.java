package com.dev.doctorfinder.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dev.doctorfinder.R;
import com.dev.doctorfinder.admin.home.fragments.AdminHomeFragment;
import com.dev.doctorfinder.admin.home.fragments.ProductDetailsFragment;
import com.dev.doctorfinder.admin.home.model.HelperClass;
import com.dev.doctorfinder.admin.home.model.ShopModel;
import com.dev.doctorfinder.databinding.ListAppointmentsBinding;
import com.dev.doctorfinder.databinding.ListShopBinding;
import com.dev.doctorfinder.home.HomeFragment;
import com.dev.doctorfinder.home.model.BookingModel;
import com.dev.doctorfinder.model.DoctorsModel;

import java.util.ArrayList;

public class ShopAdaptor extends RecyclerView.Adapter<ShopAdaptor.Vh> {

    private ArrayList<ShopModel> list;
    Fragment context;
    String value;

    public ShopAdaptor(ArrayList<ShopModel> list, Fragment context, String value) {
        this.list = list;
        this.context = context;
        this.value = value;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_shop, parent, false);
        return new Vh(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        ShopModel item = list.get(position);
        holder.binding.title.setText(item.getTitle());
        holder.binding.price.setText("$"+item.getPrice());
        Glide.with(context).load(item.getImageUri()).into(holder.binding.imgProduct);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new ProductDetailsFragment(),item, value);
            }
        });

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class Vh extends RecyclerView.ViewHolder {
        ListShopBinding binding;

        public Vh(@NonNull View itemView) {
            super(itemView);
            binding = ListShopBinding.bind(itemView);

        }
    }
    private void replaceFragments(Fragment fragment, ShopModel model, String value) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", model);
        bundle.putString("value", value);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = this.context.requireActivity().getSupportFragmentManager().beginTransaction();

        transaction.addToBackStack(null);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

}
