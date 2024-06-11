package com.dev.doctorfinder.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dev.doctorfinder.admin.home.AdminHomeActivity;
import com.dev.doctorfinder.admin.home.model.HelperClass;
import com.dev.doctorfinder.databinding.ActivityAdminLoginBinding;

public class AdminLoginActivity extends AppCompatActivity {
    String adminEmail = "admin@gmail.com";
    String adminPassword = "admin@123";
    ActivityAdminLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                if (!email.isEmpty() && !password.isEmpty()) {
                    if (email.equals(adminEmail) && password.equals(adminPassword)) {
                        HelperClass.isAdmin=true;
                        startActivity(new Intent(AdminLoginActivity.this, AdminHomeActivity.class));
                    } else {
                        Toast.makeText(AdminLoginActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminLoginActivity.this, "Check user name and password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}