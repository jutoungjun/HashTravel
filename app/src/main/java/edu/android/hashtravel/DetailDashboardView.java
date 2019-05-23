package edu.android.hashtravel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailDashboardView extends AppCompatActivity {
//TODO
    public static final String EXTRA_POST = "post_key";
    private TextView detailPostCategory, detailPostSubject, detailPostUsername,
            detailPostDate, detailPostDesc, detailPostHashTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dashboard_view);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffbb33")));
        getWindow().setStatusBarColor(Color.parseColor("#ffffbb33"));

        detailPostCategory = findViewById(R.id.detailPostCategory);
        detailPostSubject = findViewById(R.id.detailPostSubject);
        detailPostUsername = findViewById(R.id.detailPostUsername);
        detailPostDate = findViewById(R.id.detailPostDate);
        detailPostDesc = findViewById(R.id.detailPostDesc);
        detailPostHashTag = findViewById(R.id.detailPostHashTag);

        Intent intent = getIntent();
        DashBoard dashBoard = (DashBoard) intent.getSerializableExtra(EXTRA_POST);
        detailPostCategory.setText(dashBoard.getContinent() + " " + dashBoard.getCountry());
        detailPostSubject.setText(dashBoard.getSubject());
        detailPostUsername.setText(dashBoard.getUsername());
        detailPostDate.setText(dashBoard.getDate());
        detailPostDesc.setText(dashBoard.getDescription());
        detailPostHashTag.setText(dashBoard.getHashTag());

    }
}
