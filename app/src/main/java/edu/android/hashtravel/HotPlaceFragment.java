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
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HotPlaceFragment extends Fragment {

    public static final String KEY_PLACE_ID = "place_id";
    private RecyclerView recyclerView;
    private DashBoardDao dashBoardDao = DashBoardDao.getInstance();

    public HotPlaceFragment() {//
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hot_place, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        PlaceAdapter adapter = new PlaceAdapter();

        recyclerView.setAdapter(adapter);

        return view;
    }

    public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

        @NonNull
        @Override
        public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.hotplace_item, parent, false);

            ViewHolder holder = new ViewHolder(itemView);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
            DashBoard dashBoard = dashBoardDao.getDsahBoardList().get(position);

            viewHolder.imageView.setImageResource( dashBoard.getPhotoId());
            viewHolder.textView.setText( dashBoard.getSubject());


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
            private TextView textView;
            private RatingBar ratingBar;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.placeImageView);
                textView = itemView.findViewById(R.id.placeTextName);

            }
        }
    }

    private void showPlaceDetail(int position) {

        Intent intent = new Intent(getContext(), PlaceDetailActivity.class );
        // TODO position말고 id로 후에 구현
        intent.putExtra(KEY_PLACE_ID, position);
        startActivity(intent);
    }


}
