package edu.android.hashtravel;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;


// Firebase 데이터베이스에서 데이터 읽어들어오는
public class DashBoardDao {
    // 게시글 리스트
//    private List<DashBoard> dsahBoardList = new ArrayList<>();

    // Singleton 디자인 패턴
    private static DashBoardDao instance = null;
    private DashBoardDao() {}
    public static DashBoardDao getInstance() {
        if(instance == null) {
            instance = new DashBoardDao();
        }
        return instance;
    }

//    public List<DashBoard> getDsahBoardList() {
//        return dsahBoardList;
//    }
}