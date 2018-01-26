package com.danacosoftware.purchasetracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Daniel Phillips on 8/26/2016.
 */
public class About extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        setTitle("About");
        Button ReportError = (Button)findViewById(R.id.about_button);
        ReportError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                com.danacosoftware.purchasetracker.ReportError re = new ReportError();
                re.NewReport(About.this);
            }
        });
    }
}
