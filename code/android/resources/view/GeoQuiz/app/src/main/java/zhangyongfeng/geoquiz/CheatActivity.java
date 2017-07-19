package zhangyongfeng.geoquiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    //添加常量
    private static final String EXTRA_ANSWER_IS_TRUE = "zhangyongfeng.geoquiz.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN = "zhangyongfeng.geoquiz.answer_shown";

    //类型变量
    private boolean mAnserIsTrue;
    private Button mShowAnswer;
    private TextView mAnswerTextView;

    //定义一个方法
    public static Intent newIntent(Context packageContext,boolean answerIsTrue){
        Intent i = new Intent(packageContext,CheatActivity.class);
        i.putExtra(EXTRA_ANSWER_IS_TRUE,answerIsTrue);
        return i;
    }

    //定义一个方法
    private void setAnserShowResult(boolean isAnswerShown){
        //对象？
        Intent data = new Intent();
        //调用对象里的方法
        data.putExtra(EXTRA_ANSWER_SHOWN,isAnswerShown);
        //调用公共方法？把结果代码以及intent打包的方法?
        setResult(RESULT_OK,data);
    }

    public static boolean wasAnswerShow(Intent result){
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN,false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        //获取值
        mAnserIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE,false);

        //获取文本组件
        mAnswerTextView = (TextView) findViewById(R.id.mAnswerTextView);
        //获取按钮组件
        mShowAnswer =(Button) findViewById(R.id.button);
        //给按钮没置监听事件
        mShowAnswer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mAnserIsTrue){
                    mAnswerTextView.setText(R.string.true_button);
                }else{
                    mAnswerTextView.setText(R.string.false_button);
                }
                //调用方法
                setAnserShowResult(true);
            }
        });
    }

}