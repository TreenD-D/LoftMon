package com.achulkov.loftmon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.achulkov.loftmon.list.MoneyItem;
import com.achulkov.loftmon.list.ItemsAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final int ADD_ITEM = 1;

    List<MoneyItem> moneyItems = new ArrayList<>();
    ItemsAdapter itemsAdapter = new ItemsAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        RecyclerView recyclerView = findViewById(R.id.itemsView);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,
                false));

        generateData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == ADD_ITEM){
            if(resultCode == RESULT_OK) {
                MoneyItem mItem = new MoneyItem(data.getStringExtra("ITEM_NAME"),data.getStringExtra("ITEM_PRICE"));
                moneyItems.add(mItem);
                itemsAdapter.setData(moneyItems);
                itemsAdapter.notifyDataSetChanged();
                for(MoneyItem item1:moneyItems)Log.i("items", item1.getTitle());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void generateData() {


        moneyItems.add(new MoneyItem("Salary", "50000$"));
        moneyItems.add(new MoneyItem("Taxes", "25000$"));
        moneyItems.add(new MoneyItem("PS4", "1500$"));
        moneyItems.add(new MoneyItem("Food", "3500$"));

        itemsAdapter.setData(moneyItems);
        itemsAdapter.notifyDataSetChanged();
    }

    public void onClickAddItem(View view){
        Intent intent = new Intent(this, AddItemActivity.class);
        startActivityForResult(intent, ADD_ITEM);
    }

}
