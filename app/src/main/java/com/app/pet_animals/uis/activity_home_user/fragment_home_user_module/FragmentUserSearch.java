package com.app.pet_animals.uis.activity_home_user.fragment_home_user_module;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pet_animals.R;
import com.app.pet_animals.adapters.ServiceAdapter;
import com.app.pet_animals.databinding.FilterDialogBinding;
import com.app.pet_animals.databinding.FragmentHomeUserBinding;
import com.app.pet_animals.databinding.FragmentUserSearchBinding;
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

public class FragmentUserSearch extends FragmentBase {
    private HomeActivityUser activity;
    private FragmentUserSearchBinding binding;
    private ServiceAdapter adapter;
    private DatabaseReference dRef;
    private String name ="";
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivityUser) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_user_search,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.recViewLayout.tvNoData.setText(R.string.no_search_result);
        dRef = FirebaseDatabase.getInstance().getReference();
        binding.setSearchResultCount(0);
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        adapter = new ServiceAdapter(activity,getLang());
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(this::filter);
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                name = editable.toString();
                filter();
            }
        });
    }


    private void filter() {
        if (name.isEmpty()){
            List<UserModel> serviceList = new ArrayList<>();
            binding.recViewLayout.swipeRefresh.setRefreshing(false);
            adapter.updateList(serviceList);
            binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);
            binding.setSearchResultCount(0);
            return;
        }
        binding.recViewLayout.swipeRefresh.setRefreshing(true);
        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
        Query query = dRef.child(Tags.table_users);
        query.orderByChild("full_name")
                .startAt(name)
                .endAt(name+"\uf8ff")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        binding.recViewLayout.swipeRefresh.setRefreshing(false);
                        List<UserModel> serviceList = new ArrayList<>();
                        if (snapshot.getValue()!=null){
                            for (DataSnapshot ds :snapshot.getChildren()){
                                UserModel userModel = ds.getValue(UserModel.class);
                                if (userModel!=null&&userModel.getFilter_attr().equals(Tags.filter_service)){
                                    serviceList.add(userModel);
                                }
                            }

                            binding.setSearchResultCount(serviceList.size());

                            if (serviceList.size()>0){
                                adapter.updateList(serviceList);
                                binding.recViewLayout.tvNoData.setVisibility(View.GONE);
                            }else {
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



}