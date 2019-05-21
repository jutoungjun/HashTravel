package edu.android.hashtravel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailDashboardView extends AppCompatActivity {
//TODO
    public static final String EXTRA_POST_KEY = "post_key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_dashboard_view);
    }
}
