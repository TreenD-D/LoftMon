package com.achulkov.loftmon.list;

import android.content.Context;
import android.util.SparseBooleanArray;
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

    private SparseBooleanArray mSelectedItems = new SparseBooleanArray();

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

    public void setListener(final MoneyListClick moneyCellAdapterClick) {
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
        holder.bind(moneyItemList.get(position), mSelectedItems.get(position));
        holder.setListener(moneyCellAdapterClick, moneyItemList.get(position), position);
    }


    @Override
    public int getItemCount() {
        return moneyItemList.size();
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private View mItemView;
        private TextView titleTextView;
        private TextView valueTextView;
        private MoneyListClick moneyCellAdapterClick;

        public ItemViewHolder(@NonNull View itemView, final MoneyListClick moneyCellAdapterClick, final int colorId) {
            super(itemView);
            mItemView = itemView;
            this.moneyCellAdapterClick = moneyCellAdapterClick;

            titleTextView = itemView.findViewById(R.id.moneyCellTitleView);
            valueTextView = itemView.findViewById(R.id.moneyCellValueView);

            final Context context = valueTextView.getContext();
            valueTextView.setTextColor(ContextCompat.getColor(context, colorId));
        }

        public void bind(final MoneyItem moneyItem, final boolean isSelected) {
            mItemView.setSelected(isSelected);
            titleTextView.setText(moneyItem.getTitle());
            valueTextView.setText(moneyItem.getValue());

/*            itemView.setOnClickListener(new View.OnClickListener() {
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
            });*/
        }
        public void setListener(final MoneyListClick listener, final MoneyItem item, final int position) {
            mItemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View view) {
                    listener.onItemClick(item, position);
                }
            });

            mItemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(final View view) {
                    listener.onItemLongClick(item, position);
                    return false;
                }
            });
        }
    }



    public void clearSelections() {
        mSelectedItems.clear();
        notifyDataSetChanged();
    }

    public void toggleItem(final int position) {
        mSelectedItems.put(position, !mSelectedItems.get(position));
        notifyDataSetChanged();
    }

    public void clearItem(final int position) {
        mSelectedItems.put(position, false);
        notifyDataSetChanged();
    }

    public int getSelectedSize() {
        int result = 0;
        for (int i = 0; i < moneyItemList.size(); i++) {
            if (mSelectedItems.get(i)) {
                result++;
            }
        }
        return result;
    }

    public List<Integer> getSelectedItemIds() {
        List<Integer> result = new ArrayList<>();
        int i = 0;
        for (MoneyItem item: moneyItemList) {
            if (mSelectedItems.get(i)) {
                result.add(item.getId());
            }
            i++;
        }
        return result;
    }

}
