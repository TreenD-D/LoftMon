package com.achulkov.loftmon;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.achulkov.loftmon.list.ItemsAdapter;
import com.achulkov.loftmon.list.MoneyItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;

public class BudgetFragment extends Fragment {

    private static final int ADD_ITEM = 10;
    private ItemsAdapter itemsAdapter = new ItemsAdapter();

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.budget_fragment, null);

        FloatingActionButton addItemButton = view.findViewById(R.id.addNewExpense);
        addItemButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){

                    Intent intent = new Intent(getActivity(), AddItemActivity.class);
                    startActivityForResult(intent, ADD_ITEM);


            }
                                         }
        );


        RecyclerView recyclerView = view.findViewById(R.id.itemsView);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false));

        generateData();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == ADD_ITEM){
            if(resultCode == RESULT_OK) {
                MoneyItem mItem = new MoneyItem(data.getStringExtra("ITEM_NAME"),data.getStringExtra("ITEM_PRICE"));
                itemsAdapter.addItem(mItem);
                //for(MoneyItem item1:moneyItems)Log.i("items", item1.getTitle());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void generateData() {

        List<MoneyItem> moneyItems = new ArrayList<>();

        moneyItems.add(new MoneyItem("Salary", "50000$"));
        moneyItems.add(new MoneyItem("Taxes", "25000$"));
        moneyItems.add(new MoneyItem("PS4", "1500$"));
        moneyItems.add(new MoneyItem("Food", "3500$"));

        itemsAdapter.setData(moneyItems);
        itemsAdapter.notifyDataSetChanged();
    }





}

