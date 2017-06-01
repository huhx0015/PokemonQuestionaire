package com.huhx0015.instacartchallenge.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huhx0015.instacartchallenge.constants.GroceryConstants;
import com.huhx0015.instacartchallenge.fragments.QuestionFraqment;
import com.huhx0015.instacartchallenge.R;
import com.huhx0015.instacartchallenge.utils.JsonUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private String mFragmentTag;
    private String mJsonString;

    @BindView(R.id.main_fragment_container) RelativeLayout mFragmentContainer;
    @BindView(R.id.main_progress_bar) ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

        loadJson();

        // TODO: Load first fragment now, change later.
        loadFragment(new QuestionFraqment(), QuestionFraqment.class.getSimpleName());
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void loadJson() {
        JsonAsyncTask task = new JsonAsyncTask();
        task.execute(GroceryConstants.GROCERY_ASSET_NAME);
    }

    private void loadFragment(Fragment fragment, String tag) {
        this.mFragmentTag = tag;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(mFragmentContainer.getId(), fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public class JsonAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            return JsonUtils.loadJsonFromAsset(GroceryConstants.GROCERY_ASSET_NAME, MainActivity.this);
        }

        @Override
        protected void onPostExecute(String string) {
            super.onPostExecute(string);
            mProgressBar.setVisibility(View.GONE);

            if (string == null) {
                Toast.makeText(MainActivity.this, "An error occurred while attempting to read the JSON file.",
                        Toast.LENGTH_LONG).show();
            } else {
                Log.d(LOG_TAG, string);
            }
        }
    }
}