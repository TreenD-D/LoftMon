package com.achulkov.loftmon.screens.main;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.achulkov.loftmon.LoftApp;
import com.achulkov.loftmon.R;
import com.achulkov.loftmon.remote.BalanceResponse;
import com.achulkov.loftmon.remote.MoneyApi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class BalanceFragment extends Fragment {

    private MoneyApi mApi;
    private TextView myExpences;
    private TextView myIncome;
    private TextView totalFinances;
    private DiagramView mDiagramView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MainViewModel mainViewModel;

    public static BalanceFragment newInstance() {
        return new BalanceFragment();
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApi = ((LoftApp) getActivity().getApplication()).getApi();

        configureViewModel();
        mainViewModel.loadBalance(
                ((LoftApp) getActivity().getApplication()).moneyApi,
                getActivity().getSharedPreferences(getString(R.string.app_name), 0));
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull final LayoutInflater inflater,
            @Nullable final ViewGroup container,
            @Nullable final Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.balance_fragment, null);
        myExpences = view.findViewById(R.id.my_expences);
        myIncome = view.findViewById(R.id.my_income);
        totalFinances = view.findViewById(R.id.total_finances);
        mDiagramView = view.findViewById(R.id.diagram_view);
        mSwipeRefreshLayout = view.findViewById(R.id.balance_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {



            @Override
            public void onRefresh() {
                mainViewModel.loadBalance(
                        ((LoftApp) getActivity().getApplication()).moneyApi,
                        getActivity().getSharedPreferences(getString(R.string.app_name), 0));
            }
        });



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainViewModel.loadBalance(
                ((LoftApp) getActivity().getApplication()).moneyApi,
                getActivity().getSharedPreferences(getString(R.string.app_name), 0));
    }

    private void configureViewModel() {
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        mainViewModel.balanceResp.observe(this, response ->{
            final float totalExpenses = response.getTotalExpences();
            final float totalIncome = response.getTotalIncome();

            myExpences.setText(String.valueOf(totalExpenses));
            myIncome.setText(String.valueOf(totalIncome));
            totalFinances.setText(String.valueOf(totalIncome - totalExpenses));
            mDiagramView.update(totalExpenses, totalIncome);
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }

}