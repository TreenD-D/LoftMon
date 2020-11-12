package com.achulkov.loftmon.list;

public interface MoneyListClick {
    public void onItemClick(MoneyItem moneyItem, int position);
    public void onItemLongClick(MoneyItem item, int position);
}
