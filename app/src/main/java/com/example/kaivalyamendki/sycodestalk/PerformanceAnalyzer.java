package com.example.kaivalyamendki.sycodestalk;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class PerformanceAnalyzer extends Fragment {


    public PerformanceAnalyzer() {
        // Required empty public constructor
    }

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_performance_analyzer, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tabLayout = (TabLayout)getView().findViewById(R.id.performance_tabLayout);
        viewPager = (ViewPager)getView().findViewById(R.id.performance_viewPager);
        adapter = new ViewPagerAdapter(getChildFragmentManager());

        adapter.addFollowFragment(new TotalAnalysis(), "Total Analysis");
        adapter.addFollowFragment(new DailyBasis(), "Datewise Analysis");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }

}
