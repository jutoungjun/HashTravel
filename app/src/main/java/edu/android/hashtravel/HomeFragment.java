package edu.android.hashtravel;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;
import java.util.zip.Inflater;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String KEY_TAGS = "key_tags";

    interface ContinentSelectCallback {
        void onContinentSelected(String continent);
        void onSearch(String text);
    }

    private ContinentSelectCallback callback;

    public HomeFragment() {
        // Required empty public constructor
    }

    private EditText editText;
    private Button btnSearch;
    private ImageButton btnAsia, btnEurope, btnAmerica, btnAfrica, btnOceania, btnSouthAmerica;
    private TextView hashTag1, hashTag2, hashTag3, hashTag4;

    private ValueEventListener listener;
    private Thread th;
    private boolean running;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            if(data != null) {
                String[] hashTags = data.getStringArray(KEY_TAGS);
                hashTag1.setText(hashTags[0]);
                hashTag2.setText(hashTags[1]);
                hashTag3.setText(hashTags[2]);
                hashTag4.setText(hashTags[3]);
            }
        }
    };
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //  콜백 MainActivity에서 프래그먼트 교체하는거 생성
        if(context instanceof ContinentSelectCallback) {
            callback = (ContinentSelectCallback) context;
        } else {
            throw new AssertionError(context + "는 반드시 ContinentSelectCallback을 구현해야 합니다.");
        }
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        hashTag1 = view.findViewById(R.id.hashTag1);
        hashTag2 = view.findViewById(R.id.hashTag2);
        hashTag3 = view.findViewById(R.id.hashTag3);
        hashTag4 = view.findViewById(R.id.hashTag4);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("posts");
//        query = ref.limitToFirst(randomIndex).limitToLast(4);

        final ArrayList<DashBoard> dashBoards = new ArrayList<>();
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    DashBoard dashBoard = child.getValue(DashBoard.class);
                    if(dashBoard.getLikes() >= 0) { // TODO 좋아요수 몇개로 할건지
                        dashBoards.add(dashBoard);
                    }

                }
                class TagRunnable implements Runnable {
                    @Override
                    public void run() {
                        while (true) {
                            Message msg = handler.obtainMessage();

                            int[] randomIndex = randomNum();
                            String[] hashTags = {dashBoards.get(randomIndex[0]).getHashTag(), dashBoards.get(randomIndex[1]).getHashTag(),
                                    dashBoards.get(randomIndex[2]).getHashTag(), dashBoards.get(randomIndex[3]).getHashTag()  };
                            Bundle data = new Bundle();
                            data.putStringArray(KEY_TAGS, hashTags);
                            msg.setData(data);

                            handler.sendMessage(msg);

                            try {
                                Thread.sleep(5000);
                            } catch (Exception e) {
                                break;
                            }
                        }
                    }
                } // end class TagRunnable

                th = new Thread(new TagRunnable());
                th.start();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        };

        ref.addValueEventListener(listener);

        editText = view.findViewById(R.id.editText);
        btnSearch = view.findViewById(R.id.btnTextSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onSearch(editText.getText().toString());
                editText.setText("");
            }
        });

        btnAsia = view.findViewById(R.id.btnAsia);
        btnEurope = view.findViewById(R.id.btnEurope);
        btnAmerica = view.findViewById(R.id.btnAmerica);
        btnAfrica = view.findViewById(R.id.btnAfrica);
        btnOceania = view.findViewById(R.id.btnOceania);
        btnSouthAmerica = view.findViewById(R.id.btnSouthAmerica);

        btnAsia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 콜백 메소드 구현
                callback.onContinentSelected("Asia");
            }
        });

        btnEurope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onContinentSelected("Europe");
            }
        });

        btnAmerica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onContinentSelected("America");
            }
        });
        btnAfrica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onContinentSelected("Africa");
            }
        });
        btnOceania.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onContinentSelected("Oceania");
            }
        });
        btnSouthAmerica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onContinentSelected("South America");
            }
        });

        return view;
    }

    public int[] randomNum() {
        int[] a = new int[4];
        for(int i = 0; i < 4; i++) {
            a[i] = new Random().nextInt(10) + 1;
            for(int j = 0; j < i; j++) {
                if(a[i] == a[j]) {
                    i--;
                }
            }
        }
        return a;
    }

}