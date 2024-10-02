package com.niquewrld.buildco.Admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.niquewrld.buildco.Adapter.ItemsAdapter;
import com.niquewrld.buildco.Domain.ItemsDomain;
import com.niquewrld.buildco.Main.ProfileActivity;
import com.niquewrld.buildco.R;
import com.niquewrld.buildco.User.BaseActivity;
import com.niquewrld.buildco.databinding.ActivityAdminBinding;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemsAdapter adapter;
    private List<ItemsDomain> itemsList;
    private DatabaseReference databaseItems;

    private ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigation();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemsList = new ArrayList<>();
        adapter = new ItemsAdapter(this, itemsList);
        recyclerView.setAdapter(adapter);

        databaseItems = FirebaseDatabase.getInstance().getReference("Items");

        databaseItems.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ItemsDomain item = snapshot.getValue(ItemsDomain.class);
                    itemsList.add(item);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });


    }

    private void bottomNavigation() {
        binding.ProfileBtn.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this , ProfileActivity.class)));
    }
}