package com.app.pet_animals.uis.activity_sign_up;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.pet_animals.R;
import com.app.pet_animals.databinding.ActivitySignUpBinding;
import com.app.pet_animals.models.SignUpModel;
import com.app.pet_animals.models.UserModel;
import com.app.pet_animals.tags.Common;
import com.app.pet_animals.tags.Tags;
import com.app.pet_animals.uis.activity_base.ActivityBase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class SignUpActivity extends ActivityBase {
    private ActivitySignUpBinding binding;
    private SignUpModel model;
    private FirebaseAuth mAuth;
    private DatabaseReference dRef;
    private ActivityResultLauncher<Intent> launcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        initView();
    }

    private void initView() {
        model = new SignUpModel();
        if (getUserModel() != null) {
            model.setImagePath(getUserModel().getImage_url());
            model.setFirstName(getUserModel().getFirst_name());
            model.setLastName(getUserModel().getLast_name());
            model.setPhoneCode(getUserModel().getPhone_code());
            model.setPhone(getUserModel().getPhone());
            model.setUserType(getUserModel().getUser_type());
            model.setEmail(getUserModel().getEmail());
            model.setPassword(getUserModel().getPassword());
            model.setFilter_attr(getUserModel().getFilter_attr());

            binding.flImage.setVisibility(View.GONE);
            if (getUserModel().getUser_type().equals(Tags.user_animal_owner)) {
                binding.animalOwner.setChecked(true);
            } else if (getUserModel().getUser_type().equals(Tags.user_doctor)) {
                binding.doctor.setChecked(true);
            } else if (getUserModel().getUser_type().equals(Tags.user_housing_owner)) {
                binding.housingOwner.setChecked(true);
            }
            binding.llType.setVisibility(View.GONE);
            binding.tv.setText(R.string.update_profile);
            binding.btnSignUp.setText(R.string.update_profile);
            binding.llLogin.setVisibility(View.GONE);


        }
        binding.setModel(model);

        binding.llLogin.setOnClickListener(view -> {
            navigateToLoginActivity();
        });

        binding.btnSignUp.setOnClickListener(view -> {
            if (model.isDataValid(this)) {
                if (getUserModel() == null) {
                    createAccount();

                } else {

                    reAuth();
                }
            }
        });

        binding.animalOwner.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                model.setUserType(Tags.user_animal_owner);
            }
        });
        binding.housingOwner.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                model.setUserType(Tags.user_housing_owner);
            }
        });

        binding.doctor.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                model.setUserType(Tags.user_doctor);
            }
        });

        binding.image.setOnClickListener(view -> {
            checkReadPermission();
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                model.setImagePath(uri.toString());
                Picasso.get().load(uri).into(binding.image);
            }
        });


    }

    private void checkReadPermission() {
        if (ActivityCompat.checkSelfPermission(this, ActivityBase.READ_PERM) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{ActivityBase.READ_PERM}, 1);
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
        launcher.launch(Intent.createChooser(intent, "Choose profile image"));

    }


    private void createAccount() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.logging_in));
        dialog.setCancelable(false);
        dialog.show();
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(model.getEmail(), model.getPassword())
                .addOnSuccessListener(authResult -> {
                    if (authResult != null && authResult.getUser() != null) {
                        if (model.getImagePath() != null && !model.getImagePath().isEmpty()) {
                            uploadImage(dialog, authResult.getUser().getUid());
                        } else {
                            signUp(authResult.getUser().getUid(), "", dialog);
                        }

                    } else {
                        dialog.dismiss();
                        Toast.makeText(this, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }

                }).addOnFailureListener(e -> {
            Common.createAlertDialog(SignUpActivity.this, e.getMessage());

            dialog.dismiss();
        });
    }

    private void uploadImage(ProgressDialog dialog, String uid) {
        StorageReference sRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = sRef.child(Tags.user_image).child(uid).child(UUID.randomUUID().toString());
        Uri uri = Uri.parse(model.getImagePath());
        imageRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> {
                    if (taskSnapshot.getTask().isSuccessful()) {

                        imageRef.getDownloadUrl().addOnSuccessListener(uri1 -> signUp(uid, uri1.toString(), dialog));

                    }
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Common.createAlertDialog(this, e.getMessage());
        });
    }

    private void signUp(String user_id, String imageUrl, ProgressDialog dialog) {

        UserModel userModel = new UserModel(user_id, model.getFirstName(), model.getLastName(), model.getPhoneCode(), model.getPhone(), model.getEmail(), model.getPassword(), model.getUserType(), model.getFilter_attr());
        userModel.setImage_url(imageUrl);
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.table_users)
                .child(user_id)
                .setValue(userModel)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                    setUserModel(userModel);
                    setResult(RESULT_OK);
                    finish();
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Common.createAlertDialog(this, e.getMessage());
        });
    }

    private void reAuth() {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(getUserModel().getEmail(), getUserModel().getPassword());
            currentUser.reauthenticate(credential).addOnSuccessListener(unused -> {


                currentUser.updateEmail(model.getEmail())
                        .addOnSuccessListener(unused1 -> {
                            currentUser.updatePassword(model.getPassword())
                                    .addOnSuccessListener(unused2 -> {
                                        updateProfile(dialog);
                                    }).addOnFailureListener(e -> {
                                dialog.dismiss();
                                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                            });
                        }).addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

                });

            }).addOnFailureListener(e -> {
                dialog.dismiss();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "Session expired login again", Toast.LENGTH_SHORT).show();

        }

    }

    private void updateProfile(ProgressDialog dialog) {
        UserModel userModel = new UserModel(getUserModel().getUser_id(), model.getFirstName(), model.getLastName(), model.getPhoneCode(), model.getPhone(), model.getEmail(), model.getPassword(), model.getUserType(), model.getFilter_attr());
        userModel.setRate(getUserModel().getRate());
        dRef = FirebaseDatabase.getInstance().getReference();
        dRef.child(Tags.table_users)
                .child(getUserModel().getUser_id())
                .setValue(userModel)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                    setUserModel(userModel);
                    setResult(RESULT_OK);
                    finish();
                }).addOnFailureListener(e -> {
            dialog.dismiss();
            Common.createAlertDialog(this, e.getMessage());
        });
    }

    private void navigateToLoginActivity() {
        finish();
    }


}