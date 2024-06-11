package com.dev.doctorfinder.admin.home.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.dev.doctorfinder.R;
import com.dev.doctorfinder.admin.home.model.HelperClass;
import com.dev.doctorfinder.admin.home.model.ShopModel;
import com.dev.doctorfinder.databinding.FragmentBookingDetailsBinding;
import com.dev.doctorfinder.home.ProductsFragment;
import com.dev.doctorfinder.home.model.CartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProductDetailsFragment extends Fragment {
    FragmentBookingDetailsBinding binding;
    ShopModel model;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    String checkValue = "";


    public ProductDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = (ShopModel) getArguments().getSerializable("data");
        checkValue = getArguments().getString("value");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBookingDetailsBinding.inflate(getLayoutInflater(), container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        ref = firebaseDatabase.getReference("cart");
        firebaseAuth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (model != null) {
            binding.title.setText(model.getTitle());
            binding.details.setText(model.getDetails());
            Glide.with(requireContext()).load(model.getImageUri()).into(binding.imgProduct);
            binding.price.setText("$"+model.getPrice());
            if(Objects.equals(checkValue,"admin")){
                binding.btnAddToCart.setText("Update");
                binding.btnDelete.setVisibility(View.VISIBLE);
            }
            binding.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reference.child("shop").child(model.getId()).removeValue();
                    requireActivity().onBackPressed();
                }
            });
        }

        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(checkValue,"admin")){
                    replaceFragments(new AddProductFragment(),model);
                }else {
                    addToCart();
                }
            }
        });

    }

    private void  addToCart(){
        String userId = firebaseAuth.getCurrentUser().getUid();
        String id = ref.push().getKey();
        String name = model.getTitle();
        String productID = model.getId();
        ref.child(userId).orderByChild("productId").equalTo(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    CartModel cartModel = new CartModel(id, name, productID, userId, String.valueOf(model.getPrice()), 1,model.getImageUri());
                    ref.child(userId).child(id).setValue(cartModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(requireContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    Toast.makeText(requireContext(), "Product Already exists in cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void replaceFragments(Fragment fragment, ShopModel model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", model);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

        transaction.addToBackStack(null);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    private void replaceBack(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

}