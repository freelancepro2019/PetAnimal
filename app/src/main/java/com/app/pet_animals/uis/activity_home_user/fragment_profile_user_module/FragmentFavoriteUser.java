package com.app.pet_animals.uis.activity_home_user.fragment_profile_user_module;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pet_animals.R;
import com.app.pet_animals.adapters.FavouriteAdapter;
import com.app.pet_animals.databinding.FragmentFavouriteUserBinding;
import com.app.pet_animals.databinding.FragmentOrdersUserBinding;
import com.app.pet_animals.models.FavoriteModel;
import com.app.pet_animals.models.UserModel;
import com.app.pet_animals.tags.Common;
import com.app.pet_animals.tags.Tags;
import com.app.pet_animals.uis.activity_add_post.ActivityAddPost;
import com.app.pet_animals.uis.activity_base.FragmentBase;
import com.app.pet_animals.uis.activity_home_user.HomeActivityUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FragmentFavoriteUser extends FragmentBase {
    private HomeActivityUser activity;
    private FragmentFavouriteUserBinding binding;
    private FavouriteAdapter adapter;
    private DatabaseReference dRef;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivityUser) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_user, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.recViewLayout.tvNoData.setText(R.string.no_fav);
        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
        adapter = new FavouriteAdapter(activity, this);
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(this::getFavorites);
        getFavorites();
    }

    private void getFavorites() {
        binding.recViewLayout.swipeRefresh.setRefreshing(true);
        dRef = FirebaseDatabase.getInstance().getReference();
        Query query = dRef.child(Tags.table_favorite)
                .orderByChild("user_id")
                .equalTo(getUserModel().getUser_id());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.recViewLayout.swipeRefresh.setRefreshing(false);
                List<FavoriteModel> list = new ArrayList<>();
                if (snapshot.getValue() != null) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        FavoriteModel favoriteModel = ds.getValue(FavoriteModel.class);
                        if (favoriteModel != null) {
                            list.add(favoriteModel);
                        }
                    }

                    if (list.size() > 0) {
                        Collections.reverse(list);
                        adapter.updateList(list);
                        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
                    } else {
                        binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

                    }
                } else {
                    adapter.updateList(new ArrayList<>());
                    binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void unFavorite(FavoriteModel model) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.table_favorite).child(model.getId())
                .removeValue()
                .addOnSuccessListener(unused -> {

                    dialog.dismiss();
                    Toast.makeText(activity, R.string.unfav, Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    public void navigateToSendPosActivity(FavoriteModel model) {
        Intent intent = new Intent(activity, ActivityAddPost.class);
        intent.putExtra("id",model.getService_id());
        intent.putExtra("name",model.getService_name());
        intent.putExtra("phone",model.getService_phone());

        startActivity(intent);
    }
}