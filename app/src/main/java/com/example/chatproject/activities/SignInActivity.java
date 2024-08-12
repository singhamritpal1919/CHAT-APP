//package com.example.chatproject.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Patterns;
//import android.view.View;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.chatproject.R;
//import com.example.chatproject.activities.utilities.Constants;
//import com.example.chatproject.activities.utilities.PreferenceManager;
//import com.example.chatproject.databinding.ActivitySignInBinding;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//public class SignInActivity extends AppCompatActivity {
//
//    private ActivitySignInBinding binding;
//    private PreferenceManager preferenceManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        preferenceManager = new PreferenceManager(getApplicationContext());
//        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
//            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//        binding = ActivitySignInBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        setListeners();
//    }
//
//
//
//    private void setListeners(){
//        binding.textCreateNewAccount.setOnClickListener(v ->
//                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
//        binding.buttonSignIn.setOnClickListener(v -> {
//            if (inValidSignInDetails()){
//                signIn();
//            }
//        });
//    }
//
//    private void signIn(){
//        loading(true);
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        database.collection(Constants.KEY_COLLECTION_USERS)
//                .whereEqualTo(Constants.KEY_EMAIL,binding.inputEmail.getText().toString())
//                .whereEqualTo(Constants.KEY_PASSWORD,binding.inputPassword.getText().toString())
//                .get()
//                .addOnCompleteListener(task -> {
//                    if(task.isSuccessful() && task.getResult() != null
//                            && task.getResult().getDocuments().size() > 0 ){
//                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
//                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN,true);
//                        preferenceManager.putString(Constants.KEY_USER_ID,documentSnapshot.getId());
//                        preferenceManager.putString(Constants.KEY_NAME,documentSnapshot.getString(Constants.KEY_NAME));
//                        preferenceManager.putString(Constants.KEY_IMAGE,documentSnapshot.getString(Constants.KEY_IMAGE));
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                        startActivity(intent);
//                    }else {
//                        loading(false);
//                        showToast("Unable to sign in");
//                    }
//                });
//    }
//
//    private void loading(Boolean isLoading){
//        if(isLoading){
//            binding.buttonSignIn.setVisibility(View.INVISIBLE);
//            binding.progressBar1.setVisibility(View.VISIBLE);
//        }else {
//            binding.progressBar1.setVisibility(View.INVISIBLE);
//            binding.buttonSignIn.setVisibility(View.VISIBLE);
//        }
//    }
//
//    public void showToast(String message){
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//    }
//
//    private Boolean inValidSignInDetails(){
//        if (binding.inputEmail.getText().toString().trim().isEmpty()){
//            showToast("Enter Email");
//            return false;
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
//            showToast("Enter Valid Email");
//            return false;
//        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
//            showToast("Enter Password");
//            return false;
//        }else {
//            return true;
//        }
//    }
//
//
//}

package com.example.chatproject.activities;
//
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatproject.R;
import com.example.chatproject.activities.utilities.Constants;
import com.example.chatproject.activities.utilities.PreferenceManager;
import com.example.chatproject.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
//
//public class SignInActivity extends AppCompatActivity {
//
//    private ActivitySignInBinding binding;
//    private PreferenceManager preferenceManager;
//    private FirebaseAuth firebaseAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        preferenceManager = new PreferenceManager(getApplicationContext());
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//
//        binding = ActivitySignInBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        setListeners();
//    }
//
//    private void setListeners() {
//        binding.textCreateNewAccount.setOnClickListener(v ->
//                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
//        binding.buttonSignIn.setOnClickListener(v -> {
//            if (inValidSignInDetails()) {
//                signIn();
//            }
//        });
//    }
//
//    private void signIn() {
//        loading(true);
//        String email = binding.inputEmail.getText().toString().trim();
//        String password = binding.inputPassword.getText().toString().trim();
//
//        firebaseAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseFirestore database = FirebaseFirestore.getInstance();
//                            String userId = firebaseAuth.getCurrentUser().getUid();
//                            database.collection(Constants.KEY_COLLECTION_USERS)
//                                    .document(userId)
//                                    .get()
//                                    .addOnCompleteListener(task1 -> {
//                                        if (task1.isSuccessful() && task1.getResult() != null) {
//                                            DocumentSnapshot documentSnapshot = task1.getResult();
//                                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
//                                            preferenceManager.putString(Constants.KEY_USER_ID, userId);
//                                            preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
//                                            preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
//                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                            startActivity(intent);
//                                        } else {
//                                            loading(false);
//                                            showToast("Unable to fetch user data");
//                                        }
//                                    });
//                        } else {
//                            loading(false);
//                            showToast("Authentication failed");
//                        }
//                    }
//                });
//    }
//
//    private void loading(Boolean isLoading) {
//        if (isLoading) {
//            binding.buttonSignIn.setVisibility(View.INVISIBLE);
//            binding.progressBar1.setVisibility(View.VISIBLE);
//        } else {
//            binding.progressBar1.setVisibility(View.INVISIBLE);
//            binding.buttonSignIn.setVisibility(View.VISIBLE);
//        }
//    }
//
//    public void showToast(String message) {
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//    }
//
//    private Boolean inValidSignInDetails() {
//        if (binding.inputEmail.getText().toString().trim().isEmpty()) {
//            showToast("Enter Email");
//            return false;
//        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
//            showToast("Enter Valid Email");
//            return false;
//        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
//            showToast("Enter Password");
//            return false;
//        } else {
//            return true;
//        }
//    }
//}

//package com.example.chatproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatproject.R;
import com.example.chatproject.activities.utilities.Constants;
import com.example.chatproject.activities.utilities.PreferenceManager;
import com.example.chatproject.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();

        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.textCreateNewAccount.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));

        binding.textResetPassword.setOnClickListener(v -> {
            resetPassword();
        });

        binding.buttonSignIn.setOnClickListener(v -> {
            if (inValidSignInDetails()) {
                signIn();
            }
        });
    }

    private void signIn() {
        loading(true);
        String email = binding.inputEmail.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseFirestore database = FirebaseFirestore.getInstance();
                            String userId = firebaseAuth.getCurrentUser().getUid();
                            database.collection(Constants.KEY_COLLECTION_USERS)
                                    .document(userId)
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful() && task1.getResult() != null) {
                                            DocumentSnapshot documentSnapshot = task1.getResult();
                                            preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                            preferenceManager.putString(Constants.KEY_USER_ID, userId);
                                            preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                                            preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        } else {
                                            loading(false);
                                            showToast("Unable to fetch user data");
                                        }
                                    });
                        } else {
                            loading(false);
                            showToast("Authentication failed");
                        }
                    }
                });
    }

    private void resetPassword() {

        binding.textResetPassword.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });

//        String email = binding.inputEmail.getText().toString().trim();
//        if (email.isEmpty()) {
//            showToast("Please enter your email to reset password");
//            return;
//        }
//
//        firebaseAuth.sendPasswordResetEmail(email)
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        showToast("Password reset email sent");
//                    } else {
//                        showToast("Error in sending password reset email");
//                    }
//                });
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.buttonSignIn.setVisibility(View.INVISIBLE);
            binding.progressBar1.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar1.setVisibility(View.INVISIBLE);
            binding.buttonSignIn.setVisibility(View.VISIBLE);
        }
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean inValidSignInDetails() {
        if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter Email");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter Valid Email");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter Password");
            return false;
        } else {
            return true;
        }
    }
}
