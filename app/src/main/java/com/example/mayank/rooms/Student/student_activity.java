package com.example.mayank.rooms.Student;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mayank.rooms.Front.Login;
import com.example.mayank.rooms.Front.event_fragment;
import com.example.mayank.rooms.House_owner.RoomStatusFragment;
import com.example.mayank.rooms.House_owner.renterFragment;
import com.example.mayank.rooms.R;

public class student_activity extends AppCompatActivity {

    private ViewPager student_details_pager;
    private TabLayout page_header;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_activity);
        student_details_pager = findViewById(R.id.ownerDetailsViewPager);
        page_header = findViewById(R.id.pager_header);
        toolbar = findViewById(R.id.toolbar);

        page_header.setupWithViewPager(student_details_pager);
        createViewPager(student_details_pager);
    }

    public void createViewPager(ViewPager viewPager) {
        Login.ViewPagerAdapter adapter = new Login.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(event_fragment.newInstance(), "Events");
        adapter.addFrag(SearchRoomsFragment.newInstance(""), "Room Status");
        adapter.addFrag(renterFragment.newInstance(""), "Register");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                return true;

            case R.id.stu_signout:
                return true;

            case R.id.help:
                return true;

            case R.id.call:
                return true;

            case R.id.achievements:
                return true;

            case R.id.motivational:
                return true;

            case R.id.iMessage:
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
