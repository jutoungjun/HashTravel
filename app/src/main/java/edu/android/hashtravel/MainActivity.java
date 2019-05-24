package edu.android.hashtravel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RC_SIGN_IN = 11;
    private TextView userId;
    private BottomNavigationView bottomView;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private MenuItem bottomMenuItem, logInAndOut, myInfo, Write;



    // 구글 사용자 정보
    private FirebaseAuth mAuth;
    // 구글 클라이언트
    private GoogleSignInClient mGoogleSignInClient;

    // Bottom 네비게이션뷰 ViewPager로 구현
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

        logInAndOut = navigationView.getMenu().findItem(R.id.logInAndOut);
        myInfo = navigationView.getMenu().findItem(R.id.myInfo);
        Write = navigationView.getMenu().findItem(R.id.write);

        bottomView= findViewById(R.id.bottom_view);
        viewPager = findViewById(R.id.viewpager_id);

        bottomView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());


        // 구글 사용자 옵션 불러오기? 시작
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        if(mAuth.getCurrentUser() == null) {
            logInAndOut.setTitle("로그인");
            userId.setText("로그인 해주세요");
        }else{
            logInAndOut.setTitle("로그 아웃");
            userId.setText(mAuth.getCurrentUser().getEmail());
            myInfo.setEnabled(true);
            Write.setEnabled(true);
        }

        // ViewPager에 Fragment 추가
        adapter.addFragment(new HomeFragment(), "homefragment");
        Fragment dashboardFragment = new DashboardFragment(); // Fragment 생성
        if(mAuth.getCurrentUser() == null){

            adapter.addFragment(new DashboardFragment(), "dashboard");
        }else{
            Bundle bundle = new Bundle(1); // 파라미터는 전달할 데이터 개수
            bundle.putString("userId", mAuth.getCurrentUser().getDisplayName()); // key , value
            dashboardFragment.setArguments(bundle);
            adapter.addFragment(dashboardFragment, "dashboard");
        }
        adapter.addFragment(new HotPostFragment(), "hotpost");
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

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show();
                // [START_EXCLUDE]


                e.printStackTrace();
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Toast.makeText(this, "firebaseAuthWithGoogle:" , Toast.LENGTH_SHORT).show();
        // [START_EXCLUDE silent]
//        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "signInWithCredential:success", Toast.LENGTH_SHORT).show();

                            Toast.makeText(MainActivity.this, "로그인", Toast.LENGTH_SHORT).show();
                            logInAndOut.setTitle("로그 아웃");
                            userId.setText(mAuth.getCurrentUser().getEmail());
                            myInfo.setEnabled(true);
                            Write.setEnabled(true);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "signInWithCredential:failure", Toast.LENGTH_SHORT).show();

                        }

                        // [START_EXCLUDE]
//                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

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
        if (id == R.id.delteUser) {
            deleteUser();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        //TODO 추가적으로 필요한 인텐트?
        if (id == R.id.logInAndOut) {
            logInAndOut();
        } else if (id == R.id.myInfo) {
          Intent intent = new Intent(this, UserInfo.class);
          intent.putExtra("email", mAuth.getCurrentUser().getEmail());
          intent.putExtra("name", mAuth.getCurrentUser().getDisplayName());
          startActivity(intent);
        } else if (id == R.id.write) {
            // 글쓰기 액티비티로 이동
            Intent intent = new Intent(this, WriteBordActivity.class);
            intent.putExtra("mAuth", mAuth.getCurrentUser());
            startActivity(intent);
        } else if (id == R.id.notice) {
            Intent intent = new Intent(this, NoticeActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logInAndOut() {
        if(mAuth.getCurrentUser() == null){
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        }else{
            // Firebase sign out
            mAuth.signOut();

            // Google revoke access
            mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "로그 아웃", Toast.LENGTH_SHORT).show();
                            logInAndOut.setTitle("로그인");
                            userId.setText("로그인 해주세요");

                        }
                    });
        }
    }

    public void deleteUser() {
        // [START delete_user]
        // Firebase sign out

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            }else{

                            }
                        }
                    });
            // [END delete_user]
        }else{
            Toast.makeText(this, "로그인을 해주세요", Toast.LENGTH_SHORT).show();
        }
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        logInAndOut.setTitle("로그인");
                        userId.setText("로그인 해주세요");

                        Toast.makeText(MainActivity.this, "User account deleted.", Toast.LENGTH_SHORT).show();
                    }
                });
        }

}

