package com.niquewrld.buildco.Main;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.niquewrld.buildco.Admin.AdminActivity;
import com.niquewrld.buildco.Domain.ItemsDomain;
import com.niquewrld.buildco.User.BaseActivity;
import com.niquewrld.buildco.User.DetailActivity;
import com.niquewrld.buildco.User.MainActivity;
import com.niquewrld.buildco.databinding.ActivityDeepLinkBinding;

import java.util.ArrayList;

public class DeepLinkActivity extends BaseActivity {

    ArrayList<ItemsDomain> items;
    ActivityDeepLinkBinding binding;

    int id;

    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            Intent intent=new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeepLinkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth=FirebaseAuth.getInstance();

        Uri data = getIntent().getData();
        if (data != null) {
            String path = data.getPath();
            String lastPart = path.substring(path.lastIndexOf("/") + 1);

            try {
                id = Integer.parseInt(lastPart);
                loadData();
            } catch (Exception e){
                binding.status.setText("Uknown ID:" + lastPart);
                binding.progressBar4.setVisibility(View.GONE);
            }


        }
    }

    private void loadData() {
        DatabaseReference myRef = database.getReference("Items");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                binding.status.setText("Getting Data....");

                if (snapshot.exists()) {
                    items = new ArrayList<>(); // Initialize the items list
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));
                    }
                    if (!items.isEmpty()) {
                        openDetails(); // Move this call here
                        binding.status.setText("Opening....");
                    } else {
                        Toast.makeText(DeepLinkActivity.this, "No items found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.progressBar4.setVisibility(View.GONE);
                binding.status.setText("Failed to load data");
            }
        });
    }

    public void openDetails() {
        if (items != null && !items.isEmpty()) {
            try {
                Intent intent = new Intent(this, DetailActivity.class);
                intent.putExtra("object", items.get(id));
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Failed to open details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                binding.progressBar4.setVisibility(View.GONE);
            }
        } else {
            Toast.makeText(this, "No items available to display", Toast.LENGTH_SHORT).show();
        }
    }
}
