package com.dev.doctorfinder.home;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dev.doctorfinder.MainActivity;
import com.dev.doctorfinder.R;
import com.dev.doctorfinder.admin.home.AdminHomeActivity;
import com.dev.doctorfinder.admin.home.model.ShopModel;
import com.dev.doctorfinder.databinding.FragmentProductsBinding;
import com.dev.doctorfinder.home.adapter.ShopAdaptor;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProductsFragment extends Fragment {
    FragmentProductsBinding binding;
    MainActivity activity;
    ShopAdaptor adaptor;
    ArrayList<ShopModel> list;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    public ProductsFragment() {
        // Required empty public constructor
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
        binding=FragmentProductsBinding.inflate(inflater,container,false);
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();
        activity.binding.toolbar.tvTitle.setText("Shop");

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.rvShop.setLayoutManager(new LinearLayoutManager(requireContext()));
        list=new ArrayList<>();
        adaptor=new ShopAdaptor(list,this, "user");
        binding.rvShop.setAdapter(adaptor);

        reference.child("shop").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        ShopModel model=snapshot1.getValue(ShopModel.class);
                        list.add(model);
                        adaptor.notifyItemInserted(list.size()-1);
                    }
                }else {
                    Toast.makeText(activity, "No Products in Shop", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }
}