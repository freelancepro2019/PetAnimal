package com.app.pet_animals.uis.activity_home_user.fragment_profile_user_module;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pet_animals.R;
import com.app.pet_animals.databinding.FragmentProfileUserBinding;
import com.app.pet_animals.uis.activity_base.FragmentBase;
import com.app.pet_animals.uis.activity_home_user.HomeActivityUser;

public class FragmentProfileUser extends FragmentBase {
    private FragmentProfileUserBinding binding;
    private HomeActivityUser activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivityUser) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile_user,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.setLang(getLang());
        binding.setModel(getUserModel());
        binding.setOrderCount("0");
        binding.cardLogout.setOnClickListener(view -> {
            activity.logout();
        });

        binding.llAnimals.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.fragmentAnimalsUser);
        });


    }
}