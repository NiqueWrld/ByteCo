package com.niquewrld.buildco.Admin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.niquewrld.buildco.Domain.ItemsDomain;
import com.niquewrld.buildco.R;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {

    private EditText etTitle, etDescription, etPrice;
    private Button btnSave;
    private DatabaseReference databaseItems;
    private String selectedItemKey;
    private boolean isEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        // Initialize Firebase Database
        databaseItems = FirebaseDatabase.getInstance().getReference("Items");

        // Initialize views
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etPrice = findViewById(R.id.etPrice);
        btnSave = findViewById(R.id.btnSave);

        // Check if we are in edit mode or add mode
        selectedItemKey = getIntent().getStringExtra("ITEM_KEY");
        isEditMode = selectedItemKey != null;

        if (isEditMode) {
            // Load existing item if needed
            loadSelectedItem();
            btnSave.setText("Update Item");
        } else {
            btnSave.setText("Add Item");
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEditMode) {
                    updateItem();
                } else {
                    addItem();
                }
            }
        });
    }

    private void addItem() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        double oldPrice = 0;
        double price = Double.parseDouble(etPrice.getText().toString().trim());
        double rating =0;
        double review = 0;
        double numberCart = 0;
        Integer key = databaseItems.push().getKey().hashCode();

        if (!TextUtils.isEmpty(title)) {
            ArrayList<String> picUrls = new ArrayList<>(); // Replace with actual URLs
            picUrls.add("https://example.com/pic.png"); // Placeholder, replace with your actual image URL(s)

            ItemsDomain item = new ItemsDomain(title, description, picUrls, price, oldPrice, review, rating, numberCart, key);
            databaseItems.child(String.valueOf(key)).setValue(item);

            Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        } else {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateItem() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        double oldPrice = 0;
        double price = Double.parseDouble(etPrice.getText().toString().trim());
        double rating = 0;
        double review = 0;
        double numberCart = 0;
        Integer key = Integer.parseInt(selectedItemKey);

        if (!TextUtils.isEmpty(title)) {
            ArrayList<String> picUrls = new ArrayList<>(); // Replace with actual URLs
            picUrls.add("https://example.com/pic.png"); // Placeholder, replace with your actual image URL(s)

            ItemsDomain item = new ItemsDomain(title, description, picUrls, price, oldPrice, review, rating, numberCart, key);
            databaseItems.child(selectedItemKey).setValue(item);

            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        } else {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadSelectedItem() {
        databaseItems.child(selectedItemKey).get().addOnSuccessListener(snapshot -> {
            ItemsDomain item = snapshot.getValue(ItemsDomain.class);
            if (item != null) {
                etTitle.setText(item.getTitle());
                etDescription.setText(item.getDescription());
                etPrice.setText(String.valueOf(item.getPrice()));
            }
        }).addOnFailureListener(e -> Toast.makeText(ItemActivity.this, "Failed to load item", Toast.LENGTH_SHORT).show());
    }
}
