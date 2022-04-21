package com.app.pet_animals.uis.activity_home_user.fragment_home_user_module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import com.app.pet_animals.R;
import com.app.pet_animals.databinding.FragmentMainUserBinding;
import com.app.pet_animals.databinding.FragmentProfileUserBinding;
import com.app.pet_animals.models.PostModel;
import com.app.pet_animals.tags.Tags;
import com.app.pet_animals.uis.activity_base.FragmentBase;
import com.app.pet_animals.uis.activity_home_user.HomeActivityUser;
import com.app.pet_animals.uis.activity_sign_up.SignUpActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FragmentMainUser extends FragmentBase {
    private FragmentMainUserBinding binding;
    private HomeActivityUser activity;




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivityUser) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_user, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {


        binding.cardHealthCare.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.fragmentDoctorUser);
        });

        binding.cardHomeCare.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.fragmentHomeUser);
        });



    }



}