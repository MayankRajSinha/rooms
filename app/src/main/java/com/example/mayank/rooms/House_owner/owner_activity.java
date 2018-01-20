package com.example.mayank.rooms.House_owner;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mayank.rooms.Front.event_fragment;
import com.example.mayank.rooms.Front.login_fragment;
import com.example.mayank.rooms.Front.register_fragment;
import com.example.mayank.rooms.Front.Login;
import com.example.mayank.rooms.R;

public class owner_activity extends AppCompatActivity {

    private ViewPager owner_details_pager;
    private TabLayout page_header;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_activity);
        owner_details_pager = findViewById(R.id.ownerDetailsViewPager);
        page_header = findViewById(R.id.pager_header);
        toolbar = findViewById(R.id.toolbar);

        page_header.setupWithViewPager(owner_details_pager);
        createViewPager(owner_details_pager);
    }

    public void createViewPager(ViewPager viewPager) {
        Login.ViewPagerAdapter adapter = new Login.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(event_fragment.newInstance(), "Events");
        adapter.addFrag(RoomStatusFragment.newInstance(""), "Room Status");
        adapter.addFrag(renterFragment.newInstance(""), "Register");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.owner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                return true;

            case R.id.owner_signout:
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
