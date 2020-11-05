package com.achulkov.loftmon.screens.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.achulkov.loftmon.LoftApp;
import com.achulkov.loftmon.R;
import com.achulkov.loftmon.list.ItemsAdapter;
import com.achulkov.loftmon.list.MoneyItem;
import com.achulkov.loftmon.remote.MoneyApi;
import com.achulkov.loftmon.remote.MoneyRemoteItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.achulkov.loftmon.screens.main.MainActivity.ADD_ITEM;



public class BudgetFragment extends Fragment {

    private static final String COLOR_ID = "colorId";
    private static final String TYPE = "fragmentType";

    private ItemsAdapter itemsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private MoneyApi mApi;

    private MainViewModel mainViewModel;




    public static BudgetFragment newInstance(final int colorId, final String type) {
        BudgetFragment budgetFragment = new BudgetFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(COLOR_ID, colorId);
        bundle.putString(TYPE, type);
        budgetFragment.setArguments(bundle);
        return budgetFragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = ((LoftApp)getActivity().getApplication()).getApi();
        configureViewModel();
        mainViewModel.loadItems(
                ((LoftApp) getActivity().getApplication()).moneyApi,
                getActivity().getSharedPreferences(getString(R.string.app_name), 0), getArguments().getString(TYPE));
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.budget_fragment, null);





        RecyclerView recyclerView = view.findViewById(R.id.itemsView);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mainViewModel.loadItems(
                        ((LoftApp) getActivity().getApplication()).moneyApi,
                        getActivity().getSharedPreferences(getString(R.string.app_name), 0),getArguments().getString(TYPE));
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        itemsAdapter = new ItemsAdapter(getArguments().getInt(COLOR_ID));
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false));


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        mainViewModel.loadItems(
                ((LoftApp) getActivity().getApplication()).moneyApi,
                getActivity().getSharedPreferences(getString(R.string.app_name), 0), getArguments().getString(TYPE)
        );
        mSwipeRefreshLayout.setRefreshing(false);

    }

/*    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        int price;
        try {
            price = Integer.parseInt(data.getStringExtra("ITEM_PRICE"));
        } catch (NumberFormatException e) {
            price = 0;
        }
        final int realPrice = price;

        if (requestCode == ADD_ITEM){
            if(resultCode == RESULT_OK) {
                MoneyItem mItem = new MoneyItem(data.getStringExtra("ITEM_NAME"),data.getStringExtra("ITEM_PRICE"));
                itemsAdapter.addItem(mItem);
                //for(MoneyItem item1:moneyItems)Log.i("items", item1.getTitle());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }*/



    private void configureViewModel() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.moneyItemsList.observe(this, moneyItems -> {
            itemsAdapter.setData(moneyItems);
        });

        mainViewModel.messageString.observe(this, message -> {
            if (!message.equals("")) {
                showToast(message);
            }
        });

        mainViewModel.messageInt.observe(this, message -> {
            if (message > 0) {
                showToast(getString(message));
            }
        });
    }


    private void showToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


}