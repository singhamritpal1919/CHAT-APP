package com.example.chatproject.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatproject.activities.utilities.Constants;
import com.example.chatproject.activities.utilities.PreferenceManager;
import com.example.chatproject.databinding.ActivityEditProfileBinding;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());
        loadUserDetails();

        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadUserDetails() {
        binding.inputName.setText(preferenceManager.getString(Constants.KEY_NAME));
        binding.inputEmail.setText(preferenceManager.getString(Constants.KEY_EMAIL));
        binding.inputAge.setText(String.valueOf(preferenceManager.getInt(Constants.KEY_AGE, 0))); // Load age
    }

    private void saveProfile() {
        String name = binding.inputName.getText().toString().trim();
        String email = binding.inputEmail.getText().toString().trim();
        int age = Integer.parseInt(binding.inputAge.getText().toString().trim());

        if (name.isEmpty() || email.isEmpty() || age <= 0) {
            showToast("Please fill all the details");
            return;
        }

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String userId = preferenceManager.getString(Constants.KEY_USER_ID);

        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(userId)
                .update(
                        Constants.KEY_NAME, name,
                        Constants.KEY_EMAIL, email,
                        Constants.KEY_AGE, age
                )
                .addOnSuccessListener(aVoid -> {
                    preferenceManager.putString(Constants.KEY_NAME, name);
                    preferenceManager.putString(Constants.KEY_EMAIL, email);
                    preferenceManager.putInt(Constants.KEY_AGE, age);
                    showToast("Profile updated successfully");
                    finish();
                })
                .addOnFailureListener(e -> showToast("Failed to update profile: " + e.getMessage()));
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
