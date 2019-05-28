package edu.android.hashtravel;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class UserInfoActivity extends AppCompatActivity {

    private TextView textEmailInfo, textNAmeINfo;
    private ImageView imageView;
    private Button btnMyPost;
    private RecyclerView myRecycler;

    private MyRecycleAdapter adapter;
    private List<DashBoard> mList = new ArrayList<>();
    private String uid;

    private DatabaseReference mDatabase;

    class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyViewHolder> {
        public MyRecycleAdapter(List<DashBoard> list) {
            mList = list;
        }
        @NonNull
        @Override
        public MyRecycleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dashboard_item, viewGroup, false);
            return new MyRecycleAdapter.MyViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull MyRecycleAdapter.MyViewHolder holder, final int position) {
            holder.textSubject.setText(mList.get(position).getSubject());
            holder.textHashTag.setText(mList.get(position).getHashTag());
            holder.textUserId.setText(mList.get(position).getUsername());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserInfoActivity.this, DetailDashboardActivity.class);
                    intent.putExtra(DetailDashboardActivity.EXTRA_POST, mList.get(position));
                    startActivity(intent);
                }
            });
        }
        @Override
        public int getItemCount() {
            return mList.size();
        }
        private class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView textSubject, textHashTag, textUserId;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                textSubject = itemView.findViewById(R.id.textSubject);
                textHashTag = itemView.findViewById(R.id.textHashTag);
                textUserId = itemView.findViewById(R.id.textUserId);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffbb33")));
        getWindow().setStatusBarColor(Color.parseColor("#ffffbb33"));

        Intent intent = getIntent();
        String userEmail = intent.getExtras().getString("email");
        String userName = intent.getExtras().getString("name");
        uid = intent.getExtras().getString("uid");

        textEmailInfo = findViewById(R.id.textEmailInfo);
        textNAmeINfo = findViewById(R.id.textNameInfo);
        imageView = findViewById(R.id.imageMyInfo);
        btnMyPost = findViewById(R.id.btnMyPost);

        myRecycler = findViewById(R.id.myRecyclerView);

        imageView.setImageResource(R.drawable.profile);
        textEmailInfo.setText(userEmail);
        textNAmeINfo.setText(userName);


    }

    //TODO 버튼을 눌렀을 때 리사이클러 뷰에 목록이 나와야 함
    public void onClickMyPost(View view) {
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecycleAdapter(mList);
        mDatabase.child("posts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DashBoard dashBoard = dataSnapshot.getValue(DashBoard.class);
                adapter.notifyDataSetChanged();

                if(dashBoard.getUid() != null) {
                    if (dashBoard.getUid().equals(uid)) {
                        mList.add(dashBoard);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter.notifyDataSetChanged();
        myRecycler.setAdapter(adapter);

        mList = new ArrayList<>();

    }

}
