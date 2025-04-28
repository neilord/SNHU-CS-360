package com.example.uiapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/** RecyclerView adapter for the inventory grid. */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.VH> {

    public interface OnItemClickListener { void onDeleteClick(int position); }

    private List<DataItem> items;
    private final OnItemClickListener listener;

    public DataAdapter(List<DataItem> items, OnItemClickListener l) {
        this.items = items;
        this.listener = l;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View v1 = LayoutInflater.from(p.getContext()).inflate(R.layout.item_data, p, false);
        return new VH(v1);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        DataItem it = items.get(pos);
        h.title.setText(it.getTitle());
        h.details.setText(it.getDetails());
        h.qty.setText("Qty: " + it.getQty());
    }

    @Override public int getItemCount() { return items.size(); }

    public void update(List<DataItem> newData) {
        this.items = newData;
        notifyDataSetChanged();
    }

    class VH extends RecyclerView.ViewHolder {
        TextView title, details, qty;
        ImageButton del;
        VH(@NonNull View v) {
            super(v);
            title   = v.findViewById(R.id.itemTitleTextView);
            details = v.findViewById(R.id.itemDetailsTextView);
            qty     = v.findViewById(R.id.itemQtyTextView);
            del     = v.findViewById(R.id.deleteButton);
            del.setOnClickListener(view -> {
                int p = getAdapterPosition();
                if (p != RecyclerView.NO_POSITION) listener.onDeleteClick(p);
            });
        }
    }
}
