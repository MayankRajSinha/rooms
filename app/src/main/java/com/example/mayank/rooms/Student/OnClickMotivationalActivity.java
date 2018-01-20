package com.example.mayank.rooms.Student;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mayank.rooms.R;
import com.example.mayank.rooms.URLclass;
import com.squareup.picasso.Picasso;

public class OnClickMotivationalActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_click_motivational);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewPager pager = findViewById(R.id.pager);
        TextView story = findViewById(R.id.story);

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("Motivational Story");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String[] img = new String[0];
        String noi = null;
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            story.setText(bundle.getString("story"));
            img = new String[]{bundle.getString("img1"), bundle.getString("img2"), bundle.getString("img3")};
            noi = bundle.getString("noi");
        }

        pager.setOffscreenPageLimit(3);
        CustomPagerAdapter adapter = new CustomPagerAdapter(this, noi, img);
        pager.setAdapter(adapter);

    }

    static class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        int noi;
        String[]  imgs;

        public CustomPagerAdapter(Context context, String noi, String[]  imgs) {
            mContext = context;
            this.noi = Integer.parseInt(noi);
            this.imgs = imgs;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return noi;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.pagerimageView);
            Picasso.with(mContext)
                    .load(URLclass.GET_PIC_PATH + imgs[position])
                    .into(imageView);


            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
