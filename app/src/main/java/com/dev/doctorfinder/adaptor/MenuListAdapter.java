package com.dev.doctorfinder.adaptor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.doctorfinder.R;
import com.dev.doctorfinder.databinding.MenuRecyclerRowBinding;
import com.dev.doctorfinder.home.model.CartModel;

import java.util.List;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MyViewHolder> {

    public List<CartModel> menuList;


    public MenuListAdapter(List<CartModel> menuList) {
        this.menuList = menuList;
    }

    public void updateData(List<CartModel> menuList) {
        this.menuList = menuList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_recycler_row, parent, false);
        return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CartModel model= menuList.get(position);
        holder.binding.productName.setText(menuList.get(position).getProductName());
        holder.binding.productPrice.setText("Price: $"+menuList.get(position).getPrice());
        holder.binding.addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.binding.addMoreLayout.setVisibility(View.VISIBLE);
                holder.binding.addToCartButton.setVisibility(View.GONE);
                holder.binding.tvCount.setText(model.getQuantity()+"");
            }
        });
        holder.binding.imageMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartModel menu  = menuList.get(position);
                int total = menu.getQuantity();
                total--;
                if(total > 0 ) {
                    menu.setQuantity(total);
                    holder.binding.tvCount.setText(total +"");
                } else {
                    holder.binding.addMoreLayout.setVisibility(View.GONE);
                    holder.binding.addToCartButton.setVisibility(View.VISIBLE);
                    menu.setQuantity(total);
                }
            }
        });

        holder.binding.imageAddOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartModel menu  = menuList.get(position);
                int total = menu.getQuantity();
                total++;
                if(total <= 10 ) {
                    menu.setQuantity(total);
                    holder.binding.tvCount.setText(total +"");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        MenuRecyclerRowBinding binding;
        public MyViewHolder(View view) {
            super(view);
            binding=MenuRecyclerRowBinding.bind(view);
        }
    }


}
