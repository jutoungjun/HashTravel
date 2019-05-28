package edu.android.hashtravel;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DashBoardViewHolder extends RecyclerView.ViewHolder {

    public TextView textSubject, textHashTag, textUserId;

    public DashBoardViewHolder(@NonNull View itemView) {
        super(itemView);

        textSubject = itemView.findViewById(R.id.textSubject);
        textHashTag = itemView.findViewById(R.id.textHashTag);
        textUserId = itemView.findViewById(R.id.textUserId);

    }

    public void bindToPost(DashBoard dashBoard, View.OnClickListener likeClickListener) {
        textSubject.setText(dashBoard.getSubject());
        textHashTag.setText(dashBoard.getHashTag());
        textUserId.setText(dashBoard.getUid());


    }
}
