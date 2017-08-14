package br.com.battista.bgscore.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;

import java.util.List;

import br.com.battista.bgscore.R;

public class GameTabsFragment extends BaseFragment {

    private static final String TAG = GameTabsFragment.class.getSimpleName();

    public GameTabsFragment() {
    }

    public static GameTabsFragment newInstance() {
        GameTabsFragment fragment = new GameTabsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Create new GameTabsFragment!");

        final View view = inflater.inflate(R.layout.fragment_game_tabs, container, false);

        ViewPager viewPager = view.findViewById(R.id.viewpager_game);
        setupViewPager(viewPager);
        TabLayout tabs = view.findViewById(R.id.tabs_game);
        tabs.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getChildFragmentManager());
        adapter.addFragment(GameFragment.newInstance(), "Todos");
        adapter.addFragment(GameFragment.newInstance(), "Tenho");
        adapter.addFragment(GameFragment.newInstance(), "Quero");
        adapter.addFragment(GameFragment.newInstance(), "Favorito");
        viewPager.setAdapter(adapter);

    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = Lists.newArrayList();
        private final List<String> mFragmentTitleList = Lists.newArrayList();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}