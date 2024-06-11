package com.dev.doctorfinder.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.dev.doctorfinder.R;
import com.dev.doctorfinder.admin.home.fragments.AddDoctorFragment;
import com.dev.doctorfinder.admin.home.fragments.AdminHomeFragment;
import com.dev.doctorfinder.admin.home.model.HelperClass;
import com.dev.doctorfinder.databinding.FragmentDoctorDetailsBinding;
import com.dev.doctorfinder.model.DoctorsModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;


public class DoctorDetailsFragment extends Fragment {
    FragmentDoctorDetailsBinding binding;
    DoctorsModel model;
    String checkValue = "";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    public DoctorDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model= (DoctorsModel) getArguments().getSerializable("data");
        checkValue= getArguments().getString("value");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=com.dev.doctorfinder.databinding.FragmentDoctorDetailsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();

        if(model!=null){
            if (Objects.equals(checkValue, "admin")){
                binding.btnAdd.setText("Update");
                binding.btnDelete.setVisibility(View.VISIBLE);
            }
            binding.tvName.setText(model.getName());
            binding.tVDetails.setText("Details\n\n"+model.getDetails());
            binding.docLocation.setText("Location\n\n"+model.getLocation());
            binding.tvSpecialisation.setText(model.getExperts());
            Glide.with(requireActivity()).load(model.getImage()).into(binding.imgDoc);
            binding.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reference.child("doctors").child(model.getId()).removeValue();
                    replaceBack(new AdminHomeFragment());
                }
            });
        }
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(checkValue, "admin")) {
                    replaceFragments(new AddDoctorFragment(), model);
                }else {
                    replaceFragments(new BookAppointmentFragment(), model);
                }
            }
        });

        binding.docLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMap(model.getLocation());
            }
        });
    }

    private void replaceFragments(Fragment fragment, DoctorsModel model) {
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

    private void openMap(String location){
        Uri uriLocation = Uri.parse("geo:0,0?q=" + Uri.encode(location));
        Intent intentMap = new Intent(Intent.ACTION_VIEW, uriLocation);
        intentMap.setPackage("com.google.android.apps.maps");
        startActivity(intentMap);
    }

}