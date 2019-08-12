package com.example.kaivalyamendki.sycodestalk;

/**
 * Created by Kaivalya Mendki on 06-04-2018.
 */

public class Code {

    String name, domain, level, status, description;

    public Code(){
    }

    public Code(String name, String domain, String level, String status, String description) {
        this.name = name;
        this.domain = domain;
        this.level = level;
        this.status = status;
        this.description = description;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
