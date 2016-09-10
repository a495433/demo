package com.example.mm.diyprogress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mybutton ;
    private HorizontalProgressBar progressBar;
    int progress = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (HorizontalProgressBar) findViewById(R.id.progerss);
        Log.e("绘了右边","123");
        mybutton = (Button) findViewById(R.id.hello);
        mybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setProgress(progress);
                progress += 1;
            }

        });

    }

}
