package com.dev.doctorfinder.home;

import android.app.ProgressDialog;
import android.content.Context;
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
import com.dev.doctorfinder.MainActivity;
import com.dev.doctorfinder.R;
import com.dev.doctorfinder.auth.model.User;
import com.dev.doctorfinder.databinding.FragmentProfileBinding;
import com.dev.doctorfinder.model.DoctorsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference ref;
    MainActivity activity;
    User user;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity= (MainActivity) requireActivity();
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(getLayoutInflater(), container, false);
        activity.binding.toolbar.tvTitle.setText("Profile");
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(requireContext());
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.show();

        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Users");

        ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dialog.dismiss();
                if (snapshot.exists()) {
                    String name = (String) snapshot.child("name").getValue();
                    String phoneNumber = (String) snapshot.child("phoneNumber").getValue();
                    String email = (String) snapshot.child("email").getValue();
                    String imageUri = (String) snapshot.child("imageUri").getValue();
                    user=new User(FirebaseAuth.getInstance().getCurrentUser().getUid(),name,email,phoneNumber,imageUri);
                    binding.tvName.setText(name);
                    binding.tvPhone.setText(phoneNumber);
                    binding.tvEmail.setText(email);

                    Glide.with(requireContext()).load(imageUri).into(binding.imguser);

                } else {
                    Toast.makeText(requireContext(), "No data for user", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
                Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new EditProfileFragment(),user);
            }
        });

    }
    private void replaceFragments(Fragment fragment, User model) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", model);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}