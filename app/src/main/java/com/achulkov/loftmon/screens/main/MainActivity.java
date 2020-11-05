package com.achulkov.loftmon.screens.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.achulkov.loftmon.screens.addItem.AddItemActivity;
import com.achulkov.loftmon.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
//import android.util.Log;


public class MainActivity extends AppCompatActivity {


    public static final int ADD_ITEM = 10;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureViews();






    }





    static class BudgetPagerAdapter extends FragmentStateAdapter {


        public BudgetPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            switch (position) {
                case 0: {
                    return BudgetFragment.newInstance(R.color.dark_sky_blue,"expense");

                }
                case 1: {
                    return BudgetFragment.newInstance(R.color.apple_green,"income");

                }
                default:
                    return null;
            }

        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    private void configureViews() {
        TabLayout tabLayout = findViewById(R.id.tabs);

        ViewPager2 viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new BudgetPagerAdapter(getSupportFragmentManager(),getLifecycle()));

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position) {
                            case 0: {
                                tab.setText(R.string.expences);
                                break;
                            }
                            case 1: {
                                tab.setText(R.string.income);
                                break;
                            }
                        }
                    }
                }).attach();

        FloatingActionButton addItemButton = findViewById(R.id.addNewExpense);
        addItemButton.setOnClickListener(new View.OnClickListener(){
                                             @Override
                                             public void onClick(final View v){
                                                String type = new String();
                                                if(tabLayout.getSelectedTabPosition()==0) type = "expense";
                                                if(tabLayout.getSelectedTabPosition()==1) type = "income";

                                                 Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
                                                 intent.putExtra("type", type);
                                                 startActivityForResult(intent, ADD_ITEM);


                                             }
                                         }
        );
    }




    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
