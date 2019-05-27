package edu.android.hashtravel;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class DetailDashboardActivityAdapter extends RecyclerView.Adapter<DetailDashboardActivityAdapter.itemViewHolder> {
    List<DetailDashboardActivityModel> modelList = new ArrayList<>();
    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int positon) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_detail_dashboardimage_item,parent,false);

        return new itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int positon) {
            holder.onBind(modelList.get(positon));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
    void addItem(DetailDashboardActivityModel model) {
        // 외부에서 item을 추가시킬 함수입니다.
        modelList.add(model);
    }
    class  itemViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public itemViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.dashboardimage);
        }
        void  onBind(DetailDashboardActivityModel model){
            imageView.setImageResource(model.getRes());
        }
    }
}
