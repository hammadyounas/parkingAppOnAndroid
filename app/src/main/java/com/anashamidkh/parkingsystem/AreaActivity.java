package com.anashamidkh.parkingsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AreaActivity extends AppCompatActivity {
    private Button mArea1_Area;
    private Button mArea2_Area;
    private Button mArea3_Area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area);

        mArea1_Area = (Button) findViewById(R.id.area1_Area);
        mArea2_Area = (Button) findViewById(R.id.area2_Area);
        mArea3_Area = (Button) findViewById(R.id.area3_Area);

        mArea1_Area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String option = "Gulshan-e-Iqbal";
                Intent i = SlotsActivity.newIntent(AreaActivity.this,option);
                startActivity(i);
            }
        });

        mArea2_Area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String option = "Gulistan-e-Jauhar";
                Intent i = SlotsActivity.newIntent(AreaActivity.this,option);
                startActivity(i);
            }
        });

        mArea3_Area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String option = "Saddar";
                Intent i = SlotsActivity.newIntent(AreaActivity.this,option);
                startActivity(i);
            }
        });
    }
}
