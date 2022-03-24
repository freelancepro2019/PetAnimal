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
import com.app.pet_animals.adapters.UserPostsAdapter;
import com.app.pet_animals.databinding.BottomSheetRateBinding;
import com.app.pet_animals.databinding.FragmentOrdersUserBinding;
import com.app.pet_animals.models.FavoriteModel;
import com.app.pet_animals.models.PostModel;
import com.app.pet_animals.models.RateModel;
import com.app.pet_animals.tags.Common;
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


public class FragmentOrdersUser extends FragmentBase {
    private HomeActivityUser activity;
    private FragmentOrdersUserBinding binding;
    private UserPostsAdapter adapter;
    private DatabaseReference dRef;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivityUser) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orders_user, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.recViewLayout.tvNoData.setText(R.string.no_orders);
        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
        adapter = new UserPostsAdapter(activity, this);
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(this::getPosts);
        getPosts();
    }

    private void getPosts() {
        binding.recViewLayout.swipeRefresh.setRefreshing(true);
        dRef = FirebaseDatabase.getInstance().getReference();
        Query query = dRef.child(Tags.table_posts)
                .orderByChild("user_id")
                .equalTo(getUserModel().getUser_id());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.recViewLayout.swipeRefresh.setRefreshing(false);
                List<PostModel> list = new ArrayList<>();
                if (snapshot.getValue() != null) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        PostModel postModel = ds.getValue(PostModel.class);
                        if (postModel != null) {
                            list.add(postModel);
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
                    binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void createRateSheetDialog(PostModel model, int adapterPosition) {
        BottomSheetDialog dialog = new BottomSheetDialog(activity);
        BottomSheetRateBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.bottom_sheet_rate, null, false);
        dialog.setContentView(binding.getRoot());
        binding.rateBar.setOnRatingBarChangeListener((simpleRatingBar, rating, fromUser) -> {
            int rate = (int) rating;
            binding.tvRate.setText(rate + "");
        });
        binding.imageClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

        binding.btnRate.setOnClickListener(view -> {
            int rate = (int) binding.rateBar.getRating();
            addRate(model, rate, adapterPosition);
            dialog.dismiss();

        });

        binding.imageFavorite.setOnClickListener(view -> {
            checkIsGuideInMyFavorite(model);
        });
        dialog.show();

    }

    private void checkIsGuideInMyFavorite(PostModel model) {
        dRef = FirebaseDatabase.getInstance().getReference();
        Query query = dRef.child(Tags.table_favorite)
                .orderByChild("user_id")
                .equalTo(getUserModel().getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isInMyFavorite = false;
                if (snapshot.getValue() == null) {
                    addToFavorite(model);
                } else {

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        FavoriteModel favoriteModel = ds.getValue(FavoriteModel.class);
                        if (favoriteModel != null) {
                            if (favoriteModel.getService_id().equals(model.getService_id())) {
                                isInMyFavorite = true;
                                break;
                            }
                        }
                    }
                    if (isInMyFavorite) {
                        Toast.makeText(activity, R.string.already_fav, Toast.LENGTH_SHORT).show();

                    } else {
                        addToFavorite(model);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addToFavorite(PostModel model) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dRef = FirebaseDatabase.getInstance().getReference();
        String id = dRef.child(Tags.table_favorite).push().getKey();
        FavoriteModel favoriteModel = new FavoriteModel(id, getUserModel().getUser_id(), model.getService_id(), model.getService_name(), model.getService_phone());
        dRef.child(Tags.table_favorite).child(id)
                .setValue(favoriteModel)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                    Toast.makeText(activity, R.string.fav, Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

    private void addRate(PostModel model, int rate, int adapterPosition) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dRef = FirebaseDatabase.getInstance().getReference();
        String rate_id = dRef.child(Tags.table_rate).push().getKey();
        RateModel rateModel = new RateModel(rate_id, model.getService_id(), model.getPost_id(), rate);
        dRef.child(Tags.table_rate).child(rate_id)
                .setValue(rateModel)
                .addOnSuccessListener(unused -> {
                    model.setPost_status(Tags.status_rated);
                    adapter.updateItem(model, adapterPosition);
                    updateServiceRate(model, dialog);
                }).addOnFailureListener(e -> {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

    }

    private void updateServiceRate(PostModel model, ProgressDialog dialog) {
        dRef = FirebaseDatabase.getInstance().getReference();
        Query query = dRef.child(Tags.table_rate)
                .orderByChild("service_id")
                .equalTo(model.getService_id());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    int count = 0;
                    long total = snapshot.getChildrenCount();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        RateModel rateModel = ds.getValue(RateModel.class);
                        count += rateModel.getRate();
                    }

                    int rate = (int) (count / total);
                    updateUserRate(rate, model, dialog);

                } else {
                    dialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateUserRate(int rate, PostModel model, ProgressDialog dialog) {
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.table_users)
                .child(model.getService_id())
                .child("rate")
                .setValue(rate)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        });


    }

    public void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }
}