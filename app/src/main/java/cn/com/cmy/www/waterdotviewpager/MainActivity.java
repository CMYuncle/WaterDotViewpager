package cn.com.cmy.www.waterdotviewpager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Button btnstart;
    private MagicCircle circle3;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circle3 = (MagicCircle)findViewById(R.id.circle3);
        this.btnstart = (Button) findViewById(R.id.btn_start);
        final Random ran = new Random();
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                circle3.startAnimation((ran.nextInt(7))%3,ran.nextInt(9)%3);
            }
        });
    }
}
