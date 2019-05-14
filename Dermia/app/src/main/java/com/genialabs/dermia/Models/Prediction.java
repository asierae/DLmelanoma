package com.genialabs.dermia.Models;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prediction {
    private int id;
    private String  mobile;
    private String date;
    private String resume;
    private String idalbum;
    private int positive;
    @SerializedName("image")
    @Expose
    private String image;
    private String key;
    @SerializedName("pred")
    @Expose
    private String pred;
    public Prediction(int id,String mobile,String imagepath){
        this.id=id;
        this.mobile=mobile;
        this.image=image;
    }
    public Prediction(String img,String pred,int positive,int id, String date, String resume, String idalbum){
        this.id=id;
        this.image=img;
        this.pred=pred;
        this.positive=positive;
        this.date = date;
        this.resume = resume;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPred() {
        return pred;
    }

    public void setPred(String pred) {
        this.pred = pred;
    }

    public int getPositive() {
        return positive;
    }

    public void setPositive(int positive) {
        this.positive = positive;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }
    public String getIdalbum() {
        return idalbum;
    }

    public void setIdalbum(String idalbum) {
        this.idalbum = idalbum;
    }

}
