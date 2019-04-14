package com.abdelrahman.quizapp.Model;

public class Category {

    private String Name;
    private String Image;

    private Category(){}

    public Category(String name, String imageUrl) {
        this.Name = name;
        this.Image = imageUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImageUrl(String imageUrl) {
        this.Image = imageUrl;
    }
}
