package com.example.kaivalyamendki.sycodestalk;

/**
 * Created by Kaivalya Mendki on 23-03-2018.
 */

public class CodeStalk_users {

    String id, email;
    String username;
    String mobile;
    String status, picUrl;
    String hackerrank_user, codechef_user, hackerearth_user;
    String followers = "0", following = "0", follow_req = "1";
    String rating = "0.0";

    public CodeStalk_users()
    {

    }

    public CodeStalk_users(String email, String username, String picUrl)
    {
        this.email = email;
        this.username = username;
        this.picUrl = picUrl;
    }

    public CodeStalk_users (String id, String email, String username, String mobile, String status,
                            String picUrl, String hackerrank_user,String codechef_user, String hackerearth_user)
    {
        this.id = id;
        this.email = email;
        this.username = username;
        this.mobile = mobile;
        this.status = status;
        this.picUrl = picUrl;
        this.hackerrank_user = hackerrank_user;
        this.codechef_user = codechef_user;
        this.hackerearth_user = hackerearth_user;
    }

    public void setFollower(String  followers){
        this.followers = followers;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public void setFollow_req(String follow_req) {
        this.follow_req = follow_req;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFollowers() {return  followers; }

    public String getFollowing() {return  following; }

    public String getFollow_req() {return  follow_req; }

    public String getRating() {return  rating; }

    public String getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getUsername(){
        return username;
    }

    public String getMobile(){
        return  mobile;
    }

    public String getStatus(){
        return status;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public String getHackerrank_user(){
        return hackerrank_user;
    }

    public String getCodechef_user(){
        return codechef_user;
    }

    public String getHackerearth_user(){
        return hackerearth_user;
    }

}
