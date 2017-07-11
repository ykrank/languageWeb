package zhangyongfeng.checkbox_text;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;

public class CheckBox_Text extends AppCompatActivity {
    private CheckBox box = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_box__text);

        this.box = (CheckBox) super.findViewById(R.id.checkbox2);
        box.setChecked(true);
        box.setText("www.jiangker.com");
    }
}
