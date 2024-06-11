package com.dev.doctorfinder.admin.home.fragments;

import android.app.ProgressDialog;
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
import com.dev.doctorfinder.adaptor.DoctorsAdaptor;
import com.dev.doctorfinder.admin.home.AdminHomeActivity;
import com.dev.doctorfinder.databinding.FragmentAdminHomeBinding;
import com.dev.doctorfinder.databinding.FragmentHomeBinding;
import com.dev.doctorfinder.home.adapter.PagerAdapter;
import com.dev.doctorfinder.home.model.PagerModel;
import com.dev.doctorfinder.model.DoctorsModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminHomeFragment extends Fragment {
    FragmentAdminHomeBinding binding;
    PagerAdapter adapter;
    DoctorsAdaptor doctorsAdaptor;
    AdminHomeActivity activity;
    ArrayList<DoctorsModel> doctorsModels=new ArrayList<>();
    ArrayList<PagerModel> list = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    public AdminHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            activity= (AdminHomeActivity) requireActivity();
        }catch (Exception e){

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false);
//        activity.binding.toolbar.tvTitle.setText("Admin Home");
        addData();
        setViewPager();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        firebaseDatabase =FirebaseDatabase.getInstance();
        reference=firebaseDatabase.getReference();
        setRecycler();
    }

    public void setViewPager() {
        adapter = new PagerAdapter(requireContext(), list);
        binding.viewPager.setAdapter(adapter);
        binding.indicator.setViewPager2(binding.viewPager);
    }

    private void addData() {
        list.clear();
        list.add(new PagerModel(R.drawable.vp2));
        list.add(new PagerModel(R.drawable.vp2));
        list.add(new PagerModel(R.drawable.vp3));
        list.add(new PagerModel(R.drawable.vp1));
    }

    public void setRecycler(){
        binding.rvDoc.setLayoutManager(new LinearLayoutManager(requireContext()));
        doctorsAdaptor= new DoctorsAdaptor(doctorsModels,this, "admin");
        binding.rvDoc.setAdapter(doctorsAdaptor);


        ProgressDialog dialog=new ProgressDialog(requireContext());
        dialog.setCancelable(true);
        dialog.setMessage("Loading");
        dialog.show();
        doctorsModels.clear();

        reference.child("doctors").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.dismiss();
                if(snapshot.exists()){
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        DoctorsModel model=snapshot1.getValue(DoctorsModel.class);
                        doctorsModels.add(model);
                        doctorsAdaptor.notifyItemInserted(doctorsModels.size()-1);
                    }

                }else {
                    Toast.makeText(activity, "No Data to show", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

}