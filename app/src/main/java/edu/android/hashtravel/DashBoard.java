package edu.android.hashtravel;


import java.util.Date;
import java.util.List;

public class DashBoard {


    private int articleNo; // 게시글 글넘버
    private String userId; // 유저 아이디
    private String category; // 게시글 카테고리
    private String continent; // 대륙
    private String country; // 나라
    private String subject; // 글제목
    private String description; // 글내용
    // TODO 해쉬태그 List<String> ?
    private String hashTag; // 해쉬태그 내용
    private Date date;
    private int likes; // 좋아요수

    // TODO : 사진 어떻게 DB에 저장할지 찾아보기 !
    private int photoId; // 사진 리소스 아이디

    // TODO : 생성자 생각해보기
    public DashBoard(String category, String continent, String country, String subject, String description, String hashTag, int likes, int photoId) {
        this.category = category;
        this.continent = continent;
        this.country = country;
        this.subject = subject;
        this.description = description;
        this.hashTag = hashTag;
        this.likes = likes;
        this.photoId = photoId;
    }

    public int getArticleNo() {
        return articleNo;
    }

    public void setArticleNo(int articleNo) {
        this.articleNo = articleNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    //
//    static class Place {
//        private String name; // 여행지 이름
//        private String desc; // 여행지 설명
//        private float rating; // 여행지 별점 점수
//        private int photoId; // 여행지 사진 리소스 아이디
//
//        public Place(String name, String desc, float rating, int photoId) {
//            this.name = name;
//            this.desc = desc;
//            this.rating = rating;
//            this.photoId = photoId;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public String getDesc() {
//            return desc;
//        }
//
//        public float getRating() {
//            return rating;
//        }
//
//        public int getPhotoId() {
//            return photoId;
//        }
//    }



}
