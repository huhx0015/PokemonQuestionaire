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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.huhx0015.instacartchallenge.constants.GroceryConstants;
import com.huhx0015.instacartchallenge.fragments.QuestionFraqment;
import com.huhx0015.instacartchallenge.R;
import com.huhx0015.instacartchallenge.models.QuestionsResponse;
import com.huhx0015.instacartchallenge.utils.JsonUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String INSTANCE_FRAGMENT_TAG = LOG_TAG + "_FRAGMENT_TAG";
    private static final String INSTANCE_QUESTIONS = LOG_TAG + "_QUESTIONS";

    private String mFragmentTag;
    private QuestionsResponse mQuestionsResponse;

    @BindView(R.id.main_fragment_container) RelativeLayout mFragmentContainer;
    @BindView(R.id.main_progress_bar) ProgressBar mProgressBar;
    @BindView(R.id.main_toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();

        if (savedInstanceState != null) {
            mQuestionsResponse = savedInstanceState.getParcelable(INSTANCE_QUESTIONS);
            mFragmentTag = savedInstanceState.getString(INSTANCE_FRAGMENT_TAG);

            if (mQuestionsResponse != null) {
                loadFragment(new QuestionFraqment(), QuestionFraqment.class.getSimpleName());
                return;
            }
        }

        loadJson();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mQuestionsResponse != null) {
            outState.putParcelable(INSTANCE_QUESTIONS, mQuestionsResponse);
        }
        outState.putString(INSTANCE_FRAGMENT_TAG, mFragmentTag);
    }

    private void initView() {
        setSupportActionBar(mToolbar);
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

    private class JsonAsyncTask extends AsyncTask<String, Void, QuestionsResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected QuestionsResponse doInBackground(String... params) {
            String responseJson = JsonUtils.loadJsonFromAsset(GroceryConstants.GROCERY_ASSET_NAME, MainActivity.this);
            return JsonUtils.getGroceryQuestionsFromJson(responseJson);
        }

        @Override
        protected void onPostExecute(QuestionsResponse response) {
            super.onPostExecute(response);
            mProgressBar.setVisibility(View.GONE);

            if (response == null) {
                Toast.makeText(MainActivity.this, "An error occurred while attempting to read the JSON file.",
                        Toast.LENGTH_LONG).show();
            } else {
                mQuestionsResponse = response;
                loadFragment(new QuestionFraqment(), QuestionFraqment.class.getSimpleName());
                Log.d(LOG_TAG, "Size: " + response.getQuestionList().size());
            }
        }
    }
}