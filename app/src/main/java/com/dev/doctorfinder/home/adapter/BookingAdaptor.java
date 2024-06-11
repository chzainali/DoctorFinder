package com.dev.doctorfinder.home.adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.doctorfinder.R;
import com.dev.doctorfinder.admin.home.fragments.AdminBookingDetailsFragment;
import com.dev.doctorfinder.admin.home.fragments.AdminBookingFragment;
import com.dev.doctorfinder.admin.home.model.HelperClass;
import com.dev.doctorfinder.databinding.ListAppointmentsBinding;
import com.dev.doctorfinder.home.HomeFragment;
import com.dev.doctorfinder.home.model.BookingModel;

import java.util.ArrayList;

public class BookingAdaptor extends RecyclerView.Adapter<BookingAdaptor.Vh> {

    private ArrayList<BookingModel> list;
    Fragment context;
    String value;

    public BookingAdaptor(ArrayList<BookingModel> list, Fragment context, String value) {
        this.list = list;
        this.context = context;
        this.value = value;
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_appointments, parent, false);
        return new Vh(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {

        BookingModel item = list.get(position);

        holder.binding.tvDocName.setText(item.getDoctorName());
        holder.binding.tvTime.setText(item.formatedTime());
        holder.binding.tvTitle.setText(item.getTitle());
        holder.binding.status.setText(item.getStatus());

        switch (item.getStatus()) {
            case "Pending":
                holder.binding.status.setTextColor(Color.YELLOW);
                break;
            case "Approved":
                holder.binding.status.setTextColor(Color.GREEN);
                break;
            case "Completed":
                holder.binding.status.setTextColor(Color.MAGENTA);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragments(new AdminBookingDetailsFragment(),item, value);
            }
        });

    }

    private void replaceFragments(Fragment fragment, BookingModel model, String value) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", model);
        bundle.putString("value",value );
        fragment.setArguments(bundle);
        FragmentTransaction transaction = this.context.requireActivity().getSupportFragmentManager().beginTransaction();

        if(HelperClass.isAdmin){
            transaction.add(new AdminBookingFragment(),"");
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
        ListAppointmentsBinding binding;

        public Vh(@NonNull View itemView) {
            super(itemView);
            binding = ListAppointmentsBinding.bind(itemView);

        }
    }
}
