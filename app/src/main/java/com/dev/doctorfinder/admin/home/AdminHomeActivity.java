package com.dev.doctorfinder.admin.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dev.doctorfinder.MainActivity;
import com.dev.doctorfinder.R;
import com.dev.doctorfinder.admin.AdminLoginActivity;
import com.dev.doctorfinder.admin.home.fragments.AddDoctorFragment;
import com.dev.doctorfinder.admin.home.fragments.AddProductFragment;
import com.dev.doctorfinder.admin.home.fragments.AdminBookingFragment;
import com.dev.doctorfinder.admin.home.fragments.AdminHomeFragment;
import com.dev.doctorfinder.admin.home.fragments.AdminShopFragment;
import com.dev.doctorfinder.auth.LoginActivity;
import com.dev.doctorfinder.databinding.ActivityAdminHomeBinding;
import com.dev.doctorfinder.home.BookingsFragment;
import com.dev.doctorfinder.home.HomeFragment;
import com.dev.doctorfinder.home.ProfileFragment;
import com.dev.doctorfinder.home.ShoppingFragment;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class AdminHomeActivity extends AppCompatActivity {
    public ActivityAdminHomeBinding binding;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.tvTitle.setText("Admin Home");
        replaceFragments(new AdminHomeFragment());
        setBottomNavigation();

    }


    private void setBottomNavigation() {

        binding.rlBottomNav.bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_home_24));
        binding.rlBottomNav.bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_booking_24));
        binding.rlBottomNav.bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_shopping_cart_24));

        binding.rlBottomNav.bottomNavigation.show(1, true);

        binding.rlBottomNav.bottomNavigation.setOnClickMenuListener(model -> {
            switch (model.getId()) {
                case 1:
                    replaceFragments(new AdminHomeFragment());
                    break;

                case 2:
                    replaceFragments(new AdminBookingFragment());
                    break;

                case 3:
                    replaceFragments(new AdminShopFragment());
                    break;
            }
            return null;
        });

        binding.rlBottomNav.tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new AdminHomeFragment());
                binding.rlBottomNav.bottomNavigation.show(1, true);
            }
        });

        binding.rlBottomNav.tvBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new AdminBookingFragment());
                binding.rlBottomNav.bottomNavigation.show(2, true);
            }
        });

        binding.rlBottomNav.tvShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new AdminShopFragment());
                binding.rlBottomNav.bottomNavigation.show(3, true);
            }
        });

        binding.toolbar.actionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        binding.menuLayout.tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class));
                finish();
            }
        });

        binding.menuLayout.tvHome.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                binding.toolbar.tvTitle.setText("Home");
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                replaceFragments(new AdminHomeFragment());

            }
        });

        binding.menuLayout.AddProducts.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                replaceFragments(new AddProductFragment());
                binding.toolbar.tvTitle.setText("Add Product");
                binding.drawerLayout.closeDrawer(GravityCompat.START);

            }
        });

        binding.menuLayout.tvViewAppointments.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                binding.toolbar.tvTitle.setText("All Bookings");
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                replaceFragments(new AdminBookingFragment());
            }
        });

        binding.menuLayout.tvAddDoctors.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                binding.toolbar.tvTitle.setText("Add Doctor");
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                replaceFragments(new AddDoctorFragment());
            }
        });

    }

    private void replaceFragments(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}