package edu.android.hashtravel;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoticeAdepter extends RecyclerView.Adapter<NoticeAdepter.ItemViewHolder> {
    private List<NoticeModel> modelList = new ArrayList<>();
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_notice_item,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder Holder, int postition) {
        Holder.onBind(modelList.get(postition));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
    void addItem(NoticeModel model) {
        // 외부에서 item을 추가시킬 함수입니다.
        modelList.add(model);
    }
    class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView textView1;

        public  ItemViewHolder(@NonNull View itemView) {
            super(itemView);
          textView1 = itemView.findViewById(R.id.textNotice);
        }
        void onBind(NoticeModel model){
            textView1.setText(model.getText());
        }
    }
}
