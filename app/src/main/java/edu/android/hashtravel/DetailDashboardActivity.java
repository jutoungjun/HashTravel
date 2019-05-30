package edu.android.hashtravel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailDashboardActivity extends AppCompatActivity {

    public static final String EXTRA_POST = "post_key";
    public static final String EXTRA_REF = "postref";
    private TextView detailPostUsername, detailPostDate, detailPostDesc, detailPostHashTag, likeNumber;
    private DashBoard dashBoard;
    private String postKey;
    private ImageView imageView1, imageView2, imageView3;
    private FirebaseAuth mAuth;

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

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);


        Intent intent = getIntent();
        dashBoard = (DashBoard) intent.getSerializableExtra(EXTRA_POST);

        postKey = dashBoard.getPostKey();
        detailPostUsername.setText(dashBoard.getUsername());
        detailPostDate.setText(dashBoard.getDate());
        detailPostDesc.setText(dashBoard.getDescription());
        detailPostHashTag.setText(dashBoard.getHashTag());
        likeNumber.setText(dashBoard.getLikes()+"");
        imageDownload();

    }

    private void imageDownload() {
        StorageReference islandRef, islandRef2, islandRef3;
        final long TEN_MEGABYTE = 1024 * 1024 * 10;


        //firebaseStorage 인스턴스 생성
        //하나의 Storage와 연동되어 있는 경우, getInstance()의 파라미터는 공백으로 두어도 됨
        //하나의 앱이 두개 이상의 Storage와 연동이 되어있 경우, 원하는 저장소의 스킴을 입력
        //getInstance()의 파라미터는 firebase console에서 확인 가능('gs:// ... ')
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://hashtravel-566a6.appspot.com/");

        //생성된 FirebaseStorage를 참조하는 storage 생성
        StorageReference storageRef = storage.getReference();

        islandRef = storageRef.child("images/" +postKey + "_0" + ".png");


        if(islandRef != null) {
            islandRef.getBytes(TEN_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed

                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                        bitmapList.add(bitmap);
                    imageView1.setImageBitmap(bitmap);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });

        }


        islandRef2 = storageRef.child("images/" +postKey + "_1" + ".png");

        if(islandRef2 != null) {
            islandRef2.getBytes(TEN_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed

                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    bitmapList.add(bitmap);
                    imageView2.setImageBitmap(bitmap);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }

        islandRef3 = storageRef.child("images/" +postKey + "_2" + ".png");


        if(islandRef3 != null) {
            islandRef3.getBytes(TEN_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed

                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                    bitmapList.add(bitmap);
                    imageView3.setImageBitmap(bitmap);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });

        }


//        for (Bitmap b : bitmapList) {
//            imageView3.setImageBitmap(b);
//            imageView4.setImageBitmap(b);
    }

    public void onClickComment(View view) {
        if(mAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, CommentActivity.class);
            intent.putExtra(CommentActivity.EXTRA_COMMENT, dashBoard);
            startActivity(intent);
        } else {
            Toast.makeText(this, "댓글 보고싶음 로그인해요~", Toast.LENGTH_SHORT).show();
        }
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
