package com.dev.doctorfinder.admin.home.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dev.doctorfinder.MainActivity;
import com.dev.doctorfinder.R;
import com.dev.doctorfinder.admin.home.AdminHomeActivity;
import com.dev.doctorfinder.admin.home.model.ShopModel;
import com.dev.doctorfinder.databinding.FragmentAdminShopBinding;
import com.dev.doctorfinder.home.adapter.ShopAdaptor;
import com.dev.doctorfinder.model.DoctorsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminShopFragment extends Fragment {

    FragmentAdminShopBinding binding;
    AdminHomeActivity activity;
    ShopAdaptor adaptor;
    ArrayList<ShopModel> list;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    public AdminShopFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity= (AdminHomeActivity) requireActivity();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAdminShopBinding.inflate(getLayoutInflater(),container,false);
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();
        activity.binding.toolbar.tvTitle.setText("Shop");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.rvShops.setLayoutManager(new LinearLayoutManager(requireContext()));
        list=new ArrayList<>();
        adaptor=new ShopAdaptor(list,this, "admin");
        binding.rvShops.setAdapter(adaptor);

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

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new AddProductFragment());
            }
        });

    }

    private void replaceFragments(Fragment fragment) {

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}