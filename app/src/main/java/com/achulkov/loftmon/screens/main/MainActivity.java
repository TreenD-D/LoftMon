package com.achulkov.loftmon.screens.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.achulkov.loftmon.screens.addItem.AddItemActivity;
import com.achulkov.loftmon.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
//import android.util.Log;


public class MainActivity extends AppCompatActivity {


    public static final int ADD_ITEM = 10;
    private TabLayout tabLayout;
    private Toolbar mToolbar;
    public FloatingActionButton addItemButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureViews();






    }

    public void toggleFabIn(){
        addItemButton.show();
    }


    public void toggleFabOut(){
        addItemButton.hide();
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
                case 2: {
                    return BalanceFragment.newInstance();

                }
                default:
                    return null;
            }

        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    private void configureViews() {
        tabLayout = findViewById(R.id.tabs);
        mToolbar = findViewById(R.id.toolbar);

        ViewPager2 viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new BudgetPagerAdapter(getSupportFragmentManager(),getLifecycle()));

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {


            @Override
            public void onPageSelected(final int position) {

                if (position == 2) {
                    addItemButton.hide();
                } else {

                    addItemButton.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

                ActionMode actionMode;
                final int position = viewPager.getCurrentItem();
                Fragment fragment = getSupportFragmentManager().getFragments().get(position);
                if (fragment instanceof BudgetFragment) {
                    actionMode = ((BudgetFragment)fragment).getActionMode();
                } else {
                    actionMode = null;
                }
                switch (state) {
                    case ViewPager2.SCROLL_STATE_DRAGGING:
                    case ViewPager2.SCROLL_STATE_SETTLING:
                        if (null != actionMode) actionMode.finish();
                        break;
                }
            }

        });

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
                            case 2: {
                                tab.setText(R.string.balance);
                                break;
                            }
                        }
                    }
                }).attach();

        addItemButton = findViewById(R.id.addNewExpense);
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

    @Override
    public void onActionModeStarted(final ActionMode mode) {
        super.onActionModeStarted(mode);
        tabLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.dark_gray_blue));
        mToolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.dark_gray_blue));
    }

    @Override
    public void onActionModeFinished(final ActionMode mode) {
        super.onActionModeFinished(mode);
        tabLayout.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
        mToolbar.setBackgroundColor(ContextCompat.getColor(this,R.color.colorPrimary));
    }

}
