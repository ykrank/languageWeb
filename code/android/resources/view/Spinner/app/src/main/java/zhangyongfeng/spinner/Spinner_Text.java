package zhangyongfeng.spinner;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Spinner_Text extends AppCompatActivity {

    private Spinner spiColor = null;  //定义表示颜色的列表框
    private Spinner spiEdu = null;      //定义表示学历的列表框
    private ArrayAdapter<CharSequence> adapterColor = null; //下拉列表内容适配器
    private ArrayAdapter<CharSequence> adapterEdu = null;//下拉列表内容适配器
    private List<CharSequence> dataEdu = null;//集合保存下拉列表选项
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//父类方法
        setContentView(R.layout.activity_spinner__text);//调用布局管理器

        this.spiColor = (Spinner)super.findViewById(R.id.Spinner1);//取出组件
        this.spiColor.setPrompt("请选择您喜欢的颜色：");//定义提示信息
        //从资源文件读取选项
        this.adapterColor = ArrayAdapter.createFromResource(this,R.array.color_labels,android.R.layout.simple_spinner_item);
        //设置列表显示风格
        //this.adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //设置下拉列表选项
        this.spiColor.setAdapter(this.adapterColor);


        this.dataEdu = new ArrayList<CharSequence>();//实例化List集合
        this.dataEdu.add("大学生");
        this.dataEdu.add("研究生");
        this.dataEdu.add("高中");
        this.spiEdu = (Spinner)super.findViewById(R.id.Spinner2);
        this.spiEdu.setPrompt("请选择您喜欢的学历：");
        //定义一个下拉列表
        this.adapterEdu = new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,this.dataEdu);
        //设置下拉列表显示风格
        this.adapterEdu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spiEdu.setAdapter(this.adapterEdu);
    }
}
