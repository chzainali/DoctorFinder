package com.dev.doctorfinder.home;

import android.app.ProgressDialog;
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
import com.dev.doctorfinder.databinding.FragmentBookingsBinding;
import com.dev.doctorfinder.home.adapter.BookingAdaptor;
import com.dev.doctorfinder.home.model.BookingModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookingsFragment extends Fragment {
    FragmentBookingsBinding binding;
    ArrayList<BookingModel> list=new ArrayList<>();
    BookingAdaptor adaptor;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;


    public BookingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentBookingsBinding.inflate(getLayoutInflater(),container,false);

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("appointments");
        firebaseAuth= FirebaseAuth.getInstance();
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.rvAppointments.setLayoutManager(new LinearLayoutManager(requireContext()));
        adaptor=new BookingAdaptor(list,this, "user");
        binding.rvAppointments.setAdapter(adaptor);

        ProgressDialog dialog=new ProgressDialog(requireContext());
        dialog.setMessage("Loading..");
        dialog.show();

        databaseReference.orderByChild("userId").equalTo(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.cancel();
                if (snapshot.exists()){
                    list.clear();
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        BookingModel model=snapshot1.getValue(BookingModel.class);
                        list.add(model);
                        adaptor.notifyItemInserted(list.size()-1);
                    }
                }else {
                    Toast.makeText(requireContext(), "No appointments scehduled ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.cancel();
                Toast.makeText(requireContext(), error.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}