package edu.android.hashtravel;

public class NoticeModel {
    private String text;
    private int res;

    public NoticeModel(String text, int res) {
        this.text = text;
        this.res = res;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
