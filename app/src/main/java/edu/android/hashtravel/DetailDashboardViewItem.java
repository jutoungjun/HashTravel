package edu.android.hashtravel;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;

public class DetailDashboardViewItem extends AppCompatActivity {
private ImageButton button;
private int  CHECK_NUM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dashboard_view_item);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffbb33")));
        getWindow().setStatusBarColor(Color.parseColor("#ffffbb33"));
        button = findViewById(R.id.imagelike);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (CHECK_NUM == 0) {
                        button.setImageResource(R.drawable.like1);
                        CHECK_NUM = 1;
                    }else  {
                        button.setImageResource(R.drawable.like2);
                        CHECK_NUM =0;
                    }



          }



        });



    }

}
