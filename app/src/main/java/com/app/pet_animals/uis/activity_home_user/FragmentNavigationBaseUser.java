package com.app.pet_animals.uis.activity_home_user;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.app.pet_animals.R;
import com.app.pet_animals.uis.activity_base.FragmentBase;

import java.util.HashSet;
import java.util.Set;

public class FragmentNavigationBaseUser extends FragmentBase {
    private int layoutRecourseId = -1;
    private int navHostId = -1;
    private NavController navController;
    private View root;
    private HomeActivityUser activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivityUser) context;
    }

    public static FragmentNavigationBaseUser newInstance(int layoutRecourseId, int navHostId) {
        Bundle bundle = new Bundle();
        bundle.putInt("layoutRecourseId", layoutRecourseId);
        bundle.putInt("navHostId", navHostId);
        FragmentNavigationBaseUser fragment = new FragmentNavigationBaseUser();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            layoutRecourseId = bundle.getInt("layoutRecourseId");
            navHostId = bundle.getInt("navHostId");

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (layoutRecourseId != -1 && navHostId != -1) {
            root = inflater.inflate(layoutRecourseId, container, false);
            return root;
        } else {
            return null;
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        if (navHostId != -1 && layoutRecourseId != -1) {

            navController = Navigation.findNavController(activity, navHostId);
            Toolbar toolbar = root.findViewById(R.id.toolbar);
            TextView tvTitle = root.findViewById(R.id.tvTitle);
            NavigationUI.setupWithNavController(toolbar, navController);
            navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
                int id = navDestination.getId();
                if (id==R.id.fragmentProfileUser){
                    tvTitle.setText(R.string.profile);
                }else if (id==R.id.fragmentOrdersUser){
                    tvTitle.setText(R.string.orders);
                }else if (id==R.id.fragmentFavoriteUser){
                    tvTitle.setText(R.string.housing_owner_favorite);
                }else if (id==R.id.fragmentUserSearch){
                    tvTitle.setText(R.string.search);
                }else if (id==R.id.fragmentHomeUser){
                    tvTitle.setText(R.string.home);
                }
            });
        }

    }

    public boolean onBackPressed() {
        return navController.navigateUp();
    }
}

