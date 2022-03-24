package com.app.pet_animals.uis.activity_home_user.fragment_profile_user_module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.pet_animals.R;
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

public class FragmentProfileUser extends FragmentBase {
    private FragmentProfileUserBinding binding;
    private HomeActivityUser activity;
    private DatabaseReference dRef;
    private ActivityResultLauncher<Intent> launcher;
    private int req;



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivityUser) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK) {
                binding.setModel(getUserModel());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_user, container, false);
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

        binding.llPosts.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.fragmentOrdersUser);
        });

        binding.llFavorite.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.fragmentFavoriteUser);
        });

        binding.llEditProfile.setOnClickListener(view -> {
            req = 1;
            Intent intent = new Intent(activity, SignUpActivity.class);
            launcher.launch(intent);
        });

        getPostsCount();


    }

    private void getPostsCount() {
        dRef = FirebaseDatabase.getInstance().getReference();
        Query query = dRef.child(Tags.table_posts)
                .orderByChild("user_id")
                .equalTo(getUserModel().getUser_id());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    int count = 0;
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        PostModel postModel = ds.getValue(PostModel.class);
                        if (postModel != null && postModel.getPost_status().equals(Tags.status_accepted)) {
                            count++;
                        }
                    }

                    binding.setOrderCount(count + "");

                } else {
                    binding.setOrderCount("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}