package edu.android.hashtravel;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class NoticeActivity extends AppCompatActivity {
private String text;
private NoticeAdapter adapter;
private RecyclerView recyclerView;
private int res;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffbb33")));
        getWindow().setStatusBarColor(Color.parseColor("#ffffbb33"));
        init();

        getData();
    }
    private void init() {
        recyclerView = findViewById(R.id.recyclerNotice);



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new NoticeAdapter();
        recyclerView.setAdapter(adapter);

    }
    private void getData() {
            List<String> textlist = Arrays.asList(
                    "첫번째 공지사항 입니다."
            );
            List<Integer> imageList  =Arrays.asList(
                    R.drawable.notice
            );
            for(int i = 0; i<textlist.size();i++){
                NoticeModel model = new NoticeModel(text,res);
                model.setText(textlist.get(i));
                model.setRes(imageList.get(i));
                adapter.addItem(model);
            }
    }


}
