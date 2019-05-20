package edu.android.hashtravel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import static edu.android.hashtravel.HotPostFragment.*;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        getSupportActionBar().hide();
        if(savedInstanceState == null) {
            Intent intent = getIntent();
            int position = intent.getIntExtra(KEY_PLACE_ID,0);

            PostDetailFragment fragment = PostDetailFragment.newFragment(position);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detailContainer, fragment)
                    .commit();
        }
    }
}