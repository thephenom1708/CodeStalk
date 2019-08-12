package com.example.kaivalyamendki.sycodestalk;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaivalya Mendki on 02-04-2018.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> followFragments = new ArrayList<>();
    private final List<String> followTitles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return followFragments.get(position);
    }

    @Override
    public int getCount() {
        return followTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return followTitles.get(position);
    }

    public void addFollowFragment(Fragment fragment, String title)
    {
        followFragments.add(fragment);
        followTitles.add(title);
    }
}
