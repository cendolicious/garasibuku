package com.example.dickyeka.garasibuku;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class TentangKami extends AppCompatActivity {
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tentang_kami);
        title = (TextView) findViewById(R.id.titleabout);
        TextView grsbk = (TextView) findViewById(R.id.textView3);
        Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/csThin.ttf");
        title.setTypeface(typeface1);
    }
}
