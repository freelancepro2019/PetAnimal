package com.app.pet_animals.uis.activity_add_post;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.app.pet_animals.R;
import com.app.pet_animals.adapters.AnimalSpinnerAdapter;
import com.app.pet_animals.databinding.ActivityAddPostBinding;
import com.app.pet_animals.models.AnimalModel;
import com.app.pet_animals.models.PostModel;
import com.app.pet_animals.models.UserModel;
import com.app.pet_animals.tags.Common;
import com.app.pet_animals.tags.Tags;
import com.app.pet_animals.uis.activity_base.ActivityBase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ActivityAddPost extends ActivityBase {
    private ActivityAddPostBinding binding;
    private PostModel postModel;
    private String service_id;
    private String service_name;
    private String service_phone;
    private DatabaseReference dRef;
    private AnimalSpinnerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_post);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        service_id = intent.getStringExtra("id");
        service_name = intent.getStringExtra("name");
        service_phone = intent.getStringExtra("phone");

    }

    private void initView() {
        postModel = new PostModel();
        binding.setLang(getLang());
        binding.setName(service_name);
        binding.setPhone(service_phone);
        binding.setPostModel(postModel);
        adapter = new AnimalSpinnerAdapter(this);
        binding.spinner.setAdapter(adapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AnimalModel animalModel = (AnimalModel) adapterView.getSelectedItem();
                postModel.setAnimal_id(animalModel.getAnimal_id());
                postModel.setAnimal_specification(animalModel.getAnimal_specification());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.llBack.setOnClickListener(view -> finish());


        binding.btnBook.setOnClickListener(view -> {
            sendOrder();
        });
        getMyAnimals();
    }

    private void getMyAnimals() {
        dRef = FirebaseDatabase.getInstance().getReference();
        Query query = dRef.child(Tags.table_animal);
        query.orderByChild("owner_id")
                .equalTo(getUserModel().getUser_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            List<AnimalModel> animalModelList = new ArrayList<>();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                AnimalModel animalModel = ds.getValue(AnimalModel.class);
                                if (animalModel != null) {
                                    animalModelList.add(animalModel);
                                }
                            }

                            adapter.updateList(animalModelList);

                        } else {
                            adapter.updateList(new ArrayList<>());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


    private void sendOrder() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        dRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mRef = dRef.child(Tags.table_posts);
        String post_id = mRef.push().getKey();
        postModel.setPost_id(post_id);
        postModel.setPost_status(Tags.status_new);
        postModel.setService_id(service_id);
        postModel.setService_name(service_name);
        postModel.setService_phone(service_phone);
        postModel.setUser_id(getUserModel().getUser_id());
        postModel.setUser_name(getUserModel().getFirst_name() + " " + getUserModel().getLast_name());
        postModel.setUser_phone(getUserModel().getPhone_code() + getUserModel().getPhone());
        postModel.setDate(getNowDate());
        mRef.child(post_id).setValue(postModel)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                    Toast.makeText(this, R.string.suc, Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }


    private String getNowDate() {
        Calendar now = Calendar.getInstance();
        return new SimpleDateFormat("dd/MMM/yyyy hh:mm a", Locale.ENGLISH).format(now.getTime());
    }
}