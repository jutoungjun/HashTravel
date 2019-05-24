package edu.android.hashtravel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DetailDashboardActivity extends AppCompatActivity {

    public static final String EXTRA_POST = "post_key";
    private TextView detailPostUsername, detailPostDate, detailPostDesc, detailPostHashTag, likeNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dashboard);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffbb33")));
        getWindow().setStatusBarColor(Color.parseColor("#ffffbb33"));

        detailPostUsername = findViewById(R.id.detailPostUsername);
        detailPostDate = findViewById(R.id.detailPostDate);
        detailPostDesc = findViewById(R.id.detailPostDesc);
        detailPostHashTag = findViewById(R.id.detailPostHashTag);
        likeNumber = findViewById(R.id.likeNumber);

        Intent intent = getIntent();
        DashBoard dashBoard = (DashBoard) intent.getSerializableExtra(EXTRA_POST);

        detailPostUsername.setText(dashBoard.getUsername());
        detailPostDate.setText(dashBoard.getDate());
        detailPostDesc.setText(dashBoard.getDescription());
        detailPostHashTag.setText(dashBoard.getHashTag());
        likeNumber.setText(dashBoard.getLikes()+"");

    }

    public void onClickComment(View view) {
        Intent intent = new Intent(this, CommentActivity.class);

        startActivity(intent);
    }
}
