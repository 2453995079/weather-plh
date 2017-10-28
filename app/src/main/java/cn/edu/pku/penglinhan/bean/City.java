package cn.edu.pku.penglinhan.bean;

/**
 * Created by Administrator on 2017/10/28 0028.
 */

public class City {
    private String province;
    private String city;
    private String number;
    private String firstPY;
    private String allPY;
    private String allFristPY;
    public City(String province, String city, String number, String firstPY, String allPY, String allFristPY) {
        this.province = province;
        this.city = city;
        this.number = number;
        this.firstPY = firstPY;
        this.allPY = allPY;
        this.allFristPY = allFristPY;
    }


    public String getCity() {
        return city;
    }

    public String getNumber() {
        return number;
    }
}
