package com.app.pet_animals.uis.activity_forget_password;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import com.app.pet_animals.R;
import com.app.pet_animals.databinding.ActivityForgetPasswordBinding;
import com.app.pet_animals.databinding.ActivityLoginBinding;
import com.app.pet_animals.models.LoginModel;
import com.app.pet_animals.tags.Common;
import com.app.pet_animals.uis.activity_base.ActivityBase;
import com.app.pet_animals.uis.activity_login.LoginActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class ForgetPasswordActivity extends ActivityBase {
    private ActivityForgetPasswordBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        initView();
    }

    private void initView() {

        binding.btnSend.setOnClickListener(view -> {
            String email = binding.edtEmail.getText().toString().trim();

            if (!email.isEmpty()&& Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Common.CloseKeyBoard(this, binding.edtEmail);
                binding.edtEmail.setError(null);
                forgetPassword(email);
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.edtEmail.setError(getString(R.string.inv_email));

            }else if (email.isEmpty()){
                binding.edtEmail.setError(getString(R.string.field_required));

            }
        });

        binding.llLogin.setOnClickListener(view -> {
            finish();
        });
    }


    private void forgetPassword(String email) {
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.logging_in));
        dialog.setCancelable(false);
        dialog.show();
        mAuth = FirebaseAuth.getInstance();

        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(unused -> {
                    dialog.dismiss();
                    Toast.makeText(this, "Check your email address to rest your password", Toast.LENGTH_SHORT).show();
                    finish();
                }).addOnFailureListener(e ->{
                    dialog.dismiss();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}