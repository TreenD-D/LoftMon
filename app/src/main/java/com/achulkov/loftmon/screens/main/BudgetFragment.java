package com.achulkov.loftmon.screens.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.achulkov.loftmon.LoftApp;
import com.achulkov.loftmon.R;
import com.achulkov.loftmon.list.ItemsAdapter;
import com.achulkov.loftmon.list.MoneyItem;
import com.achulkov.loftmon.list.MoneyListClick;
import com.achulkov.loftmon.remote.MoneyApi;
import com.achulkov.loftmon.remote.MoneyRemoteItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.achulkov.loftmon.screens.main.MainActivity.ADD_ITEM;



public class BudgetFragment extends Fragment implements MoneyListClick, ActionMode.Callback{

    private static final String COLOR_ID = "colorId";
    private static final String TYPE = "fragmentType";

    private ItemsAdapter itemsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private MoneyApi mApi;

    private MainViewModel mainViewModel;
    private ActionMode mActionMode = null;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();




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
        itemsAdapter.setListener(this);
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false));




        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
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
            mainViewModel.loadBalance(
                    ((LoftApp) getActivity().getApplication()).moneyApi,
                    getActivity().getSharedPreferences(getString(R.string.app_name), 0));

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

        mainViewModel.statusResp.observe(this, message ->{
            updateData();
        });
    }


    private void showToast(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onItemClick(final MoneyItem item, final int position) {
        itemsAdapter.clearItem(position);
        if (mActionMode != null) {
            mActionMode.setTitle(getString(R.string.selected, String.valueOf(itemsAdapter.getSelectedSize())));
        }
    }

    @Override
    public void onItemLongClick(final MoneyItem item, final int position) {
        if (mActionMode == null) {
            getActivity().startActionMode(this);
        }
        itemsAdapter.toggleItem(position);
        if (mActionMode != null) {
            mActionMode.setTitle(getString(R.string.selected, String.valueOf(itemsAdapter.getSelectedSize())));
        }
    }

    @Override
    public boolean onCreateActionMode(final ActionMode actionMode, final Menu menu) {
        mActionMode = actionMode;
        ((MainActivity)getActivity()).toggleFabOut();
        return true;


    }

    @Override
    public boolean onPrepareActionMode(final ActionMode actionMode, final Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getActivity());
        menuInflater.inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(final ActionMode actionMode, final MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.remove) {
            new AlertDialog.Builder(getContext())
                    .setMessage(R.string.confirmation)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialogInterface, final int i) {

                            List<Integer> ids = itemsAdapter.getSelectedItemIds();
                            Observable<Integer> idsObservable = Observable.fromIterable(ids);
                            compositeDisposable.add(
                                    idsObservable
                                    .subscribeOn(Schedulers.io())
                                    .subscribe(id -> {
                                            mainViewModel.removeItems(
                                            ((LoftApp) getActivity().getApplication()).moneyApi,
                                            getActivity().getSharedPreferences(getString(R.string.app_name), 0),String.valueOf(id));}

                                            ));
                        actionMode.setTitle(getString(R.string.selected, String.valueOf(itemsAdapter.getSelectedSize())));

                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(final DialogInterface dialogInterface, final int i) {

                        }
                    }).show();
        }
        return true;
    }

    public void updateData(){
        mainViewModel.loadItems(
                ((LoftApp) getActivity().getApplication()).moneyApi,
                getActivity().getSharedPreferences(getString(R.string.app_name), 0), getArguments().getString(TYPE)
        );
        itemsAdapter.clearSelections();

        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyActionMode(final ActionMode actionMode) {
        mActionMode = null;
        ((MainActivity)getActivity()).toggleFabIn();
        itemsAdapter.clearSelections();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();

    }
}