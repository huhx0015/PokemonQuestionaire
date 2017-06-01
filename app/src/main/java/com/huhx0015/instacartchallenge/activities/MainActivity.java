package com.huhx0015.instacartchallenge.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.huhx0015.instacartchallenge.services.TimerService;
import com.huhx0015.instacartchallenge.interfaces.MainActivityListener;
import com.huhx0015.instacartchallenge.constants.GroceryConstants;
import com.huhx0015.instacartchallenge.fragments.QuestionFraqment;
import com.huhx0015.instacartchallenge.R;
import com.huhx0015.instacartchallenge.fragments.ResultFragment;
import com.huhx0015.instacartchallenge.models.Question;
import com.huhx0015.instacartchallenge.models.QuestionsResponse;
import com.huhx0015.instacartchallenge.utils.JsonUtils;
import com.huhx0015.instacartchallenge.utils.QuestionUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainActivityListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private static final String INSTANCE_FRAGMENT_TAG = LOG_TAG + "_FRAGMENT_TAG";
    private static final String INSTANCE_QUESTIONS = LOG_TAG + "_QUESTIONS";
    private static final String INSTANCE_SELECTED_QUESTION = LOG_TAG + "_SELECTED_QUESTION";
    private static final String INSTANCE_CORRECT_POSITION = LOG_TAG + "_CORRECT_POSITION";

    private int mCorrectPosition = GroceryConstants.STATE_CORRECT_POSITION_UNSET;
    private String mFragmentTag;
    private Question mSelectedQuestion;
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
            mSelectedQuestion = savedInstanceState.getParcelable(INSTANCE_SELECTED_QUESTION);
            mFragmentTag = savedInstanceState.getString(INSTANCE_FRAGMENT_TAG);
            mCorrectPosition = savedInstanceState.getInt(INSTANCE_CORRECT_POSITION);

            if (mSelectedQuestion != null) {
                loadFragment(QuestionFraqment.newInstance(mSelectedQuestion, mCorrectPosition, this),
                        QuestionFraqment.class.getSimpleName());
                return;
            }
        }

        loadJson();
    }

    @Override
    public void onBackPressed() {
        startTimer(false);
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mQuestionsResponse != null) {
            outState.putParcelable(INSTANCE_QUESTIONS, mQuestionsResponse);
        }
        if (mSelectedQuestion != null) {
            outState.putParcelable(INSTANCE_SELECTED_QUESTION, mSelectedQuestion);
        }
        outState.putString(INSTANCE_FRAGMENT_TAG, mFragmentTag);
        outState.putInt(INSTANCE_CORRECT_POSITION, mCorrectPosition);
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

    private void startTimer(boolean isStart) {
        if (isStart) {
            startService(new Intent(this, TimerService.class));
        } else {
            stopService(new Intent(this, TimerService.class));
        }
    }

    @Override
    public void onAnswerSelected(boolean isCorrect) {
        loadFragment(ResultFragment.newInstance(isCorrect, this), ResultFragment.class.getSimpleName());
        startTimer(false);
    }

    @Override
    public void onTryAgainSelected(boolean isNewQuestion) {
        if (isNewQuestion) {
            mSelectedQuestion = QuestionUtils.getRandomQuestion(mQuestionsResponse.getQuestionList());
            mCorrectPosition = QuestionUtils.getRandomPosition();
        }

        loadFragment(QuestionFraqment.newInstance(mSelectedQuestion, mCorrectPosition, this),
                QuestionFraqment.class.getSimpleName());
        startTimer(true);
    }

    private class JsonAsyncTask extends AsyncTask<String, Void, Question> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Question doInBackground(String... params) {
            String responseJson = JsonUtils.loadJsonFromAsset(GroceryConstants.GROCERY_ASSET_NAME, MainActivity.this);
            QuestionsResponse questionResponse = JsonUtils.getGroceryQuestionsFromJson(responseJson);

            if (questionResponse != null && questionResponse.getQuestionList() != null &&
                    questionResponse.getQuestionList().size() > 0) {
                mQuestionsResponse = questionResponse;
                return QuestionUtils.getRandomQuestion(questionResponse.getQuestionList());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Question question) {
            super.onPostExecute(question);
            mProgressBar.setVisibility(View.GONE);

            if (question == null) {
                Toast.makeText(MainActivity.this, "An error occurred while attempting to load a question.",
                        Toast.LENGTH_LONG).show();
            } else {
                mSelectedQuestion = question;
                mCorrectPosition = QuestionUtils.getRandomPosition();
                loadFragment(QuestionFraqment.newInstance(mSelectedQuestion, mCorrectPosition,
                        MainActivity.this), QuestionFraqment.class.getSimpleName());
                startTimer(true);
            }
        }
    }
}