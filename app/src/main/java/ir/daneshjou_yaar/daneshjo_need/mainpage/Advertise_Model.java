package ir.daneshjou_yaar.daneshjo_need.mainpage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iqfarhad on 2/7/2018.
 */

public class Advertise_Model implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String user_id;
    private String title;
    private String intro;
    private String desc;
    private String image;
    private String date;
    private String price;
    private String cat;

    public Advertise_Model() {
    }

    public Advertise_Model(String id, String user_id, String title, String intro, String desc, String image, String date, String price, String cat) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
        this.intro = intro;
        this.desc = desc;
        this.image = image;
        this.date = date;
        this.price = price;
        this.cat = cat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "Advertise_Model{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", title='" + title + '\'' +
                ", intro='" + intro + '\'' +
                ", desc='" + desc + '\'' +
                ", image='" + image + '\'' +
                ", date='" + date + '\'' +
                ", price='" + price + '\'' +
                ", cat='" + cat + '\'' +
                '}';
    }

    public static List<Advertise_Model> createMovies(int itemCount) {
        List<Advertise_Model> ads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Advertise_Model movie = new Advertise_Model(null , null , "Ads " + (itemCount == 0 ?
                    (itemCount + 1 + i) : (itemCount + i)) , null , null , null , null , null , null);
            ads.add(movie);
        }
        return ads;
    }
}
