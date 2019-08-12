package com.example.kaivalyamendki.sycodestalk;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaivalya Mendki on 02-04-2018.
 */

public class Follow_Request {

    List<String> requestFromId = new ArrayList<>();
    //String requestToId;

    public Follow_Request(){}

    public Follow_Request(List<String> requestFromId)
    {
        this.requestFromId = requestFromId;
    }

    public void addNewRequest(String req){
        requestFromId.add(req);
    }

    public List<String> getRequestFromId(){
        return requestFromId;
    }

    /*public String getRequestToId(){
        return requestToId;
    }*/
}

