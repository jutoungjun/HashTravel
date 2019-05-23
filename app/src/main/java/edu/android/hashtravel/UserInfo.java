package edu.android.hashtravel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfo extends AppCompatActivity {

    private TextView textEmailInfo, textNAmeINfo;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffbb33")));
        getWindow().setStatusBarColor(Color.parseColor("#ffffbb33"));
        Intent intent = getIntent();
        String userEmail = intent.getExtras().getString("email");
        String userName = intent.getExtras().getString("name");
        textEmailInfo = findViewById(R.id.textEmailInfo);
        textNAmeINfo = findViewById(R.id.textNameInfo);
        imageView = findViewById(R.id.imageMyInfo);


        imageView.setImageResource(R.drawable.comment11);
        textEmailInfo.setText(userEmail.toString());
        textNAmeINfo.setText(userName.toString());

    }

    //TODO 버튼을 눌렀을 때 리사이클러 뷰에 목록이 나와야 함
}
