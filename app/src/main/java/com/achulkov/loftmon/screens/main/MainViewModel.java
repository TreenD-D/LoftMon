package com.achulkov.loftmon.screens.main;

import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.achulkov.loftmon.LoftApp;
import com.achulkov.loftmon.R;
import com.achulkov.loftmon.list.MoneyItem;
import com.achulkov.loftmon.remote.MoneyApi;
import com.achulkov.loftmon.remote.MoneyRemoteItem;
import com.achulkov.loftmon.remote.MoneyResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainViewModel extends ViewModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<List<MoneyItem>> moneyItemsList = new MutableLiveData<>();
    public MutableLiveData<String> messageString = new MutableLiveData<>("");
    public MutableLiveData<Integer> messageInt = new MutableLiveData<>(-1);

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    public void loadItems(MoneyApi moneyApi, SharedPreferences sharedPreferences, String type) {
        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");

        compositeDisposable.add(moneyApi.getMoneyItems(type, authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moneyRemoteItems -> {
                    List<MoneyItem> moneyItems = new ArrayList<>();


                    for (MoneyRemoteItem moneyRemoteItem : moneyRemoteItems) {
                        moneyItems.add(MoneyItem.getInstance(moneyRemoteItem));
                    }
                    Collections.reverse(moneyItems);
                    moneyItemsList.postValue(moneyItems);
                }, throwable -> {
                    messageString.postValue(throwable.getLocalizedMessage());
                }));
    }
}