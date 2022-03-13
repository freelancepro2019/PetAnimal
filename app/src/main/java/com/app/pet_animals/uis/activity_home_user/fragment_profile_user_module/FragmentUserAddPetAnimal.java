package com.app.pet_animals.uis.activity_home_user.fragment_profile_user_module;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.pet_animals.R;
import com.app.pet_animals.adapters.ServiceAdapter;
import com.app.pet_animals.databinding.FragmentUserAddAnimalBinding;
import com.app.pet_animals.databinding.FragmentUserSearchBinding;
import com.app.pet_animals.models.AddAnimalModel;
import com.app.pet_animals.models.AnimalModel;
import com.app.pet_animals.models.UserModel;
import com.app.pet_animals.tags.Common;
import com.app.pet_animals.tags.Tags;
import com.app.pet_animals.uis.activity_base.ActivityBase;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FragmentUserAddPetAnimal extends FragmentBase {
    private HomeActivityUser activity;
    private FragmentUserAddAnimalBinding binding;
    private ActivityResultLauncher<Intent> launcher;
    private AddAnimalModel model;
    private AnimalModel animalModel = null;
    private DatabaseReference dRef;
    private StorageReference sRef;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivityUser) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                model.setImagePath(uri.toString());
                Picasso.get().load(uri).into(binding.image);
                binding.tvPhoto.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            animalModel = (AnimalModel) bundle.getSerializable("data");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_add_animal, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        model = new AddAnimalModel();
        if (animalModel != null) {
            model.setName(animalModel.getAnimal_name());
            model.setSpecialization(animalModel.getAnimal_specification());
            model.setImagePath(animalModel.getAnimal_photo());
            Picasso.get().load(Uri.parse(animalModel.getAnimal_photo())).into(binding.image);
            binding.tvPhoto.setVisibility(View.GONE);
            binding.tv.setText(R.string.update);
            binding.btnAdd.setText(R.string.update);

        }
        binding.setModel(model);
        binding.image.setOnClickListener(view -> checkReadPermission());
        binding.btnAdd.setOnClickListener(view -> {
            if (model.isDataValid(activity)) {
                if (animalModel == null) {
                    addAnimal();
                } else {
                    if (model.getImagePath().startsWith("http")) {
                        updateAnimalWithout();
                    } else {
                        updateAnimalWithImage();
                    }

                }

            }
        });
    }

    private void addAnimal() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        dRef = FirebaseDatabase.getInstance().getReference();
        String animal_id = dRef.child(Tags.table_animal).push().getKey();
        sRef = FirebaseStorage.getInstance().getReference();
        StorageReference mSRef = sRef.child(Tags.animals_image).child(animal_id);
        mSRef.putFile(Uri.parse(model.getImagePath()))
                .addOnSuccessListener(taskSnapshot -> {
                    mSRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        AnimalModel animalModel = new AnimalModel(animal_id, model.getName(), model.getSpecialization(), uri.toString(), getUserModel().getUser_id());
                        addData(animalModel, dialog);

                    }).addOnFailureListener(e -> {
                        dialog.dismiss();
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();

                    });
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        });


    }

    private void addData(AnimalModel animalModel, ProgressDialog dialog) {
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.table_animal).child(animalModel.getAnimal_id())
                .setValue(animalModel)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                    Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(binding.getRoot()).navigateUp();
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();

        });
    }

    private void updateAnimalWithImage() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        dRef = FirebaseDatabase.getInstance().getReference();
        String animal_id = animalModel.getAnimal_id();

        sRef = FirebaseStorage.getInstance().getReference();
        StorageReference mSRef = sRef.child(Tags.animals_image).child(animal_id);
        mSRef.putFile(Uri.parse(model.getImagePath()))
                .addOnSuccessListener(taskSnapshot -> {
                    mSRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        AnimalModel animalModel = new AnimalModel(animal_id, model.getName(), model.getSpecialization(), uri.toString(), getUserModel().getUser_id());
                        addData(animalModel, dialog);

                    }).addOnFailureListener(e -> {
                        dialog.dismiss();
                        Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();

                    });
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void updateAnimalWithout() {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        animalModel.setAnimal_name(model.getName());
        animalModel.setAnimal_specification(model.getSpecialization());
        addData(animalModel, dialog);
    }


    private void checkReadPermission() {
        if (ActivityCompat.checkSelfPermission(activity, ActivityBase.READ_PERM) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{ActivityBase.READ_PERM}, 1);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length == 1) {
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        launcher.launch(Intent.createChooser(intent, "Choose animal photo"));

    }


}