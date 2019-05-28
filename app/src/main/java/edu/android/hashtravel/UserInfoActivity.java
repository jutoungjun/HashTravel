package edu.android.hashtravel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfoActivity extends AppCompatActivity {

    private TextView textEmailInfo, textNAmeINfo;
    private ImageView imageView;
    private Button btnMyPost;
    private RecyclerView myRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffbb33")));
        getWindow().setStatusBarColor(Color.parseColor("#ffffbb33"));
        Intent intent = getIntent();
        String userEmail = intent.getExtras().getString("email");
        String userName = intent.getExtras().getString("name");
        String uid = intent.getExtras().getString("uid");

        textEmailInfo = findViewById(R.id.textEmailInfo);
        textNAmeINfo = findViewById(R.id.textNameInfo);
        imageView = findViewById(R.id.imageMyInfo);
        btnMyPost = findViewById(R.id.btnMyPost);
        myRecycler = findViewById(R.id.myRecyclerView);

        imageView.setImageResource(R.drawable.comment11);
        textEmailInfo.setText(userEmail);
        textNAmeINfo.setText(userName);

    }

    //TODO 버튼을 눌렀을 때 리사이클러 뷰에 목록이 나와야 함
}
