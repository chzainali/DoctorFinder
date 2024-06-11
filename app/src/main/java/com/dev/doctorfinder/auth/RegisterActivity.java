package com.dev.doctorfinder.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.dev.doctorfinder.MainActivity;
import com.dev.doctorfinder.R;
import com.dev.doctorfinder.auth.model.User;
import com.dev.doctorfinder.databinding.ActivityRegisterBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yesterselga.countrypicker.CountryPicker;
import com.yesterselga.countrypicker.CountryPickerListener;
import com.yesterselga.countrypicker.Theme;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    String countryCode, imageUri="";
    int PICK_IMAGE_GALLERY = 124;
    FirebaseDatabase database;
    DatabaseReference db;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        database = FirebaseDatabase.getInstance();
        db = database.getReference("Users");
        firebaseAuth = FirebaseAuth.getInstance();

        binding.tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setTitle("Going Good...");
                progressDialog.setMessage("It takes Just a few Seconds... ");
                progressDialog.setIcon(R.drawable.happy);
                progressDialog.setCancelable(false);

                String phoneNumber = binding.etPhone.getText().toString();
                StringBuilder builder = new StringBuilder();
                builder.append(countryCode);
                builder.append(phoneNumber);
                String name = binding.etName.getText().toString();
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();

                if (name.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                }else if (email.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
                }else  if (!(Patterns.EMAIL_ADDRESS).matcher(email).matches()){
                    Toast.makeText(RegisterActivity.this, "Please enter email in correct format", Toast.LENGTH_SHORT).show();
                }else if (countryCode.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please select country", Toast.LENGTH_SHORT).show();
                }else if (phoneNumber.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                }else if (password.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                }else if (imageUri.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String id = FirebaseAuth.getInstance().getUid();
                            try {
                                signUp(id, name, email, builder,progressDialog);
                            } catch (FileNotFoundException e) {
                                progressDialog.dismiss();
                                e.printStackTrace();
                            }
                        }
                    });

                }


            }
        });

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.ivProfile.setOnClickListener(v -> {

            ImagePicker.with(this)
                    .compress(512)
                    .maxResultSize(512, 512)
                    .start();

            Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
        });

        binding.etCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CountryPicker picker = CountryPicker.newInstance("Select Country", Theme.LIGHT);  // dialog title and theme
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        countryCode = dialCode;
                        binding.etCountry.setText(name);
                        picker.dismiss();
                    }
                });
                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Getting Gallery Image uri
            Uri uriImage = data.getData();
            try {
                binding.ivProfile.setImageURI(uriImage);
                imageUri = uriImage.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void signUp(String userId, String name, String email, StringBuilder builder,ProgressDialog progressDialog) throws FileNotFoundException {
        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
        StorageReference reference=firebaseStorage.getReference();
        StorageReference profileRef=reference.child("images"+userId+".jpg");
        InputStream inputStream = getContentResolver().openInputStream(Uri.parse(imageUri));

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask=profileRef.putBytes(data);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return profileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    User user = new User(userId, name, email, builder.toString(), downloadUri.toString());

                    db.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                    Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
                }
    });
}

}