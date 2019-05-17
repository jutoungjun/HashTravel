package edu.android.hashtravel;

import java.util.ArrayList;
import java.util.List;


public class DashBoardDao {
    // 게시글 리스트
    private List<DashBoard> dsahBoardList = new ArrayList<>();

    // Singleton 디자인 패턴
    private static DashBoardDao instance = null;
    private DashBoardDao() {
        makeDummyData();
    }

    private void makeDummyData() {
//        dsahBoardList.add(new DashBoard("후기","유럽","독일","독일여행갔다옴","안녕","#dd#여행",0,R.drawable.arc_de_triomphe));

    }

    public static DashBoardDao getInstance() {
        if(instance == null) {
            instance = new DashBoardDao();
        }
        return instance;
    }

    public List<DashBoard> getDsahBoardList() {
        return dsahBoardList;
    }
}
