package com.niquewrld.buildco.Main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.niquewrld.buildco.Adapter.PopularAdapter;
import com.niquewrld.buildco.Domain.ItemsDomain;
import com.niquewrld.buildco.R;
import com.niquewrld.buildco.User.BaseActivity;
import com.niquewrld.buildco.User.CartActivity;
import com.niquewrld.buildco.User.MainActivity;
import com.niquewrld.buildco.databinding.ActivityProfileBinding;

import java.util.ArrayList;
import java.util.Objects;

public class ProfileActivity extends BaseActivity {
    FirebaseAuth mAuth;
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.CartBtn.setOnClickListener(v -> startActivity(new Intent(this , CartActivity.class)));

        mAuth=FirebaseAuth.getInstance();

        setVariable();
        setFields();
    }

    private void setFields() {
        FirebaseUser currentUser = mAuth.getCurrentUser();


        DatabaseReference myRef = database.getReference("Users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    StringBuilder keysBuilder = new StringBuilder();
                    for (DataSnapshot issue : snapshot.getChildren()) {

                        if(Objects.equals(issue.getKey(), currentUser.getUid())){
                            String firstName = issue.child("firstname").getValue(String.class);
                            String lastname = issue.child("lastname").getValue(String.class);
                            String number = issue.child("telephone").getValue(String.class);

                            binding.userName.setText(firstName + " " + lastname);
                            binding.userEmail.setText(currentUser.getEmail());
                            binding.userMobile.setText(number);
                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors here
            }
        });



    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(view -> finish());
        binding.logoutBtn.setOnClickListener(view -> logout());
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();//to sign out using firebase
        Intent intent=new Intent(getApplicationContext(), WelcomeActivity.class);//now the user will be signed out then we
        startActivity(intent);// move to login activity
        finish();
    }
}