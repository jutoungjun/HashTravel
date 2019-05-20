package edu.android.hashtravel;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotPostFragment extends Fragment {

    public static final String KEY_PLACE_ID = "place_id";
    private RecyclerView recyclerView;
    private TextView textTag;
    private DashBoardDao dashBoardDao = DashBoardDao.getInstance();
    private DashBoard dashBoard;

    private int mPostNum;

    public HotPostFragment() {//
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        // 데이터베이스에서 게시글 정보 읽기
        // TODO 게시글 리스트 !
        ValueEventListener postListner = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dashBoard = dataSnapshot.getValue(DashBoard.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getContext(), "게시글 정보 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hot_post, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        HotPostAdapter adapter = new HotPostAdapter();

        recyclerView.setAdapter(adapter);
        // 연습
        textTag = view.findViewById(R.id.textTag);
//        textTag.setText( dashBoard.getHashTag());

        return view;
    }

    public class HotPostAdapter extends RecyclerView.Adapter<HotPostAdapter.ViewHolder> {

        @NonNull
        @Override
        public HotPostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.hotpost_item, parent, false);

            ViewHolder holder = new ViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
//            DashBoard dashBoard = dashBoardDao.getDsahBoardList().get(position);

//            viewHolder.imageView.setImageResource( dashBoard.getPhotoId());
//            viewHolder.textTag.setText( dashBoard.getHashTag());
//            viewHolder.textLikeNum.setText(dashBoard.getLikes());

            // TODO
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPlaceDetail(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dashBoardDao.getDsahBoardList().size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView imageView;
            private TextView textTag, textLikeNum;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.placeImageView);
                textTag = itemView.findViewById(R.id.hotTextTag);
                textLikeNum = itemView.findViewById(R.id.textLikeNum);
            }
        }
    }

    private void showPlaceDetail(int position) {

        Intent intent = new Intent(getContext(), PostDetailActivity.class );
        // TODO position말고 게시글 넘버로 후에 구현
        intent.putExtra(KEY_PLACE_ID, position);
        startActivity(intent);
    }


}
