package zhangyongfeng.timepicker_test;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TimePicker extends AppCompatActivity {

    private TimePicker mytimepicker = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_picker);

        this.mytimepicker = (TimePicker)super.findViewById(R.id.timepicker1);
        this.mytimepicker.setls24HourView(true);
        this.mytimepicker.setCurrentHour(18);
        this.mytimepicker.setCurrentMinute(30);

    }



}
