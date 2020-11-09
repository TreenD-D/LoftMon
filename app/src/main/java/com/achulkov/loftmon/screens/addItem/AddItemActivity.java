package com.achulkov.loftmon.screens.addItem;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import com.achulkov.loftmon.LoftApp;
import com.achulkov.loftmon.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddItemActivity extends AppCompatActivity {

    private EditText addItem_editTextPrice;
    private EditText addItem_editTextName;
    private Button addItemAct_addButton;

    String mName;
    String mPrice;

    private void configurebutton(String type){
        addItemAct_addButton.setOnClickListener(v -> {
            if (addItem_editTextName.getText().toString().equals("") || addItem_editTextPrice.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.fill_fields), Toast.LENGTH_LONG).show();
                return;
            }

            Disposable disposable = ((LoftApp) getApplication()).moneyApi.postMoney(
                    Integer.parseInt(addItem_editTextPrice.getText().toString()),
                    addItem_editTextName.getText().toString(),
                    type,
                    getSharedPreferences(getString(R.string.app_name), 0).getString(LoftApp.AUTH_KEY, "")
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Toast.makeText(getApplicationContext(), getString(R.string.success_added), Toast.LENGTH_LONG).show();
                        finish();
                    }, throwable -> {
                        Toast.makeText(getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    });
        });


//        Intent intent = new Intent();
//        intent.putExtra("ITEM_PRICE", addItem_editTextPrice.getText().toString());
//        intent.putExtra("ITEM_NAME", addItem_editTextName.getText().toString());
//        setResult(RESULT_OK,intent);
//        finish();
    }








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        addItem_editTextPrice = findViewById(R.id.addItem_editTextPrice);
        addItem_editTextName = findViewById(R.id.addItem_editTextName);
        addItemAct_addButton = findViewById(R.id.addItemAct_addButton);


        addItem_editTextName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    final CharSequence charSequence,
                    final int i,
                    final int i1,
                    final int i2
            ) {

            }

            @Override
            public void onTextChanged(
                    final CharSequence charSequence,
                    final int i,
                    final int i1,
                    final int i2
            ) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                mName = editable.toString();
                checkEditTextHasText();
            }
        });

        addItem_editTextPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(
                    final CharSequence charSequence,
                    final int i,
                    final int i1,
                    final int i2
            ) {

            }

            @Override
            public void onTextChanged(
                    final CharSequence charSequence,
                    final int i,
                    final int i1,
                    final int i2
            ) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                mPrice = editable.toString();
                checkEditTextHasText();
            }
        });
        switch (getIntent().getStringExtra("type")) {
            case "expense": {
                addItem_editTextPrice.setTextColor(getResources().getColor(R.color.dark_sky_blue));
                addItem_editTextName.setTextColor(getResources().getColor(R.color.dark_sky_blue));
                addItemAct_addButton.setTextColor(getResources().getColorStateList(R.color.add_button_text_color_selector));
                addItemAct_addButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.check_selector),null,null,null);
                break;
            }
            case "income": {
                addItem_editTextPrice.setTextColor(getResources().getColor(R.color.apple_green));
                addItem_editTextName.setTextColor(getResources().getColor(R.color.apple_green));
                addItemAct_addButton.setTextColor(getResources().getColorStateList(R.color.add_button_text_color_selector_income));
                addItemAct_addButton.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.check_selector_income),null,null,null);
                break;
            }
        }


        configurebutton(getIntent().getStringExtra("type"));

    }


    public void checkEditTextHasText() {
        addItemAct_addButton.setEnabled(!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mPrice));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.from_bottom_in,R.anim.to_top_out);
    }

}
