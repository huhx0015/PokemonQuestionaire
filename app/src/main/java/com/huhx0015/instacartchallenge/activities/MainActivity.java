package com.huhx0015.instacartchallenge.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.huhx0015.instacartchallenge.fragments.QuestionFraqment;
import com.huhx0015.instacartchallenge.R;

public class MainActivity extends AppCompatActivity {

    private String mFragmentTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        // TODO: Load first fragment now, change later.
        loadFragment(new QuestionFraqment(), QuestionFraqment.class.getSimpleName());
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void loadFragment(Fragment fragment, String tag) {
        this.mFragmentTag = tag;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_container, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }
}