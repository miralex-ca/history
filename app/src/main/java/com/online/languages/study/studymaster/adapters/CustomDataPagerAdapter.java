package com.online.languages.study.studymaster.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.online.languages.study.studymaster.fragments.CustomDataFragment;
import com.online.languages.study.studymaster.fragments.CustomTabFragment2;
import com.online.languages.study.studymaster.fragments.CustomTabFragment3;


public class CustomDataPagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public CustomDataPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new CustomDataFragment();
            case 1:
                return new CustomTabFragment2();
            case 2:
                return new CustomTabFragment3();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);

        registeredFragments.put(position, fragment);

        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

}