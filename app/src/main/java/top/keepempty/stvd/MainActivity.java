package top.keepempty.stvd;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Typeface textTypeface;
    private Typeface numTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        textTypeface = Typeface.createFromAsset(this.getResources().getAssets(), "fonts/hanyizhongyuanjian.ttf");
        numTypeface = Typeface.createFromAsset(this.getResources().getAssets(), "fonts/helveticarounded.otf");


        StrokeTextView strokeTextView1 = findViewById(R.id.strok_tv1);
        // 设置文字字体
        strokeTextView1.setStrokeTypeface(textTypeface,null);

        StrokeTextView strokeTextView2 = findViewById(R.id.strok_tv2);
        // 设置文字字体
        strokeTextView2.setStrokeTypeface(textTypeface,null);

        StrokeTextView strokeTextView4 = findViewById(R.id.strok_tv4);
        // 设置文字和数字字体
        strokeTextView4.setStrokeTypeface(textTypeface,numTypeface);


        StrokeTextView strokeTextView5 = findViewById(R.id.strok_tv5);
        // 设置文字和数字字体
        strokeTextView5.setStrokeTypeface(textTypeface,numTypeface);
    }
}
