package com.example.ortho;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numberOfTabs;

    public PagerAdapter(@NonNull FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {


        switch (position){

            case 0:
                ExpenseFragment expenseFragment = new ExpenseFragment();
                return expenseFragment;

            case 1:
                BankFragment bankFragment = new BankFragment();
                return bankFragment;

            case 2:
                DebtsFragment debtsFragment = new DebtsFragment();
                return debtsFragment;

             default:
                 return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }


}
