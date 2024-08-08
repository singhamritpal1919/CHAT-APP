package com.example.chatproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatproject.databinding.ActivityResetPasswordBinding;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ResetPasswordActivity extends AppCompatActivity {

    private ActivityResetPasswordBinding binding;
    private FirebaseAuth firebaseAuth;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();

        setListeners();
    }

    private void setListeners() {
        binding.buttonSendVerificationCode.setOnClickListener(v -> {
            String email = binding.inputEmail.getText().toString().trim();
            if (email.isEmpty()) {
                showToast("Please enter your email");
                return;
            }
            sendVerificationCode(email);


        });

//        binding.buttonResetPassword.setOnClickListener(v -> {
//            String newPassword = binding.inputNewPassword.getText().toString().trim();
//            String code = binding.inputVerificationCode.getText().toString().trim();
//            if (newPassword.isEmpty() || code.isEmpty()) {
//                showToast("Please enter all fields");
//                return;
//            }
//            resetPassword(code, newPassword);
//        });

        binding.buttonBackToSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(ResetPasswordActivity.this, SignInActivity.class);
            startActivity(intent);
            finish(); // Close the ResetPasswordActivity
        });

    }

    private void sendVerificationCode(String email) {

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showToast("Verification code sent to your email");

                        binding.buttonSendVerificationCode.setVisibility(View.GONE);
                        binding.buttonBackToSignIn.setVisibility(View.VISIBLE);
                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                            showToast("No account with this email found");
                        } else {
                            showToast("Error sending verification code");
                        }
                    }
                });
    }



    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}



