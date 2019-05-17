package edu.android.hashtravel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView userId;
    private BottomNavigationView bottomView;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private MenuItem bottomMenuItem;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_hotplace:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // userId 찾음
        userId = navigationView.getHeaderView(0).findViewById(R.id.userId);

        bottomView= findViewById(R.id.bottom_view);
        viewPager = findViewById(R.id.viewpager_id);

        bottomView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // ViewPager에 Fragment 추가
        adapter.addFragment(new HomeFragment(), "homefragment");
        adapter.addFragment(new DashboardFragment(), "dashboard");
        adapter.addFragment(new HotPlaceFragment(), "hotplace");
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int postion, float positonOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int postion) {
                if(bottomMenuItem != null) {
                    bottomMenuItem.setChecked(false);
                } else {
                    bottomView.getMenu().getItem(0).setChecked(false);
                }

                bottomView.getMenu().getItem(postion).setChecked(true);
                bottomMenuItem = bottomView.getMenu().getItem(postion);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // TODO 네비게이션뷰 아이템 클릭했을때 기능 구현
        int id = item.getItemId();

        if (id == R.id.nav_logIn) {


        } else if (id == R.id.nav_myRead) {

        } else if (id == R.id.nav_favoriteCountry) {

        } else if (id == R.id.nav_write) {
            // 글쓰기 액티비티로 이동
            Intent intent = new Intent(this, WriteBordActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_myInfo) {

        } else if (id == R.id.nav_notice) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
