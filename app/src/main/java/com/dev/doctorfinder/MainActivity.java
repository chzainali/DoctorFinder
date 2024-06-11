package com.dev.doctorfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.dev.doctorfinder.auth.LoginActivity;
import com.dev.doctorfinder.databinding.ActivityMainBinding;
import com.dev.doctorfinder.home.BookingsFragment;
import com.dev.doctorfinder.home.CartFragment;
import com.dev.doctorfinder.home.HomeFragment;
import com.dev.doctorfinder.home.MapsFragment;
import com.dev.doctorfinder.home.ProductsFragment;
import com.dev.doctorfinder.home.ProfileFragment;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

   public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setBottomNavigation();
        replaceFragments(new HomeFragment());

    }

    private void setBottomNavigation() {

        binding.rlBottomNav.bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_home_24));
        binding.rlBottomNav.bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_booking_24));
        binding.rlBottomNav.bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_shopping_cart_24));
        binding.rlBottomNav.bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_baseline_person_24));

        binding.rlBottomNav.bottomNavigation.show(1, true);

        binding.rlBottomNav.bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()) {
                    case 1:
                        replaceFragments(new HomeFragment());
                        break;

                    case 2:
                       replaceFragments(new BookingsFragment());
                        break;

                    case 3:
                       replaceFragments(new ProductsFragment());
                        break;

                    case 4:
                       replaceFragments(new ProfileFragment());
                        break;

                }


                return null;
            }
        });

        binding.rlBottomNav.tvHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new HomeFragment());
                binding.rlBottomNav.bottomNavigation.show(1, true);
            }
        });
        binding.rlBottomNav.tvBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new BookingsFragment());
                binding.rlBottomNav.bottomNavigation.show(2, true);
            }
        });
        binding.rlBottomNav.tvShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new ProductsFragment());
                binding.rlBottomNav.bottomNavigation.show(3, true);
            }
        });
        binding.rlBottomNav.tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new ProfileFragment());
                binding.rlBottomNav.bottomNavigation.show(4, true);
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
                FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        binding.menuLayout.tvHome.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                binding.rlBottomNav.bottomNavigation.show(1, true);
                replaceFragments(new HomeFragment());
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                binding.rlBottom.setVisibility(View.VISIBLE);
                binding.toolbar.tvTitle.setText("Home");
            }
        });

        binding.menuLayout.tvBooking.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                replaceFragments(new BookingsFragment());
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                binding.rlBottom.setVisibility(View.GONE);
                binding.toolbar.tvTitle.setText("Booking");
            }
        });

        binding.menuLayout.tvCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                replaceFragments(new CartFragment());
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                binding.rlBottom.setVisibility(View.GONE);
                binding.toolbar.tvTitle.setText("Cart");
            }
        });

        binding.menuLayout.map.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
               replaceFragments(new MapsFragment());
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                binding.rlBottom.setVisibility(View.GONE);
                binding.toolbar.tvTitle.setText("Map");
            }
        });

    }

    private void replaceFragments(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }
}