package edu.android.hashtravel;

import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import static edu.android.hashtravel.HotPlaceFragment.*;

public class PlaceDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        getSupportActionBar().hide();
        if(savedInstanceState == null) {
            Intent intent = getIntent();
            int position = intent.getIntExtra(KEY_PLACE_ID,0);

            PlaceDetailFragment fragment = PlaceDetailFragment.newFragment(position);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detailContainer, fragment)
                    .commit();
        }
    }
}
