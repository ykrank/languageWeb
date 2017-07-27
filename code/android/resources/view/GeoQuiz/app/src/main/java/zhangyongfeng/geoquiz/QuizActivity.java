package zhangyongfeng.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;
    private Button next_buttonleft;
    private Button next_buttonright;
    private Button mCheatButton;

//    @Override
//    public void onClick(View v){
//        Intent i = new Intent(QuizActivity.this, CheatActivity.class);
//    }
//    startActivity(i);

    //设置TAG常量
    private static final String TAG = "QuizActivity";
    //定义一个常量
    private static final int REQUEST_CODE_CHEAT = 0;
    private boolean mIsCheater;
    //定义数组对象
    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    //定义一个变量
    private int mCurrentIndex = 0;

    //定义一个方法
    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    //定义一个方法
    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;
        if (mIsCheater){
            messageResId = R.string.judgment_toast;
        }else{
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
            } else {
                messageResId = R.string.incorrect_toast;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@2onCreate(Bundle) called@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        setContentView(R.layout.activity_quiz);

//      -设置按钮，点击进入答案检查页面-start-----------------------------------------------------------------------------
        mCheatButton = (Button)findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
//                Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                //获取列表数据对应的值
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
//
                Intent i = CheatActivity.newIntent(QuizActivity.this, answerIsTrue);
//                startActivity(i);
                //调用公共方法从子activity拿返回的信息
                startActivityForResult(i,REQUEST_CODE_CHEAT);
            }
        });


//      -设置按钮，点击进入答案检查页面-start-----------------------------------------------------------------------------



        //测试异常
        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
        //设置文本框的监听事件
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);//引用组件
        mFalseButton = (Button) findViewById(R.id.false_button);//引用组件

        //按扭设置监听 调用监听方法
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(QuizActivity.this,R.string.correct_toast,Toast.LENGTH_SHORT).show();
                checkAnswer(true);
            }
        });

        //按钮
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Toast.makeText(QuizActivity.this,R.string.incorrect_toast,Toast.LENGTH_SHORT).show();
                checkAnswer(false);
            }
        });

        //获取按钮组件对象
        mNextButton = (Button) findViewById(R.id.next_button);
//        mNextButton = (Button) findViewById(R.id.question_text_view);
        //设置按钮的监听事件
        mNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
//                int question = mQuestionBank[mCurrentIndex].getTextResId();
//                mQuestionTextView.setText(question);
                updateQuestion();
            }
        });

        //获取左边接钮对象
        next_buttonleft = (Button) findViewById(R.id.next_buttonleft);
        //设置按钮的监听事件
        next_buttonleft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mCurrentIndex == 0) {
                    mCurrentIndex = 5;
                }
                mCurrentIndex = (mCurrentIndex - 1) % mQuestionBank.length;
//                int question = mQuestionBank[mCurrentIndex].getTextResId();
//                mQuestionTextView.setText(question);
                updateQuestion();
            }
        });

        //获取右边接钮对象
        next_buttonright = (Button) findViewById(R.id.next_buttonright);
        //设置按钮的监听事件
        next_buttonright.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
//                int question = mQuestionBank[mCurrentIndex].getTextResId();
//                mQuestionTextView.setText(question);
                updateQuestion();

            }
        });

        updateQuestion();
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT){
            if (data == null){
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShow(data);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
}
