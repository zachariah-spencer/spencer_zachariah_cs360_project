package com.example.spencer_zachariah_cs360_project.db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter to display Item objects in a RecyclerView grid.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final List<Item> items = new ArrayList<>();
    private final LayoutInflater inflater;
    private final OnItemChangedListener listener;

    public interface OnItemChangedListener {
        void onDelete(Item item);
        void onUpdate(Item item);
    }

    public ItemAdapter(Context context, OnItemChangedListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public void setItems(List<Item> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_data, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.nameText.setText(item.name + " (" + item.quantity + ")");
        holder.deleteButton.setOnClickListener(v -> listener.onDelete(item));
        holder.itemView.setOnClickListener(v -> listener.onUpdate(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        final TextView nameText;
        final Button deleteButton;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.item_text);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}
