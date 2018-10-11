package com.example.dickyeka.garasibuku;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class BeliBuku extends AppCompatActivity {

    private TabLayout tablay;
    private ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beli_buku);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_dua);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.belibuku);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tablay = (TabLayout) findViewById(R.id.tab1);
        viewpager = (ViewPager) findViewById(R.id.viewPager1);
        init();
    }


    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tablay));
    }
    private void init(){
        setupViewPager(viewpager);
        tablay.setupWithViewPager(viewpager);
    }
}
