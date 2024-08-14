//package com.example.chatproject.activities;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.util.Base64;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.example.chatproject.R;
//import com.example.chatproject.activities.utilities.Constants;
//import com.example.chatproject.activities.utilities.PreferenceManager;
//import com.example.chatproject.databinding.ActivityMainBinding;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FieldValue;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.messaging.FirebaseMessaging;
//
//import java.util.HashMap;
//
//public class MainActivity extends AppCompatActivity {
//
//    private ActivityMainBinding binding;
//    private PreferenceManager preferenceManager;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(R.layout.activity_main);
//        preferenceManager = new PreferenceManager(getApplicationContext());
//        setListeners();
//        loadUserDetails();
//        getToken();
//
//    }
//
//    private void setListeners(){
//        binding.imageSignOut.setOnClickListener(v -> signOut());
//    }
//
////    private void loadUserDetails(){
////        String name = preferenceManager.getString(Constants.KEY_NAME);
////        String imageString = preferenceManager.getString(Constants.KEY_IMAGE);
////
////        Log.d("MainActivity", "Name: " + name);
////        Log.d("MainActivity", "Image String: " + imageString);
////
////        if (name != null) {
////            binding.textName.setText(name);
////        } else {
////            Log.e("MainActivity", "Name is null");
////        }
////
////        if (imageString != null) {
////            byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
////            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
////            if (bitmap != null) {
////                binding.imageProfile.setImageBitmap(bitmap);
////            } else {
////                Log.e("MainActivity", "Bitmap is null");
////            }
////        } else {
////            Log.e("MainActivity", "Image String is null");
////        }
////    }
//    private void loadUserDetails(){
//        binding.textName1.setText(preferenceManager.getString(Constants.KEY_NAME));
//        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//        binding.imageProfile1.setImageBitmap(bitmap);
//    }
//
//    private void getToken(){
//        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::upadateToken);
//    }
//
//    private void upadateToken(String token){
//        FirebaseFirestore databse = FirebaseFirestore.getInstance();
//        DocumentReference documentReference = databse.collection(Constants.KEY_COLLECTION_USERS).document(
//                preferenceManager.getString(Constants.KEY_USER_ID)
//        );
//        documentReference.update(Constants.KEY_FCM_TOKEN, token)
//                .addOnSuccessListener(unused ->
//                    showToast("Token Updated Successfully"))
//                .addOnFailureListener(e ->
//                    showToast("Unable to update token"));
//
//    }
//
//
//
//    private void signOut(){
//        showToast("Singing Out...");
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        DocumentReference documentReference =
//                database.collection(Constants.KEY_COLLECTION_USERS).document(
//                        preferenceManager.getString(Constants.KEY_USER_ID)
//                );
//        HashMap<String, Object> updates = new HashMap<>();
//        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
//        documentReference.update(updates)
//                .addOnSuccessListener(unused -> {
//                    preferenceManager.clear();
//                    startActivity(new Intent(getApplicationContext(),SignInActivity.class));
//                    finish();
//                })
//                .addOnFailureListener(e -> showToast("Unable to Sign Out"));
//    }
//
//    public void showToast(String message){
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//    }
//}

package com.example.chatproject.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chatproject.R;
import com.example.chatproject.activities.utilities.Constants;
import com.example.chatproject.activities.utilities.PreferenceManager;
import com.example.chatproject.adapters.RecentConversationsAdapter;
import com.example.chatproject.databinding.ActivityMainBinding;
import com.example.chatproject.models.ChatMessage;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PreferenceManager preferenceManager;
    private List<ChatMessage> conversations;
    private RecentConversationsAdapter conversationsAdapter;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());
        init();
        loadUserDetails();

        binding.buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        binding.textName1.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        binding.fabNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UsersActivity.class));
            }
        });
    }

    private void init() {
        conversations = new ArrayList<>();
        conversationsAdapter = new RecentConversationsAdapter(conversations);
        binding.conversationsRecyclerView.setAdapter(conversationsAdapter);
        database = FirebaseFirestore.getInstance();
    }

    private void loadUserDetails() {
        String name = preferenceManager.getString(Constants.KEY_NAME);
        String imageString = preferenceManager.getString(Constants.KEY_IMAGE);

        if (name != null) {
            binding.textName1.setText(name);
        } else {
            Log.e("MainActivity", "Name is null");
        }

        if (imageString != null) {
            byte[] bytes = Base64.decode(imageString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            if (bitmap != null) {
                binding.imageProfile1.setImageBitmap(bitmap);
            } else {
                Log.e("MainActivity", "Bitmap is null");
            }
        } else {
            Log.e("MainActivity", "Image String is null");
        }
    }

    private void signOut() {
        // Clear the preferences
        preferenceManager.clear();

        // Show a toast message
        showToast("Signed out successfully");

        // Redirect to login activity
        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }



    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
      if(error != null) {
          return;
      }
      if(value != null) {
          for(DocumentChange documentChange : value.getDocumentChanges()) {
              if(documentChange.getType() == DocumentChange.Type.ADDED) {
                  String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                  String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                  ChatMessage chatMessage = new ChatMessage();
                  chatMessage.senderId = senderId;
                  chatMessage.receiverId = receiverId;
                  if(preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)) {
                      chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                      chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                      chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);

                  }else {
                      chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                      chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                      chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                  }

                  chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                  chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                  conversations.add(chatMessage);
              } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                  for (int i = 0 ; i < conversations.size(); i++) {
                      String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                      String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                      if (conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)) {
                          conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                          conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                          break;
                      }
                  }
              }
          }
      }
    };
}