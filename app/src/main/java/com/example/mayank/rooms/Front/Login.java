package com.example.mayank.rooms.Front;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mayank.rooms.Front.event_fragment;
import com.example.mayank.rooms.Front.login_fragment;
import com.example.mayank.rooms.Front.register_fragment;
import com.example.mayank.rooms.R;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {


    private ViewPager login_pager;
    private TabLayout page_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_pager = findViewById(R.id.loginViewpager);
        page_header = findViewById(R.id.pager_header);
        page_header.setupWithViewPager(login_pager);
        createViewPager(login_pager);

    }

    public void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new event_fragment(), "Events");
        adapter.addFrag(new login_fragment(), "Login");
        adapter.addFrag(new register_fragment(), "Register");
        viewPager.setAdapter(adapter);
    }

    public static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
