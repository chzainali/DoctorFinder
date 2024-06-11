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

import com.dev.doctorfinder.R;
import com.dev.doctorfinder.admin.home.AdminHomeActivity;
import com.dev.doctorfinder.databinding.FragmentAdminBookingBinding;
import com.dev.doctorfinder.home.adapter.BookingAdaptor;
import com.dev.doctorfinder.home.model.BookingModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class AdminBookingFragment extends Fragment {
    FragmentAdminBookingBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ArrayList<BookingModel> list=new ArrayList<>();
    BookingAdaptor adaptor;
    AdminHomeActivity activity;

    public AdminBookingFragment() {
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
        binding= FragmentAdminBookingBinding.inflate(inflater, container, false);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("appointments");
        activity.binding.toolbar.tvTitle.setText("All Bookings");

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.rvAppointments.setLayoutManager(new LinearLayoutManager(requireContext()));
        adaptor=new BookingAdaptor(list,this, "admin");
        binding.rvAppointments.setAdapter(adaptor);

        ProgressDialog dialog=new ProgressDialog(requireContext());
        dialog.setMessage("Loading");
        dialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list.clear();
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        BookingModel model=snapshot1.getValue(BookingModel.class);
                        list.add(model);
                        adaptor.notifyItemInserted(list.size()-1);
                    }
                }else {
                    Toast.makeText(requireContext(), "No data to show", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}