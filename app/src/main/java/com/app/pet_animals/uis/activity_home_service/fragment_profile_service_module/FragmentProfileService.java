package com.app.pet_animals.uis.activity_home_service.fragment_profile_service_module;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import com.app.pet_animals.R;
import com.app.pet_animals.databinding.FragmentProfileServiceBinding;
import com.app.pet_animals.databinding.FragmentProfileUserBinding;
import com.app.pet_animals.models.PostModel;
import com.app.pet_animals.models.UserModel;
import com.app.pet_animals.tags.Common;
import com.app.pet_animals.tags.Tags;
import com.app.pet_animals.uis.activity_base.FragmentBase;
import com.app.pet_animals.uis.activity_home_service.HomeActivityService;
import com.app.pet_animals.uis.activity_home_user.HomeActivityUser;
import com.app.pet_animals.uis.activity_sign_up.SignUpActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FragmentProfileService extends FragmentBase {
    private FragmentProfileServiceBinding binding;
    private HomeActivityService activity;
    private DatabaseReference dRef;
    private ActivityResultLauncher<Intent> launcher;
    private int req;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivityService) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK) {
                binding.setModel(getUserModel());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_service, container, false);
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
        binding.cardLogout.setOnClickListener(view -> {
            activity.logout();
        });

        binding.switchBtn.setOnClickListener(view -> {
            boolean checked = binding.switchBtn.isChecked();
            updateServiceStatus(checked);
        });

        binding.llUpdateProfile.setOnClickListener(view -> {
            req = 1;
            Intent intent = new Intent(activity, SignUpActivity.class);
            launcher.launch(intent);
        });

        getRate();

    }

    private void updateServiceStatus(boolean checked) {
        dRef = FirebaseDatabase.getInstance().getReference();
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        dRef.child(Tags.table_users)
                .child(getUserModel().getUser_id())
                .child("canReceiveOrders")
                .setValue(checked)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                    UserModel model = getUserModel();
                    model.setCanReceiveOrders(checked);
                    setUserModel(model);
                    binding.setModel(model);

                }).addOnFailureListener(e -> {
            dialog.dismiss();
            UserModel model = getUserModel();
            binding.setModel(model);
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();


        });
    }

    private void getRate() {
        dRef = FirebaseDatabase.getInstance().getReference();

        dRef.child(Tags.table_users)
                .child(getUserModel().getUser_id())
                .child("rate")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            long rate = (long) snapshot.getValue();
                            binding.setRate(rate+"");

                        } else {
                            binding.setRate("0");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }



}