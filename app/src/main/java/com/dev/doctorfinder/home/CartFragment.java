package com.dev.doctorfinder.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dev.doctorfinder.MainActivity;
import com.dev.doctorfinder.R;
import com.dev.doctorfinder.adaptor.MenuListAdapter;
import com.dev.doctorfinder.admin.home.model.ShopModel;
import com.dev.doctorfinder.databinding.FragmentCartBinding;
import com.dev.doctorfinder.home.adapter.CartItemsAdapter;
import com.dev.doctorfinder.home.model.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class CartFragment extends Fragment {
    FragmentCartBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference db;
    CartItemsAdapter adaptor;
    ArrayList<CartModel> list = new ArrayList<>();
    MainActivity activity;
    float price = 0;

    public CartFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity= (MainActivity) requireActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentCartBinding.inflate(inflater,container,false);
        firebaseDatabase=FirebaseDatabase.getInstance();
        db=firebaseDatabase.getReference();
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        db.child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    binding.llBottom.setVisibility(View.VISIBLE);
                    list.clear();
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        CartModel model=snapshot1.getValue(CartModel.class);
                        list.add(model);
                    }
                    price=0;
                    for (CartModel itemsModel : list) {
                        float productPrice = (Integer.parseInt(itemsModel.getPrice()))*itemsModel.getQuantity();
                        price += productPrice;
                    }

                    binding.rvShop.setLayoutManager(new LinearLayoutManager(activity));
                    adaptor=new CartItemsAdapter(activity,list);
                    binding.rvShop.setAdapter(adaptor);
                    adaptor.notifyDataSetChanged();

                    binding.tvTotalPrice.setText("$"+price);

                }else {
                    binding.llBottom.setVisibility(View.GONE);
                    Toast.makeText(activity, "No Products in Cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        binding.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogue();
            }
        });

    }

    private void showDialogue(){
        AlertDialog.Builder dialog=new AlertDialog.Builder(requireContext());
        dialog.setIcon(R.drawable.doctor_logo);
        dialog.setTitle("Check Out Cart");
        dialog.setMessage("Are you sure you want to check out items from your cart");
        dialog.setPositiveButton("CheckOut", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.child("cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            list.clear();
                            adaptor.notifyDataSetChanged();
                            Toast.makeText(requireContext(), "SuccessFully Checked Out", Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            }
        });
        dialog.show();
    }
}