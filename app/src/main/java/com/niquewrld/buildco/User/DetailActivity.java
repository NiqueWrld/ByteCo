package com.niquewrld.buildco.User;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.niquewrld.buildco.Adapter.SliderAdapter;
import com.niquewrld.buildco.Domain.SliderItems;
import com.niquewrld.buildco.Domain.ItemsDomain;
import com.niquewrld.buildco.Fragment.DescriptionFragment;
import com.niquewrld.buildco.Fragment.ReviewFragment;
import com.niquewrld.buildco.Fragment.SoldFragment;
import com.niquewrld.buildco.Helper.ManagmentCart;
import com.niquewrld.buildco.R;
import com.niquewrld.buildco.databinding.ActivityDetailBinding;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends BaseActivity {
ActivityDetailBinding binding;
private ItemsDomain object;
private int numberOrder = 1;
private ManagmentCart managmentCart;
private Handler slideHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managmentCart = new ManagmentCart(this);
        getBundle();
        banners();
        setupViewPager();
    }


    private void banners() {
        ArrayList<SliderItems> sliderItems = new ArrayList<>();
        for(int i =0; i < object.getPicUrl().size(); i++){
            sliderItems.add(new SliderItems(object.getPicUrl().get(i)));
        }

        binding.viewpagerSlider.setAdapter(new SliderAdapter(sliderItems , binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setClipChildren(false);
        binding.viewpagerSlider.setOffscreenPageLimit(3);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    private void getBundle() {
        object = (ItemsDomain) getIntent().getSerializableExtra("object");
        binding.titleTxt.setText(object.getTitle());
        binding.priceTxt.setText("R" + object.getPrice());
        binding.ratingBar.setRating( (float) object.getRating() );
        binding.ratingTxt.setText(object.getRating() + " Rating");


        binding.addToCartBtn.setOnClickListener(v -> {
            object.setNumberCart(numberOrder);
            managmentCart.insertFood(object);
        });

        binding.backBtn.setOnClickListener(v -> finish());

        ImageView shareButton = findViewById(R.id.shareBtn);
        shareButton.setOnClickListener(v -> {
            // Create the share intent
            String url = "https://niquewrld.github.io/ByteCo/Item/";
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, url + object.getKey());

            // Start the share activity
            startActivity(Intent.createChooser(shareIntent, "Share text via"));
        });

    }
    public void setupViewPager(){
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());

        DescriptionFragment tab1 = new DescriptionFragment();
        ReviewFragment tab2 = new ReviewFragment();
        SoldFragment tab3 = new SoldFragment();

        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();
        Bundle bundle3 = new Bundle();

        bundle1.putString("description" , object.getDescription());

        tab1.setArguments(bundle1);
        tab2.setArguments(bundle2);
        tab3.setArguments(bundle3);

        adapter.addFrag(tab1,"Descriptions");
        adapter.addFrag(tab2,"Reviews");
        adapter.addFrag(tab3,"Sold");

        binding.viewpager.setAdapter(adapter);
        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }

    private  class  ViewPageAdapter extends FragmentPagerAdapter implements com.niquewrld.buildco.User.ViewPageAdapter {
        private  final  List<Fragment> mFragmentList = new ArrayList<>();
        private final List <String> mFragmentTittleList = new ArrayList<>();
        public ViewPageAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private  void addFrag(Fragment fragment , String title){
            mFragmentList.add(fragment);
            mFragmentTittleList.add(title);
        }

        @Override
        public  CharSequence getPageTitle(int position){
            return  mFragmentTittleList.get(position);
        }
    }
}