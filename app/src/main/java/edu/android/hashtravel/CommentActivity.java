package edu.android.hashtravel;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class CommentActivity extends AppCompatActivity {

private ImageButton button;
private int  CHECK_NUM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffbb33")));
        getWindow().setStatusBarColor(Color.parseColor("#ffffbb33"));
        button = findViewById(R.id.imagelike);

    }

    public void onClicklike(View view) {

        if (CHECK_NUM == 0) {
            button.setImageResource(R.drawable.like1);
            CHECK_NUM = 1;
        }else  {
            button.setImageResource(R.drawable.like2);
            CHECK_NUM =0;
        }


    }
}
