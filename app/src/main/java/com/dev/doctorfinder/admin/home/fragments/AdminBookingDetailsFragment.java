package com.dev.doctorfinder.admin.home.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dev.doctorfinder.R;
import com.dev.doctorfinder.databinding.FragmentAdminBookingDetailsBinding;
import com.dev.doctorfinder.home.model.BookingModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.Objects;

public class AdminBookingDetailsFragment extends Fragment {

    FragmentAdminBookingDetailsBinding binding;
    BookingModel model;
    String checkValue = "";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;

    public AdminBookingDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = (BookingModel) getArguments().getSerializable("data");
        checkValue = getArguments().getString("value");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAdminBookingDetailsBinding.inflate(getLayoutInflater(), container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("appointments");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (model != null) {
            binding.tvTitle.setText(model.getTitle());
            binding.tvDetails.setText(model.getDetails());
            binding.tvEmail.setText(model.getEmail());
            binding.tvDate.setText(model.formatedTime());
            binding.tvStatus.setText(model.getStatus());
        }

        if (Objects.equals(checkValue, "user")){
            binding.btnUpdate.setVisibility(View.GONE);
        }

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status = binding.tvStatus.getText().toString();
                model.setStatus(status);
                ref.child(model.getId()).child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Status Changed Successfully", Toast.LENGTH_SHORT).show();
                            replaceBack(new AdminBookingFragment());
                        }
                    }
                });

            }
        });

        binding.tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(checkValue, "admin")){
                    powerMenu(v);
                }
            }
        });
    }

    public void powerMenu(View view) {
        PowerMenu menu = new PowerMenu.Builder(requireContext()).build();
        menu.setTextColor(Color.BLACK);
        menu.setMenuRadius(15f);
        menu.addItem(new PowerMenuItem("Pending"));
        menu.addItem(new PowerMenuItem("Approved"));
        menu.addItem(new PowerMenuItem("Reject"));

        menu.setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
            @Override
            public void onItemClick(int position, PowerMenuItem item) {
                binding.tvStatus.setText(item.title);
            }
        });

        menu.showAsAnchorLeftBottom(view);

    }

    private void replaceBack(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}