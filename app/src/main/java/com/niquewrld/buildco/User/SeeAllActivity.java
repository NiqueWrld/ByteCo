package com.niquewrld.buildco.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.niquewrld.buildco.Adapter.CategoryAdapter;
import com.niquewrld.buildco.Adapter.PopularAdapter;
import com.niquewrld.buildco.Adapter.SliderAdapter;
import com.niquewrld.buildco.Domain.CategoryDomain;
import com.niquewrld.buildco.Domain.ItemsDomain;
import com.niquewrld.buildco.Domain.SliderItems;
import com.niquewrld.buildco.Main.ProfileActivity;
import com.niquewrld.buildco.databinding.ActivityMainBinding;
import com.niquewrld.buildco.databinding.ActivitySeeallBinding;

import java.util.ArrayList;

public class SeeAllActivity extends BaseActivity {
    private ActivitySeeallBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeeallBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initPopular();
        bottomNavigation();
    }

    private void bottomNavigation() {
        binding.CartBtn.setOnClickListener(v -> startActivity(new Intent(SeeAllActivity.this , CartActivity.class)));
        binding.ProfileBtn.setOnClickListener(v -> startActivity(new Intent(SeeAllActivity.this , ProfileActivity.class)));
    }

    private void initPopular() {
        DatabaseReference myRef = database.getReference("Items");
        binding.progressBar3.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(ItemsDomain.class));
                    }
                    if(!items.isEmpty()){
                        binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(SeeAllActivity.this, 2));
                        binding.recyclerViewPopular.setAdapter(new PopularAdapter(items));
                    }
                    binding.progressBar3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}