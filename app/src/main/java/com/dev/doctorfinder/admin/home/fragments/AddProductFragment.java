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
import com.dev.doctorfinder.admin.home.model.ShopModel;
import com.dev.doctorfinder.databinding.FragmentAddProductBinding;
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

public class AddProductFragment extends Fragment {
    FragmentAddProductBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    String imageUri="";
    int PICK_IMAGE_GALLERY = 124;
    ProgressDialog progressDialog;
    ShopModel model;

    public AddProductFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = (ShopModel) getArguments().getSerializable("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddProductBinding.inflate(getLayoutInflater(), container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        progressDialog = new ProgressDialog(requireContext());
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (model!=null){
            binding.etDetails.setText(model.getDetails());
            binding.tvName.setText(model.getTitle());
            binding.tvPrice.setText(String.valueOf(model.getPrice()));
            binding.etDetails.setText(model.getDetails());
            Glide.with(requireContext()).load(model.getImageUri()).into(binding.imguser);
            imageUri=model.getImageUri();
        }
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEditing = model != null;
                if (isEditing) {
                    if (Objects.equals(imageUri, model.getImageUri())) {
                       updateProduct();
                    } else {
                        try {
                            if (imageUri.isEmpty()){
                                Toast.makeText(requireContext(), "Please Select Image First", Toast.LENGTH_SHORT).show();
                            }else{
                                addProduct(true);
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }else {
                    try {
                        addProduct(false);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        binding.imguser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);

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
                binding.imguser.setImageURI(uriImage);
                imageUri = uriImage.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addProduct(boolean b) throws FileNotFoundException {
        progressDialog.setMessage("Adding..");
        progressDialog.setCancelable(false);

        String name = binding.tvName.getText().toString();
        String details = binding.etDetails.getText().toString();
        String price = binding.tvPrice.getText().toString();
        String id;
        if (b) id = model.getId();
        else id = reference.push().getKey();

        if(!name.isEmpty() && !price.isEmpty() && !details.isEmpty() && !imageUri.isEmpty()) {
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
                        ShopModel model = new ShopModel(Integer.parseInt(price), id, name, details, downloadUri.toString());
                        reference.child("shop").child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(requireContext(), "Product Added Successfully", Toast.LENGTH_SHORT).show();
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

        }else {
            Toast.makeText(requireActivity(), "Error some Fields are missing", Toast.LENGTH_SHORT).show();
        }

    }

    private void updateProduct(){
        progressDialog.setMessage("Updating..");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String name = binding.tvName.getText().toString();
        String details = binding.etDetails.getText().toString();
        String price = binding.tvPrice.getText().toString();
        String id= model.getId();

        ShopModel model = new ShopModel(Integer.parseInt(price), id, name, details, imageUri);
        reference.child("shop").child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(requireContext(), "Product updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}