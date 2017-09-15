package com.micste.improveme;


import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class GoalsFragment extends Fragment {

    CoordinatorLayout coordinatorLayout;


    public GoalsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflatedView = inflater.inflate(R.layout.fragment_goals, container, false);
        coordinatorLayout = (CoordinatorLayout) inflatedView.findViewById(R.id.goals_coordinator_layout);
        TabLayout tabLayout = (TabLayout) inflatedView.findViewById(R.id.tab_layout);

        CustomViewPager viewPager = (CustomViewPager) inflatedView.findViewById(R.id.view_pager);
        viewPager.setAdapter(new CustomPagerAdapter(getChildFragmentManager(), getActivity().getApplicationContext()));
        viewPager.setPagingEnabled(false);

        tabLayout.setupWithViewPager(viewPager);

        getActivity().setTitle("Goals");

        return inflatedView;
    }

}
