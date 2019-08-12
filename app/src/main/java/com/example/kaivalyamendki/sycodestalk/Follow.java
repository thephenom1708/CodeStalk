package com.example.kaivalyamendki.sycodestalk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaivalya Mendki on 05-04-2018.
 */

public class Follow {

    List<String> followers = new ArrayList<>();
    List<String> following = new ArrayList<>();

    public Follow(){}

    public Follow(List<String> followers, List<String> following){
        this.followers = followers;
        this.following = following;
    }

    public void addFollower(String follower) {
        this.followers.add(follower);
    }

    public void addFollowing(String following) {
        this.following.add(following);
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public List<String> getFollowing() {
        return following;
    }
}
