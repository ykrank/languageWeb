package zhangyongfeng.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class QuizActivity extends AppCompatActivity {

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;

    //定义数组对象
    private Question[] mQuestionBank = new Question[]{
        new Question(R.string.question_oceans,true),
        new Question(R.string.question_africa,false),
        new Question(R.string.question_americas,true),
        new Question(R.string.question_mideast,false),
        new Question(R.string.question_asia,true),
    };

    //定义一个变量
    private  int mCurrentIndex = 0;

    //定义一个方法
    private void updateQuestion(){
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    //定义一个方法
    private  void checkAnswer(boolean userPressedTrue){
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (userPressedTrue == answerIsTrue){
            messageResId = R.string.correct_toast;
        }else {
            messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this,messageResId,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);

        mTrueButton = (Button) findViewById(R.id.true_button);//引用组件
        mFalseButton = (Button) findViewById(R.id.false_button);//引用组件

        //按扭设置监听 调用监听方法
        mTrueButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Toast.makeText(QuizActivity.this,R.string.correct_toast,Toast.LENGTH_SHORT).show();
                checkAnswer(true);
            }
        });

        //按钮
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //Toast.makeText(QuizActivity.this,R.string.incorrect_toast,Toast.LENGTH_SHORT).show();
                checkAnswer(false);
            }
        });

        //获取按钮组件对象
        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mCurrentIndex = (mCurrentIndex+1)% mQuestionBank.length;
//                int question = mQuestionBank[mCurrentIndex].getTextResId();
//                mQuestionTextView.setText(question);
                updateQuestion();
            }
        });

        updateQuestion();

    }
}
