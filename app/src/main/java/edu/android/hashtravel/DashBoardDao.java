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
        dsahBoardList.add(new DashBoard("후기","유럽","독일","독일여행갔다옴","안녕","#dd#여행",0,R.drawable.arc_de_triomphe));
//        placeList.add(new Place("개선문", "개선문입니다", 3.5F, R.drawable.arc_de_triomphe));
//        placeList.add(new Place("에펠탑", "에펠탑 가고싶다", 4.0F, R.drawable.eiffel_tower));
//        placeList.add(new Place("금문교", "금문교 갈까", 3.0F, R.drawable.golden_gate_bridge));
//        placeList.add(new Place("국회의사당", "국회의사당...", 3.5F, R.drawable.house_of_parliament));
//        placeList.add(new Place("런던아이", "런던도 가고싶다", 5.0F, R.drawable.london_eye));
//        placeList.add(new Place("타워브리지", "타워브리지 멋잇네", 4.5F, R.drawable.tower_bridge));
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
