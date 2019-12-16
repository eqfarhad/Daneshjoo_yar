package ir.daneshjou_yaar.daneshjo_need.categories;

import java.io.Serializable;

/**
 * Created by iqfarhad on 2/6/2018.
 */

public class Category_Model implements Serializable{
    String name;
    String id;
    String amount;
    String image;

    public Category_Model(String name, String id, String amount, String image) {
        this.name = name;
        this.id = id;
        this.amount = amount;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
