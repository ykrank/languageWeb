package zhangyongfeng.edit_text;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Edit_Text extends AppCompatActivity {

    private EditText edit = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__text);//要放在前面
        //设置辑编框状态不可选
        super.setContentView(R.layout.activity_edit__text);
        this.edit = (EditText)super.findViewById(R.id.my_EditText5);
        this.edit.setEnabled(false);

    }
}
