package edu.android.hashtravel;


import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DashBoard implements Serializable {

    private String uid; // Key값 유저 아이디
    private String username;
    private String postKey; // Key값 게시글 키
    private String category; // 게시글 카테고리
    private String continent; // 대륙
    private String country; // 나라
    private String subject; // 글제목
    private String description; // 글내용
    // TODO 해쉬태그 List<String> ?
    private String hashTag; // 해쉬태그 내용
    private String date;
    public int likes; // 좋아요수
    public Map<String, Boolean> stars = new HashMap<>();


    // TODO : 사진 어떻게 DB에 저장할지 찾아보기 !
    private String photoId; // 사진 리소스 아이디(Storage 사용)

    // 기본 생성자 필요함
    public DashBoard() {}


    public DashBoard(String postKey, String uid, String username, String date, String category, String continent, String country, String subject, String description, String hashTag, int likes, String photoId) {
        this.postKey = postKey;
        this.uid = uid;
        this.username = username;
        this.date = date;
        this.category = category;
        this.continent = continent;
        this.country = country;
        this.subject = subject;
        this.description = description;
        this.hashTag = hashTag;
        this.likes = likes;
        this.photoId = photoId;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("postKey", postKey);
        result.put("uid", uid);
        result.put("username" , username);
        result.put("date", date);
        result.put("category", category);
        result.put("continent", continent);
        result.put("country", country);
        result.put("subject", subject);
        result.put("description", description);
        result.put("hashTag", hashTag);
        result.put("likes",likes);
        result.put("stars", stars);
        result.put("photoId", photoId);
        return result;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
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


    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }
}