package com.dev.doctorfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.dev.doctorfinder.auth.LoginActivity;
import com.dev.doctorfinder.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    ActivitySplashBinding binding;
    Animation fromTop;
    Animation fromBottom;
    Animation fromRightToLeft;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        firebaseAuth=FirebaseAuth.getInstance();
        fromTop = AnimationUtils.loadAnimation(this, R.anim.fromtop);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        fromRightToLeft = AnimationUtils.loadAnimation(this, R.anim.righttoleft);
        binding.tvWelcome.setAnimation(fromTop);
        binding.tvAppName.setAnimation(fromBottom);
        binding.ivLogo.setAnimation(fromRightToLeft);
        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3500);
                    if(firebaseAuth.getCurrentUser()!=null) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                } catch (InterruptedException e) {
                    Toast.makeText(SplashActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

        };
        thread.start();

    }
}