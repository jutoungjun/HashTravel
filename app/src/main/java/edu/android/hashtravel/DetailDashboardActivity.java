package edu.android.hashtravel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.Arrays;
import java.util.List;

public class DetailDashboardActivity extends AppCompatActivity {

    public static final String EXTRA_POST = "post_key";
    public static final String EXTRA_REF = "postref";
    private TextView detailPostUsername, detailPostDate, detailPostDesc, detailPostHashTag, likeNumber;
    private DashBoard dashBoard;
    private RecyclerView recyclerView;
    private ImageAdapter adapter;
    private  int res;
    private String postKey;

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
        dashBoard = (DashBoard) intent.getSerializableExtra(EXTRA_POST);

        postKey = dashBoard.getPostKey();
        detailPostUsername.setText(dashBoard.getUsername());
        detailPostDate.setText(dashBoard.getDate());
        detailPostDesc.setText(dashBoard.getDescription());
        detailPostHashTag.setText(dashBoard.getHashTag());
        likeNumber.setText(dashBoard.getLikes()+"");
        init();
        getData();

    }

    private void init() {

        recyclerView = findViewById(R.id.imageRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ImageAdapter();
        recyclerView.setAdapter(adapter);

    }
    private void getData() {
        List<Integer> listContent = Arrays.asList(
R.drawable.common_google_signin_btn_icon_dark_normal_background,
R.drawable.album
        );
        for(int i=0; i<listContent.size();i++){
                Image model = new Image(res);
                model.setRes(listContent.get(i));
                adapter.addItem(model);

        }
    }
    public void onClickComment(View view) {
        Intent intent = new Intent(this, CommentActivity.class);
        startActivity(intent);
    }

    public void onClickLike(View view) {
        // TODO Firebase에 좋아요 수 업데이트 하기
//        String postkey = getIntent().getStringExtra(EXTRA_REF);
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("posts").child(postKey);
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                DashBoard d = mutableData.getValue(DashBoard.class);
                if (d == null) {
                    return Transaction.success(mutableData);
                }

                d.setLikes(d.getLikes() + 1);

                String uid = dashBoard.getUid();
//                if(uid != null) {
//                    if (d.getStars().containsKey(uid)) {
//                        // Unstar the post and remove self from stars
//                        d.setLikes(d.getLikes() - 1);
//                        d.stars.remove(uid);
//                        Toast.makeText(DetailDashboardActivity.this, "좋아요 취소", Toast.LENGTH_SHORT).show();
//                    } else {
//                        // Star the post and add self to stars
//                        d.setLikes(d.getLikes() + 1);
//                        d.stars.put(uid, true);
//                        Toast.makeText(DetailDashboardActivity.this, "좋아요!", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
                // Set value and report transaction success
                mutableData.setValue(d);
                return Transaction.success(mutableData);

            }
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                likeNumber.setText((dashBoard.getLikes()+1)+"");
                Toast.makeText(DetailDashboardActivity.this, "좋아요!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
