package com.niquewrld.buildco.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.niquewrld.buildco.Admin.ItemActivity;
import com.niquewrld.buildco.Domain.ItemsDomain;
import com.niquewrld.buildco.R;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_ADD = 1;

    private List<ItemsDomain> itemsList;
    private Context context;

    public ItemsAdapter(Context context, List<ItemsDomain> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == itemsList.size()) ? VIEW_TYPE_ADD : VIEW_TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_button_layout, parent, false);
            return new AddButtonViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            ItemsDomain item = itemsList.get(position);
            itemViewHolder.tvTitle.setText(item.getTitle());

            itemViewHolder.btnEdit.setOnClickListener(v -> {
                // Implement edit action
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("ITEM_KEY", item.getKey().toString());
                context.startActivity(intent);
            });

            itemViewHolder.btnDelete.setOnClickListener(v -> {
                // Implement delete action
                DatabaseReference databaseItems = FirebaseDatabase.getInstance().getReference("Items");
                databaseItems.child(String.valueOf(item.getKey())).removeValue();
                Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
            });

        } else if (holder.getItemViewType() == VIEW_TYPE_ADD) {
            AddButtonViewHolder addButtonViewHolder = (AddButtonViewHolder) holder;
            addButtonViewHolder.btnAdd.setOnClickListener(v -> {
                // Implement add action
                Intent intent = new Intent(context, ItemActivity.class);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return itemsList.size() + 1; // +1 for the add button
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public Button btnEdit, btnDelete;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public static class AddButtonViewHolder extends RecyclerView.ViewHolder {
        public Button btnAdd;

        public AddButtonViewHolder(View itemView) {
            super(itemView);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}
