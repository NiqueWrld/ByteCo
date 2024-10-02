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
import com.niquewrld.buildco.Admin.AdminActivity;
import com.niquewrld.buildco.Domain.CategoryDomain;
import com.niquewrld.buildco.Domain.SliderItems;
import com.niquewrld.buildco.Domain.ItemsDomain;
import com.niquewrld.buildco.Main.ProfileActivity;
import com.niquewrld.buildco.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBanner();
        initCategory();
        initPopular();
        bottomNavigation();
    }

    private void bottomNavigation() {
        binding.CartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this , CartActivity.class)));
        binding.ProfileBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this , ProfileActivity.class)));
    }

    private void initPopular() {
        DatabaseReference myRef = database.getReference("Items");
        binding.progressBar3.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int count = 0; // Counter to track the number of items added
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        if (count < 4) { // Check if we have already added 4 items
                            items.add(issue.getValue(ItemsDomain.class));
                            count++; // Increment the counter
                        } else {
                            break; // Exit the loop if we have added 4 items
                        }
                    }
                    if (!items.isEmpty()) {
                        binding.recyclerViewPopular.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                        binding.recyclerViewPopular.setAdapter(new PopularAdapter(items));
                    }
                    binding.progressBar3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors here
            }
        });
    }


    private void initCategory() {
        DatabaseReference myRef = database.getReference("Category");
        binding.progressBarOfficial.setVisibility(View.VISIBLE);
        ArrayList<CategoryDomain> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(CategoryDomain.class));
                    }
                    if(!items.isEmpty()){
                        binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                                LinearLayoutManager.HORIZONTAL, false));
                        binding.recyclerViewOfficial.setAdapter(new CategoryAdapter(items));
                    }
                    binding.progressBarOfficial.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public  void  seeallpopularproducts(View view){
        Intent intent = new Intent(this, SeeAllActivity.class);
        startActivity(intent);
    }

    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot issue:snapshot.getChildren()){
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
public void banners(ArrayList<SliderItems> items){
    binding.viewpagerSlider.setAdapter(new SliderAdapter(items , binding.viewpagerSlider));
    binding.viewpagerSlider.setClipToPadding(false);
    binding.viewpagerSlider.setClipChildren(false);
    binding.viewpagerSlider.setOffscreenPageLimit(3);
    binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

    CompositePageTransformer compositePageTransformer =new CompositePageTransformer();
    compositePageTransformer.addTransformer(new MarginPageTransformer(40));

    binding.viewpagerSlider.setPageTransformer(compositePageTransformer);
}

}