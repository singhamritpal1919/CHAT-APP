//package com.example.chatproject.activities;
//
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Base64;
//import android.util.Patterns;
//import android.view.View;
//import android.widget.Toast;
//
//
//import com.example.chatproject.activities.utilities.Constants;
//import com.example.chatproject.activities.utilities.PreferenceManager;
//import com.example.chatproject.databinding.ActivitySignUpBinding;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import java.io.ByteArrayOutputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//import java.util.HashMap;
//
//public class SignUpActivity extends AppCompatActivity {
//
//    private ActivitySignUpBinding binding;
//    private PreferenceManager preferenceManager;
//    private String encodedImage;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        preferenceManager = new PreferenceManager(getApplicationContext());
//        setListeners();
//    }
//
//    private void setListeners(){
//        binding.textSignIn.setOnClickListener(v -> onBackPressed());
//        binding.buttonSignUp.setOnClickListener(v -> {
//            if (inValidSignUpDetails()) {
//                signup();
//            }
//        });
//        binding.layoutImage.setOnClickListener(v -> {
//            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            pickImage.launch(intent);
//        });
//    }
//
//    private void showToast(String message){
//        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
//    }
//
//    private void signup(){
//        loading(true);
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        HashMap<String, Object> user = new HashMap<>();
//        user.put(Constants.KEY_NAME,binding.inputName.getText().toString());
//        user.put(Constants.KEY_EMAIL,binding.inputEmail.getText().toString());
//        user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
//        user.put(Constants.KEY_IMAGE,encodedImage);
//        database.collection(Constants.KEY_COLLECTION_USERS)
//                .add(user)
//                .addOnSuccessListener(documentReference -> {
//                    loading(false);
//                    preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
//                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
//                    preferenceManager.putString(Constants.KEY_NAME,binding.inputName.getText().toString());
//                    preferenceManager.putString(Constants.KEY_IMAGE,encodedImage);
//                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                })
//                .addOnFailureListener(exception -> {
//                    loading(false);
//                    showToast(exception.getMessage());
//
//                });
//
//    }
//
//    private String encodedImage(Bitmap bitmap){
//        int previewWidth = 150;
//        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
//        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);
//        byte[] bytes = byteArrayOutputStream.toByteArray();
//        return Base64.encodeToString(bytes, Base64.DEFAULT);
//    }
//
//    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if(result.getResultCode() == RESULT_OK){
//                    if(result.getData() != null){
//                        Uri imageUri = result.getData().getData();
//                        try{
//                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
//                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                            binding.imageProfile.setImageBitmap(bitmap);
//                            binding.textAddImage.setVisibility(View.GONE);
//                            encodedImage = encodedImage(bitmap);
//                        }catch(FileNotFoundException e){
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//    );
//
//    private Boolean inValidSignUpDetails(){
//        if (encodedImage == null){
//            showToast("Select profile image");
//            return false;
//        }else if(binding.inputName.getText().toString().trim().isEmpty()){
//            showToast("Enter name");
//            return false;
//        } else if (binding.inputEmail.getText().toString().trim().isEmpty()) {
//            showToast("Enter email");
//            return false;
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
//            showToast("Enter Valid Email");
//            return false;
//        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
//            showToast("Enter Password");
//            return false;
//        } else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
//            showToast("Confirm Your Password");
//        } else if(!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())){
//            showToast("Password & confirm password must be same");
//            return false;
//        } else {
//            return true;
//        }
//        return true;
//
//    }
//
//    private void loading(Boolean isLoading){
//        if(isLoading){
//            binding.buttonSignUp.setVisibility(View.INVISIBLE);
//            binding.progressBar.setVisibility(View.VISIBLE);
//        }else {
//            binding.progressBar.setVisibility(View.INVISIBLE);
//            binding.buttonSignUp.setVisibility(View.VISIBLE);
//        }
//    }
//}

package com.example.chatproject.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.chatproject.activities.utilities.Constants;
import com.example.chatproject.activities.utilities.PreferenceManager;
import com.example.chatproject.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private PreferenceManager preferenceManager;
    private String encodedImage;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        firebaseAuth = FirebaseAuth.getInstance();
        setListeners();
    }

    private void setListeners(){
        binding.textSignIn.setOnClickListener(v -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(v -> {
            if (inValidSignUpDetails()) {
                signUp();
            }
        });
        binding.layoutImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void signUp() {
        loading(true);
        String email = binding.inputEmail.getText().toString().trim();
        String password = binding.inputPassword.getText().toString().trim();
        String dateOfBirth = binding.inputDateOfBirth.getText().toString().trim();

        int age = calculateAge(dateOfBirth);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseFirestore database = FirebaseFirestore.getInstance();
                            String userId = firebaseAuth.getCurrentUser().getUid();

                            HashMap<String, Object> user = new HashMap<>();
                            user.put(Constants.KEY_NAME, binding.inputName.getText().toString());
                            user.put(Constants.KEY_EMAIL, email);
                            user.put(Constants.KEY_IMAGE, encodedImage);
                            user.put(Constants.KEY_DOB, dateOfBirth);
                            user.put(Constants.KEY_AGE, age);

                            database.collection(Constants.KEY_COLLECTION_USERS)
                                    .document(userId)
                                    .set(user)
                                    .addOnSuccessListener(documentReference -> {
                                        loading(false);
                                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                                        preferenceManager.putString(Constants.KEY_USER_ID, userId);
                                        preferenceManager.putString(Constants.KEY_NAME, binding.inputName.getText().toString());
                                        preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(exception -> {
                                        loading(false);
                                        showToast("Failed to store user data: " + exception.getMessage());
                                    });
                        } else {
                            loading(false);
                            showToast("Authentication failed: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private String encodedImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imageProfile.setImageBitmap(bitmap);
                            binding.textAddImage.setVisibility(View.GONE);
                            encodedImage = encodedImage(bitmap);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );


    private int calculateAge(String dateOfBirth) {

        String[] dobParts = dateOfBirth.split("/");
        int day = Integer.parseInt(dobParts[0]);
        int month = Integer.parseInt(dobParts[1]);
        int year = Integer.parseInt(dobParts[2]);

        Calendar dob = Calendar.getInstance();
        dob.set(year, month - 1, day);

        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    private Boolean inValidSignUpDetails(){
        if (encodedImage == null){
            showToast("Select profile image");
            return false;
        } else if(binding.inputName.getText().toString().trim().isEmpty()){
            showToast("Enter name");
            return false;
        } else if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
        } else if (binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else if (binding.inputConfirmPassword.getText().toString().trim().isEmpty()) {
            showToast("Confirm your password");
            return false;
        } else if (!binding.inputPassword.getText().toString().equals(binding.inputConfirmPassword.getText().toString())) {
            showToast("Password & confirm password must be same");
            return false;
        } else if (binding.inputDateOfBirth.getText().toString().trim().isEmpty()) {
            showToast("Enter date of birth");
            return false;
        } else {
            return true;
        }

    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
        }
    }
}