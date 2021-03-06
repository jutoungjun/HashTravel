package edu.android.hashtravel;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotPostFragment extends Fragment {

    public static final String KEY_PLACE_ID = "place_id";

    public HotPostFragment() {//
        // Required empty public constructor
    }

    private ImageView hopostImage;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<DashBoard, HotpostViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hot_post, container, false);

        hopostImage = view.findViewById(R.id.hotPostImage);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRecycler = view.findViewById(R.id.hotPostRecyclerView);
        mRecycler.setHasFixedSize(true);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        Query hotPostquery = mDatabase.child("posts").orderByChild("likes").limitToLast(10);
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<DashBoard>()
                .setQuery(hotPostquery, DashBoard.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<DashBoard, HotpostViewHolder>(options) {
            @Override
            public HotpostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new HotpostViewHolder(inflater.inflate(R.layout.hotpost_item, viewGroup, false));
            }

            @Override
            protected void onBindViewHolder(HotpostViewHolder viewHolder, int position, final DashBoard model) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DetailDashboardActivity.class);
                        intent.putExtra(DetailDashboardActivity.EXTRA_POST, model);
                        intent.putExtra(DetailDashboardActivity.EXTRA_REF, postKey);
                        startActivity(intent);
                    }
                });

                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference gpostRef = mDatabase.child("posts").child(postRef.getKey());
                        // Run two transactions

                        onlikeClicked(gpostRef);
                    }
                });
            }
        };
        mRecycler.setAdapter(mAdapter);

    }


    class HotpostViewHolder extends RecyclerView.ViewHolder {
        private ImageView hotpostImage;
        private TextView hotTextTag, hotLikeNum;

        public HotpostViewHolder(@NonNull View itemView) {
            super(itemView);

            hotpostImage = itemView.findViewById(R.id.hotPostImage);
            hotTextTag = itemView.findViewById(R.id.hotTextTag);
            hotLikeNum= itemView.findViewById(R.id.textLikeNum);
        }

        public void bindToPost(DashBoard dashBoard, View.OnClickListener likeClickListener) {

            hotTextTag.setText(dashBoard.getHashTag());
            hotLikeNum.setText(dashBoard.getLikes() +"");


            StorageReference islandRef, islandRef2, islandRef3;
            final long ONE_MEGABYTE = 1024 * 1024;

            //firebaseStorage 인스턴스 생성
            //하나의 Storage와 연동되어 있는 경우, getInstance()의 파라미터는 공백으로 두어도 됨
            //하나의 앱이 두개 이상의 Storage와 연동이 되어있 경우, 원하는 저장소의 스킴을 입력
            //getInstance()의 파라미터는 firebase console에서 확인 가능('gs:// ... ')
            FirebaseStorage storage = FirebaseStorage.getInstance("gs://hashtravel-566a6.appspot.com/");

            //생성된 FirebaseStorage를 참조하는 storage 생성
            StorageReference storageRef = storage.getReference();




            islandRef = storageRef.child("images/" + dashBoard.getPostKey() + "_0" + ".png");


            if(islandRef != null) {
                islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Data for "images/island.jpg" is returns, use this as needed

                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        hotpostImage.setImageBitmap(bitmap);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });

            }
        }
    }

    private void onlikeClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                DashBoard d = mutableData.getValue(DashBoard.class);
                if(d == null) {
                    return Transaction.success(mutableData);
                }

                mutableData.setValue(d);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        if(mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAdapter != null) {
            mAdapter.stopListening();
        }
    }

}
