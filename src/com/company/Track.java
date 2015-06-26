package com.company;

public class Track {


    public Track(String url, int group, int price) {
        this.url=url;
        this.group=group;
        this.price=price;
    }

    private String url;

    private int group;

    private int price;

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
