package edu.android.hashtravel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

public class DetailDashboardActivity extends AppCompatActivity {

    public static final String EXTRA_POST = "post_key";
    public static final String EXTRA_REF = "postref";
    private TextView detailPostUsername, detailPostDate, detailPostDesc, detailPostHashTag, likeNumber;
    private DashBoard dashBoard;

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

    public void onClickLike(View view) {
        // TODO Firebase에 좋아요 수 업데이트 하기
        String postkey = getIntent().getStringExtra(EXTRA_REF);
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("posts").child(postkey);
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                DashBoard d = mutableData.getValue(DashBoard.class);
                if (d == null) {
                    return Transaction.success(mutableData);
                }

                d.setLikes(d.getLikes() + 1);

//                if (p.stars.containsKey(getUid())) {
//                    // Unstar the post and remove self from stars
//                    p.starCount = p.starCount - 1;
//                    p.stars.remove(getUid());
//                } else {
//                    // Star the post and add self to stars
//                    p.starCount = p.starCount + 1;
//                    p.stars.put(getUid(), true);
//                }

                // Set value and report transaction success
                mutableData.setValue(d);
                return Transaction.success(mutableData);

            }
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }
}
