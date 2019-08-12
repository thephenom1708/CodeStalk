package com.example.kaivalyamendki.sycodestalk;

/**
 * Created by Kaivalya Mendki on 06-04-2018.
 */

public class Contest {

    String name, domain, totalQue, submittedQue, rank;
    public Contest() {
    }

    public Contest(String name, String domain, String totalQue, String submittedQue, String rank) {
        this.name = name;
        this.domain = domain;
        this.totalQue = totalQue;
        this.submittedQue = submittedQue;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTotalQue() {
        return totalQue;
    }

    public void setTotalQue(String totalQue) {
        this.totalQue = totalQue;
    }

    public String getSubmittedQue() {
        return submittedQue;
    }

    public void setSubmittedQue(String submittedQue) {
        this.submittedQue = submittedQue;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
