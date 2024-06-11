package com.dev.doctorfinder.adaptor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dev.doctorfinder.R;
import com.dev.doctorfinder.admin.home.fragments.AdminHomeFragment;
import com.dev.doctorfinder.admin.home.model.HelperClass;
import com.dev.doctorfinder.databinding.ListDocsBinding;
import com.dev.doctorfinder.home.DoctorDetailsFragment;
import com.dev.doctorfinder.home.HomeFragment;
import com.dev.doctorfinder.model.DoctorsModel;

import java.util.ArrayList;

public class DoctorsAdaptor extends RecyclerView.Adapter<DoctorsAdaptor.Vh> {

    private ArrayList<DoctorsModel> list;
    Fragment context;
    String value;

    public DoctorsAdaptor(ArrayList<DoctorsModel> list, Fragment context, String value) {
        this.list = list;
        this.context = context;
        this.value = value;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_docs, parent, false);
        return new Vh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {

        DoctorsModel item = list.get(position);
        holder.name.setText(item.getName());
        holder.description.setText(item.getDate());
        holder.details.setText(item.getDetails());
        Glide.with(holder.image).load(item.getImage()).into(holder.image);
        holder.binding.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new DoctorDetailsFragment(), item, value);
            }
        });

    }

    private void replaceFragments(Fragment fragment, DoctorsModel model, String value) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", model);
        bundle.putString("value", value);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = this.context.requireActivity().getSupportFragmentManager().beginTransaction();

        if(HelperClass.isAdmin){
            transaction.add(new AdminHomeFragment(),"");
        }else {
            transaction.add(new HomeFragment(),"");
        }
        transaction.addToBackStack(null);
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Vh extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, description, details;
        ListDocsBinding binding;

        public Vh(@NonNull View itemView) {
            super(itemView);

            binding = ListDocsBinding.bind(itemView);
            image = itemView.findViewById(R.id.img_doc);
            name = itemView.findViewById(R.id.tv_name);
            description = itemView.findViewById(R.id.tv_desc);
            details = itemView.findViewById(R.id.tV_details);
        }
    }
}
