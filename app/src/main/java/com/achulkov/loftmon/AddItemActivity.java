package com.achulkov.loftmon;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class AddItemActivity extends AppCompatActivity {

    public void addItem(View view){
        EditText addItem_editTextPrice = findViewById(R.id.addItem_editTextPrice);
        EditText addItem_editTextName = findViewById(R.id.addItem_editTextName);

        //Log.i("addbutton","intent start" );

        Intent intent = new Intent();
        intent.putExtra("ITEM_PRICE", addItem_editTextPrice.getText().toString());
        intent.putExtra("ITEM_NAME", addItem_editTextName.getText().toString());
        setResult(RESULT_OK,intent);
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

    }



}
