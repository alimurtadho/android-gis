package com.dariyanti.uasgis;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dariyanti.uasgis.Home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private ImageView image_load_all, image_home;
    private TextView text_home, text_load_all;
    private LinearLayout ll_home, ll_load_all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFragment(new HomeFragment());

        ll_home = findViewById(R.id.ll_home);
        ll_load_all = findViewById(R.id.ll_load_all);
        image_home = findViewById(R.id.image_home);
        image_load_all = findViewById(R.id.image_load_all);
        text_home = findViewById(R.id.text_home);
        text_load_all = findViewById(R.id.text_load_all);
        final ColorStateList oldColors = text_home.getTextColors();
        text_home.setTextColor(Color.parseColor("#FF0090"));
        image_home.setPressed(true);

        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_home.setTextColor(Color.parseColor("#FF0090"));
                text_load_all.setTextColor(oldColors);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        image_home.setPressed(true);
                        image_load_all.setPressed(false);
                    }
                });
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.fl_main);
                if(fragment != null && fragment instanceof HomeFragment){

                }else{
                    initFragment(new HomeFragment());
                }
            }
        });

    }

    private void initFragment(Fragment classFragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_main, classFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



}
