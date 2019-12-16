package ir.daneshjou_yaar.location_address;

/**
 * Created by iqfarhad on 12/31/2017.
 */

public class Object_model {
    String id;
    String name ;
    String address;
    String info;
    String category;
    String img;
    String longitude;
    String latitude;

    public Object_model() {
    }

    public Object_model(String id, String name, String address, String info, String category, String img, String longitude, String latitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.info = info;
        this.category = category;
        this.img = img;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String  id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
