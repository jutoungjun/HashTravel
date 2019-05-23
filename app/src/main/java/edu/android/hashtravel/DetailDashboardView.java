package edu.android.hashtravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailDashboardView extends AppCompatActivity {
//TODO
    public static final String EXTRA_POST = "post_key";
    private TextView detailPostCategory, detailPostSubject, detailPostUsername,
            detailPostDate, detailPostDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dashboard_view);
        getSupportActionBar().hide();

        detailPostCategory = findViewById(R.id.detailPostCategory);
        detailPostSubject = findViewById(R.id.detailPostSubject);
        detailPostUsername = findViewById(R.id.detailPostUsername);
        detailPostDate = findViewById(R.id.detailPostDate);
        detailPostDesc = findViewById(R.id.detailPostDesc);

        Intent intent = getIntent();
        DashBoard dashBoard = (DashBoard) intent.getSerializableExtra(EXTRA_POST);
        detailPostCategory.setText(dashBoard.getContinent() + " " + dashBoard.getCountry());
        detailPostSubject.setText(dashBoard.getSubject());
        detailPostUsername.setText(dashBoard.getUsername());
        detailPostDate.setText(dashBoard.getDate());
        detailPostDesc.setText(dashBoard.getDescription());

    }
}
