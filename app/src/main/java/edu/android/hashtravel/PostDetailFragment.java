package edu.android.hashtravel;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostDetailFragment extends Fragment {
    private static final String ARG_PLACE_INDEX = "arg_place_index";

    private ImageView imageView;
    private TextView textName, textDesc;

    private DashBoardDao dao = DashBoardDao.getInstance();

    public PostDetailFragment() {
        // Required empty public constructor
    }


    // Factory 메소드
    // TODO 후에 index로 말고 장소 아이디로 가져오기
    public static PostDetailFragment newFragment(int index) {
        PostDetailFragment fragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PLACE_INDEX, index);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);

        imageView = view.findViewById(R.id.detailImage);
        textName = view.findViewById(R.id.detailName);
        textDesc = view.findViewById(R.id.detailDesc);


        Bundle args = getArguments();
        int position = args.getInt(ARG_PLACE_INDEX);

        DashBoard dashBoard = dao.getDsahBoardList().get(position);
//        imageView.setImageResource(dashBoard.getPhotoId());
        textName.setText(dashBoard.getCategory());

        textDesc.setText(dashBoard.getDescription());


        return view;
    }

}
