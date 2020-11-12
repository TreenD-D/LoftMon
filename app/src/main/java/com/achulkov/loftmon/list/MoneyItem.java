package com.achulkov.loftmon.list;

import com.achulkov.loftmon.remote.MoneyRemoteItem;

public class MoneyItem {
    private String title;
    private String value;


    private int id;

    public MoneyItem(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public MoneyItem(String title, String value, int id) {
        this.title = title;
        this.value = value;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }

    public static MoneyItem getInstance(MoneyRemoteItem moneyRemoteItem) {
        return new MoneyItem(moneyRemoteItem.getName(), moneyRemoteItem.getPrice() + "$", Integer.parseInt(moneyRemoteItem.getItemId()));
    }
}
