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
                    "국화", "사막", "수국", "해파리", "코알라", "등대", "펭귄", "튤립"
            );
            for(int i = 0; i<textlist.size();i++){
                NoticeModel model = new NoticeModel(text);
                model.setText(textlist.get(i));
                adapter.addItem(model);
            }
    }


}
