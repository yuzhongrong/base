package com.android.base.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

 public class MypageAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFagments;
    private List<String> mTitles;
    public MypageAdapter(FragmentManager fm, ArrayList<String> mTitles, List<Fragment> mFagments) {
        super(fm);
        this.mFagments=mFagments;
        this.mTitles=mTitles;
    }

    @Override
    public int getCount() {
        return mFagments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return mFagments.get(position);
    }
}
