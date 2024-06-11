package com.dev.doctorfinder.home.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.borjabravo.readmoretextview.ReadMoreTextView;
import com.bumptech.glide.Glide;
import com.dev.doctorfinder.R;
import com.dev.doctorfinder.home.model.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.ViewHolder>{
    private Context context;
    ArrayList<CartModel> list;
  //  ItemCartListener itemCartListener;
    int count;
    int price;

    public CartItemsAdapter(Context context, ArrayList<CartModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_main, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartModel data = list.get(position);
        holder.tvQuantity.setText(String.valueOf(data.getQuantity()));
        holder.tvName.setText(data.getProductName());
        holder.tvPrice.setText("£"+data.getPrice());
        holder.btnAddToCart.setVisibility(View.GONE);
        holder.btnDelete.setVisibility(View.VISIBLE);
        Glide.with(context).load(data.getImageUri()).into(holder.ivImage);
        holder.rlPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity=data.addQuantity();
                int price=data.addPrice();
                holder.tvPrice.setText("$"+ price);
                holder.tvQuantity.setText(String.valueOf(quantity));
                updateData(quantity, data.getId());
            }
        });

        holder.rlMinus.setOnClickListener(v -> {
            int quantity=data.minusQuantity();
            int price = data.addPrice();
            holder.tvPrice.setText("£"+price);
            holder.tvQuantity.setText(String.valueOf(quantity));
            updateData(quantity, data.getId());

        });

        holder.btnDelete.setOnClickListener(v -> {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cart").child(auth.getCurrentUser().getUid());
            databaseReference.child(data.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(context, "Deleted ", Toast.LENGTH_SHORT).show();
                        list.remove(data);
                        notifyItemRemoved(holder.getAdapterPosition());
                    }
                }
            });

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void updateData(int quantity, String pushID){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference dbUpdate = FirebaseDatabase.getInstance().getReference("cart").child(auth.getCurrentUser().getUid()).child(pushID);
        Map<String, Object> update = new HashMap<>();
        update.put("quantity", quantity);
        dbUpdate.updateChildren(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context, "DataUpdated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQuantity;
        ReadMoreTextView tvDescription;
        ImageView ivImage;
        AppCompatButton btnAddToCart, btnDelete;
        RelativeLayout rlMinus, rlPlus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            rlMinus = itemView.findViewById(R.id.rlMinus);
            rlPlus = itemView.findViewById(R.id.rlPlus);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

}


