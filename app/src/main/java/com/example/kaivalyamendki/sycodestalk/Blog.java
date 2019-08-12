package com.example.kaivalyamendki.sycodestalk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaivalya Mendki on 05-04-2018.
 */

public class Blog {

    public String title, description, username, image, blogId, likes = "0";
    public List<String> likeArray = new ArrayList<>();
    public Blog(){}

    public Blog(String blogId, String title, String description, String username, String image, String likes){
        this.blogId = blogId;

        this.title = title;
        this.description = description;
        this.username = username;
        this.image = image;
        this.likes = likes;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }


    public List<String> getLikeArray() {
        return likeArray;
    }

    public void setLikeArray(List<String> likeArray) {
        this.likeArray = likeArray;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }
}
