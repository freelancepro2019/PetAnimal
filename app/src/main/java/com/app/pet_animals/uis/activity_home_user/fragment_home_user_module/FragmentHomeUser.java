package com.app.pet_animals.uis.activity_home_user.fragment_home_user_module;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.app.pet_animals.R;
import com.app.pet_animals.adapters.ServiceAdapter;
import com.app.pet_animals.databinding.FilterDialogBinding;
import com.app.pet_animals.databinding.FragmentHomeUserBinding;
import com.app.pet_animals.models.UserModel;
import com.app.pet_animals.tags.Tags;
import com.app.pet_animals.uis.activity_base.FragmentBase;
import com.app.pet_animals.uis.activity_home_user.HomeActivityUser;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentHomeUser extends FragmentBase {
    private HomeActivityUser activity;
    private FragmentHomeUserBinding binding;
    private ServiceAdapter adapter;
    private DatabaseReference dRef;
    private String filterBy =Tags.user_housing_owner;
    private String dialogFilterQuery = filterBy;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivityUser) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home_user,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.recViewLayout.tvNoData.setText(R.string.no_data);
        dRef = FirebaseDatabase.getInstance().getReference();
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ServiceAdapter(activity,getLang());
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(this::filter);
        binding.recViewLayout.tvNoData.setText(R.string.no_data);
        binding.imageFilter.setOnClickListener(view -> showFilterDialog());
        filter();
    }

    private void filter() {
        binding.recViewLayout.swipeRefresh.setRefreshing(true);
        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
        Query query = dRef.child(Tags.table_users);
        query.orderByChild("user_type")
                .equalTo(filterBy)
                .limitToFirst(20)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        binding.recViewLayout.swipeRefresh.setRefreshing(false);
                        List<UserModel> guidesList = new ArrayList<>();
                        if (snapshot.getValue()!=null){
                            for (DataSnapshot ds :snapshot.getChildren()){
                                UserModel userModel = ds.getValue(UserModel.class);
                                if (userModel!=null){
                                    guidesList.add(userModel);
                                }
                            }

                            if (guidesList.size()>0){
                                adapter.updateList(sortedList(guidesList));
                                binding.recViewLayout.tvNoData.setVisibility(View.GONE);
                            }else {
                                Log.e("f","f");
                                binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

                            }
                        }else {
                            binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private List<UserModel> sortedList(List<UserModel> list) {
        Collections.sort(list, (m1, m2) -> Double.compare(m2.getRate(),m1.getRate()));
        return list;
    }

    private void showFilterDialog(){
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        FilterDialogBinding filterDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),R.layout.filter_dialog,null,false);
        if (filterBy.equals(Tags.user_housing_owner)){
            filterDialogBinding.rbHouse.setChecked(true);
        }else {
            filterDialogBinding.rbDoctor.setChecked(true);

        }

        filterDialogBinding.rbDoctor.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                dialogFilterQuery = Tags.user_doctor;
            }
        });

        filterDialogBinding.rbHouse.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                dialogFilterQuery = Tags.user_housing_owner;
            }
        });

        filterDialogBinding.btnFilter.setOnClickListener(view -> {
            filterBy = dialogFilterQuery;
            filter();
            dialog.dismiss();
        });

        filterDialogBinding.imageClose.setOnClickListener(view -> dialog.dismiss());

        dialog.setContentView(filterDialogBinding.getRoot());
        dialog.show();
    }
}