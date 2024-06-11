package com.dev.doctorfinder.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.doctorfinder.MainActivity;
import com.dev.doctorfinder.R;
import com.dev.doctorfinder.adaptor.MenuListAdapter;
import com.dev.doctorfinder.databinding.FragmentShoppingBinding;
import com.dev.doctorfinder.home.model.CartModel;

import java.util.ArrayList;

public class ShoppingFragment extends Fragment {
    FragmentShoppingBinding binding;
    MenuListAdapter adapter;
    ArrayList<CartModel> list=new ArrayList<>();
    MainActivity activity;
    public ShoppingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity= (MainActivity) requireActivity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentShoppingBinding.inflate(getLayoutInflater(),container,false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        adapter=new MenuListAdapter(list);
        binding.rvProducts.setLayoutManager(new GridLayoutManager(requireContext(),2));
        binding.rvProducts.setAdapter(adapter);

    }
}