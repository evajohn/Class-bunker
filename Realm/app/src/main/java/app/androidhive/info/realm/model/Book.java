package app.androidhive.info.realm.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Book extends RealmObject {

    @PrimaryKey
    private long id;

    private String title;
    private String day;
    private int bunked;
    private int total_classes;
    private boolean extra;
    private boolean ifbunked;
    private boolean cancled;
    private boolean update;

    public boolean isIfbunked() {
        return ifbunked;
    }

    public void setIfbunked(boolean ifbunked) {
        this.ifbunked = ifbunked;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isCancled() {
        return cancled;
    }

    public void setCancled(boolean cancled) {
        this.cancled = cancled;
    }


    public boolean isExtra() {

        return extra;
    }

    public void setExtra(boolean extra) {
        this.extra = extra;
    }

    public int getTotal_classes() {
        return total_classes;
    }

    public void setTotal_classes(int total_classes) {
        this.total_classes = total_classes;
    }

    public int getBunked() {
        return bunked;
    }

    public void setBunked(int bunked) {
        this.bunked = bunked;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    // Standard getters & setters generated by your IDE…
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

  }

