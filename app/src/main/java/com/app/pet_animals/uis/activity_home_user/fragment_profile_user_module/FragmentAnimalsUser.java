package com.app.pet_animals.uis.activity_home_user.fragment_profile_user_module;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pet_animals.R;
import com.app.pet_animals.adapters.AnimalAdapter;
import com.app.pet_animals.databinding.FragmentAnimalUserBinding;
import com.app.pet_animals.databinding.FragmentOrdersUserBinding;
import com.app.pet_animals.models.AnimalModel;
import com.app.pet_animals.tags.Common;
import com.app.pet_animals.tags.Tags;
import com.app.pet_animals.uis.activity_base.FragmentBase;
import com.app.pet_animals.uis.activity_home_user.HomeActivityUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class FragmentAnimalsUser extends FragmentBase {
    private HomeActivityUser activity;
    private FragmentAnimalUserBinding binding;
    private AnimalAdapter adapter;
    private DatabaseReference dRef;
    private StorageReference sRef;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivityUser) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_animal_user, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        adapter = new AnimalAdapter(activity, this);
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.tvNoData.setText(R.string.no_pet_animal);
        binding.recViewLayout.tvNoData.setVisibility(View.GONE);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(this::getAnimals);
        binding.fab.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.fragmentAddAnimalsUser);
        });
        getAnimals();
    }

    private void getAnimals() {
        binding.recViewLayout.swipeRefresh.setRefreshing(true);
        dRef = FirebaseDatabase.getInstance().getReference();
        Query query = dRef.child(Tags.table_animal);
        query.orderByChild("owner_id")
                .equalTo(getUserModel().getUser_id())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        binding.recViewLayout.swipeRefresh.setRefreshing(false);
                        if (snapshot.getValue() != null) {
                            List<AnimalModel> animalModelList = new ArrayList<>();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                AnimalModel animalModel = ds.getValue(AnimalModel.class);
                                if (animalModel != null) {
                                    animalModelList.add(animalModel);
                                }
                            }

                            if (animalModelList.size() > 0) {
                                binding.recViewLayout.tvNoData.setVisibility(View.GONE);

                            } else {
                                binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

                            }
                            adapter.updateList(animalModelList);

                        } else {
                            binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public void delete(AnimalModel animalModel, int adapterPosition) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        sRef = FirebaseStorage.getInstance().getReference();
        StorageReference mSRef = sRef.child(Tags.animals_image).child(animalModel.getAnimal_id());
        mSRef.delete().addOnSuccessListener(unused -> {
            removeAnimalData(animalModel,adapterPosition,dialog);
        }).addOnFailureListener(e -> {
           dialog.dismiss();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();

        });

    }

    private void removeAnimalData(AnimalModel animalModel, int adapterPosition, ProgressDialog dialog) {
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.table_animal).child(animalModel.getAnimal_id())
                .removeValue()
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                    adapter.removeItem(adapterPosition);
                    if (adapter.getList()!=null&&adapter.getList().size()==0){
                        binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);
                    }else {
                        binding.recViewLayout.tvNoData.setVisibility(View.GONE);

                    }
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();

        });
    }

    public void update(AnimalModel animalModel, int adapterPosition) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",animalModel);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.fragmentAddAnimalsUser,bundle);
    }
}