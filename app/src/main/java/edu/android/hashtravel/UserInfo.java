package edu.android.hashtravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class UserInfo extends AppCompatActivity {

    private TextView userInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Intent intent = getIntent();
        String userEmail = intent.getExtras().getString("email");
        String userName = intent.getExtras().getString("name");

        userInfoText = findViewById(R.id.userInfoText);
        userInfoText.setText("유저 정보" + "\n" + "이메일 : " + userEmail
                               + "\n" +"이름 : " + userName);
    }

    //TODO 버튼을 눌렀을 때 리사이클러 뷰에 목록이 나와야 함
}
