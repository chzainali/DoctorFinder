package com.dev.doctorfinder.admin.home.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.dev.doctorfinder.databinding.FragmentAddDoctorBinding;
import com.dev.doctorfinder.model.DoctorsModel;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;

public class AddDoctorFragment extends Fragment {

    FragmentAddDoctorBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    String imageUri;
    int PICK_IMAGE_GALLERY = 124;
    ProgressDialog progressDialog;
    DoctorsModel model;

    public AddDoctorFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = (DoctorsModel) getArguments().getSerializable("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddDoctorBinding.inflate(inflater, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        progressDialog = new ProgressDialog(requireContext());
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if (model != null) {
            Glide.with(requireActivity()).load(model.getImage()).into(binding.imguser);
            binding.etDetails.setText(model.getDetails());
            binding.tvEmail.setText(model.getEmail());
            binding.tvName.setText(model.getName());
            binding.tvTime.setText(model.getDate());
            binding.etLocation.setText(model.getLocation());
            binding.tvExpertise.setText(model.getExperts());
            imageUri = model.getImage();
        }

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEditing = (model != null);
                try {
                    if (!isEditing) {
                        if (imageUri != null) {
                            addDoctor(false);
                        } else {
                            Toast.makeText(requireContext(), "Please Add image", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (Objects.equals(imageUri, model.getImage())) {
                            updateDoctor();
                        } else {
                            addDoctor(true);
                        }

                    }
                } catch (FileNotFoundException e) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    e.printStackTrace();
                }
            }
        });

        binding.imguser.setOnClickListener(v -> {

            Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Getting Gallery Image uri
            Uri uriImage = data.getData();
            try {
                binding.imguser.setImageURI(uriImage);
                imageUri = uriImage.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addDoctor(boolean b) throws FileNotFoundException {
        progressDialog.setMessage("Adding..");
        progressDialog.setCancelable(false);

        String name = binding.tvName.getText().toString();
        String email = binding.tvEmail.getText().toString();
        String expertise = binding.tvExpertise.getText().toString();
        String timeTable = binding.tvTime.getText().toString();
        String details = binding.etDetails.getText().toString();
        String location = binding.etLocation.getText().toString();
        String id;
        if (b) id = model.getId();
        else id = reference.push().getKey();

        if (!name.isEmpty() && !email.isEmpty() && !expertise.isEmpty() && !timeTable.isEmpty() && !details.isEmpty() && !location.isEmpty()) {

            progressDialog.show();

            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference profileRef = storageReference.child("images" + id + ".jpg");

            InputStream inputStream = getActivity().getContentResolver().openInputStream(Uri.parse(imageUri));
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();

            UploadTask uploadTask = profileRef.putBytes(bytes);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        progressDialog.dismiss();
                        throw task.getException();
                    }
                    return profileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {

                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        DoctorsModel model = new DoctorsModel(id, name, details, timeTable, "80", downloadUri.toString(), expertise, email, location);
                        reference.child("doctors").child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(requireContext(), "Doctor Added Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        } else {
            Toast.makeText(requireActivity(), "Error some Fields are missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDoctor() {
        progressDialog.setMessage("Adding..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String name = binding.tvName.getText().toString();
        String email = binding.tvEmail.getText().toString();
        String expertise = binding.tvExpertise.getText().toString();
        String timeTable = binding.tvTime.getText().toString();
        String details = binding.etDetails.getText().toString();
        String location = binding.etLocation.getText().toString();
        String id = model.getId();

        DoctorsModel model = new DoctorsModel(id, name, details, timeTable, "80", imageUri, expertise, email, location);

        reference.child("doctors").child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(requireContext(), "Doctor Added Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}