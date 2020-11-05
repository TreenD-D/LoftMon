package com.achulkov.loftmon.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.achulkov.loftmon.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {
    private List<MoneyItem> moneyItemList = new ArrayList<>();

    public MoneyListClick moneyCellAdapterClick;
    private final int colorId;

    public ItemsAdapter(final int colorId) {
        this.colorId = colorId;
    }

    public void setData(List<MoneyItem> moneyItems) {
        moneyItemList.clear();
        moneyItemList.addAll(moneyItems);

        notifyDataSetChanged();
    }

    public void addItem(MoneyItem item) {
        moneyItemList.add(item);
        notifyDataSetChanged();
    }

    public void clearItems() {
        moneyItemList.clear();
        notifyDataSetChanged();
    }

    public void setMoneyCellAdapterClick(MoneyListClick moneyCellAdapterClick) {
        this.moneyCellAdapterClick = moneyCellAdapterClick;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ItemViewHolder(layoutInflater.inflate(R.layout.cell_money, parent, false),
                moneyCellAdapterClick, colorId);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(moneyItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return moneyItemList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView titleTextView;
        private TextView valueTextView;
        private MoneyListClick moneyCellAdapterClick;

        public ItemViewHolder(@NonNull View itemView, final MoneyListClick moneyCellAdapterClick, final int colorId) {
            super(itemView);

            this.moneyCellAdapterClick = moneyCellAdapterClick;

            titleTextView = itemView.findViewById(R.id.moneyCellTitleView);
            valueTextView = itemView.findViewById(R.id.moneyCellValueView);

            final Context context = valueTextView.getContext();
            valueTextView.setTextColor(ContextCompat.getColor(context, colorId));
        }

        public void bind(final MoneyItem moneyItem) {
            titleTextView.setText(moneyItem.getTitle());
            valueTextView.setText(moneyItem.getValue());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (moneyCellAdapterClick != null) {
                        moneyCellAdapterClick.onCellClick(moneyItem);
                    }
                }
            });

            titleTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (moneyCellAdapterClick != null) {
                        moneyCellAdapterClick.onTitleClick();
                    }
                }
            });
        }
    }
}
