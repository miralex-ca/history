package com.online.languages.study.studymaster.adapters;

import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.online.languages.study.studymaster.fragments.StarredGalleryTab;
import com.online.languages.study.studymaster.fragments.StarredTabOne;


public class StarredTabsPagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private String tabs = "normal";

    public StarredTabsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    public StarredTabsPagerAdapter(FragmentManager fm, int NumOfTabs, String _tabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        tabs = _tabs;
    }



    @Override
    public Fragment getItem(int position) {

        if (!tabs.equals("normal")) {

            switch (position) {
                case 1:
                    return new StarredTabOne();
                case 0:
                    return new StarredGalleryTab();
                default:
                    return null;
            }

        } else {

            switch (position) {
                case 0:
                    return new StarredTabOne();
                case 1:
                    return new StarredGalleryTab();
                default:
                    return null;
            }

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

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        //do nothing here! no call to super.restoreState(arg0, arg1);
        // http://speakman.net.nz/blog/2014/02/20/a-bug-in-and-a-fix-for-the-way-fragmentstatepageradapter-handles-fragment-restoration/
    }

}