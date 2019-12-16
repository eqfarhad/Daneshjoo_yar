package ir.daneshjou_yaar.daneshjo_need.mainpage.ads_detail;

import java.io.Serializable;

/**
 * Created by iqfarhad on 3/13/2018.
 */

public class User_Model implements Serializable {
    private String name;
    private String family;
    private String user_id;
    private String phone;
    private String email;
    private String telegram_id;
    private String profile_pic;

    public User_Model(String name, String family, String user_id, String phone, String email, String telegram_id, String profile_pic) {
        this.name = name;
        this.family = family;
        this.user_id = user_id;
        this.phone = phone;
        this.email = email;
        this.telegram_id = telegram_id;
        this.profile_pic = profile_pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelegram_id() {
        return telegram_id;
    }

    public void setTelegram_id(String telegram_id) {
        this.telegram_id = telegram_id;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    @Override
    public String toString() {
        return "User_Model{" +
                "name='" + name + '\'' +
                ", family='" + family + '\'' +
                ", user_id='" + user_id + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", telegram_id='" + telegram_id + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                '}';
    }
}
