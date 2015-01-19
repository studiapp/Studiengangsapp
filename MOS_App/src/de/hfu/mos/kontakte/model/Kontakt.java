package de.hfu.mos.kontakte.model;

/**
 * Created by gera on 21.12.14.
 */
import java.util.ArrayList;

public class Kontakt {
    private String name;
    private String thumbnailUrl;
    private String function;
    private String tel;
    private String mail;
    private String room;

    public Kontakt() {
    }

    public Kontakt(String name, String thumbnailUrl, String function, String tel, String mail, String room) {
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.function = function;
        this.tel = tel;
        this.mail = mail;
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getFunction() {   return function;    }

    public void setFunction(String function) {  this.function = function;   }

    public String getTel() { return tel; }

    public void setTel(String tel) { this.tel = tel; }

    public String getMail() {   return mail;  }

    public void setMail(String mail) {  this.mail = mail;   }

    public String getRoom() { return room; }

    public void setRoom(String room) { this.room = room; }

}