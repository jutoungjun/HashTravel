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
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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

import java.util.ArrayList;

public class DetailDashboardActivity extends AppCompatActivity {

    public static final String EXTRA_POST = "post_key";
    public static final String EXTRA_REF = "postref";
    private TextView detailPostUsername, detailPostDate, detailPostDesc, detailPostHashTag, likeNumber;
    private DashBoard dashBoard;
    private String postKey;
    private ImageView imageView1, imageView2, imageView3;
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private int imgCount = 0;

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

        imageViews.add(imageView1);
        imageViews.add(imageView2);
        imageViews.add(imageView3);

        StorageReference islandRef;
        final long TEN_MEGABYTE = 1024 * 1024 * 10;


        //firebaseStorage 인스턴스 생성
        //하나의 Storage와 연동되어 있는 경우, getInstance()의 파라미터는 공백으로 두어도 됨
        //하나의 앱이 두개 이상의 Storage와 연동이 되어있 경우, 원하는 저장소의 스킴을 입력
        //getInstance()의 파라미터는 firebase console에서 확인 가능('gs:// ... ')
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://hashtravel-566a6.appspot.com/");

        //생성된 FirebaseStorage를 참조하는 storage 생성
        StorageReference storageRef = storage.getReference();

        for (; imgCount<3; imgCount++) {
            final int imageCount = imgCount;
            islandRef = storageRef.child("images/" + postKey + "_" + imageCount + ".png");

            if (islandRef != null) {
                islandRef.getBytes(TEN_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imageViews.get(imageCount).setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // 해당 이미지가 없으니 메소드를 끝내자
                        return;
                    }
                });
            }
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
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

    private boolean like;
    private int currentLike;

    public void onClickLike(View view) {

        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("posts").child(postKey);
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                DashBoard d = mutableData.getValue(DashBoard.class);
                if (d == null) {
                    return Transaction.success(mutableData);
                }

                if (d.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    like = false;
                    currentLike = d.likes;
                    d.likes = d.likes - 1;
                    d.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    like = true;
                    currentLike = d.likes;
                    d.likes = d.likes + 1;
                    d.stars.put(getUid(), true);
                }

                mutableData.setValue(d);
                return Transaction.success(mutableData);

            }
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                if(databaseError == null ) {
                    if (like) {
                        Toast.makeText(DetailDashboardActivity.this, "좋아요!", Toast.LENGTH_SHORT).show();
                        likeNumber.setText((currentLike+1)+"");
                    } else {
                        likeNumber.setText((currentLike-1)+"");
                        Toast.makeText(DetailDashboardActivity.this, "좋아요 취소", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailDashboardActivity.this, "로그인 하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
